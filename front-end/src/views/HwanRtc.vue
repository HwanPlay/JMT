<template>
  <div>

  <h1>
    Video OneWay Broadcasting using RTCMultiConnection
    <p class="no-mobile">
      Multi-user (one-to-many) video broadcasting using star topology.
    </p>
  </h1>

  <section class="make-center">
    <input type="text" id="room-id" value="abcdef" autocorrect=off autocapitalize=off size=20>
    <button id="open-room">Open Room</button>
    <button id="join-room">Join Room</button>
    <button id="open-or-join-room" @click="openjoin">Auto Open Or Join Room</button>

    <div id="videos-container" style="margin: 20px 0;"></div>

    <div id="room-urls" style="text-align: center;display: none;background: #F1EDED;margin: 15px -10px;border: 1px solid rgb(189, 189, 189);border-left: 0;border-right: 0;"></div>
  </section>
  </div>
</template>


<script>
export default {
  name: 'HwanRtc',
  data() {
    return {
      connection: new RTCMultiConnection(),
    }
  },
  methods: {
    openjoin() {
      disableInputButtons();
      connection.openOrJoin(document.getElementById('room-id').value, function(isRoomExist, roomid) {
        if (isRoomExist === false && connection.isInitiator === true) {
            // if room doesn't exist, it means that current user will create the room
            showRoomURL(roomid);
        }

        if(isRoomExist) {
          connection.sdpConstraints.mandatory = {
              OfferToReceiveAudio: true,
              OfferToReceiveVideo: true
          };
        }
    });
    },

  },
  mounted() {
    this.connection.socketURL = '/';

// comment-out below line if you do not have your own socket.io server
// connection.socketURL = 'https://rtcmulticonnection.herokuapp.com:443/';

this.connection.socketMessageEvent = 'video-broadcast-demo';

this.connection.session = {
    audio: true,
    video: true,
    oneway: true
};

this.connection.sdpConstraints.mandatory = {
    OfferToReceiveAudio: false,
    OfferToReceiveVideo: false
};

// https://www.rtcmulticonnection.org/docs/iceServers/
// use your own TURN-server here!
this.connection.iceServers = [{
    'urls': [
        'stun:stun.l.google.com:19302',
        'stun:stun1.l.google.com:19302',
        'stun:stun2.l.google.com:19302',
        'stun:stun.l.google.com:19302?transport=udp',
    ]
}];

this.connection.videosContainer = document.getElementById('videos-container');

this.connection.onstream = function(event) {
    var existing = document.getElementById(event.streamid);
    if(existing && existing.parentNode) {
      existing.parentNode.removeChild(existing);
    }

    event.mediaElement.removeAttribute('src');
    event.mediaElement.removeAttribute('srcObject');
    event.mediaElement.muted = true;
    event.mediaElement.volume = 0;

    var video = document.createElement('video');

    try {
        video.setAttributeNode(document.createAttribute('autoplay'));
        video.setAttributeNode(document.createAttribute('playsinline'));
    } catch (e) {
        video.setAttribute('autoplay', true);
        video.setAttribute('playsinline', true);
    }

    if(event.type === 'local') {
      video.volume = 0;
      try {
          video.setAttributeNode(document.createAttribute('muted'));
      } catch (e) {
          video.setAttribute('muted', true);
      }
    }
    video.srcObject = event.stream;

    var width = parseInt(this.connection.videosContainer.clientWidth / 3) - 20;
    var mediaElement = getHTMLMediaElement(video, {
        title: event.userid,
        buttons: ['full-screen'],
        width: width,
        showOnMouseEnter: false
    });

    this.connection.videosContainer.appendChild(mediaElement);

    setTimeout(function() {
        mediaElement.media.play();
    }, 5000);

    mediaElement.id = event.streamid;
};

this.connection.onstreamended = function(event) {
    var mediaElement = document.getElementById(event.streamid);
    if (mediaElement) {
        mediaElement.parentNode.removeChild(mediaElement);

        if(event.userid === this.connection.sessionid && !this.connection.isInitiator) {
          alert('Broadcast is ended. We will reload this page to clear the cache.');
          location.reload();
        }
    }
};

this.connection.onMediaError = function(e) {
    if (e.message === 'Concurrent mic process limit.') {
        if (DetectRTC.audioInputDevices.length <= 1) {
            alert('Please select external microphone. Check github issue number 483.');
            return;
        }

        var secondaryMic = DetectRTC.audioInputDevices[1].deviceId;
        this.connection.mediaConstraints.audio = {
            deviceId: secondaryMic
        };

        this.connection.join(this.connection.sessionid);
    }
};

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