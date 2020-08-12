<template>
  <videochat :roomId='this.$route.params.roomId' />
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
    console.log('checkThis!!', this.$route.params);
  }
};
</script>
