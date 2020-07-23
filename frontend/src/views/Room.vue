<template>
  <div>
    <h1>화상회의를 만들어 보자꾸나</h1>
    <hr />
    <button id="open-or-join-room" @click="openRoom" :disabled="disableOpenChat">채팅방 입장</button>
    <hr />
    <div id="chat-container">
      <input type="text" id="input-text-chat" placeholder="Enter Text Chat" @keyup.13="textSend" :disabled="disableInputBool" />
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
  data(){
    return {
      disableInputBool : true,
      inputText : "",
      disableOpenChat : false

    }
  },
  methods: {
    appendDIV(event) {
      var connection = new RTCMultiConnection();
    var chatContainer = document.querySelector('.chat-output');
    var div = document.createElement('div');
    div.innerHTML = event.data || event;
    chatContainer.insertBefore(div, chatContainer.firstChild);
    div.tabIndex = 0; div.focus();
    
    document.getElementById('input-text-chat').focus();
    },
    openRoom() {
      this.disableInputBool = false;
      this.disableOpenChat = true;
      var connection = new RTCMultiConnection();
      connection.socketURL = "https://rtcmulticonnection.herokuapp.com:443/";
      connection.session = {
        audio: true,
        video: true,
        data: true
      };
      connection.openOrJoin("jin");

      connection.onstream = function(event) {
          document.body.appendChild(event.mediaElement);
      };
      

    },
    textSend(e){
      var connection = new RTCMultiConnection();
      // removing trailing/leading whitespace
      this.value = 'a'+':'+e.target.value.toString().replace(/^\s+|\s+$/g, '');
      // .replace(/^\s+|\s+$/g,'') : 앞뒤 공백 제거
      console.log(this.value);
      connection.send(this.value);
      console.log(connection.send);
      // connection.onmessage = this.appendDIV(this.value);
      connection.onmessage = this.appendDIV(this.value);

      e.target.value =  '';
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