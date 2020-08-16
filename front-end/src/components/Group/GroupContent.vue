<template>
  <v-row>
    <!-- 좌측 그룹 정보 부분 -->
    <v-col cols="4" style="vertical-align:middle; padding-top: 20px; margin-left : 50px;">
      <v-row justify="center">
        <v-col id="onLineStatus">
          <v-row>
            <div id="conferenceStatusBox" style="width:100%;">
            <p
              v-if="groupInfo.hasMeeting"
              class="conferenceStatus"
              style="color: red; border: 2px solid red;"
            >회의 진행중</p>
            <p
              v-if="groupInfo.hasMeeting == false"
              class="conferenceStatus"
              style="color: green; border: 2px solid green;"
            >진행중인 회의가 없습니다.</p>
            </div>
          </v-row>
        </v-col>
      </v-row>
      <v-row justify="center">
        <v-col id="conferenceBox">
          <div>
            <v-row>
              <v-col>
                <h3 id="GroupContentgroupName" style="width:150px; float:left; color:Black;">{{ groupInfo.groupName }}</h3>
                <v-btn style="float:right;"
                  @click="sModal=true;"
                  v-if="(groupInfo.hostId === this.$store.state.userId) && !groupInfo.hasMeeting"
                  dark
                  color="green"
                >
                  회의 시작
                </v-btn>
                <v-dialog v-model="sModal" width="500px">
                  <v-card width="500px">
                    <v-card-title class="top">회의 시작하기</v-card-title>
                    <v-container>
                      <v-form ref="form" width="500px;" lazy-validation class="ml-2 mr-2">
                        <v-text-field v-model="meetingTitle" label="회의 명" required></v-text-field>
                        <v-card-actions>
                          <v-spacer></v-spacer>
                          <v-btn text color="error" class="mr-4" @click="startMeeting">회의 시작</v-btn>
                        </v-card-actions>
                      </v-form>
                    </v-container>
                  </v-card>
                </v-dialog>
                <v-btn
                  @click="joinMeeting"
                  v-if="(groupInfo.hostId != this.$store.state.userId) && groupInfo.hasMeeting"
                  dark
                  color="blue darken-2"
                >
                  회의 참여
                </v-btn>
              </v-col>
            </v-row>

            <h6>호스트 : {{ groupInfo.hostName }}</h6>
            <div style="height:60px; overflow-y:auto;">
              <p style="font-size : 15px;">소개 : {{ groupInfo.groupIntro }}</p>
            </div>
          </div>
        </v-col>
      </v-row>
      <!-- <v-divider class="mb-10"></v-divider> -->

      <v-row class="GroupListBox">
        <v-col>
          <v-row>
            <v-col cols="8">
              <h3>그룹원</h3>
            </v-col>
            <v-col  cols="4">
              <div v-if="groupInfo.hostId === this.$store.state.userId" style="float:right;">
                <InviteMember
                  :groupNo="groupInfo.groupNo"
                  :groupName="groupInfo.groupName"
                  :hostId="groupInfo.hostId"
                />
              </div>
            </v-col>
          </v-row>

          <v-card outlined>
            <v-col id="MemberListBox"  style="height : 200px;">
              <div v-if="members && members.length === 0">그룹원이 없습니다</div>
              <div v-else>
                <v-card-text
                  v-for="memberInfo in members.slice(0,3)"
                  :key="memberInfo.id"
                  style="padding: 5px; margin-left:-20px; margin-top:-10px;"
                >
                  <MemberCard :userInfo="memberInfo" />
                </v-card-text>
              </div>

              <v-card-actions>
                <v-spacer></v-spacer>
                <GroupMembers
                  :membersInfo="members"
                  @refresh="getGroupMembers"
                  :groupNo="groupInfo.groupNo"
                  :hostId="groupInfo.hostId"
                />
              </v-card-actions>
            </v-col>
          </v-card>
        </v-col>
      </v-row>

      <v-col>
        <v-row justify="end">
          <div class="mr-2" v-if="groupInfo.hostId === this.$store.state.userId">
            <v-btn dark color="red" @click="onModal=true" style="margin-top : 20px;">그룹 관리</v-btn>
            <v-dialog v-model="onModal" max-width="500px">
              <EditGroup @close="onModal=false" :groupInfo="groupInfo" />
            </v-dialog>
          </div>
          <v-btn class="mr-2"
            dark
            color="red"
            @click="exitGroup"
            v-if="groupInfo.hostId !== this.$store.state.userId"
            style="margin-top : 20px;"
          >그룹 탈퇴</v-btn>
        </v-row>
      </v-col>
    </v-col>

    <v-spacer></v-spacer>
    <!-- 우측 캘린더 부분 -->
    <v-col cols="7">
      <GroupCalendar />
    </v-col>
  </v-row>
