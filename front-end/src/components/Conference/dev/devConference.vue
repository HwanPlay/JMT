<template>
  <div class="MainContainer">
    <div class="MainContent">
      <div class="Minivideo_list" id="Minivideo_list">
        <div class="videos-container" id="videos-container"></div>
      </div>

      <div class="video_list_videOrshow">
        <!-- <span class="triangle test_1"></span> -->
        <div class="text-center" >
          <v-btn text color="rgb(255, 128, 74)" @click="videoBar" background-color="rgba(14, 23, 38, 1)">
            <v-icon v-show="!videoBarNav">mdi-chevron-down</v-icon>
            <v-icon v-show="videoBarNav">mdi-chevron-up</v-icon>
          </v-btn>
        </div>
      </div>

      <div class="Mainvideo">
        <div class="Main-videos-container" id="Main-videos-container">
          <v-sheet
            class="mx-auto"
            elevation="8"
            max-width="800"
          >
            <v-slide-group
              v-model="model"
              class="pa-4"
              center-active
              show-arrows
            >
              <v-slide-item
                v-for="n in 6"
                :key="n"
                v-slot:default="{ active, toggle }"
              >
                <v-card
                  :color="active ? 'primary' : 'grey lighten-1'"
                  class="ma-4"
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
          </v-sheet>
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

          <!-- <v-btn @click="onBroadcast">
            <span>BroadCast</span>
            <v-icon>mdi-cast</v-icon>
          </v-btn>

          <v-btn @click="offBroadcast">
            <span>BroadCast off</span>
            <v-icon>mdi-cast-off</v-icon>
          </v-btn> -->

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
      <NoteEditor />
    </div>

    <div id="chat-container">
      <div id="container">
        <div class="row header-one text-white p-1">
          <div class="col-md-8 name pl-3">
            <i class="fa fa-comment fa-2x" style="float : left; margin-right : 15px;"></i>
            <!-- <h4 class="ml-1 mb-0">{{ this.meetingInfo.groupName }}
            </h4> -->
          </div>
          <div class="col-md-4 options text-right pr-0">
            <i class="fa fa-times hover text-center pt-1" @click="onChat"></i>
          </div>
        </div>

        <div class="chat-content">
          <div class="chats pt-3 pl-2 pr-3">
            <div class="chat-output"></div>
          </div>
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
</template>
<script src="https://cdn.webrtc-experiment.com/FileBufferReader.js"></script>
<!-- socket.io for signaling -->
<script src="https://rtcmulticonnection.herokuapp.com/socket.io/socket.io.js"></script>

