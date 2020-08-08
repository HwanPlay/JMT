<template>
  <!-- <div style="height: 100%; width: 100%;"> -->
  <videochat />
  <!-- </div> -->
</template>

<script>
// import Videochat from '../components/Conference/Videochat.vue';
import Videochat from "../components/Conference/VideochatGrid.vue";

export default {
  name: "Conference",
  components: {
    Videochat,
  },
};
</script>