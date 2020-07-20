<template>
  <v-app>
    <!-- NavBar -->
    <v-app-bar
      app
      color=""
      dark
      style="padding: 0px 10px;"
    >
    <div style="height: 100%">
      <router-link to="/Home">
        <v-btn style="height: 100%">
          <!-- <v-icon>fas fa-user</v-icon> 로고를 만들어서 추가하자-->
          <span class="routerLink mr-2">서비스 로고</span>
        </v-btn>
      </router-link>
    </div>

      <div class="text-center" style="height: 100%">
        <!-- Profile Router -->
        <router-link to="/Info">
          <v-btn style="height: 99%">
            <v-icon>fas fa-user</v-icon>
            <span class="routerLink mr-2 ml-2">Profile</span>
          </v-btn>
        </router-link>

        <!-- Room Router -->
        <router-link to="/Room">
          <v-btn style="height: 99%">
            <v-icon>fas fa-solar-panel</v-icon>
            <span class="routerLink mr-2 ml-2">Conference</span>
          </v-btn>
        </router-link>

        <!-- Note Router -->
        <router-link to="/Note">
          <v-btn style="height: 99%">
            <v-icon>fas fa-sticky-note</v-icon>
            <span class="routerLink mr-2 ml-2">Note</span>
          </v-btn>
        </router-link>
      </div>

      <!-- 검색창 -->
      <div class="ml-5 mt-5" style="height: 99%; width: 35%">
        <v-spacer></v-spacer>
        <v-toolbar dense style="width: 90%">
          <v-text-field append-icon="fas fa-search" hint="검색어를 입력해달라">
          </v-text-field>
        </v-toolbar>
      </div>
      <v-spacer></v-spacer>
      <!-- Settings Router -->
      <div class="text-center" style="height: 97%">
        <router-link to="/Settings">
          <v-btn class="ml-3" style="height: 100%; width:100%">
            <v-icon>fas fa-cog</v-icon>
          </v-btn>
        </router-link>
      </div>


    </v-app-bar>
    <v-content>
      <router-view />
    </v-content>
  </v-app>
</template>

<script lang="ts">
import Vue from 'vue';

export default Vue.extend({
  name: 'App',

  components: {
  },

  data: () => ({
    //
  }),
});
</script>

<style>
.routerLink {
  color: white;
}
</style>