<script>
import RTCMultiConnection from "../../../api/RTCMultiConnection";
import Broadcast from "../../../api/broadcast";
// import Sharescreen from './Sharescreen.vue';
import $ from "jquery";
import Vue from "vue";
// import WebRTC from '../../api/webrtc';
// import CanvasDesigner from "../../assets/canvas/canvas-designer-widget";
// import BroadCast from '../../api/broadcast';
import NoteEditor from "../ConfNoteEditor";

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

      model: null,
    };
  },
  methods: {
    //회의방 참가
    onJoin() {
      this.disableInputBool = false;
      this.connection.session = {
        data: true,
        video: false,
        audio: true
      };
      this.connection.openOrJoin(this.roomId);
      document.getElementById("videos-container").style.display = "block";
      this.overlay = false;
    },
    //회의방 나가기
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
      document.getElementById("videos-container").style.display = "none";

      this.$router.push("/Group");
    },
    //비디오 끄고,켜기
    onCam() {
      if (this.videoOnOff == true) {
        this.connection.streamEvents.selectFirst('local').stream.getTracks()[1].enabled = false; // it will disable only video track
        console.log(this.connection.streamEvents.selectFirst('local'))
       
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

      } else {
        let localStream = this.connection.attachStreams[0];
        this.connection.streamEvents.selectFirst("local").isAudioMuted = false;
        localStream.unmute("video");
      }
      this.videoOnOff = !this.videoOnOff;
    },
    onMic() {
      if (this.micOnOff == true) {
        let localStream = this.connection.attachStreams[0];
        localStream.mute("audio");
        localStream.muted = true;
      } else {
        let localStream = this.connection.attachStreams[0];
        localStream.unmute("audio");
        this.connection.streamEvents.selectFirst("local").mediaElement.muted = true;
      }
      this.micOnOff = !this.micOnOff;
    },
    onCast() {
      if (this.castOnOff == false) {

      } else {

      }
      this.castOnOff = !this.castOnOff;
    },
    offBroadcast() {
      this.broadcast.dontAttachStream = true;
    },
    onBroadcast() {
      this.broadcast.session = {
        video: true
      };
      this.broadcast.openOrJoin(this.roomId + "a");
    },
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
        $(".MainContent").css("width", "70%");
        this.NoteBool = true;
      } else if (this.NoteBool == true && this.Chatbool == false) {
        $(".MainContent").css("width", "100%");
        this.NoteBool = false;
      } else if (this.NoteBool == false && this.Chatbool == true) {
        $(".MainContent").css("width", "50%");
        this.NoteBool = true;
      } else {
        $(".MainContent").css("width", "80%");
        this.NoteBool = false;
      }
    },
    onChat() {
      $("#chat-container").toggle();
      if (this.Chatbool == false && this.NoteBool == false) {
        $(".MainContent").css("width", "80%");
        this.Chatbool = true;
      } else if (this.Chatbool == true && this.NoteBool == false) {
        $(".MainContent").css("width", "100%");
        this.Chatbool = false;
      } else if (this.Chatbool == false && this.NoteBool == true) {
        $(".MainContent").css("width", "50%");
        this.Chatbool = true;
      } else {
        $(".MainContent").css("width", "70%");
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
        "<ul class='p-0'><li class='receive-msg float-left mb-2'><div class='sender-img'><img src='https://img1.daumcdn.net/thumb/R720x0.q80/?scode=mtistory2&fname=http%3A%2F%2Fcfile7.uf.tistory.com%2Fimage%2F24283C3858F778CA2EFABE' class='float-left'></div><div class='receive-msg-desc float-left ml-2'><p class='bg-white m-0 pt-1 pb-1 pl-2 pr-2 rounded'>" +
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
    }
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
  },
  mounted() {
    this.onJoin();
    this.chatContainer = document.querySelector(".chat-output");
    this.connection.videosContainer = document.querySelector(
      ".videos-container"
    );
    this.broadcast.videosContainer = document.querySelector(
      ".Main-videos-container"
    );
    
    // this.designer = new CanvasDesigner();
  },
  destroyed() {
    this.onLeave();
  }
};
</script>

<style>
.videos-container video {
  height: 100px;
  overflow-x: hidden;
  border: 2px solid white;
}

.Main-videos-container video {
  height: 90%;
  overflow-x: hidden;
}
.MainContent {
  position: relative;
  height: 100%;
  width: 100%;
  float: left;
  overflow-y: hidden;
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

.MainContainer {
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
  position: relative;
  width: 100%;
  border: 2px solid #aaa;
  border-radius: 4px;
  outline: none;
  padding: 8px;
  box-sizing: border-box;
  transition: 0.3s;
  background-color: white;
  z-index: 4;
  overflow: hidden;
}

.video_list_videOrshow {
  position: absolute;
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


.chat-main {
  position: relative;
  width: 100%;
  height: 100%;
}
.header-one {
  margin: 0;
  height: 100px;
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
  font-size: 20px;
}
.sender-img {
  display: inline;
}
.sender-img img {
  width: 32px;
  height: 32px;
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
  height: 80%;
  overflow: hidden;
}

video::-webkit-media-controls {
  display: none;
}
</style>
