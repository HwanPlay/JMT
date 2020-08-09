<template>
<div style="height: 100%; width: 100%;">
<!-- <v-container fluid> -->
  <v-layout row wrap style="height: auto">
    <v-flex xs12 sm12 md12 lg12 xl12 style="height: 100%">
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
    </v-flex>
  </v-layout>
  <!-- <v-col class="RoomInput" cols="12" align="center">
    <input v-model="roomId" id="RoomInput" style="color: black;" />
  </v-col> -->
  <div class="overflow-hidden fixed-bottom">
    <div class="text-center mb-2">
      <v-btn
        text
        color="rgb(255, 128, 74)"
        @click="showNav = !showNav"
      >
        <v-icon v-if="showNav">mdi-chevron-down</v-icon>
        <v-icon v-if="!showNav">mdi-chevron-up</v-icon>
      </v-btn>
    </div>
    <v-bottom-navigation
      v-model="activeBtn"
      :input-value="showNav"
      color="rgb(255, 128, 74)"
      background-color= "rgba( 255, 255, 255, 0.4 )"
    >
      <v-btn outlined @click="overlay = !overlay">
        <span>Join</span>
        <v-icon>mdi-login</v-icon>
      </v-btn>
      <v-overlay :value="overlay">
        <v-progress-circular indeterminate size="64"></v-progress-circular>
      </v-overlay>

      <v-btn @click="onLeave">
        <span>Leave</span>
        <v-icon>mdi-logout</v-icon>
      </v-btn>

      <!-- <v-btn @click="onCapture">
        <span>Capture Photo</span>
        <v-icon>mdi-map-marker</v-icon>
      </v-btn> -->

      <!-- <v-btn @click="onShareScreen">
        <span>Share Screen</span>
        <v-icon>mdi-history</v-icon>
      </v-btn> -->

      <v-btn @click="onChat">
        <span>Chatting</span>
        <v-icon>mdi-forum</v-icon>
      </v-btn>

      <v-btn @click="onNote">
        <span>Note</span>
        <v-icon>mdi-book</v-icon>
      </v-btn>

      <v-btn @click="onCanvas" :disabled="disableCanvasBool">
        <span>Canvas</span>
        <v-icon>mdi-palette</v-icon>
      </v-btn>

      <v-btn @click="onBroadcast">
        <span>BroadCast</span>
        <v-icon>mdi-cast</v-icon>
      </v-btn>

      <v-btn @click="offBroadcast">
        <span>BroadCast off</span>
        <v-icon>mdi-cast-off</v-icon>
      </v-btn>
    </v-bottom-navigation>
  </div>
<!-- </v-container> -->
</div>
</template>

<script>
import $ from 'jquery';
import Vue from 'vue';
import WebRTC from 'vue-webrtc';

Vue.use(WebRTC);

export default {
  name: 'Videochat',
  components: {
  },
  data() {
    return {
      img: null,
      roomId: 'public-roomqweasdasds',
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
      overlay: false,
      showNav: true,
      activeBtn: 0,
    };
  },
  watch: {
    overlay (val) {
      val && this.onJoin();
    },
  },
  methods: {
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
        // $('.video-list-2').css('height', '62%');
        $('.video-list-2').css('top', '100px');
        // $('.video-item-2').css('height', '500px');
      } else {
        // $('.video-list-2').css('height', '74%');
        $('.video-list-2').css('top', '0px');
        $('.video-item-2').css('height', '670px');
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
        this.NoteBool = false;
      }
    },
    onChat() {
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
      this.$refs.webrtc.join();
      this.disableCanvasBool = false;
      this.disableInputBool = false;
      $( window ).resize(function() {
        //창크기 변화 감지
        console.log(screen.width);
      });
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
      this.overlay = false;
    },
    appendDIV(event) {
      this.textArea = document.createElement('div');
      this.textArea.innerHTML = event.data || event;
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
      this.$refs.webrtc.rtcmConnection.send(this.value);
      this.appendDIV(this.value);
      e.target.value = '';
    },
  },
  updated() {
    this.$refs.webrtc.rtcmConnection.onmessage = this.appendDIV;
    $('video').click(function () {
      console.log('클릭');
    });
  },
  mounted() {
    this.chatContainer = document.querySelector('.chat-output');
  },
};
</script>