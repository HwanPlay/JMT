<template>
<div class="MainContainer">
  <div class="MainContent">



    <div class="video_list">
      <div class="video_list_videOrshow" @click="videoBar">
      <span class="triangle test_1"></span>
      </div>
        <vue-webrtc ref="webrtc"
                    width ="100%"
                    :roomId="roomId"
                    v-on:joined-room="logEvent"
                    v-on:left-room="logEvent"
                    v-on:opened-room="logEvent"
                    v-on:share-started="logEvent"
                    v-on:share-stopped="logEvent"
                    @error="onError" />
              <div class="footer">
                <div class="RoomInput" >
                  채팅방이름을 입력하세요<input v-model="roomId" id="RoomInput" style="color: white;">
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
          <!-- <div id="widget-container"></div> -->
        </div>
    </div>

        <video class="ss" data-v-49ef9b35="" controls="controls" autoplay="autoplay" playsinline="" height="141" id="UlfcEJxrujOcm0U93w2jRYqQmKSYGPlz7yIl"></video>
  </div>
    <div id="note-container">
      <NoteEditor />
    </div>
    <div id="chat-container">

        <div id="container">
          <div class="chat-output"></div>
        </div>
        <input type="text" id="input-text-chat" placeholder="Enter Text Chat" @keyup.13="textSend" :disabled="disableInputBool" />

    </div>

    <!-- <div class="row">
      <div class="col-md-12 my-3">
        <h2>Room</h2>
        <input v-model="roomId">
      </div>
    </div>
    <div class="row">
      <div class="col-md-12">
        <Sharescreen />
      </div>
    </div>

    <div class="row">
      <div class="col-md-12">
        <h2>Captured Image</h2>
        <figure class="figure">
          <img :src="img" class="img-responsive" />
        </figure>
      </div>
    </div> -->
    <!-- <div class="row">
      <div class="col-md-12">
        <select id="picture" class="image-picker show-html"></select>
        <video autoplay></video>
        <p><button class="bttn">Enable Capture</button></p>
      </div>
    </div>
     -->
  </div>

</template>

<script src="app.js"></script>
<script>
import Sharescreen from './Sharescreen.vue';
import $ from 'jquery';
import Vue from 'vue';
import WebRTC from 'vue-webrtc';
import CanvasDesigner from '../../assets/canvas/canvas-designer-widget';
import NoteEditor from '../../components/Note/NoteEditor';
Vue.use(WebRTC)

export default {
  name: 'Videochat',
  components: {
    Sharescreen,
    NoteEditor,
  },
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
      NoteBool: false,
      Chatbool: false,
      Bar: false,
      video:  Object
    };
  },
  methods: {
    videoBar(){
       $(".video-list-1").toggle();
       this.Bar = !this.Bar;
       if(this.Bar == false){
           $(".ss").css("height","62%");
           $(".ss").css("top","165px");
       }
       else{
         $(".ss").css("height","74%");
         $(".ss").css("top","65px");
       }

       
    },
    onNote(){
      $("#note-container").toggle();
      if(this.NoteBool == false && this.Chatbool==false){
        $(".video_list").css("width","70%");
        $(".ss").css("width","70%");
        this.NoteBool = true;

      }
      else if(this.NoteBool == true && this.Chatbool==false){
        $(".video_list").css("width","100%");
        $(".ss").css("width","100%");
        this.NoteBool = false;
      }
      else if(this.NoteBool == false && this.Chatbool==true){
        $(".video_list").css("width","50%");
        $(".ss").css("width","50%");
        this.NoteBool = true;
      }
      else{
        $(".video_list").css("width","80%");
        $(".ss").css("width","80%");
        this.NoteBool = false;
        
      }
    },
    onChat(){
      $("#chat-container").toggle();
      if(this.Chatbool == false && this.NoteBool==false){
        $(".video_list").css("width","80%");
        $(".ss").css("width","80%");
        this.Chatbool = true;
      }
      else if(this.Chatbool == true && this.NoteBool==false){
        $(".video_list").css("width","100%");
        $(".ss").css("width","100%");
        this.Chatbool = false;
      }
      else if(this.Chatbool == false && this.NoteBool==true){
        $(".video_list").css("width","50%");
        $(".ss").css("width","50%");
        this.Chatbool = true;
      }
      else{
        $(".video_list").css("width","70%");
        $(".ss").css("width","70%");
        this.Chatbool = false;
      }
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
      $('video').click(function(){
        console.log("클릭");
      });
  },
  mounted() {
    this.chatContainer = document.querySelector('.chat-output');
    this.designer = new CanvasDesigner();

 }
}
</script>

<style lang="scss">

.video_list{
  float: left;
  position: absolute;
  top: 0;
  bottom: 0;
  height: 100%;
  width: 100%;
  overflow-y: auto;
}

#note-container{
  display: none;
  float: right;
  width: 30%;
  height: 100%;
  overflow-y: auto;
  background-color: white;
}


#chat-container{
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
  height: 94%;
  /* overflow-y: auto; */
}

.chat-output {
  background-color:white;
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
  bottom: 10px;
  width: 100%;
  padding: 0;
	text-align: center;
	color: black;
  padding-bottom: 0px;
}
.main{
  background-color: rgb(7, 14, 29);
  
}
.MainContainer{
    position: relative;
    margin-top: 0;
    background-color: rgb(52, 63, 87);
    width: 100%;
    height: 100%;
    overflow-y: auto;
}
.RoomInput{
  color: white;
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
  display: none;
  position: relative; 
  bottom: 0;
  height: 100px; 
  border-top:0; 
  border-bottom: 0;
  margin-top: 50px;
}
#input-text-chat{
    float:right;
    width:100%;
    border:2px solid #aaa;
    border-radius:4px;
    margin-left: 30px;
    outline:none;
    padding:8px;
    box-sizing:border-box;
    transition:.3s;
    background-color: white;
    overflow-y: auto;
    
}

.video_list_videOrshow{
   position: absolute;
   top: 66px;
   left: 50%;
   width: auto;
   text-align: center;
   z-index: 4;
}

.triangle {
  display:inline-block; 
  width:0; 
  height:0; 
  border-style:solid; 
  border-width:20px;
  transition: all ease 1s;
  }

.triangle.test_1 {
  border-color:#7d1919 transparent transparent transparent;
  }

.triangle.test_1:hover {
  border-color:blue transparent transparent transparent;
  }

.video-list-1{
  background:black; 
  height:100px; 
  width: 100%; 
  position:relative; 
  top:65px; z-index: 1;
  overflow-x: scroll;
  overflow-y: hidden;
  white-space: nowrap;

}

.video-item{
  background:black; 
  height:100px; 
  width: 100%;
  position:relative; 
  top:0px; z-index: 200;
  overflow-x: auto;
  white-space: nowrap;
  display:inline-block;
}

.ss{
  position: absolute;
  top: 165px;
  width: 100%;
  height: 62%;
}
</style>
