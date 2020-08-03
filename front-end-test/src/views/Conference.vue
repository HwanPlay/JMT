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
    <div class="row">
      <div class="col-md-12">
        <select id="picture" class="image-picker show-html"></select>
        <video autoplay></video>
        <p><button class="bttn">Enable Capture</button></p>
      </div>
    </div>
  </div>

</template>

<script>
import Vue from 'vue'
import WebRTC from 'vue-webrtc'

Vue.use(WebRTC) //asda

export default {
  data() {
    return {
      img: null,
      roomId: "jmt-room",
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

<style scoped>
  body {
    background: white;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
  }
  video {
    width: 480px;
    height: 360px;
    background: rgba(0,0,0,0.25);
  }
  .bttn {
    display: inline-block;
    background: -webkit-linear-gradient(#F9F9F9 40%, #E3E3E3 70%);
    background: linear-gradient(#F9F9F9 40%, #E3E3E3 70%);
    border: 1px solid #999;
    -webkit-border-radius: 3px;
    border-radius: 3px;
    padding: 5px 8px;
    outline: none;
    white-space: nowrap;
    -webkit-user-select: none;
    user-select: none;
    cursor: pointer;
    text-shadow: 1px 1px #fff;
    font-weight: 700;
    font-size: 10pt;
  }
  .bttn:hover,
  .bttn.active {
    border-color: black;
  }
  .bttn:active,
  .bttn.active {
    background: -webkit-linear-gradient(#E3E3E3 40%, #F9F9F9 70%);
    background: linear-gradient(#E3E3E3 40%, #F9F9F9 70%);
  }
  video {
    background: gray;
    border: 1px solid #e2e2e2;
    box-shadow: 0 1px 1px rgba(0,0,0,0.2);
  } 
</style>
