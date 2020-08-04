<template>
  <div>
    <select id="picture" class="image-picker show-html"></select>
    <video autoplay></video>
    <p><button>Enable Capture</button></p>
  </div>
</template>

<script>
export default {
  name: 'Sharescreen',
};
</script>

<style scoped>
  body {
    background: white;
    display: -webkit-flex;
    -webkit-justify-content: center;
    -webkit-align-items: center;
    -webkit-flex-direction: column;
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