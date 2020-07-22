<template>
  <div>
    <h1>화상회의를 만들어 보자꾸나</h1>
    <hr />
    <button id="open-or-join-room" @click="openRoom">채팅방 입장</button>
    <hr />
    <div id="chat-container">
      <input type="text" id="input-text-chat" placeholder="Enter Text Chat" disabled />
      <br />
      <div class="chat-output"></div>
    </div>
    <hr />
  </div>
</template>

<script src="https://rtcmulticonnection.herokuapp.com/dist/RTCMultiConnection.js"></script>
<script src="https://cdn.webrtc-experiment.com/FileBufferReader.js"></script>

<!-- socket.io for signaling -->
<script src="https://rtcmulticonnection.herokuapp.com/socket.io/socket.io.js"></script>

<script>
export default {
  data() {
    return {

    };
  },
  methods: {
    openRoom() {
      var connection = new RTCMultiConnection();
      connection.socketURL = "https://rtcmulticonnection.herokuapp.com:443/";
      connection.session = {
        audio: true,
        video: true,
        data: true
      };
      connection.openOrJoin("jin");
    }
  },

  created() {
    let src1 = document.createElement("script");
    src1.setAttribute(
      "src",
      "https://rtcmulticonnection.herokuapp.com/dist/RTCMultiConnection.js"
    );
    document.body.appendChild(src1);

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
  }
};
</script>

<style>
</style>