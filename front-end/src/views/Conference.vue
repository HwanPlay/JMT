<template>
  <div class="container">
    <div class="row">
      <div class="col-md-12 my-3">
        <h2>Room</h2>
        <input v-model="roomId">
      </div>
    </div>
    <div class="row">
      <div class="col-md-12">
        <div class="">
          <vue-webrtc ref="webrtc"
                      width="100%"
                      :roomId="roomId"
                      v-on:joined-room="logEvent"
                      v-on:left-room="logEvent"
                      v-on:opened-room="logEvent"
                      v-on:share-started="logEvent"
                      v-on:share-stopped="logEvent"
                      @error="onError" />
        </div>
        <div class="row">
          <div class="col-md-12 my-3">
            <button type="button" class="btn btn-primary" @click="onJoin">Join</button>
            <button type="button" class="btn btn-primary" @click="onLeave">Leave</button>
            <button type="button" class="btn btn-primary" @click="onCapture">Capture Photo</button>
            <button type="button" class="btn btn-primary" @click="onShareScreen">Share Screen</button>
          </div>
        </div>
      </div>
    </div>
    <div id="chat-container">
      <input type="text" id="input-text-chat" placeholder="Enter Text Chat" @keyup.13="textSend" :disabled="disableInputBool" />
      <br />
      <div class="chat-output"></div>
    </div>

    <div class="row">
      <div class="col-md-12">
        <h2>Captured Image</h2>
        <figure class="figure">
          <img :src="img" class="img-responsive" />
        </figure>
      </div>
    </div>
  </div>

</template>

<script src="app.js"></script>
<script>
import Vue from 'vue'
import WebRTC from 'vue-webrtc'

Vue.use(WebRTC) //asda

export default {
  data() {
    return {
      img: null,
      roomId: "public-room",
      disableInputBool : true,
      chatContainer : null,
      value : "",
      textArea: null
    };
  },
  methods: {
    onCapture() {
      this.img = this.$refs.webrtc.capture();
    },
    onJoin() {
      this.$refs.webrtc.join();
      this.disableInputBool = false;
    },
    onLeave() {
      this.$refs.webrtc.leave();
    },
    onShareScreen() {
      this.img = this.$refs.webrtc.shareScreen();
    },
    onError(error, stream) {
      console.log('On Error Event', error, stream);
    },
    logEvent(event) {
      console.log('Event : ', event);
    },
    appendDIV(event) {
          this.textArea = document.createElement('div');
          this.textArea.innerHTML = event.data || event;
          this.chatContainer.appendChild(this.textArea);
          this.textArea.tabIndex = 0;
          this.textArea.focus();
          document.getElementById('input-text-chat').focus();
      },
    textSend(e) {
      console.log(e.target.value);
      // removing trailing/leading whitespace
      this.value = 'a'+':'+e.target.value.toString().replace(/^\s+|\s+$/g, '');
      // .replace(/^\s+|\s+$/g,'') : 앞뒤 공백 제거
      this.$refs.webrtc.rtcmConnection.send(this.value);
      this.appendDIV(this.value);
      e.target.value =  '';
      },
  },
  updated() {
      this.$refs.webrtc.rtcmConnection.onmessage = this.appendDIV;
  },
  mounted() {
    this.chatContainer = document.querySelector('.chat-output');
  }
}
</script>

<style>

</style>
