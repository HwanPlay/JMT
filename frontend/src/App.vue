<template>
  <v-app class="main">
    <!-- Login Component -->
    <div v-if="isLoggedIn === false">
      <Login @loginConfirm="enterService"/>
    </div>

    <!-- NavBar -->
    <div v-else>
      <v-app-bar app color = "rgb(14, 23, 38)" dark style="padding: 0px 10px; margin: 30px 0 64px;">
        <div style="height: 100%;">
          <router-link to="/Home">
            <v-btn text style="height: 100%">
              <!-- <v-icon>fas fa-home</v-icon> -->
              <span class="routerLink mr-2">
                <v-img :src="require('./JMTwithLogo.png')" max-height="60px" max-width="120px"></v-img>
                <!-- Home -->
              </span>
            </v-btn>
          </router-link>
        </div>

        <div class="text-center" style="height: 100%">
          <!-- Group Router -->
          <router-link to="/Group">
            <v-btn text style="height: 99%;">
              <v-icon>fas fa-users</v-icon>
              <span class="routerLink mr-2 ml-2">Group</span>
            </v-btn>
          </router-link>

          <!-- Room Router -->
          <router-link to="/Room">
            <v-btn text style="height: 99%">
              <v-icon>fas fa-solar-panel</v-icon>
              <span class="routerLink mr-2 ml-2">Conference</span>
            </v-btn>
          </router-link>

          <!-- Note Router -->
          <router-link to="/Note">
            <v-btn text style="height: 99%">
              <v-icon>fas fa-sticky-note</v-icon>
              <span class="routerLink mr-2 ml-2">Note</span>
            </v-btn>
          </router-link>
        </div>

        <v-spacer></v-spacer>

        <MyProfile />

        <!-- Settings Router -->
        <div class="text-center" style="height: 97%">
          <router-link to="/Settings">
            <v-btn text class="ml-2" style="height: 100%; width:100%">
              <v-icon size=30>fas fa-cog</v-icon>
            </v-btn>
          </router-link>
        </div>


      </v-app-bar>
    <v-main style="padding-top:0px;">
      <router-view @goToGroup="goToGroup" @goToNote="goToNote" />
    </v-main>
    </div>

  </v-app>
</template>

<script lang="ts">
import Vue from 'vue';

import Login from '../src/components/Login.vue'
import MyProfile from '../src/components/MyProfile.vue'

export default Vue.extend({
  name: 'App',

  components: {
    Login,
    MyProfile
  },

  data () {
    return {
      isLoggedIn: false,
    }
  },
  methods:{
    enterService(){
      this.isLoggedIn = true;
    },
    goToGroup(){
      this.$router.push('Group')
    },
    goToNote(){
      this.$router.push('Note')
    }
  },
  mounted(){
    this.$router.push('Home')
  }
});
</script>

<style>
v-app-bar{
  -webkit-app-region: no-drag;
}
.main{
  background-color: rgb(7, 14, 29);
}
</style>
