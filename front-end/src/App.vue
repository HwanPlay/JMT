<template>
  <v-app>
    <!-- Login Component -->
    <div v-if="!isLoggedIn && !tmpLogin">
      <Login @loginConfirm="enterService" />
    </div>

    <!-- NavBar -->
    <div v-else>
        <vue-webrtc
          ref="webrtc"
        />
      <v-app-bar app color="rgb(14, 23, 38)" dark style="margin-top: 30px;">
        <div style="height: 100%;">
          <router-link to="/Home">
            <v-btn text style="height: 100%; outline:none;">
              <!-- <v-icon>fas fa-home</v-icon> -->
              <span class="routerLink" @click="closeWebrtc">
                <v-img :src="require('./JMTwithLogo.png')" max-height="60px" max-width="120px"></v-img>
                <!-- Home -->
              </span>
            </v-btn>
          </router-link>
        </div>

        <div class="text-center" style="height: 100%">
          <!-- Group Router -->
          <router-link to="/Group" style="text-decoration: none;">
            <v-btn text style="height: 99%; outline:none;">
              <v-icon>fas fa-users</v-icon>
              <span class="routerLink mr-2 ml-2">Group</span>
            </v-btn>
          </router-link>

          <!-- Conference Router -->
          <router-link to="/Conference" style="text-decoration: none;">
            <v-btn text style="height: 99%; outline:none;">
              <v-icon>fas fa-chalkboard-teacher</v-icon>
              <span class="routerLink mr-2 ml-2">Conference</span>
            </v-btn>
          </router-link>

          <!-- Note Router -->
          <router-link to="/Note" style="text-decoration: none;">
            <v-btn text style="height: 99%; outline:none;">
              <v-icon>fas fa-sticky-note</v-icon>
              <span class="routerLink mr-2 ml-2">Note</span>
            </v-btn>
          </router-link>
        </div>

        <v-spacer></v-spacer>

        <MyProfile />

        <!-- logout Router -->
        <div class="text-center" style="height: 100%">
          <v-btn text class="mr-2" @click="logout()" style="height: 100%; width:100%; outline:none;">
            <v-icon size="30">mdi-lock-open-outline</v-icon>
          </v-btn>
        </div>
      </v-app-bar>

      <v-main>
        <!-- <v-container pa-0 ma-0 style="height: 100%"> -->
          <router-view @goToGroup="goToGroup" @goToNote="goToNote" />
      </v-main>
    </div>
  </v-app>
</template>

<script>
import Vue from 'vue';
import { mapGetters, mapActions } from 'vuex';
import WebRTC from '../src/api/webrtc';

import Login from '../src/components/Account/Login.vue';
import MyProfile from '../src/components/Account/MyProfile.vue';

Vue.use(WebRTC);
export default Vue.extend({
  name: 'App',

  components: {
    Login,
    MyProfile,
  },

  data() {
    return {
      tmpLogin: false,
    };
  },
  methods: {
    closeWebrtc(){
      this.$refs.webrtc.leave();
    },
    goToGroup() {
      this.$router.push('Group');
    },
    // goToConference() {
    //   this.$router.push('Conference');
    // },
    goToNote() {
      this.$router.push('Note');
    },
    ...mapActions(['logout']),
    enterService() {
      this.tmpLogin = true;
    },
  },
  mounted() {
    this.$router.push('Home');
  },
  computed: {
    isLoggedIn() {
      return this.$store.getters.isLoggedIn;
    },
  },
});
</script>

<style>
v-app-bar {
  -webkit-app-region: no-drag;
}
</style>
