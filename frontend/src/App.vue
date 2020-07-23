<template>
  <v-app class="main">
    <!-- Login Component -->
    <div v-if="isLoggedIn === false">
      <Login @loginConfirm="enterService"/>
    </div>

    <!-- NavBar -->
    <div v-else>
    <v-app-bar app color = "rgb(14, 23, 38)" dark style="padding: 0px 10px; margin-top: 30px;">
    <div style="height: 100%">
      <router-link to="/Home">
        <v-btn text style="height: 100%">
          <!-- <v-icon>fas fa-user</v-icon> 로고를 만들어서 추가하자-->
          <span class="routerLink mr-2">서비스 로고</span>
        </v-btn>
      </router-link>
    </div>

      <div class="text-center" style="height: 100%">
        <!-- Profile Router -->
        <router-link to="/Info">
          <v-btn text style="height: 99%">
            <v-icon>fas fa-user</v-icon>
            <span class="routerLink mr-2 ml-2">Profile</span>
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

      <!-- 검색창 -->
      <div class="ml-5 mt-5" style="height: 99%; width: 35% ">
        <v-spacer></v-spacer>
        <v-toolbar flat color = "rgb(14, 23, 38)" dense style="width: 90%">
          <v-text-field append-icon="fas fa-search" hint="검색어를 입력해달라">
          </v-text-field>
        </v-toolbar>
      </div>
      <v-spacer></v-spacer>

      <!-- Settings Router -->
      <div class="text-center" style="height: 97%">
        <router-link to="/Settings">
          <v-btn text class="ml-3" style="height: 100%; width:100%">
            <v-icon>fas fa-cog</v-icon>
          </v-btn>
        </router-link>
      </div>


    </v-app-bar>
    <v-main style="margin-top: 64px; padding-top:0px;">
      <router-view/>
    </v-main>
    </div>

  </v-app>
</template>

<script lang="ts">
import Vue from 'vue';

import Login from '../src/components/Login.vue'

export default Vue.extend({
  name: 'App',

  components: {
    Login,
  },

  data () {
    return {
      isLoggedIn: false,
    }
  },
  methods:{
    enterService(){
      this.isLoggedIn = true;
    }
  }
});
</script>

<style>

.main{
  background-color: rgb(7, 14, 29);
}
.routerLink {
  color: white;
}
</style>
