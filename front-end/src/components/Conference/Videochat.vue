<template>
<div class="MainContainer">
  <div class="MainContent">
    <div class="video_list">
        <vue-webrtc ref="webrtc"
                    width="100%"
                    :roomId="roomId"
                    v-on:joined-room="logEvent"
                    v-on:left-room="logEvent"
                    v-on:opened-room="logEvent"
                    v-on:share-started="logEvent"
                    v-on:share-stopped="logEvent"
                    @error="onError" />
              <div class="footer">
                <div class="RoomInput">
                  채팅방이름을 입력하세요<input v-model="roomId" id="RoomInput">
                </div>

                <div class="MenuBtn">
                  <button class="btn btn-primary" @click="onJoin">Join</button>
                  <button class="btn btn-success" @click="onLeave">Leave</button>
                  <button class="btn btn-info" @click="onCapture">Capture Photo</button>
                  <button class="btn btn-warning" @click="onShareScreen">Share Screen</button>
                  <button class="btn btn-chatting" @click="onChat" style="background-color: #f44336;">Chatting</button>
                  <!-- <button class="btn btn-Note" @click="$emit('noteonoff')" style="background-color: gray;" >Note</button> -->
                  <button class="btn btn-Note" @click="onNote" style="background-color: gray;" >Note</button>
                  <button class="btn btn-danger" @click="onCanvas" :disabled="disableCanvasBool"  >Canvas</button>
                </div>
                <div id="widget-container"></div>
            </div>
    </div>


  </div>
    <div id="chat-container">
        <input type="text" id="input-text-chat" placeholder="Enter Text Chat" @keyup.13="textSend" :disabled="disableInputBool" />
        <br />
        <div id="container">
          <div class="chat-output"></div>
        </div>  
    </div>
    <!-- <div class="row">
      <div class="col-md-12 my-3">
        <h2>Room</h2>
        <input v-model="roomId">
      </div>
    </div> -->


    <!-- <div class="row">
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
    </div> -->
    
  </div>

</template>

<script src="app.js"></script>
<script>
import $ from 'jquery';
import Vue from 'vue';
import WebRTC from 'vue-webrtc';
import CanvasDesigner from '@/assets/canvas-designer-widget';

Vue.use(WebRTC)

export default {
  data() {
    return {
      img: null,
      roomId: 'public-room',
      disableInputBool : true, 
      disableCanvasBool : true,
      chatContainer : null,
      value : '',
      textArea: null,
      designer : null,
      connection: null,
      NoteShow:{},
      NoteBool: true
    };
  },
  methods: {
    onNote(){
      this.$emit("Videochat",this.NoteBool);
    },
    onChat(){
      $("#chat-container").toggle();
    },
    onCanvas(){
      this.disableCanvasBool = true;
      this.designer.widgetHtmlURL = 'https://www.webrtc-experiment.com/Canvas-Designer/widget.html'; 
      this.designer.widgetJsURL = 'https://www.webrtc-experiment.com/Canvas-Designer/widget.js';
      this.designer.appendTo(document.getElementById('widget-container'));

    },
    onCapture() {
      this.img = this.$refs.webrtc.capture();
    },
    onJoin() {
      this.$refs.webrtc.join();
      this.disableCanvasBool = false;
      this.disableInputBool = false;
    },
    onLeave() {
      this.$refs.webrtc.leave();
      // this.$refs.webrtc.rtcmConnection.onclose();
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
    this.designer = new CanvasDesigner();
 }
}
</script>

<style scoped>

.video_list{
  float: left;
  position: relative;
  height: 700px;
  width: 100%;
}

#chat-container{
  display: none;
  float: left;
  width: auto;
  height: 100%;
}
#container {
  background-color: darkgrey;
  position: relative;
  border: 1px #ddd solid;
  height: 180px;
  overflow-y: auto;
}

.chat-output {
  background-color:lightblue;
  position: absolute;
  bottom: 0px;
}

.action {
  font-style: italic;
  color: gray;
}

.you {
  font-weight: bold;
}

.footer{
  float: left;
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  padding: 0;
	text-align: center;
	color: black;
}
.main{
  background-color: rgb(7, 14, 29);
  
}
.MainContainer{
    position: relative;
    margin-top: 50px;
    background-color: rgb(52, 63, 87);
    width: 100%;
    height: 100%;
}
.RoomInput{

  font-size: 20px;
}
#RoomInput{
    width:100%;
    border:2px solid #aaa;
    border-radius:4px;
    margin:8px 0;
    outline:none;
    padding:8px;
    box-sizing:border-box;
    transition:.3s;
}
#RoomInput:focus{
    border-color:dodgerBlue;
    box-shadow:0 0 8px 0 dodgerBlue;
}
.btn{
  -webkit-appearance:default-button;
  font-size: 12px;
}

#widget-container{
  position: relative; 
  bottom: 0;
  height: 100%; 
  border-top:0; 
  border-bottom: 0;
  margin-top: 50px;
}
#input-text-chat{

    width:30%;
    border:2px solid #aaa;
    border-radius:4px;
    margin:8px 0;
    outline:none;
    padding:8px;
    box-sizing:border-box;
    transition:.3s;
    background-color: white;
}
</style>
