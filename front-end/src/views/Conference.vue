<template>
  <videochat :roomId="this.$route.params.roomId" :hostId="this.$route.params.hostId" :meetingInfo="this.$route.query" />
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
    console.log(this.$route.params.roomId, this.$route.query);
  }
};
</script>
