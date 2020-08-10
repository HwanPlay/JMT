<template>
  <div class="MainContainer">
<<<<<<< HEAD
    <div class="video_list">
      <div class="video_list_videOrshow" @click="videoBar">
        <span class="triangle test_1"></span>
=======
    <div class="MainContent">
        <div class="Minivideo_list"><div class="videos-container" id="videos-container" style="display:none" ></div></div>
        
      <div class="video_list_videOrshow" @click="videoBar">
          <span class="triangle test_1"></span>
      </div>
      
      <div class="Mainvideo">
        <div class="Main-videos-container" id="Main-videos-container" >
        </div>
              <div class="footer">
                <div class="RoomInput">
                  채팅방이름을 입력하세요
                  <input type="text" placeholder="채팅방 이름을 입력해주세요" v-model="roomid"> 
                </div>
                <div class="MenuBtn">
                  <button type="button" class="btn btn-success btn-round btn-lg bg-primary border-0" @click="onJoin"><div class="btnIcon"><b-icon icon="camera-video" font-scale="2"></b-icon></div></button>
                  <button type="button" class="btn btn-success btn-round btn-lg bg-primary border-0" @click="offLocalVideo"><div class="btnIcon"><b-icon icon="camera-video-off-fill" font-scale="2"></b-icon></div></button>
                  <button type="button" id="audio" class="btn btn-success btn-round btn-lg bg-primary border-0" @click="offMic" ><div class="btnIcon"><b-icon icon="Mic-mute-fill" font-scale="2"></b-icon></div></button>
                  <!-- <button type="button" class="btn btn-success btn-round" @click="onCapture"><b-icon icon="camera"></b-icon></button> -->
                  <!-- <button type="button" class="btn btn-success btn-round btn-lg" @click="onShareScreen"><b-icon icon="box-arrow-up"></b-icon></button> -->
                  <button type="button" class="btn btn-success btn-round btn-lg bg-primary border-0" @click="onChat"><div class="btnIcon"><b-icon icon="chat-dots-fill" font-scale="2"></b-icon></div></button>
                  <button type="button" class="btn btn-success btn-round btn-lg bg-primary border-0" @click="onNote"><div class="btnIcon"><b-icon icon="markdown-fill" font-scale="2"></b-icon></div></button>
                  <!-- <button type="button" class="btn btn-success btn-round btn-lg" @click="onCanvas"><b-icon icon="pencil-square"></b-icon></button> -->
                  <!-- <button type="button" class="btn btn-success btn-round btn-lg" @click="onCanvas"><b-icon icon="share-fill"></b-icon></button> -->
                  <button type="button" class="btn btn-success btn-round btn-lg bg-primary border-0" @click="onBroadcast"><div class="btnIcon"><b-icon icon="eject-fill" font-scale="2"></b-icon></div></button>
                  <button type="button" class="btn btn-success btn-round btn-lg bg-primary border-0" @click="offBroadcast"><div class="btnIcon"><b-icon icon="eject-fill" font-scale="2" style="transform: rotateZ(180deg); "></b-icon></div></button>
                  <div class="container">

                </div>
                </div>
              </div>
>>>>>>> feature-front-conference-video
      </div>
      <vue-webrtc
        ref="webrtc"
        width="100%"
        :roomId="roomId"
        v-on:joined-room="logEvent"
        v-on:left-room="logEvent"
        v-on:opened-room="logEvent"
        v-on:share-started="logEvent"
        v-on:share-stopped="logEvent"
        @error="onError"
      />
      <broad-cast
        ref="broadcast"
        width="100%"
        height="100%"
        :roomId="roomId"
        v-on:joined-room="logEvent"
        v-on:left-room="logEvent"
        v-on:opened-room="logEvent"
        v-on:share-started="logEvent"
        v-on:share-stopped="logEvent"
        @error="onError"
      />
    </div>

