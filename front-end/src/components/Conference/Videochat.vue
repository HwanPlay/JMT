<template>
  <div id="MainContainer">
    <v-sheet id="MainContent">
      <!-- <v-slide-group id="Minivideo_list" center-active show-arrows dark> -->
      <!-- <v-slide-item id="videos-container"> -->
      <!-- <div class="mx-auto"></div> -->
      <!-- </v-slide-item> -->
      <!-- </v-slide-group> -->

      <!-- <div id="video_list_videOrshow">
        <div class="text-center">
          <v-btn
            text
            color="rgb(255, 128, 74)"
            @click="videoBar"
            background-color="rgba(14, 23, 38, 1)"
          >
            <v-icon v-show="videoBarNav">mdi-chevron-down</v-icon>
            <v-icon v-show="!videoBarNav">mdi-chevron-up</v-icon>
          </v-btn>
        </div>
      </div>-->

      <div id="Mainvideo">
        <div style="width:100%; height: 100%;">
          <div id="videos-container">
          </div>
        </div>
      </div>

      <div class="footer">
        <div class="text-center mb-2">
          <v-btn text color="rgb(255, 128, 74)" @click="showNav = !showNav">
            <v-icon v-show="showNav">mdi-chevron-down</v-icon>
            <v-icon v-show="!showNav">mdi-chevron-up</v-icon>
          </v-btn>
        </div>
        <v-bottom-navigation dark :input-value="showNav" background-color="rgba(14, 23, 38, 1)">
          <!-- <v-btn @click="overlay = !overlay"> -->
          <!-- <v-btn @click="onJoin">
            <span>Join</span>
            <v-icon>mdi-login</v-icon>
          </v-btn>-->
          <!-- <v-overlay :value="overlay">
          <v-progress-circular indeterminate size="64"></v-progress-circular>
          </v-overlay>-->

          <v-btn @click="onCam">
            <span class="conferenceBtn" v-show="!videoOnOff">OFF</span>
            <v-icon v-show="!videoOnOff">mdi-video-off</v-icon>

            <span class="conferenceBtn" v-show="videoOnOff" style="color: rgb(255, 128, 74);">ON</span>
            <v-icon v-show="videoOnOff" color="rgb(255, 128, 74)">mdi-video</v-icon>
          </v-btn>

          <v-btn @click="onMic">
            <span class="conferenceBtn" v-show="!micOnOff">OFF</span>
            <v-icon v-show="!micOnOff">mdi-microphone-off</v-icon>

            <span class="conferenceBtn" v-show="micOnOff" style="color: rgb(255, 128, 74);">ON</span>
            <v-icon v-show="micOnOff" color="rgb(255, 128, 74)">mdi-microphone</v-icon>
          </v-btn>

          <v-btn @click="onChat">
            <span class="conferenceBtn" v-show="!chatOnOff">CHAT</span>
            <v-icon v-show="!chatOnOff">mdi-forum</v-icon>

            <span class="conferenceBtn" v-show="chatOnOff" style="color: rgb(255, 128, 74);">CHAT</span>
            <v-icon v-show="chatOnOff" color="rgb(255, 128, 74)">mdi-forum</v-icon>
          </v-btn>

          <v-btn @click="onNote">
            <span class="conferenceBtn" v-show="!noteOnOff">NOTE</span>
            <v-icon v-show="!noteOnOff">fas fa-file-alt</v-icon>

            <span class="conferenceBtn" v-show="noteOnOff" style="color: rgb(255, 128, 74);">NOTE</span>
            <v-icon v-show="noteOnOff" color="rgb(255, 128, 74)">fas fa-file-alt</v-icon>
          </v-btn>

          <!-- <v-btn @click="onCanvas" :disabled="disableCanvasBool">
            <span>Canvas</span>
            <v-icon>mdi-palette</v-icon>
          </v-btn>-->

          <v-btn @click="onLeave" color="red">
            <span class="conferenceBtn">QUIT</span>
            <v-icon>mdi-export</v-icon>
          </v-btn>
        </v-bottom-navigation>
      </div>
    </v-sheet>

    <div id="note-container">
      <NoteEditor :meetingInfo="meetingInfo" :groupInfo="groupInfo" />
    </div>

    <div id="chat-container">
      <div id="container">
        <div class="row header-one text-white p-1">
          <div class="col-md-10 name pl-3">
            <!-- <i class="fa fa-comment fa-2x" style="float : left; margin-right : 15px;"></i> -->
            <h6 class="ml-2 mb-0">{{ this.groupInfo.groupName }}</h6>
          </div>
          <div class="col-md-1 options text-right pr-0">
            <i class="fa fa-times hover text-center" @click="onChat"></i>
          </div>
        </div>

        <div class="chat-content">
          <div class="chats">
            <div class="chat-output"></div>
          </div>
          <input
            type="text"
            id="input-text-chat"
            placeholder="메시지 입력"
            @keyup.13="textSend"
            :disabled="disableInputBool"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<!-- socket.io for signaling -->
