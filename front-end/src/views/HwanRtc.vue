<template>
  <div>
    <h1>
      Video OneWay Broadcasting using RTCMultiConnection
      <p class="no-mobile">Multi-user (one-to-many) video broadcasting using star topology.</p>
    </h1>

    <section class="make-center">
      <input
        type="text"
        id="room-id"
        value="abcdef"
        autocorrect="off"
        autocapitalize="off"
        size="20"
      />
      <button id="open-room">Open Room</button>
      <button id="join-room">Join Room</button>
      <button id="open-or-join-room" @click="openjoin">Auto Open Or Join Room</button>

      <div id="videos-container" style="margin: 20px 0;"></div>

      <div
        id="room-urls"
        style="text-align: center;display: none;background: #F1EDED;margin: 15px -10px;border: 1px solid rgb(189, 189, 189);border-left: 0;border-right: 0;"
      ></div>
    </section>
  </div>
</template>


<script>
import connection from '../api/RTCMultiConnection.js';

export default {
  name: 'HwanRtc',
  data() {
    return {
    };
  },
  porps: {
    connection,
  },
  methods: {
    openjoin() {
      this.disableInputButtons();
      connection.openOrJoin(document.getElementById('room-id').value, function(isRoomExist, roomid) {
        if (isRoomExist === false && this.connection.isInitiator === true) {
          // if room doesn't exist, it means that current user will create the room
          this.showRoomURL(roomid);
        }

        if(isRoomExist) {
          this.connection.sdpConstraints.mandatory = {
            OfferToReceiveAudio: true,
            OfferToReceiveVideo: true
          };
        }
      });
    },

    disableInputButtons() {
      // document.getElementById('room-id').onkeyup();

      document.getElementById('open-or-join-room').disabled = true;
      document.getElementById('open-room').disabled = true;
      document.getElementById('join-room').disabled = true;
      document.getElementById('room-id').disabled = true;
    },
    showRoomURL(roomid) {
      var roomHashURL = '#' + roomid;
      var roomQueryStringURL = '?roomid=' + roomid;

      var html = '<h2>Unique URL for your room:</h2><br>';

      html += 'Hash URL: <a href="' + roomHashURL + '" target="_blank">' + roomHashURL + '</a>';
      html += '<br>';
      html += 'QueryString URL: <a href="' + roomQueryStringURL + '" target="_blank">' + roomQueryStringURL + '</a>';

      var roomURLsDiv = document.getElementById('room-urls');
      roomURLsDiv.innerHTML = html;

      roomURLsDiv.style.display = 'block';
    }

  },
  mounted() {

  }
};
</script>

<style>
button {
  outline-color: brown;
  outline-style: solid;
  outline-width: 5px;
}
</style>