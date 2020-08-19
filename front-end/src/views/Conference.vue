<template>
  <videochat :groupInfo="this.$route.params" :meetingInfo="this.$route.query" />
</template>

<script>
// import Videochat from '../components/Conference/Videochat.vue';
import Videochat from '../components/Conference/devVideochat.vue';

export default {
  name: 'Conference',
  components: {
    Videochat,
  },
  mounted(){
    console.log(this.$route.params, this.$route.query);
  }
};
</script>