</template>

<script>
import SERVER from '../../api/spring.js';
import axios from 'axios';

import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';

import MemberCard from './MemberCard.vue';
import GroupMembers from './GroupMembers.vue';
import InviteMember from './InviteMember.vue';
import EditGroup from './EditGroup.vue';
import GroupCalendar from './GroupCalendar.vue';

export default {
  name: 'group',
  components: {
    MemberCard,
    GroupMembers,
    InviteMember,
    GroupCalendar,
    EditGroup,
  },
  props: {
    groupInfo: Object,
  },
  data() {
    return {
      onModal: false,
      members: [],
      sock: null,
      ws: null,
      reconnect: 0,
      token: '',
      meetingNo: null,
      meetingTitle: this.$store.state.myName + '의 회의',
      sModal: false
    };
  },
  methods: {
    exitGroup() {
      axios
        .delete(
          SERVER.URL +
            '/groupmember/delno/' +
            this.groupInfo.groupNo +
            '/' +
            this.$store.state.userId
        )
        .then((res) => {
          console.log(res);
          this.$router.push('/Home');
        })
        .catch((err) => console.log(err.response));
    },

    changeHasMeeting() {
      var tmp = null;
      axios
        .put(SERVER.URL + '/group/hasmeeting/' + this.groupInfo.groupNo)
        .then((res) => {
          tmp = res.data.hasMeeting;
        })
        .finally(() => {
          this.send(tmp);
        });
    },
    startMeetingOn() {
      console.log('hoo');
    },

    getGroupMembers() {
      axios
        .get(SERVER.URL + '/groupmember/getno/' + this.groupInfo.groupNo)
        .then((res) => {
          this.members = res.data.groupMembers;
        })
        .catch((err) => console.log(err.response));
    },

    startMeeting() {
      this.changeHasMeeting();
      axios
        .post(SERVER.URL + '/meeting/add', {
          groupNo: this.groupInfo.groupNo,
          title: this.meetingTitle,
        })
        .then((res) => {
          this.meetingNo = res.data.meetingNo;
          this.sModal = false;
          this.$router.push({
            name: 'Conference',
            params: {
              roomId: this.groupInfo.roomId,
              hostId: this.groupInfo.hostId,
              groupNo: this.groupInfo.groupNo,
              groupName: this.groupInfo.groupName,
            },
            query: { meetingNo: this.meetingNo },
          });
        });
    },

    joinMeeting() {
      axios
        .get(
          SERVER.URL + '/meeting/get/currentmeeting/' + this.groupInfo.groupNo
        )
        .then((res) => {
          this.meetingNo = res.data.meetingNo;
          this.$router.push({
            name: 'Conference',
            params: {
              roomId: this.groupInfo.roomId,
              hostId: this.groupInfo.hostId,
              groupNo: this.groupInfo.groupNo,
              groupName: this.groupInfo.groupName,
            },
            query: { meetingNo: this.meetingNo },
          });
        });
    },

    send(tmp) {
      const msg = {
        meeting: tmp,
        groupNo: this.groupInfo.groupNo,
      };
      this.ws.send('/meeting', JSON.stringify(msg), {
        token: this.$store.state.accessToken,
      });
    },
  },

  created() {
    this.sock = new SockJS(SERVER.URL2);
    this.ws = Stomp.over(this.sock);
  },

  mounted() {
    this.sModal = false;
    console.log('여길봐 ', this.meetingTitle);
    console.log(this.$store.state.userId);
  },

  watch: {
    groupInfo() {
      this.getGroupMembers();
    },
  },
};
</script>


<style scoped>
#conferenceBox {
  margin-top: 10px;
  margin-bottom: 30px;
  border-radius: 15px;
  height: 180px;
  padding: 20px;
  /* padding: 15px; */
  box-shadow: 1px 1px 2px 2px rgb(167, 167, 167);
}

.GroupListBox {
  height: 320px;
  border-radius: 15px;
  padding: 15px;
  box-shadow: 1px 1px 2px 2px rgb(167, 167, 167);
}
#conferenceStatusBox{
 width: 100%;
 text-align: center;
}
.conferenceStatus {
  padding-top: 5px;
  font-size: 17px;
  border-radius: 10px;
  display: inline-block;
  height: 40px;
  text-align: center;
  width: 70%;
}


#onLineStatus {
  margin: 0;
}

#MemberListBox{
  overflow-y :auto;
}
</style>