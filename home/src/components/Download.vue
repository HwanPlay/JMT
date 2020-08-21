<template>
  <v-row class="d-flex justify-space-around">
    <v-spacer></v-spacer>
    <v-col cols="6" class="text-align">
      <v-btn href="http://joinmeeting.tk/dist/JoinMeetingTyping%Setup%0.1.0.exe" download text class="btn btn-color"><span>윈도우 버전 무료 다운로드</span></v-btn>
    </v-col>
    <v-spacer></v-spacer>
    <v-col cols="6" class="text-align">
      <v-btn  href="http://joinmeeting.tk/dist/JoinMeetingTyping-0.1.0.dmg" download text class="btn btn-color"><span>MAC 버전 무료 다운로드</span></v-btn>
    </v-col>

  </v-row>
</template>

<script>
export default {
  name: 'Download',
  methods: {
    downWindow() {

    }
  }
}
</script>

<style scoped>
.text-align {
  text-align: center;
}
.btn-color {
  /* color : blue; */
}
.btn-color:hover {
  color: rgb(255, 128, 74);
}
span {
  font-size : 30px;
}
</style>