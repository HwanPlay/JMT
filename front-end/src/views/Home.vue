<template>
  <v-row style="height: 100%; width: 100%;">
    <!-- 좌측 버튼 부분 -->
    <v-col cols="6" style="height: 100%;">
      <v-row style="height: 100%;" align="center" justify="center">
        
        <v-col class="text-center" cols="12" sm="4">
          <div class="my-2">
            <v-btn class="rounded-xl" dark depressed style="height: 130px; width: 130px; outline: none;" color="rgb(255, 128, 74)">
              <v-icon size="60">fas fa-video</v-icon>
            </v-btn>
          </div>
          <h5 style="margin-top: 20px;">새 회의</h5>
          <div class="my-2">
            <v-btn class="rounded-xl" dark depressed style="margin-top: 50px; height: 130px; width: 130px; outline: none;" color="rgb(52, 63, 87)" @click="goToGroup">
              <v-icon size="60">fas fa-user-friends</v-icon>
            </v-btn>
          </div>
          <h5 style="margin-top: 20px;">내 그룹</h5>
        </v-col>

        <v-col class="text-center" cols="12" sm="4">
          <div class="my-2">
            <v-btn class="rounded-xl" dark depressed style="height: 130px; width: 130px; outline: none;" @click="modalOn=true" color="rgb(52, 63, 87)">
              <v-icon size="60">fas fa-plus-square</v-icon>
            </v-btn>
            <v-dialog v-model="modalOn" max-width="500px">
              <CreateGroup @close="closeModal" />
            </v-dialog>
          </div>
          <h5 style="margin-top: 20px;">새 그룹</h5>

          <div class="my-2">
            <v-btn class="rounded-xl" dark depressed style="margin-top: 50px; height: 130px; width: 130px; outline: none;" color="rgb(52, 63, 87)" @click="goToNote">
              <v-icon size="60">fas fa-file-alt</v-icon>
            </v-btn>
          </div>
          <h5 style="margin-top: 20px;">내 노트</h5>
        </v-col>
        
      </v-row>
    </v-col>

    <v-spacer></v-spacer>

    <!-- 우측 시계 + 일정 버튼 -->
    <v-col cols="4" style="height: 100%;">
      <v-row style="height: 100%;" align="center" justify="center">
       <v-col cols="12">
        <v-card class="mx-auto" width="100%">
          <v-img class="white--text align-center" height="200px" :src="require('../assets/Watch/watch50.jpg')">
            <h1 class="ml-5">{{ clock | moment('HH:mm:ss') }}</h1>
            <h4 class="ml-5" style="color:rgb(187, 201, 224)">{{clock | moment('YYYY-MM-DD') }}</h4>
          </v-img>

          <v-card-subtitle class="pb-0"><h3>현재 회의중</h3></v-card-subtitle>
          <hr class="m-2">
          <v-card-text class="text--primary" style="height: 100%;">
            <h4>Team DNS</h4>
            <div>How to complete the Project : JMT</div>
            <hr>
            <h4>SSAFY 3기 대전 1반</h4>
            <div>Make our own Service</div>
          </v-card-text>
        </v-card>
        </v-col>
      </v-row>
    </v-col>
    <v-spacer></v-spacer>
  </v-row>
</template>

<script>
import CreateGroup from '../components/Group/CreateGroup.vue';
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
import SERVER from '../api/spring.js';


export default {
  name: 'Home',
  components: {
    CreateGroup,
  },
  methods: {
    goToGroup () {
      this.$emit('goToGroup');
    },
    goToNote () {
      this.$emit('goToNote');
    },
    closeModal (){
      this.modalOn = false;
    },

    connect() {
      this.ws.connect({'token' : this.$store.state.accessToken}, frame => {
        console.log('소켓 연결 성공', frame);
        this.ws.subscribe('/send/request/' + this.$store.state.userId, res => {
          console.log('구독으로 받은 메세지 입니다', res.body);
          this.recvList.push(JSON.parse(res.body));
          console.log(this.recvList);
        });
      });
    },
  },

  created() {
    this.sock = new SockJS(SERVER.URL2 + '/ws');
    this.ws = Stomp.over(this.sock);
  },

  mounted() {
    this.connect();
  },

  data () {
    return {
      clock: new Date(),
      timerCount: 5,
      modalOn: false,
      groupNo : null,
      sock : null,
      ws : null,
      recvList : [],
    };
  },
  watch: {
    timerCount: {
      handler (value) {
        if (value > 0) {
          setTimeout(() => {
            this.timerCount--;
            this.clock = new Date();
          }, 1000);
        }else if(value === 0) {
          this.timerCount = 5;
          this.clock = new Date();
        }
      },
      immediate: true // This ensures the watcher is triggered upon creation
    }
  }
};
</script>

<style scoped>
v-col {
  height: 100%;
  padding: 0px;
}
.box1 {
  height: 100%;
  background-color: rgb(52, 63, 87);
  box-shadow: rgba(0, 0, 0, 0.1) 8px 0px 16px;
}

</style>
