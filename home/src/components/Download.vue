<template>
  <v-row class="d-flex justify-space-around">
    <v-spacer></v-spacer>
    <v-col cols="4" class="text-align">
      <v-btn @click="downWindow" text class="btn-color">윈도우 버전 무료 다운로드</v-btn>
    </v-col>

    <v-col cols="4" class="text-align">
      <v-btn text class="btn-color">MAC 버전 무료 다운로드</v-btn>
    </v-col>
    <v-spacer></v-spacer>
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