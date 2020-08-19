<template>
  <v-row style="height: 100%; width: 100%;">
    <!-- 좌측 버튼 부분 -->
    <v-col cols="1"></v-col>
    <v-col cols="5" style="height: 100%;" class="ml-2">
      <v-row style="height: 100%;" align="center" justify="center">
        
        <v-col class="text-center mr-4" cols="12" sm="4">
          <div class="my-2">
            <v-btn class="rounded-xl" dark depressed style="height: 145px; width: 145px; outline: none;" color="rgb(255, 128, 74)" @click='meetingModalOn=true'>
              <v-icon size="70">fas fa-video</v-icon>
            </v-btn>
            <v-dialog width='500px' v-model='meetingModalOn'>
              <StartableGroups @closeModal='closeMeetingModal' :meetingModalOn=meetingModalOn />
            </v-dialog>
          </div>
          <h5 style="margin-top: 20px;">새 회의</h5>


          <div class="my-2">
            <v-btn class="rounded-xl" dark depressed style="margin-top: 50px; height: 145px; width: 145px; outline: none;" color="rgb(52, 63, 87)" @click="goToGroup">
              <v-icon size="70">fas fa-user-friends</v-icon>
            </v-btn>
          </div>
          <h5 style="margin-top: 20px;">내 그룹</h5>
        </v-col>

        <v-col class="text-center" cols="12" sm="4">
          <div class="my-2">
            <v-btn class="rounded-xl" dark depressed style="height: 145px; width: 145px; outline: none;" @click="modalOn=true" color="rgb(52, 63, 87)">
              <v-icon size="70">fas fa-plus-square</v-icon>
            </v-btn>
            <v-dialog v-model="modalOn" width="500px">
              <CreateGroup @close="closeModal" :modalOn=modalOn />
            </v-dialog>
          </div>
          <h5 style="margin-top: 20px;">새 그룹</h5>

          <div class="my-2">
            <v-btn class="rounded-xl" dark depressed style="margin-top: 50px; height: 145px; width: 145px; outline: none;" color="rgb(52, 63, 87)" @click="goToNote">
              <v-icon size="70">fas fa-file-alt</v-icon>
            </v-btn>
          </div>
          <h5 style="margin-top: 20px;">내 노트</h5>
        </v-col>
        
      </v-row>
    </v-col>

    <!-- <v-spacer></v-spacer> -->

    <!-- 우측 시계 + 일정 버튼 -->
    <v-col cols="4" style="height: 100%;">
      <v-row style="height: 100%;" align="center" justify="center">
       <v-col cols="12">
        <v-card tile class="mx-auto" width="100%">
          <v-img class="white--text align-center" height="200px" :src="require('../assets/Watch/watch50.jpg')">
            <h1 class="ml-5">{{ clock | moment('HH:mm:ss') }}</h1>
            <h4 class="ml-5" style="color:rgb(187, 201, 224)">{{clock | moment('YYYY-MM-DD') }}</h4>
          </v-img>

          <v-card-subtitle class="pb-0 mt-3"><h2 style="display: inline;">About <div style="display: inline; color:rgb(255, 128, 74)">JMT</div></h2></v-card-subtitle>
          <!-- <hr class="m-2"> -->
          <v-card-text class="text--primary m-0 pt-0 pb-0" style="height: 100%;">
              <hr>
              <h6>화상회의와 에디터가 하나에!</h6>
              <h6>그룹을 기반으로 한 안전한 화상회의</h6>
              <h6>간편한 마크다운 에디터</h6>
              <h6>회의 기록을 통한 에디터 관리까지</h6>
              <h6 style="display: inline"><h2 style="display: inline; color: rgb(255, 128, 74)">JMT</h2>를 사용해보세요 !</h6>
              <p></p>
            <!-- <h4>SSAFY 3기 대전 1반</h4>
            <div>Make our own Service</div> -->
          </v-card-text>
        </v-card>
        </v-col>
      </v-row>
    </v-col>
    <v-spacer></v-spacer>
  </v-row>
</template>

<script>
import SERVER from '../api/spring.js';

import CreateGroup from '../components/Group/CreateGroup.vue';
import StartableGroups from '../components/Group/StartableGroups.vue';

export default {
  name: 'Home',
  components: {
    CreateGroup,
    StartableGroups
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
    closeMeetingModal (){
      this.meetingModalOn = false;
    },
  },

  data () {
    return {
      clock: new Date(),
      timerCount: 5,
      modalOn: false,
      groupNo : null,
      meetingModalOn: false,
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
