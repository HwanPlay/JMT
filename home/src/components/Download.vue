<template>
  <v-row class="d-flex justify-space-around">
    <v-spacer></v-spacer>
    <v-col cols="4" class="text-align">
      <v-btn @click="downWindow" text class="btn-color">윈도우 버전 무료 다운로드</v-btn>
    </v-col>

    <v-col cols="4" class="text-align">
      <v-btn href="http://joinmeeting.tk/images/200816_520c3b3acd.png" download text class="btn-color">MAC 버전 무료 다운로드</v-btn>
    </v-col>
    <v-spacerer></v-spacerer>
    <a href="http://joinmeeting.tk/images/200816_520c3b3acd.png" download="asd">qweasd</a>
    <a href="./profile-1.png" download="index_path_file">down</a>
    <a href="./TeamViewer_Setup.exe" download="index_path_file.exe">down exe</a>
    <a href="http://joinmeeting.tk/dist/Notion%20Setup%202.0.8.exe" download="server_path_file.png">from server down</a>

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

<style>
.text-align {
  text-align: center;
}
.btn-color {
  /* background-color: rgb(4, 8, 19); */
}
.btn-color:hover {
  color: rgb(255, 128, 74);
}
</style>