<script src="https://rtcmulticonnection.herokuapp.com/socket.io/socket.io.js"></script>

<script>
import Vue from "vue";
import axios from "axios";
import $ from "jquery";

import RTCMultiConnection from "../../api/RTCMultiConnection";

import NoteEditor from "./ConfNoteEditor";

import SERVER from "../../api/spring";

import SockJS from "sockjs-client";
import Stomp from "webstomp-client";

export default {
  name: "Videochat",
  components: {
    NoteEditor
  },
  props: {
    groupInfo: Object,
    meetingInfo: Object
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
      video: Object,
      div: null,
      inputText: "",
      message: "Default Message",
      broadcast: null,
      videoLength: null,

      // overlay: false,
      videoBarNav: false,
      showNav: true,
      videoOnOff: true,
      micOnOff: true,
      castOnOff: false,
      chatOnOff: false,
      noteOnOff: false,
      activeBtn: 0,

      myVideoTrackIsMuted: false,
      trackId: null,
      streamId: null,

      endMeeting: null,
      model: null,

      //---------------WebSocket-----------------
      sock: null,
      ws: null,
      reconnect: 0,
      recv: ""
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

    //Voice Detect & change Bordor color 
      var that = this;
      this.connection.onstream = function(event) {
        var parentNode = that.connection.videosContainer;
        parentNode.insertBefore(event.mediaElement, parentNode.firstChild);
        var played = event.mediaElement.play();

        initHark({
          stream: event.stream,
          streamedObject: event,
          connection: that.connection
        });

        if (typeof played !== "undefined") {
          played
            .catch(function() {
              /*** iOS 11 doesn't allow automatic play and rejects ***/
            })
            .then(function() {
              setTimeout(function() {
                event.mediaElement.play();
              }, 2000);
            });
          return;
        }

        setTimeout(function() {
          event.mediaElement.play();
        }, 2000);
      };

      this.connection.onspeaking = function(e) {
        // e.streamid, e.userid, e.stream, etc.
        e.mediaElement.style.border = "1px solid red";
        console.log(e.mediaElement);
      };

      this.connection.onsilence = function(e) {
        // e.streamid, e.userid, e.stream, etc.
        e.mediaElement.style.border = "";
        console.log(e.mediaElement);
      };

      this.connection.onvolumechange = function(event) {
        event.mediaElement.style.borderWidth = event.volume;
      };

      function initHark(args) {
        if (!window.hark) {
          throw "Please link hark.js";
          return;
        }
        var connection = args.connection;
        var streamedObject = args.streamedObject;
        var stream = args.stream;

        var options = {};
        var speechEvents = hark(stream, options);

        speechEvents.on("speaking", function() {
          connection.onspeaking(streamedObject);
        });

        speechEvents.on("stopped_speaking", function() {
          connection.onsilence(streamedObject);
        });

        speechEvents.on("volume_change", function(volume, threshold) {
          streamedObject.volume = volume;
          streamedObject.threshold = threshold;
          connection.onvolumechange(streamedObject);
        });
      }
      // end Voice Detect


      this.connection.openOrJoin(this.groupInfo.roomId);
      document.getElementById("videos-container").style.display = "block";
      
      // this.overlay = false;
    },
    //회의방 나가기
    onLeave() {
      // console.log('나갈건데 이미지부터 ' +stream.streamid);
      let localStream = this.connection.attachStreams[0];
      localStream.mute("exit");

      this.$router.push("/Group");
      this.$store.commit("SET_VIDEO_ON", false);
    },
    onDisconnect() {
      this.connection.dontAttachStream = true;
      this.connection.attachStreams.forEach(function(localStream) {
        localStream.stop();
      });
      var that = this;
      this.connection.getAllParticipants().forEach(function(pid) {
        that.connection.disconnectWith(pid);
      });
    },
    onBroadDisconnect() {
      this.broadcast.dontAttachStream = true;
      this.broadcast.attachStreams.forEach(function(localStream) {
        localStream.stop();
      });
      var that = this;
      this.broadcast.getAllParticipants().forEach(function(pid) {
        that.broadcast.disconnectWith(pid);
      });
    },
    //비디오 끄고,켜기
    onCam() {
      // 카메라 끄기
      if (this.videoOnOff == true) {
        // this.connection.streamEvents
        //   .selectFirst("local")
        //   .stream.getTracks()[1].enabled = false;
        let localStream = this.connection.attachStreams[0];
        localStream.mute("video-onoff");
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
        this.connection.streamEvents.selectFirst(
          "local"
        ).mediaElement.muted = true;
      }
      this.micOnOff = !this.micOnOff;
    },
    onCast() {
      if (!this.castOnOff) {
        var that = this;
        this.broadcast.session = {
          audio: true,
          video: true
        };

        this.broadcast.dontAttachStream = true;
        navigator.webkitGetUserMedia(
          {
            video: true,
            audio: true
          },
          function(yourExternalStream) {
            that.broadcast.attachStreams = [yourExternalStream];
            that.broadcast.openOrJoin(that.groupInfo.roomId);
          },
          function(error) {}
        );

        this.broadcast.attachStreams.forEach(function(localStream) {
          localStream.stop();
        });

        this.showNav = !this.showNav;
      } else {
        this.onBroadDisconnect();
      }
      this.videoBar();
      this.castOnOff = !this.castOnOff;
    },
    videoBar() {
      $("#video_list").toggle();
      if (this.videoBarNav) {
        $("#Mainvideo").css("height", "88%");
      } else {
        $("#Mainvideo").css("height", "100%");
      }
      this.videoBarNav = !this.videoBarNav;
    },
    onNote() {
      $("#note-container").toggle();
      if (this.noteOnOff == false && this.chatOnOff == false) {
        $("#MainContent").css("width", "70%");
        this.noteOnOff = true;
      } else if (this.noteOnOff == true && this.chatOnOff == false) {
        $("#MainContent").css("width", "100%");
        this.noteOnOff = false;
      } else if (this.noteOnOff == false && this.chatOnOff == true) {
        $("#MainContent").css("width", "50%");
        this.noteOnOff = true;
      } else {
        $("#MainContent").css("width", "80%");
        this.noteOnOff = false;
      }
    },
    onChat() {
      $("#chat-container").toggle();
      if (this.chatOnOff == false && this.noteOnOff == false) {
        $("#MainContent").css("width", "80%");
        this.chatOnOff = true;
      } else if (this.chatOnOff == true && this.noteOnOff == false) {
        $("#MainContent").css("width", "100%");
        this.chatOnOff = false;
      } else if (this.chatOnOff == false && this.noteOnOff == true) {
        $("#MainContent").css("width", "50%");
        this.chatOnOff = true;
      } else {
        $("#MainContent").css("width", "70%");
        this.chatOnOff = false;
      }
    },

    onError(error, stream) {
      console.log("On Error Event", error, stream);
    },
    logEvent(event) {
      console.log("Event : ", event);
    },
    appendDIV(event) {
      this.textArea = document.createElement("div");
      // console.log(userInfo)

      var picture = event.data.substring(0, 21);
      var text = event.data.substring(21);

      if (event.data.substring(18, 22) == "jpeg") {
        picture = event.data.substring(0, 22);
        text = event.data.substring(22);
      }

      const strCopy = text.split(":");
      // console.log("name : " + strCopy[0]);

      // this.textArea.innerHTML =
      //   "<ul class='p-0'>" +
      //   "<li class='receive-msg float-left mb-1'>" +
      //   "<p class='receive-msg-username' style='margin-bottom:2px;'>" +
      //   strCopy[0] +
      //   "</p>" +
      //   "<div class='sender-img'>" +
      //   "<img src='http://joinmeeting.tk/images/" +
      //   picture +
      //   "' class='float-left'>" +
      //   "</div>" +
      //   "<div class='receive-msg-desc float-left ml-2'>" +
      //   "<p class='receive-msg-context bg-white m-0 pt-1 pb-1 pl-2 pr-2 rounded'>" +
      //   (strCopy[1] || event) +
      //   "</p>" +
      //   "</div>" +
      //   "</li></ul>";

      this.textArea.innerHTML =
        "<table class='receive-msg-tb' style='width:auto;'>" +
        "<tr>" +
        "<th class='receive-msg-th-img' rowspan='2' style='width:57px;'>" +
        "<div class='sender-img'>" +
        "<img src='https://joinmeeting.tk/images/" +
        picture +
        "' class='float-left'>" +
        "</div>" +
        "</th>" +
        "<td>" +
        "<p class='receive-msg-username' style='margin-bottom:2px;'>" +
        strCopy[0] +
        "</p>" +
        "</td>" +
        "</tr>" +
        "<tr>" +
        "<td>" +
        "<div class='receive-msg-context bg-white m-0 pt-1 pb-1 pl-2 pr-2 rounded'>" +
        (strCopy[1] || event) +
        "</div>" +
        "</td>" +
        "</tr>" +
        "</table>";

      // console.log(this.textArea);
      this.chatContainer.appendChild(this.textArea);
      this.textArea.tabIndex = 0;
      this.textArea.focus();
      document.getElementById("input-text-chat").focus();
    },
    textSend(e) {
      if (e.target.value) {
        // removing trailing/leading whitespace
        console.log("내 닉네임 :  " + this.meetingInfo.nickname);
        this.value =
          this.meetingInfo.nickname +
          ": " +
          e.target.value.toString().replace(/^\s+|\s+$/g, "");
        // .replace(/^\s+|\s+$/g,'') : 앞뒤 공백 제거
        this.connection.send(this.$store.state.myPicture + this.value);
        var msg = {
          data: this.$store.state.myPicture + this.value
        };
        this.appendDIV(msg);
      }
      e.target.value = "";
    },

    //---------------WebSocket-----------------

    connect() {
      this.ws.connect(
        { token: this.$store.state.accessToKen },
        frame => {
          this.ws.subscribe(
            "/send/conference/" + this.meetingInfo.meetingNo,
            res => {
              this.recv = res.body;
              // console.log('res.body', res.body);
              this.endMeeting = JSON.parse(this.recv);
              if (
                this.endMeeting.host &&
                this.$store.state.userId !== this.groupInfo.hostId
              ) {
                this.onDisconnect();
                console.log("호스트가 회의를 종료하였습니다.");
                this.$router.push("/Group");
                this.$store.commit("SET_VIDEO_ON", false);
              }
            }
          );
        },
        () => {
          if (this.reconnect++ <= 5) {
            setTimeout(() => {
              this.sock = new SockJS(SERVER.URL2);
              this.ws = Stomp.over(this.sock);
              this.connect();
            }, 10 * 1000);
          }
        }
      );
    },

    send(param) {
      const msg = {
        host: param,
        meetingNo: this.meetingInfo.meetingNo
      };
      this.ws.send("/conference", JSON.stringify(msg), {
        token: this.$store.state.accessToken
      });
    }
  },

  updated() {
    this.connection.onmessage = this.appendDIV;
    this.connection.onclose = function(event) {
      console.log("data connection closed between you and " + event.userid);
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
    // voice detect를 위해 필요한 hark.js 스크립트 삽입
    let src = document.createElement("script");
    src.setAttribute(
      "src",
      "https://cdn.webrtc-experiment.com/hark.js"
    );
    document.body.appendChild(src);

    // this.broadcast.videosContainer = document.querySelector("#Main-videos-container");

    this.$store.commit("SET_VIDEO_ON", true);
    //---------------WebSocket-----------------
    this.connect();
  },
  beforeDestroy() {
    this.onDisconnect();
    var numberOfUsers = this.connection.getAllParticipants().length;
    if (this.$store.state.userId === this.groupInfo.hostId) {
      this.send(true);
      axios.put(SERVER.URL + "/group/hasmeeting/" + this.groupInfo.groupNo);
      axios.put(SERVER.URL + "/meeting/update/" + this.meetingInfo.meetingNo);
      console.log(
        numberOfUsers + "명이 당신과 함께하였습니다. 회의가 종료되었습니다."
      );
    } else {
      console.log(
        numberOfUsers +
          "명이 당신과 함께하였습니다. 호스트가 회의를 종료하였습니다."
      );
    }
  },
  destroyed() {
    this.ws.disconnect();
  }
};
</script>

