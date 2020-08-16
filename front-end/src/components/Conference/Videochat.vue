<template>
  <div id="MainContainer">
    <div id="MainContent">
      <div id="Minivideo_list">
        <div id="videos-container"></div>
      </div>

      <!-- <v-sheet>
        <v-slide-group
          v-model="model"
          center-active
          show-arrows
        >
          <v-slide-item
            v-for="n in 10"
            :key="n"
            v-slot:default="{ active, toggle }"
          >
            <v-card
              :color="active ? 'primary' : 'grey lighten-1'"
              class="mx-1"
              height="100"
              width="132"
              @click="toggle"
            >
              <v-row
                class="fill-height"
                align="center"
                justify="center"
              >
                <v-scale-transition>
                  <v-icon
                    v-if="active"
                    color="white"
                    size="48"
                    v-text="'mdi-close-circle-outline'"
                  ></v-icon>
                </v-scale-transition>
              </v-row>
            </v-card>
            
          </v-slide-item>
        </v-slide-group>
      </v-sheet>   -->

      <div id="video_list_videOrshow">
        <div class="text-center" >
          <v-btn text color="rgb(255, 128, 74)" @click="videoBar" background-color="rgba(14, 23, 38, 1)">
            <v-icon v-show="!videoBarNav">mdi-chevron-down</v-icon>
            <v-icon v-show="videoBarNav">mdi-chevron-up</v-icon>
          </v-btn>
        </div>
      </div>

      <div class="Mainvideo">
        <div class="Main-videos-container" id="Main-videos-container">
        </div>
      </div>

      <!-- <div class="fixed-bottom"> -->
      <div class="footer">
        <div class="text-center mb-2">
          <v-btn text color="rgb(255, 128, 74)" @click="showNav = !showNav">
            <v-icon v-show="showNav">mdi-chevron-down</v-icon>
            <v-icon v-show="!showNav">mdi-chevron-up</v-icon>
          </v-btn>
        </div>
        <v-bottom-navigation
          dark
          v-model="activeBtn"
          :input-value="showNav"
          color="rgb(255, 128, 74)"
          background-color="rgba(14, 23, 38, 1)"
        >
          <!-- <v-btn @click="overlay = !overlay"> -->
          <!-- <v-btn @click="onJoin">
            <span>Join</span>
            <v-icon>mdi-login</v-icon>
          </v-btn> -->
          <!-- <v-overlay :value="overlay">
          <v-progress-circular indeterminate size="64"></v-progress-circular>
          </v-overlay>-->

          <v-btn @click="onCam" >
            <span v-show="!videoOnOff">OFF</span>
            <v-icon v-show="!videoOnOff">mdi-video-off</v-icon>

            <span v-show="videoOnOff">ON</span>
            <v-icon v-show="videoOnOff">mdi-video</v-icon>
          </v-btn>

          <v-btn @click="onMic">
            <span v-show="!micOnOff">OFF</span>
            <v-icon v-show="!micOnOff">mdi-microphone-off</v-icon>

            <span v-show="micOnOff">ON</span>
            <v-icon v-show="micOnOff">mdi-microphone</v-icon>
          </v-btn>

          <v-btn @click="onCast">
            <span v-show="!castOnOff">OFF</span>
            <v-icon v-show="!castOnOff">mdi-cast-off</v-icon>

            <span v-show="castOnOff">ON</span>
            <v-icon v-show="castOnOff">mdi-cast</v-icon>
          </v-btn>

          <v-btn @click="onChat">
            <span>Chatting</span>
            <v-icon>mdi-forum</v-icon>
          </v-btn>

          <v-btn @click="onNote">
            <span>Note</span>
            <v-icon>mdi-book</v-icon>
          </v-btn>

          <!-- <v-btn @click="onCanvas" :disabled="disableCanvasBool">
            <span>Canvas</span>
            <v-icon>mdi-palette</v-icon>
          </v-btn> -->

          <v-btn @click="onLeave">
            <span>Leave</span>
            <v-icon>mdi-logout</v-icon>
          </v-btn>
        </v-bottom-navigation>
      </div>

    </div>
    <div id="note-container">
      <NoteEditor :meetingInfo="meetingInfo" />
    </div>
    <div id="chat-container">
      <div id="container">
        <div class="row header-one text-white p-1">
          <div class="col-md-8 name pl-3">
            <!-- <i class="fa fa-comment fa-2x" style="float : left; margin-right : 15px;"></i> -->
            <h6 class="ml-1 mb-0">{{ this.meetingInfo.groupName }}
            </h6>
          </div>
          <div class="col-md-4 options text-right pr-0">
            <i class="fa fa-times hover text-center pt-1" @click="onChat"></i>
          </div>
        </div>

        <div class="chat-content">
          <div class="chats">
            <div class="chat-output"></div>
          </div>
        <input
          type="text"
          id="input-text-chat"
          placeholder="Enter Text Chat"
          @keyup.13="textSend"
          :disabled="disableInputBool"
        />
        </div>
      </div>
    </div>
  </div>
