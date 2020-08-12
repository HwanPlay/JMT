<template>
  <div>
    <select id="picture" class="image-picker show-html"></select>
    <video autoplay></video>
    <p><button>Enable Capture</button></p>
  </div>
</template>
<!--
<script type="module" src="../../assets/sharescreen/jquery.js"></script>
<script type="module" src="../../assets/sharescreen/image-picker.js"></script>
<script type="module" src="../../assets/sharescreen/app.js"></script>
-->
<script>
// import { refresh, addSource, showSources, toggle, onAccessApproved } from '../../assets/sharescreen/app';
const { desktopCapturer } = window.require('electron');
const $ = require('jquery');

let desktopSharing = false;
let localStream;

export default {
  name: 'Sharescreen',
  methods: {
    refresh() {
      $('select').imagepicker({
        hide_select: true,
        show_label: true
      });
    },
    addSource(source) {
      console.log('add');
      $('select').append($('<option>', {
        value: source.id.replace(':', ''),
        text: source.name
      }));
      $('select option[value="' + source.id.replace(':', '') + '"]').attr('data-img-src', source.thumbnail.toDataURL());
      this.refresh();
    },
    showSources() {
      console.log('show');
      desktopCapturer.getSources({ types: ['window', 'screen'] }).then(async sources => {
        for (let source of sources) {
          console.log('Name: ' + source.name);
          this.addSource(source);
        }
      });
    },
    toggle() {
      if (!desktopSharing) {
        var id = ($('select').val()).replace(/window|screen/g, function (match) { return match + ':'; });
        this.onAccessApproved(id);
      } else {
        desktopSharing = false;

        if (localStream)
          localStream.getTracks()[0].stop();
        localStream = null;

        document.querySelector('button').innerHTML = 'Enable Capture';

        $('select').empty();
        this.showSources();
        this.refresh();
      }
    },
    onAccessApproved(desktop_id) {
      if (!desktop_id) {
        console.log('Desktop Capture access rejected.');
        return;
      }
      desktopSharing = true;
      document.querySelector('button').innerHTML = 'Disable Capture';
      console.log('Desktop sharing started.. desktop_id:' + desktop_id);
      navigator.webkitGetUserMedia({
        audio: false,
        video: {
          mandatory: {
            chromeMediaSource: 'desktop',
            chromeMediaSourceId: desktop_id,
            minWidth: 1280,
            maxWidth: 1280,
            minHeight: 720,
            maxHeight: 720
          }
        }
      }, gotStream, getUserMediaError);

      function gotStream(stream) {
        localStream = stream;
        let video = document.querySelector('video');
        video.srcObject = stream;
        video.onloadedmetadata = () => video.play();
        stream.onended = function () {
          if (desktopSharing) {
            this.toggle();
          }
        };
      }

      function getUserMediaError(e) {
        console.log('getUserMediaError: ' + JSON.stringify(e, null, '---'));
      }
    }
  },
  created() {
    document.querySelector('button').addEventListener('click', function () {
      this.toggle();
    });
  },
  updated() {
    $(document).ready(function () {
      this.showSources();
      this.refresh();
    });
  }
};
</script>

<style scoped>
  body {
    background: white;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
  }
  video {
    width: 480px;
    height: 360px;
    background: rgba(0,0,0,0.25);
  }
  button {
    display: inline-block;
    background: -webkit-linear-gradient(#F9F9F9 40%, #E3E3E3 70%);
    background: linear-gradient(#F9F9F9 40%, #E3E3E3 70%);
    border: 1px solid #999;
    -webkit-border-radius: 3px;
    border-radius: 3px;
    padding: 5px 8px;
    outline: none;
    white-space: nowrap;
    -webkit-user-select: none;
    user-select: none;
    cursor: pointer;
    text-shadow: 1px 1px #fff;
    font-weight: 700;
    font-size: 10pt;
  }
  button:hover,
  button.active {
    border-color: black;
  }
  button:active,
  button.active {
    background: -webkit-linear-gradient(#E3E3E3 40%, #F9F9F9 70%);
    background: linear-gradient(#E3E3E3 40%, #F9F9F9 70%);
  }
  video {
    background: gray center no-repeat;
    border: 1px solid #e2e2e2;
    box-shadow: 0 1px 1px rgba(0,0,0,0.2);
  }
  ul.thumbnails.image_picker_selector {
  overflow: auto;
  list-style-image: none;
  list-style-position: outside;
  list-style-type: none;
  padding: 0px;
  margin: 0px; }
  ul.thumbnails.image_picker_selector ul {
    overflow: auto;
    list-style-image: none;
    list-style-position: outside;
    list-style-type: none;
    padding: 0px;
    margin: 0px; }
  ul.thumbnails.image_picker_selector li.group_title {
    float: none; }
  ul.thumbnails.image_picker_selector li {
    margin: 0px 12px 12px 0px;
    float: left; }
    ul.thumbnails.image_picker_selector li .thumbnail {
      padding: 6px;
      border: 1px solid #dddddd;
      -webkit-user-select: none;
      -moz-user-select: none;
      -ms-user-select: none; }
      ul.thumbnails.image_picker_selector li .thumbnail img {
        -webkit-user-drag: none; }
    ul.thumbnails.image_picker_selector li .thumbnail.selected {
    background: #0088cc; }
</style>