<style>
#videos-container {
  width: 100%;
  height: 100%;
  overflow-y: auto;
}
#videos-container video {
  height: 30%;
  width: 33%;
  /* margin: 0px 1px; */
  border: 1px groove white;
  border-radius: 3px;
  padding : 0px;
}
.video-off{
   height: 0%;
   width: 33%;
   display: none;
   /* opacity : 0; */
}
.video-on{
  display: unset;
  /* opacity: 1; */
}

.canvas-video{
  height: 30%;
  width: 33%;
  border: 1px groove white;
  border-radius: 3px;
  padding : 0px;
  vertical-align: unset;
  /* position: absolute; */
   /* top: 50%;
    left: 50%; */
    /* margin: -10px 0 0 -20px; */
}
#Mainvideo {
  position: relative;
  height: 95%;
  background-color: black;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
}
#Main-videos-container {
  width: 640px;
  height: 480px;
  border: 2px groove black;
}
#Main-videos-container video {
  height: 100%;
}
#MainContent {
  position: relative;
  height: 100%;
  width: 100%;
  float: left;
  overflow-y: hidden;
}
#video_list {
  position: relative;
  height: 100%;
  background-color: black;
}
#note-container {
  display: none;
  float: right;
  width: 30%;
  height: 100%;
  overflow-y: hidden;
  background-color: white;
  border-left: 1px solid #b3b3b3;
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
  /* border: 1px #ddd solid; */
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
  background-color: black;
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
  border-top: 1px solid #d9d9d9;
  outline: none;
  padding: 8px;
  box-sizing: border-box;
  transition: 0.3s;
  background-color: white;
  z-index: 4;
  height: 8%;
  font-family: "Noto Sans KR", sans-serif;
  font-size: 14px;
  padding-left: 12px;
}

