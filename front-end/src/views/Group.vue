<template>
  <v-row style="width: 100%; height: 100%;">
    <div class="nav" id="nav-box" >
      <v-navigation-drawer height="100%" permanent>
        <div id='myProfile'>
          <div>
          <img id="myImage" src="../assets/profile/profile1.jpg">
          </div>
               <v-list-item-content class="item-content">
                <v-list-item-title class="myName">
                  <h5>{{ $store.state.myName }}</h5>
                </v-list-item-title>
                <v-list-item-subtitle>
                  <div id="userIdBox">
                    <p>{{ $store.state.userId }}</p>
                  </div>
                </v-list-item-subtitle>
              </v-list-item-content> 
        </div>
        <v-divider></v-divider>
        <v-list nav dense>
          <v-list-item-group  v-model="group" active-class="border"  color="orange">
            <div id="v-list-item-box" >
            <v-list-item 
              v-for="(group, i) in this.$store.state.myGroups"
              :key="group.groupNo"
              @click="toggle(i)"
            >
              <v-badge v-if="$store.state.userId === group.hostId" color="red" dot overlap offset-x="25" offset-y="15">
                <v-list-item-icon>
                  <v-icon dark style="margin-top : 5px; margin-left : 10px;  margin-right : -15px;">mdi-account-multiple</v-icon>
                </v-list-item-icon>
              </v-badge>
          
              <v-badge v-else color="rgb(0, 0, 0, 0)" dot overlap offset-x="25" offset-y="15">
                <v-list-item-icon>
                  <v-icon dark style="margin-top : 5px; margin-left : 10px; margin-right : -15px;">mdi-account-multiple</v-icon>
                </v-list-item-icon>
              </v-badge>

              <v-list-item-content>
                <p id="groupNameText" style="padding-top: 5px" v-text="group.groupName"></p>
              </v-list-item-content>
            </v-list-item>
            </div>
          </v-list-item-group>
          
          <v-btn id="groupCreate" @click="modalOn= !modalOn" text dark class="mt-3">
            <v-icon class="mr-2" medium>mdi-account-multiple-plus</v-icon>
            <p>그룹 만들기</p>
          </v-btn>
          <v-dialog v-model="modalOn" max-width="500px">
            <CreateGroup @close="closeModal" />
          </v-dialog>

        </v-list>
      </v-navigation-drawer>
    </div>

    <v-col v-if='$store.state.myGroups.length !== 0 ' style="margin-left : 20px;" >
      <GroupContent :groupInfo="$store.state.myGroups[this.onboarding]" />
    </v-col>
    <v-col v-else>
      <EmptyGroup />
    </v-col>
  </v-row>
</template>

<script>
import GroupContent from '../components/Group/GroupContent.vue';
import CreateGroup from '../components/Group/CreateGroup.vue';
import EmptyGroup from '../components/Group/EmptyGroup.vue';

export default {
  name: 'Groups',
  components: {
    GroupContent,
    CreateGroup,
    EmptyGroup,
  },
  data() {
    return {
      group: {},
      modalOn: false,
      onboarding: 0,
    };
  },
  methods: {
    toggle(i) {
      console.log('change!', i);
      this.onboarding = i;
    },
    createGroup() {
      this.$store.state.groupModalOn = true;
      console.log(this.$store.state.groupModalOn);
    },
    closeModal() {
      this.modalOn = false;
    }
  },
  mounted() {
    this.$store.dispatch('getGroupInfo');
    console.log('res',this.$store.state.myGroups.length);
  }
};
</script>

<style >
.nav {
  padding: 0;
}

#nav-box{
  margin-left: 15px;
}
.v-navigation-drawer__content {
  background-color: rgb(52, 63, 87);
  height: 100%;
  overflow-y: hidden;
}

#myProfile {
  width: 100%;
  text-align: center;
}

#myImage{
    margin-top: 20px;
    display: inline-block;
    width: 100px; height: 100px;
    object-fit: cover;
    border-radius: 50%;
}
.myName{
  
  word-wrap: break-word;
  display: inline-block;
}
.myName h5{
  color : white; 
  word-wrap: break-word;
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 200px;
  height: 25px;
}
#userIdBox{
  display: inline-block;
  word-wrap: break-word;
}

#userIdBox p {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 200px;
  height: 20px;
  font-size: 16px;
  color: white;
}

#userIdBox p:hover {
    text-overflow: clip;
    white-space: normal;
    Word-break: break-all;
}

#groupNameText{
    color: white;
    font-size: 20px;
    left: -20px;
}
#v-list-item-box{
  height: 400px;
  overflow-y: auto;
}

#groupCreate{
  margin-left: 20px;
  background-color : orange;
  height: 45px;
  width: 200px;
  position: absolute;
  bottom: 60px;
}
#groupCreate p{
  font-size: 20px;
  font-weight:550;
}

#GroupContentBox{
  display: inline-block;
  position:absolute;
  left: 241px;
  height: 100%;
  right: 0;
  overflow: hidden;
  /* background-color: aqua; */
}
#GroupContentBox .row{
  margin-left: 10px;
  margin-right: 10px;
  margin-bottom: 10px;
  
}

</style>
