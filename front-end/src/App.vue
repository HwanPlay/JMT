<template>
  <v-app>
    <!-- Login Component -->
    <div v-if="!isLoggedIn">
      <Login />
    </div>

    <!-- NavBar -->
    <div v-else-if="!$store.state.videoOn">
      <v-app-bar app color="rgb(14, 23, 38)" dark style="margin-top: 30px;">
        <div style="height: 100%;">
          <router-link to="/Home">
            <v-btn class="shake" text style="height: 100%; outline:none;">
              <!-- <v-icon>fas fa-home</v-icon> -->
              <v-img :src="require('./JMTLogo.png')" max-height="60px" max-width="70px"></v-img>
                <!-- Home -->
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
          <!-- <router-link to="/devConference" style="text-decoration: none;">
            <v-btn text style="height: 99%; outline:none;">
              <v-icon>fas fa-chalkboard-teacher</v-icon>
              <span class="routerLink mr-2 ml-2">devConference</span>
            </v-btn>
          </router-link> -->

          <!-- Note Router -->
          <router-link to="/editor" style="text-decoration: none;">
            <v-btn text style="height: 99%; outline:none;">
              <v-icon>fas fa-file-alt</v-icon>
              <span class="routerLink mr-2 ml-2">Note</span>
            </v-btn>
          </router-link>


          <!-- <router-link to="/hwan" style="text-decoration: none;">
            <v-btn text style="height: 99%; outline:none;">
              <v-icon>fas fa-users</v-icon>
              <span class="routerLink mr-2 ml-2">hwan</span>
            </v-btn>
          </router-link> -->

          <!-- <router-link to="/editor">
            editor
          </router-link> -->
        </div>

        <v-spacer></v-spacer>

        <RequestModal />

        <MyProfile />

        <!-- logout Router -->
        <div class="text-center" style="height: 100%">
          <v-btn text class="mr-2" @click="logout(); ws.disconnect();" style="height: 100%; width:100%; outline:none;">
            <v-icon size="30">mdi-logout</v-icon>
          </v-btn>
        </div>
      </v-app-bar>
      <v-dialog v-model='inviteModal' width="400px">
        <InviteMessage :message=recv @done="inviteModal=false;"  />
      </v-dialog>
    </div >

      <v-main v-if="isLoggedIn">
        <router-view @goToGroup="goToGroup" @goToNote="goToNote" />
      </v-main>
  </v-app>
</template>

<script>
import Vue from 'vue';
import { mapGetters, mapActions } from 'vuex';
import WebRTC from '../src/api/webrtc';

import Login from './components/Account/Login.vue';
import MyProfile from './components/Account/MyProfile.vue';
import RequestModal from './components/Account/RequestModal.vue';
import InviteMessage from './components/Group/InviteMessage.vue';

import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
import SERVER from './api/spring.js';

Vue.use(WebRTC);
export default Vue.extend({
  name: 'App',

  components: {
    Login,
    MyProfile,
    RequestModal,
    InviteMessage
  },

  data() {
    return {
      sock : null,
      ws : null,
      recv : null,
      meetingModalOn: false,
      reconnect : 0,
      inviteModal: false,
      isLogin: this.isLoggedIn,
    };
  },
  methods: {
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


    connect() {
      this.sock = new SockJS(SERVER.URL2);
      this.ws = Stomp.over(this.sock);
      this.ws.connect({'token' : this.$store.state.accessToken}, frame => {
        this.ws.subscribe('/send/request/' + this.$store.state.userId, res => {
          this.recv = JSON.parse(res.body);
          if(this.recv.receiver == this.$store.state.userId){
            this.inviteModal = true;
          }
        });
      }, error => {
        if(this.reconnect++ <= 5) {
          setTimeout(()=> {
            this.sock = new SockJS(SERVER.URL2);
            this.ws = Stomp.over(this.sock);
            this.connect();
          }, 1*1000);
        }
      });
    },
  },

  mounted() {
    this.$router.push('Home');
  },
  
  created() {
    this.sock = new SockJS(SERVER.URL2);
    this.ws = Stomp.over(this.sock);
  },
  destroyed() {
    this.ws.disconnect();
  },
  
  computed: {
    isLoggedIn() {
      return this.$store.getters.isLoggedIn;
    },
  },
  watch: {
    isLoggedIn(){
      if (this.isLoggedIn){
        this.connect();
      }
    }
  }


});
</script>

<style>
v-app-bar {
  -webkit-app-region: no-drag;
}
</style>