</template>
<script src="https://cdn.webrtc-experiment.com/FileBufferReader.js"></script>
<!-- socket.io for signaling -->
<script src="https://rtcmulticonnection.herokuapp.com/socket.io/socket.io.js"></script>

<script>
import axios from 'axios';

import RTCMultiConnection from "../../api/RTCMultiConnection";
import Broadcast from "../../api/broadcast";
// import Sharescreen from './Sharescreen.vue';
import $ from "jquery";
import Vue from "vue";
// import WebRTC from '../../api/webrtc';
// import CanvasDesigner from "../../assets/canvas/canvas-designer-widget";
// import BroadCast from '../../api/broadcast';
import NoteEditor from "./ConfNoteEditor";

import SERVER from '../../api/spring';

import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';

// Vue.use(WebRTC)
// Vue.use(BroadCast)

export default {
  name: "Videochat",
  components: {
    // Sharescreen,
    NoteEditor,
    RTCMultiConnection,
    Broadcast
  },
  props:{
    roomId: String,
    hostId: String,
    meetingInfo: Object,
  },
  data() {
    return {
      img: null,
      disableInputBool: true,
      // disableCanvasBool: true,
      chatContainer: null,
      value: "",
      textArea: null,
      designer: null,
      connection: null,
      NoteShow: {},
      NoteBool: false,
      Chatbool: false,
      Bar: false,
      video: Object,
      div: null,
      inputText: "",
      message: "Default Message",
      broadcast: null,
      videoLength: null,

      // overlay: false,
      videoBarNav: true,
      showNav: true,
      videoOnOff: true,
      micOnOff: true,
      castOnOff: false,
      activeBtn: 0,

      myVideoTrackIsMuted: false,
      trackId: null,
      streamId : null,

      // model: null,

      //---------------WebSocket-----------------
      sock : null,
      ws : null,
      reconnect : 0,
      recv : '',
    };
  },
  methods: {
    //회의방 참가
    onJoin() {
      this.disableInputBool = false;
      this.connection.session = {
        data: true,
        video: true,
        audio: true
      };
      this.connection.openOrJoin(this.roomId);
      document.getElementById("videos-container").style.display = "block";
      this.overlay = false;
    },
    //회의방 나가기
    onLeave() { // 재접은 autoCloseEntireSession T/F 에 따라 가불가
      var that = this;
      console.log(this.$store.state.userId, this.hostId)
      if (this.$store.state.userId === this.hostId) {
        this.connection.closeSocket();
        // that.connection.closeSocket();
        var numberOfUsers = this.connection.getAllParticipants().length;
        alert(numberOfUsers + '명이 당신과 함께하였습니다. 회의가 종료되었습니다.');
      } else {
        this.connection.dontAttachStream = true;
        this.connection.attachStreams.forEach(function(localStream) { // 커넥션에서 내 스트림만 없애기(채팅가능), 상대방꺼는 주고받을 수 있음
          localStream.stop();
        });
        var numberOfUsers = this.connection.getAllParticipants().length;
        alert(numberOfUsers + '명이 당신과 함께하였습니다. 호스트가 회의를 종료하였습니다.');
      }

      // disconnect with all users
      // this.connection.getAllParticipants().forEach(function(pid) {
      //   that.connection.disconnectWith(pid); // 특정 리모트 유저(게스트) 와의 연결 끊기 포문돌려서 모든 연결 끊기가 된다.

          // var user = that.connection.peers[pid];
          // var hisFullName = user.extra;
          // var hisUID = user.userid;
          // var hisNativePeer = user.peer;
          // var hisIncomingStreams = user.peer.getRemoteStreams();
          // var hisDataChannels = user.channels;
          // console.log(user)
          // console.log(hisFullName)
          // console.log(hisUID)
          // console.log(hisNativePeer)
          // console.log(hisIncomingStreams)
          // console.log(hisDataChannels)
      // });

      // this.connection.dontAttachStream = true;  // 상대방 접속해도 비디오 안생기게 하는 것 채팅 가능, 리브누르고 재접속 가능
      // this.broadcast.dontAttachStream = true;
      // // stop all local cameras
      // this.connection.attachStreams.forEach(function(localStream) { // 커넥션에서 내 스트림만 없애기(채팅가능), 상대방꺼는 주고받을 수 있음
      //   localStream.stop();
      // });
      // // close socket.io connection
      // this.connection.closeSocket();  //새로고침할때랑 거의 동일, 각자의 로컬 스트림은 살아있고, 통신이 끊김(채팅도 불가) 호스트는 게스트 스트림 멈춤(리브누른사람의 스트림 사라짐), 호스트가 재접속시 게스트들 streamid 그대로 다시 연결됨 게스트 재접속시 새로방만들어짐

      document.getElementById("videos-container").style.display = "none";
      axios.put(SERVER.URL + '/group/hasmeeting/'+this.meetingInfo.groupNo);
      this.$router.push("/Group");
    },
    //비디오 끄고,켜기
    onCam() {
      // 카메라 끄기
      if (this.videoOnOff == true) {
        this.connection.streamEvents.selectFirst('local').stream.getTracks()[1].enabled = false; // it will disable only video track
        // console.log(this.connection.streamEvents.selectFirst('local'))
        // this.connection.streamEvents.selectFirst('local').mediaElement.autoplay = 'false';
        // this.connection.streamEvents.selectFirst('local').mediaElement.style.background = 'transparent url(https://cdn.webrtc-experiment.com/images/muted.png) no-repeat center center';
        // console.log(this.connection.streamEvents.selectFirst('local'))
       
        // this.connection.send({
        //     myVideoTrackIsMuted: true,
        //     trackId: this.connection.streamEvents.selectFirst('local').stream.getTracks()[1].id,
        //     streamId: this.connection.streamEvents.selectFirst('local').streamid
        // });

        // this.connection.onmessage = function(event) {
        //     if(event.data.myVideoTrackIsMuted === true) {
        //         document.getElementById(event.data.streamId).pause(); // you can set "srcObject=null" or removeAttribute('srcObject')
        //         document.getElementById(event.data.streamId).poster = '/images/poster.png'; // or background image
        //     }
        // };
      // 카메라 켜기
      } else {
        let localStream = this.connection.attachStreams[0];
        this.connection.streamEvents.selectFirst("local").isAudioMuted = false;
        localStream.unmute("video");
      }
      this.videoOnOff = !this.videoOnOff;
    },
    onMic() {
      // 마이크 끄기
      if (this.micOnOff == true) {
        let localStream = this.connection.attachStreams[0];
        localStream.mute("audio");
        localStream.muted = true;
      // 마이크 켜기
      } else {
        let localStream = this.connection.attachStreams[0];
        localStream.unmute("audio");
        this.connection.streamEvents.selectFirst("local").mediaElement.muted = true;
      }
      this.micOnOff = !this.micOnOff;
    },
    onCast() {
      if (this.castOnOff == false) {
        console.log('캐스트 켜기')
      } else {
        console.log('캐스트 끄기')
      }
      this.castOnOff = !this.castOnOff;
    },
    // offBroadcast() {
    //   this.broadcast.dontAttachStream = true;
    // },
    // onBroadcast() {
    //   this.broadcast.session = {
    //     video: true
    //   };
    //   this.broadcast.openOrJoin(this.roomId + "a");
    // },
    videoBar() {
      this.videoBarNav = !this.videoBarNav;
      $("#Minivideo_list").toggle();
      this.Bar = !this.Bar;
      if (this.Bar == false) {
        $(".Mainvideo").css("height", "88%");
      } else {
        $(".Mainvideo").css("height", "100%");
      }
    },
    onNote() {
      $("#note-container").toggle();
      if (this.NoteBool == false && this.Chatbool == false) {
        $("#MainContent").css("width", "70%");
        this.NoteBool = true;
      } else if (this.NoteBool == true && this.Chatbool == false) {
        $("#MainContent").css("width", "100%");
        this.NoteBool = false;
      } else if (this.NoteBool == false && this.Chatbool == true) {
        $("#MainContent").css("width", "50%");
        this.NoteBool = true;
      } else {
        $("#MainContent").css("width", "80%");
        this.NoteBool = false;
      }
    },
    onChat() {
      $("#chat-container").toggle();
      if (this.Chatbool == false && this.NoteBool == false) {
        $("#MainContent").css("width", "80%");
        this.Chatbool = true;
      } else if (this.Chatbool == true && this.NoteBool == false) {
        $("#MainContent").css("width", "100%");
        this.Chatbool = false;
      } else if (this.Chatbool == false && this.NoteBool == true) {
        $("#MainContent").css("width", "50%");
        this.Chatbool = true;
      } else {
        $("#MainContent").css("width", "70%");
        this.Chatbool = false;
      }
    },
    // onCanvas() {
    //   this.disableCanvasBool = true;
    //   this.designer.widgetHtmlURL =
    //     "https://www.webrtc-experiment.com/Canvas-Designer/widget.html";
    //   this.designer.widgetJsURL =
    //     "https://www.webrtc-experiment.com/Canvas-Designer/widget.js";
    //   this.designer.appendTo(document.getElementById("widget-container"));
    // },
    // onCapture() {
    //   this.img = this.$refs.webrtc.capture();
    // },


    onError(error, stream) {
      console.log("On Error Event", error, stream);
    },
    logEvent(event) {
      console.log("Event : ", event);
    },
    appendDIV(event) {
      this.textArea = document.createElement("div");
      this.textArea.innerHTML =
        "<ul class='p-0'><li class='receive-msg float-left mb-2'><div class='sender-img'><img src='http://joinmeeting.tk/images/"+this.$store.state.myPicture+"' class='float-left'></div><div class='receive-msg-desc float-left ml-2'><p class='bg-white m-0 pt-1 pb-1 pl-2 pr-2 rounded'>" +
        (event.data || event) +
        "</p></div></li></ul>";
      this.chatContainer.appendChild(this.textArea);
      this.textArea.tabIndex = 0;
      this.textArea.focus();
      document.getElementById("input-text-chat").focus();
    },
    textSend(e) {
      console.log(e.target.value);
      // removing trailing/leading whitespace
      this.value =
        this.$store.state.myName +
        ": " +
        e.target.value.toString().replace(/^\s+|\s+$/g, "");
      // .replace(/^\s+|\s+$/g,'') : 앞뒤 공백 제거
      this.connection.send(this.value);
      this.appendDIV(this.value);
      e.target.value = "";
    },

    //---------------WebSocket-----------------
    
    connect() {
      this.ws.connect({ token : this.$store.state.accessToKen }, frame => {
        console.log('챗 소켓 연결 성공', frame);
        this.ws.subscribe('/send/conference/' + this.meetingInfo.meetingNo, res => {
          this.recv = res.body;
          console.log('챗 받은 데이터:', JSON.parse(this.recv));
        });
      }, () => {
        if(this.reconnect++ <= 5) {
          setTimeout(()=> {
            console.log('connection reconnect');
            this.sock = new SockJS(SERVER.URL2);
            this.ws = Stomp.over(this.sock);
            this.connect();
          }, 10*1000);
        }
      });
    },
  },


  updated() {
    this.connection.onmessage = this.appendDIV;
    this.connection.onclose = function(event) {
        console.log('data connection closed between you and ' + event.userid);
    };

  },

  created() {
    this.connection = new RTCMultiConnection();
    this.broadcast = new RTCMultiConnection();
    this.connection.socketURL = "https://rtcmulticonnection.herokuapp.com:443/";
    this.broadcast.socketURL = "https://rtcmulticonnection.herokuapp.com:443/";

    //---------------WebSocket-----------------
    this.sock = new SockJS(SERVER.URL2);
    this.ws = Stomp.over(this.sock);

  },
  mounted() {
    this.onJoin();
    this.chatContainer = document.querySelector(".chat-output");
    this.connection.videosContainer = document.querySelector(
      "#videos-container"
    );
    this.broadcast.videosContainer = document.querySelector(
      ".Main-videos-container"
    );

    //---------------WebSocket-----------------
    this.connect();
  },
  destroyed() {
    if (!!this.connection.getAllParticipants().length) {
      this.onLeave();
    }
  }
};