<<<<<<< HEAD
    <div class="footer">
      <div class="RoomInput">
        채팅방이름을 입력하세요
        <input v-model="roomId" id="RoomInput" style="color: white;" />
      </div>
      <div class="MenuBtn">
        <button
          type="button"
          class="conf-btn btn-success btn-round btn-lg bg-primary border-0"
          @click="onJoin"
        >
          <div class="btnIcon">
            <b-icon icon="camera-video" font-scale="2"></b-icon>
          </div>
        </button>
        <button
          type="button"
          class="conf-btn btn-success btn-round btn-lg bg-primary border-0"
          @click="onLeave"
        >
          <div class="btnIcon">
            <b-icon icon="camera-video-off-fill" font-scale="2"></b-icon>
          </div>
        </button>
        <!-- <button type="button" class="btn btn-success btn-round" @click="onCapture"><b-icon icon="camera"></b-icon></button> -->
        <!-- <button type="button" class="btn btn-success btn-round btn-lg" @click="onShareScreen"><b-icon icon="box-arrow-up"></b-icon></button> -->
        <button
          type="button"
          class="conf-btn btn-success btn-round btn-lg bg-primary border-0"
          @click="onChat"
        >
          <div class="btnIcon">
            <b-icon icon="chat-dots-fill" font-scale="2"></b-icon>
          </div>
        </button>
        <button
          type="button"
          class="conf-btn btn-success btn-round btn-lg bg-primary border-0"
          @click="onNote"
        >
          <div class="btnIcon">
            <b-icon icon="markdown-fill" font-scale="2"></b-icon>
          </div>
        </button>
        <!-- <button type="button" class="btn btn-success btn-round btn-lg" @click="onCanvas"><b-icon icon="pencil-square"></b-icon></button> -->
        <!-- <button type="button" class="btn btn-success btn-round btn-lg" @click="onCanvas"><b-icon icon="share-fill"></b-icon></button> -->
        <button
          type="button"
          class="conf-btn btn-success btn-round btn-lg bg-primary border-0"
          @click="onBroadcast"
        >
          <div class="btnIcon">
            <b-icon icon="eject-fill" font-scale="2"></b-icon>
          </div>
        </button>
        <button
          type="button"
          class="conf-btn btn-success btn-round btn-lg bg-primary border-0"
          @click="offBroadcast"
        >
          <div class="btnIcon">
            <b-icon icon="eject-fill" font-scale="2" style="transform: rotateZ(180deg); "></b-icon>
          </div>
        </button>
        <div class="container"></div>
      </div>
=======


>>>>>>> feature-front-conference-video
    </div>

    <div id="note-container">
      <NoteEditor />
    </div>
    <div id="chat-container">
<<<<<<< HEAD
      <div id="container">
        <div class="row header-one text-white p-1">
          <div class="col-md-8 name pl-2">
            <i class="fa fa-comment"></i>
            <h6 class="ml-1 mb-0">Ketty Peris</h6>
          </div>
          <div class="col-md-4 options text-right pr-0">
            <i class="fa fa-times hover text-center pt-1"></i>
          </div>
        </div>

        <div class="chat-content">
          <div class="chats pt-3 pl-2 pr-3">
            <div class="chat-output">
              <!-- <ul class="p-0">
                <li class="receive-msg float-left mb-2">
                    <div class="sender-img">
                        <img src="http://nicesnippets.com/demo/image1.jpg" class="float-left">
                    </div>
                    <div class="receive-msg-desc float-left ml-2">
                        <p class="bg-white m-0 pt-1 pb-1 pl-2 pr-2 rounded">
                            Yes always
                        </p>
                    </div>
                </li>
              </ul>-->
            </div>
          </div>
=======
        <div id="container">
          
                    <div class="row header-one text-white p-1">
                        <div class="col-md-8 name pl-2">
                            <i class="fa fa-comment"></i>
                            <h6 class="ml-1 mb-0">Ketty Peris</h6>
                        </div>
                        <div class="col-md-4 options text-right pr-0">
                            <i class="fa fa-times hover text-center pt-1"></i>
                        </div>
                    </div> 

             <div class="chat-content">
                    <div class="chats pt-3 pl-2 pr-3 ">
                      <div class="chat-output">
                      </div>
                    </div>
              </div>
              <input type="text" id="input-text-chat" placeholder="Enter Text Chat" @keyup.13="textSend" :disabled="disableInputBool"/>
