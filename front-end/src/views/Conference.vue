<template>
  <videochat :meetingInfo="this.$route.query" />
</template>

<script>
import Videochat from '../components/Conference/Videochat.vue';
// import Videochat from '../components/Conference/VideochatGrid.vue';


export default {
  name: 'Conference',
  components: {
    Videochat,
  },
};
</script>