#video_list_videOrshow {
  position: absolute;
  width: auto;
  left: 50%;
  transform: translate(-50%, 0);
  z-index: 7;
}

.chat-main {
  position: relative;
  width: 100%;
  height: 100%;
}
.header-one {
  margin: 0;
  height: 7%;
  position: relative;
  width: 100%;
  background: #404040;
}

.options i,
.options .arrow-up {
  height: 20px;
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
  height: 93%;

  overflow-x: scroll;
  overflow-x: hidden;
  background: #eceff1;
  position: relative;
  vertical-align: bottom;
}
.chat-output {
  height: auto;
  overflow-x: scroll;
  overflow-x: hidden;
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
  width: 45px;
  height: 45px;
  border-radius: 100%;
  margin: 0px 7px;
  background: white;
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
  height: 92%;
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
}

video::-webkit-media-controls {
  display: none;
}

.receive-msg-username {
  color: gray;
  font-size: 13px;
}
.receive-msg-context {
  font-size: 14px;
  font-family: "NanumSquare", sans-serif;
  word-break: break-all;
}
.receive-msg-tb {
  margin: 10px 10px 10px 0px;
}
.receive-msg-th-img {
  vertical-align: top;
}
.conferenceBtn {
  font-family: "Open Sans", sans-serif;
  font-size: 12px;
}
</style>