>>>>>>> feature-front-conference-video
        </div>

        <input
          type="text"
          id="input-text-chat"
          placeholder="Enter Text Chat"
          @keyup.13="textSend"
          :disabled="disableInputBool"
        />
        <!-- <div class="chat-output"></div> -->
      </div>
    </div>
  </div>
</template>
<script src="https://cdn.webrtc-experiment.com/FileBufferReader.js"></script>
<!-- socket.io for signaling -->
<script src="https://rtcmulticonnection.herokuapp.com/socket.io/socket.io.js"></script>

<script>
<<<<<<< HEAD
=======
import RTCMultiConnection from '../../api/RTCMultiConnection';
import Broadcast from '../../api/broadcast'
// import Sharescreen from './Sharescreen.vue';
>>>>>>> feature-front-conference-video
import $ from 'jquery';
import Vue from 'vue';
// import WebRTC from '../../api/webrtc';
import CanvasDesigner from '../../assets/canvas/canvas-designer-widget';
<<<<<<< HEAD
import BroadCast from '../../api/broadcast';
import NoteEditor from './ConfNoteEditor';
Vue.use(WebRTC);
Vue.use(BroadCast);
=======
// import BroadCast from '../../api/broadcast';
import NoteEditor from "./ConfNoteEditor";


// Vue.use(WebRTC)
// Vue.use(BroadCast)

>>>>>>> feature-front-conference-video

