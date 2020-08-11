<template>
  <!-- <div style="height: 100%; width: 100%;"> -->
  <videochat :roomId = this.$route.params.roomId />
  <!-- </div> -->
</template>

<script>
import Videochat from '../components/Conference/Videochat.vue';
// import Videochat from '../components/Conference/VideochatGrid.vue';

export default {
  name: 'Conference',
  components: {
    Videochat,
  },
  mounted(){
    console.log('checkThis!!', this.$route.params.roomId);
  }
};
</script>
