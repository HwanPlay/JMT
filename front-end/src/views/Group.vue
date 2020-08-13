<template>
  <v-row style="width: 100%; height: 100%;">
    <v-col class="nav" id="nav-box" cols="2">
      <v-navigation-drawer height="100%" permanent>
        <div id='myProfile'>
          <div>
          <img id="myImage" src="../assets/profile/profile1.jpg">
          </div>
          <!-- <v-list id ='myProfileContent'>
            <v-list-item style="text-align : center; ">
              <v-list-item-avatar style="width : 100px; height:100px ">
                <v-img src="../assets/profile/profile1.jpg"></v-img>
              </v-list-item-avatar>
            </v-list-item>

            <v-list-item link> -->
              <!-- @click="$router.push('/MyProfile')" -->
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
              <!-- <v-list-item-action>
              <v-icon>mdi-menu-down</v-icon>
              </v-list-item-action>-->
            <!-- </v-list-item>
          </v-list> -->
        </div>
        <v-divider></v-divider>
        <v-list nav dense>
          <v-list-item-group  v-model="group" active-class="border"  color="orange">
            <div id="v-list-item-box"  style=" margin-left : 15px;">
            <v-list-item 
              v-for="(group, i) in this.$store.state.myGroups"
              :key="i"
              @click="toggle(i)"
            >
              <v-list-item-icon >
                <v-icon style="margin-top : 5px; margin-left : 5px;  margin-right : -5px;">mdi-account-multiple</v-icon>
              </v-list-item-icon>

              <v-list-item-content>
                <p id="groupNameText" v-text="group.groupName"></p>
              </v-list-item-content>
            </v-list-item>
            </div>
          </v-list-item-group>
          
          <v-btn id="groupCreate"  @click="modalOn= !modalOn" text >
            <v-icon class="mr-2" style="color : white; ">mdi-account-multiple-plus</v-icon>
            <p style="color : white; ">그룹 만들기</p>
          </v-btn>
          <v-dialog v-model="modalOn" max-width="500px">
            <CreateGroup @close="closeModal" />
          </v-dialog>
          
        </v-list>
      </v-navigation-drawer>
    </v-col>

    <v-col cols="10">
      <!-- <Group1 :groupInfo="this.$store.state.myGroups[this.onboarding]" /> -->
      <GroupContent :groupInfo="this.$store.state.myGroups[this.onboarding]" />
    </v-col>
  </v-row>
</template>

<script>
// import Group1 from '../components/Group/Group1.vue';
import GroupContent from '../components/Group/GroupContent.vue';
import CreateGroup from '../components/Group/CreateGroup';

export default {
  name: 'Groups',
  components: {
    // Group1,
    GroupContent,
    CreateGroup
  },
  data() {
    return {
      group: 0,
      modalOn: false,
      onboarding: 0
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
  }
};
</script>

<style >
.nav {
  padding: 0;
}
.v-navigation-drawer__content {
  background-color: rgb(52, 63, 87);
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
  height: 530px;
  overflow-y: auto;
}

#groupCreate{
  margin-left: 25px;
  background-color : orange;
  height: 45px;
  width: 200px;
}

</style>