export default {
  name: 'Videochat',
  components: {
<<<<<<< HEAD
    NoteEditor,
=======
    // Sharescreen,
    NoteEditor,
    RTCMultiConnection,
    Broadcast
>>>>>>> feature-front-conference-video
  },
  data() {
    return {
      img: null,
      roomId: 'public-room',
      disableInputBool: true,
      disableCanvasBool: true,
      chatContainer: null,
      value: '',
      textArea: null,
      designer: null,
      connection: null,
      NoteShow: {},
      NoteBool: false,
      Chatbool: false,
      Bar: false,
      video: Object,
      div : null,
      inputText: "",
      message: 'Default Message',
      broadcast: null,
      AudioBool : false,

    };
  },
  methods: {
<<<<<<< HEAD
    offBroadcast() {
      this.$refs.broadcast.offbroadcast();
    },
    onBroadcast() {
      console.log('브로드캐스팅');
      this.$refs.broadcast.onbroadcast();
      this.disableCanvasBool = false;
      this.disableInputBool = false;
    },
    videoBar() {
      $('.video-list-1').toggle();
      this.Bar = !this.Bar;
      if (this.Bar == false) {
        $('.video-list-2').css('height', '70%');
      } else {
        $('.video-list-2').css('height', '83.6%');
      }
    },
    onNote() {
      $('#note-container').toggle();
      if (this.NoteBool == false && this.Chatbool == false) {
        $('.video_list').css('width', '70%');
        $('.footer').css('width', '70%');
        this.NoteBool = true;
      } else if (this.NoteBool == true && this.Chatbool == false) {
        $('.video_list').css('width', '100%');
        $('.footer').css('width', '100%');
        this.NoteBool = false;
      } else if (this.NoteBool == false && this.Chatbool == true) {
        $('.video_list').css('width', '50%');
        $('.footer').css('width', '50%');
        this.NoteBool = true;
      } else {
        $('.video_list').css('width', '80%');
        $('.footer').css('width', '80%');
=======
    offMic(){
        if(this.AudioBool == false){
          let localStream = this.connection.attachStreams[0];
          localStream.mute('audio');
          this.AudioBool = !this.AudioBool;
          console.log(this.AudioBool);
        }
        else{
          let localStream = this.connection.attachStreams[0];
          localStream.unmute('audio');
          this.AudioBool = !this.AudioBool;
          console.log(this.AudioBool);
        }
    },
    offBroadcast(){
      this.broadcast.dontAttachStream = true;
    },
    onBroadcast(){

      this.broadcast.session = {
        video : true,
        audio : true
      };
      this.broadcast.openOrJoin(this.roomid+'a');
    },
    videoBar(){
       $(".Minivideo_list").toggle();
       this.Bar = !this.Bar;
       if(this.Bar == false){
           $(".Mainvideo").css("height","88%");
       }
       else{
         $(".Mainvideo").css("height","100%");
       }
    },
    onNote() {
      $("#note-container").toggle();
      if(this.NoteBool == false && this.Chatbool==false){
        $(".MainContent").css("width","70%");
        this.NoteBool = true;
      }
      else if(this.NoteBool == true && this.Chatbool==false){
        $(".MainContent").css("width","100%");
        this.NoteBool = false;
      }
      else if(this.NoteBool == false && this.Chatbool==true){
        $(".MainContent").css("width","50%");
        this.NoteBool = true;
      }
      else{
        $(".MainContent").css("width","80%");
>>>>>>> feature-front-conference-video
        this.NoteBool = false;
      }
    },
    onChat() {
<<<<<<< HEAD
      $('#chat-container').toggle();
      if (this.Chatbool == false && this.NoteBool == false) {
        $('.video_list').css('width', '80%');
        $('.footer').css('width', '80%');
        this.Chatbool = true;
      } else if (this.Chatbool == true && this.NoteBool == false) {
        $('.video_list').css('width', '100%');
        $('.footer').css('width', '100%');
        this.Chatbool = false;
      } else if (this.Chatbool == false && this.NoteBool == true) {
        $('.video_list').css('width', '50%');
        $('.footer').css('width', '50%');
        this.Chatbool = true;
      } else {
        $('.video_list').css('width', '70%');
        $('.footer').css('width', '70%');
=======
      $("#chat-container").toggle();
      if(this.Chatbool == false && this.NoteBool==false){
        $(".MainContent").css("width","80%");
        this.Chatbool = true;
      }
      else if(this.Chatbool == true && this.NoteBool==false){
        $(".MainContent").css("width","100%");
        this.Chatbool = false;
      }
      else if(this.Chatbool == false && this.NoteBool==true){
        $(".MainContent").css("width","50%");
        this.Chatbool = true;
      }
      else{
        $(".MainContent").css("width","70%");
>>>>>>> feature-front-conference-video
        this.Chatbool = false;
      }
    },
    onCanvas() {
      this.disableCanvasBool = true;
      this.designer.widgetHtmlURL =
        'https://www.webrtc-experiment.com/Canvas-Designer/widget.html';
      this.designer.widgetJsURL =
        'https://www.webrtc-experiment.com/Canvas-Designer/widget.js';
      this.designer.appendTo(document.getElementById('widget-container'));
    },
    onCapture() {
      this.img = this.$refs.webrtc.capture();
    },
    onJoin() {
<<<<<<< HEAD
      this.$refs.webrtc.join();
      this.disableCanvasBool = false;
      this.disableInputBool = false;

      $('.video-item')
        .find('video')
        .each(function (i, e) {
          console.log($(this).get());
        });
=======
      this.disableInputBool = false
      this.connection.session = {
        data: true,
        video : true,
        audio : true
      };
      this.connection.openOrJoin(this.roomid);
      document.getElementById("videos-container").style.display="block";

>>>>>>> feature-front-conference-video
    },
    offLocalVideo(){
          this.connection.dontAttachStream = true;
          this.connection.attachStreams.forEach(function(localStream) {
              localStream.stop();
          });
    },
    onLeave() {
    this.connection.dontAttachStream = true;
    this.broadcast.dontAttachStream = true;
    // stop all local cameras
    this.connection.attachStreams.forEach(function(localStream) {
        localStream.stop();
    });
    this.broadcast.attachStreams.forEach(function(localStream) {
        localStream.stop();
    });
    document.getElementById("videos-container").style.display="none";
    },
    // onShareScreen() {
    //   this.img = this.$refs.webrtc.shareScreen();
    // },
    onError(error, stream) {
      console.log('On Error Event', error, stream);
    },
    logEvent(event) {
      console.log('Event : ', event);
    },
    appendDIV(event) {
      this.textArea = document.createElement('div');
      this.textArea.innerHTML =
        '<ul class=\'p-0\'><li class=\'receive-msg float-left mb-2\'><div class=\'sender-img\'><img src=\'http://nicesnippets.com/demo/image1.jpg\' class=\'float-left\'></div><div class=\'receive-msg-desc float-left ml-2\'><p class=\'bg-white m-0 pt-1 pb-1 pl-2 pr-2 rounded\'>' +
        (event.data || event) +
        '</p></div></li></ul>';
      this.chatContainer.appendChild(this.textArea);
      this.textArea.tabIndex = 0;
      // this.textArea.style.whiteSpace = 'normal';
      this.textArea.focus();
      document.getElementById('input-text-chat').focus();
    },
    textSend(e) {
      console.log(e.target.value);
      // removing trailing/leading whitespace
      this.value =
        'a' + ':' + e.target.value.toString().replace(/^\s+|\s+$/g, '');
      // .replace(/^\s+|\s+$/g,'') : 앞뒤 공백 제거
      this.connection.send(this.value);
      this.appendDIV(this.value);
      e.target.value = '';
    },
  },
<<<<<<< HEAD
  updated() {
    this.$refs.webrtc.rtcmConnection.onmessage = this.appendDIV;
    $('video').click(function () {
      console.log('클릭');
    });
  },
  mounted() {
    this.chatContainer = document.querySelector('.chat-output');
=======
   updated() {
      this.connection.onmessage = this.appendDIV;
    },
  
  created() {
     this.connection = new RTCMultiConnection();
     this.broadcast = new Broadcast();
     this.connection.socketURL = "https://rtcmulticonnection.herokuapp.com:443/";    
     this.broadcast.socketURL = "https://rtcmulticonnection.herokuapp.com:443/";  
     let src2 = document.createElement("script");
     src2.setAttribute(
       "src",
       "https://cdn.webrtc-experiment.com/FileBufferReader.js"
     );
     document.body.appendChild(src2);

     let src3 = document.createElement("script");
     src3.setAttribute(
       "src",
       "https://rtcmulticonnection.herokuapp.com/socket.io/socket.io.js"
    );
     document.body.appendChild(src3);
    
  },
  mounted() {
    this.chatContainer = document.querySelector(".chat-output");
    this.connection.videosContainer = document.querySelector('.videos-container');
    this.broadcast.videosContainer = document.querySelector('.Main-videos-container');
>>>>>>> feature-front-conference-video
    this.designer = new CanvasDesigner();
  },
  destroyed(){
    this.onLeave();
  }
};
</script>

<style>
.MainContainer {
  position: relative;
  background-color: rgb(52, 63, 87);
  width: 100%;
  height: 100%;
  overflow-y: auto;
}
.video_list {
  float: left;
  position: relative;
  top: 0;
  bottom: 0;
  height: 100px;
  width: 100%;
  overflow-y: auto;
  background-color: black;
}
.videos-container video{
  height: 100px;
  overflow-x: hidden;
 
}
.Main-videos-container{
  height: 100%;
}
.Main-videos-container video{
  height: 90%;
  overflow-x: hidden;
 
}
.MainContent{
  position: relative;
  height: 100%;
  width: 100%;
  float: left;
}
.Minivideo_list{
  position: relative;
  height: 100px;
  width: 100%;
  background-color: black;
  border: 2px solid red;
  white-space: nowrap;
  overflow-x: scroll;
  overflow-y: hidden;
}
.Mainvideo{
  position: relative;
  width: 100%;
  height: 88.2%;
  overflow-y: hidden;
  text-align: center;
  background-color:black;
}
#note-container {
  display: none;
  float: right;
  width: 30%;
  height: 100%;
  overflow-y: auto;
  background-color: white;
}

#chat-container {
  display: none;
  float: right;
  width: 20%;
  height: 100%;
  overflow-y: hidden;
}
#container {
  background-color: lightgrey;
  position: relative;
  border: 1px #ddd solid;
  height: 100%;
  /* overflow-y: auto; */
}

.chat-output {
  float: left;
  position: absolute;
  bottom: 0px;
}


.footer {
  float: left;
  position: absolute;
  left: 0;
  height: 100px;
  bottom: 0px;
  width: 100%;
  padding: 0;
  text-align: center;
  color: black;
  padding-bottom: 0px;
  background-color: rgb(52, 63, 87);
}
/* .MenuBtn {
  position: relative;
<<<<<<< HEAD
  z-index: 2;
} */
=======
  margin-top: 0;
  // background-color: rgb(52, 63, 87);
  width: 100%;
  height: 100%;
  overflow-y: auto;
}
>>>>>>> feature-front-conference-video
.RoomInput {
  position: relative;
  color: white;
  font-size: 20px;
}
#RoomInput {
  width: 100%;
  border: 2px solid #aaa;
  border-radius: 4px;
  margin: 8px 0;
  outline: none;
  padding: 8px;
  box-sizing: border-box;
  transition: 0.3s;
}
#RoomInput:focus {
  border-color: dodgerBlue;
  box-shadow: 0 0 8px 0 dodgerBlue;
}
.conf-btn {
  -webkit-appearance: default-button;
  font-size: 12px;
}

