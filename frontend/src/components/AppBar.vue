<template>
  <div style="width:100%; height: 30px; margin:0px; padding: 0px">
    <v-row style="height:30px; width:100%;">
      <div class="float-left">
        <v-btn text class="ml-3">Service</v-btn>
        <v-btn text @click="back">←</v-btn>
        <v-btn text @click="forward">→</v-btn>
        <v-btn text @click="reload">♬</v-btn>
      </div>
      <v-spacer style="-webkit-app-region: drag;"></v-spacer>
      <div class="float-right">
        <v-btn @click="minimize" text>_</v-btn>
        <v-btn @click="maximize" text>ㅁ</v-btn>
        <v-btn @click="exit" text>X</v-btn>
      </div>
    </v-row>
  </div>

</template>

<script>

export default {
  name : 'AppBar',
  methods:{
    back(){
      this.$router.go(-1)
    },
    forward(){
      this.$router.go(1)
    },
    reload(){
      this.$router.go(0)
    },
    minimize(){
      const remote = require('electron').remote
      const win = remote.BrowserWindow.getFocusedWindow();
      win.minimize()
      console.log('mini')
    },
    maximize(){
      console.log('maxi')
    },
    exit(){
      console.log("exit!")
    }
  }
}
</script>

<style>
  .v-btn{
    padding: 8px;
    margin: 0px;
  }
  .left-box{
    float: left;
  }
  .right-box{
    float: right;
  }
</style>