<template>
  <div style="height: 100%; width: 100%;">
    <div class="mainCoference">
      <div class="main_1 main_common">
        <videochat />
      </div>
    </div>
    <!-- <div class="main_2 main_common">
      <Sharescreen />
    </div> -->
    <!-- <div class="main_3 main_common">
      <Webrtc />
    </div> -->
  </div>
</template>

<script>
import Videochat from '../components/Conference/Videochat.vue';
// import Sharescreen from '../components/Conference/Sharescreen.vue';
import Webrtc from '../components/Conference/webrtc.vue';

export default {
  name: 'Conference',
  components: {
    Videochat,
    // Sharescreen,
    // Webrtc,
  },
};
</script>

<style>
.mainCoference {
  position: relative;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
}

.main_1 {
  width: 100%;
  height: 100%;
  float: left;
  position: relative;
}
</style>