#widget-container {
  display: none;
  position: relative;
  bottom: 0;
  height: 100px;
  border-top: 0;
  border-bottom: 0;
  margin-top: 50px;
}
#input-text-chat {
<<<<<<< HEAD
  position: relative;
  height: 70px;
=======
  position: relative;;
  height: 83px;
>>>>>>> feature-front-conference-video
  width: 100%;
  border: 2px solid #aaa;
  border-radius: 4px;
  outline: none;
  padding: 8px;
  background-color: white;
  z-index: 4;
}

.video_list_videOrshow {
  position:absolute;
  left: 50%;
  width: auto;
  z-index: 7;
}

.triangle {
  display: inline-block;
  width: 20px;
  border-style: solid;
  border-width: 20px;
  transition: all ease 1s;
}

.triangle.test_1 {
  border-color: #7d1919 transparent transparent transparent;
}
.triangle.test_1:hover {
  border-color: blue transparent transparent transparent;
}

<<<<<<< HEAD
.video-list-1 {
  background: black;
  height: 100px;
  position: relative;
  /* z-index: 0; */
  overflow-x: scroll;
  overflow-y: hidden;
  white-space: nowrap;
  border: 2px solid red;
}
.video-list-2 {
  background: black;
  height: 70%;
  position: relative;
  width: 100%;
  z-index: 1;
  overflow-x: scroll;
  overflow-y: hidden;
  white-space: nowrap;
  text-align: center;
}