</script>

<style>
#videos-container{
  
  white-space: nowrap;
  overflow-x: auto;
  -webkit-overflow-scrolling: touch; 
  -ms-overflow-style: -ms-autohiding-scrollbar;
}

/* .videos-container::-webkit-scrollbar{
  display: none;
} */


#videos-container video {
  height: 98px;
  overflow-x: hidden;
  border: 2px solid white;
}

.Main-videos-container video {
  height: 90%;
  overflow-x: hidden;
}
#MainContent {
  position: relative;
  height: 100%;
  width: 100%;
  float: left;
  overflow-y: hidden;
  text-align: center;
}
#Minivideo_list {
  position: relative;
  height: 100px;
  width: 100%;
  background-color: black;
  border: 2px solid white;
  white-space: nowrap;
  overflow-x: scroll;
  overflow-y: hidden;
}
.Mainvideo {
  position: relative;
  width: 100%;
  height: 88.2%;
  overflow-y: hidden;
  text-align: center;
  background-color: black;
}
#note-container {
  display: none;
  float: right;
  width: 30%;
  height: 100%;
  overflow-y: hidden;
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
  padding-bottom: 0px;
}

#MainContainer {
  position: relative;
  margin-top: 0;
  /* background-color: rgb(52, 63, 87); */
  width: 100%;
  height: 100%;
  overflow-y: auto;
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
  width: 100%;
  border: 2px solid #aaa;
  border-radius: 4px;
  outline: none;
  padding: 8px;
  box-sizing: border-box;
  transition: 0.3s;
  background-color: white;
  z-index: 4;

}

#video_list_videOrshow {
  position: absolute;
  width: auto;
  left:50%;
  transform: translate(-50%,0);
  z-index: 7;
}

.chat-main {
  position: relative;
  width: 100%;
  height: 100%;
}
.header-one {
  margin: 0;
  height: 60px;
  position: relative;
  width: 100%;
  background: #404040;
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

.chats {
  height: 93.3%;
  overflow-x: scroll;
  overflow-x: hidden;
  background: #eceff1;
  position: relative;
}
.chats ul li {
  display: inline-block;
  list-style: none;
  clear: both;
  font-size: 15px;
}
.sender-img {
  display: inline;
}
.sender-img img {
  width: 25px;
  height: 25px;
  border-radius: 100%;
}

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
  height: 92.4%;
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;  
  box-sizing: border-box; 
}

video::-webkit-media-controls {
  display: none;
}
</style>
