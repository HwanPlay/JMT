<template>
  <v-row style="width: 100%; height: 100%;" >
    <v-col class="nav" cols="2">
      <v-navigation-drawer dark height="100%" permanent>
        <div class="myProfile">
        <v-list>
          
          <v-list-item  style="text-align : center; ">
            <v-list-item-avatar style="width : 100px; height:100px " >
              <v-img  src="../assets/profile/profile1.jpg"></v-img>
            </v-list-item-avatar>
          </v-list-item>

          <v-list-item link> <!-- @click="$router.push('/MyProfile')" -->
            <v-list-item-content class="item-content">
              <v-list-item-title class="myName"><h2 style="color : white">{{ $store.state.myName }}</h2></v-list-item-title>
              <v-list-item-subtitle><h5 style="color : white">{{ $store.state.userId }}</h5></v-list-item-subtitle>
            </v-list-item-content>
            <!-- <v-list-item-action>
              <v-icon>mdi-menu-down</v-icon>
            </v-list-item-action> -->
          </v-list-item>
        </v-list>
          </div>
        <v-divider></v-divider>
        <v-list
          nav
          dense
        >
          <v-list-item-group v-model="group">
            <v-list-item
              v-for="(group, i) in this.$store.state.myGroups"
              :key="i" @click="toggle(i)"
            >
              <v-list-item-icon>
                <v-icon>mdi-account-multiple</v-icon>
              </v-list-item-icon>

              <v-list-item-content>
                <v-list-item-title v-text="group.groupName"></v-list-item-title>
              </v-list-item-content>

            </v-list-item>
          </v-list-item-group>
          
          <v-btn
            @click="modalOn= !modalOn"
            width="100%"
            text
          >
          <v-icon class="mr-2">mdi-account-multiple-plus</v-icon>
          <p>그룹 만들기</p>
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
    CreateGroup,
  },
  data() {
    return {
      group: 0,
      modalOn: false,
      onboarding: 0,
    };
  },
  methods: {
    toggle(i) {
      console.log('change!',i);
      this.onboarding = i;
    },
    createGroup() {
      this.$store.state.groupModalOn = true;
      console.log(this.$store.state.groupModalOn);
    },
    closeModal() {
      this.modalOn = false;
    },
  },
  mounted() {
    this.$store.dispatch('getGroupInfo');
  },
};
</script>

<style >
.nav{
  padding: 0;
}
.v-navigation-drawer__content{
  background-color: rgb(52, 63, 87);
}
/* .v-list-item-content{
  float: left;
} */

.myProfile{
    display : flex; 
    justify-content : center; 
    align-items: center;

}
</style>