.video-item {
  background: black;
  height: 100px;
  position: relative;
  /* top: 0px; */
  /* z-index: 200; */
  overflow-x: auto;
  white-space: nowrap;
  display: inline-block;
}

.video-item-2 {
  background: black;
  height: 100%;
  width: 100%;
  /* top: 0px; */
  /* z-index: 200; */
  overflow-x: auto;
  white-space: nowrap;
  display: inline-block;
}
=======
>>>>>>> feature-front-conference-video

.btn-round {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.btn-round.btn-lg {
  width: 48px;
  height: 48px;
}

.btn-round.btn-sm {
  width: 34px;
  height: 34px;
}

.btn-round.btn-xs {
  width: 24px;
  height: 24px;
}
.btn btn-success btn-round btn-lg {
  position: absolute;
  left: 10px;
}
<<<<<<< HEAD
.btnIcon {
=======

.btnIcon{
>>>>>>> feature-front-conference-video
  position: relative;
  top: 0;
  left: -5px;
}

.chat-main {
  position: relative;
  width: 100%;
  height: 100%;
}
.header-one {
  margin: 0;
  height: 10%;
  position: relative;
  width: 100%;
<<<<<<< HEAD
  background: #404040;
}
.name h6 {
  display: inline-block;
  font-size: 14px;
}
.options i,
.options .arrow-up {
  height: 10px;
  width: 25px;
}
.options i {
  color: #b2b2b2;
  font-size: 16px;
  cursor: pointer;
}
.options .hover:hover,
.options .arrow-up:hover {
  background: #737373;
}
.options .arrow-up {
  display: inline-block;
  line-height: 0;
}
.options .hover:hover,
.options .arrow-up:hover .fa-arrow-up {
  color: #fff;
}
.options .fa-arrow-up {
  transform: rotate(40deg);
}
.header-two {
  border-top: 2px solid #35ac19;
  background: #eceff1;
  color: #5e6060;
  box-shadow: 0px 6px 13px -7px #c1c1c1;
  z-index: 1000;
  position: absolute;
}
.options-left i,
.options-right i {
  font-size: 20px;
  cursor: pointer;
}
.options-left i:hover,
.options-right i:hover {
  color: #000;
}
.chats {
  height: 100%;
  overflow-x: scroll;
  overflow-x: hidden;
  background: #eceff1;
  position: relative;
}
.chats ul li {
  display: inline-block;
  list-style: none;
  clear: both;
  font-size: 13px;
}
.send-msg {
  position: relative;
}
.send-msg:after {
  content: "";
  width: 0;
  height: 0;
  top: 0px;
  right: -8px;
  position: absolute;
  border-top: 8px solid #cfd8dc;
  border-left: 8px solid transparent;
  border-right: 8px solid transparent;
}
.send-msg p {
  background: #cfd8dc;
}
.sender-img {
  display: inline;
}
.sender-img img {
  width: 32px;
  height: 32px;
  border-radius: 100%;
}
.receive-msg .receive-msg-desc {
  display: inline-block;
  position: relative;
}
.receive-msg-desc:before {
  content: "";
  width: 0;
  height: 0;
  top: 0px;
  left: -8px;
  position: absolute;
  border-top: 8px solid #fff;
  border-left: 8px solid transparent;
  border-right: 8px solid transparent;
}
.receive-msg-time,
.send-msg-time {
  color: #7d7e87;
  font-size: 10px;
}
.receive-msg-time i {
  font-size: 4px;
}
=======
	background: #404040;
}

.options i,.options .arrow-up{
	height: 10px;
	width: 25px;
}
.options i{
	color: #B2B2B2;
	font-size: 16px;
	cursor: pointer;
}
.options .hover:hover, .options .arrow-up:hover{
	background: #737373;
}
.options .arrow-up{
    display: inline-block;
    line-height: 0;
}
.options .hover:hover, .options .arrow-up:hover .fa-arrow-up{
	color: #fff;
}
.options .fa-arrow-up{
	transform: rotate(40deg);
}
.options-left i, .options-right i{
	font-size: 20px;
	cursor: pointer;
}
.options-left i:hover, .options-right i:hover{
	color: #000;
}
.chats{
	height: 100%;
	overflow-x: scroll;
	overflow-x: hidden;
	background: #ECEFF1;
	position: relative;
}
.chats ul li{
	display: inline-block;
	list-style: none;
	clear: both;
	font-size: 13px;
}

.sender-img{
	display: inline;
}
.sender-img img{
	width: 32px;
	height: 32px;
	border-radius: 100%;
}

>>>>>>> feature-front-conference-video
.msg-box {
  margin-bottom: 0;
}
.msg-box i {
  color: #404040;
}
.msg-box input {
  font-size: 14px;
}
.msg-box input:focus {
  outline: none;
}

.chat-content {
  width: 100%;
  height: 80%;
  overflow: hidden;
}
<<<<<<< HEAD
=======

video::-webkit-media-controls {
  display: none;
}

>>>>>>> feature-front-conference-video
</style>
