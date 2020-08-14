<template>
  <v-list-item>
    <v-list-item-content>
      <v-list-item-title>{{ Request.groupName }} 그룹</v-list-item-title>
      <v-list-item-subtitle>{{ Request.hostId }}님의 초대</v-list-item-subtitle>
    </v-list-item-content>
    <div>
      <v-btn color='error' style='outline: none;' @click='acceptInvite'>수락</v-btn>
      <v-btn color='danger' style='outline: none' @click='removeRequest'>거절</v-btn>
    </div>
  </v-list-item>
</template>

<script>
import axios from 'axios';
import SERVER from '../../api/spring.js';

export default {
  name: 'RequestCard',
  props: {
    Request: Object
  },
  methods:{
    acceptInvite(){
      const Info = {
        groupNo: this.Request.groupNo,
        id: this.$store.state.userId,
        nickname: this.$store.state.myName
      };
      console.log(Info);
      axios.post(SERVER.URL + '/groupmember/add', Info);
      console.log('good');
      this.removeRequest();
    },
    removeRequest(){
      axios.delete(SERVER.URL + '/request/delno/' + this.Request.requestNo)
        .then(() => {
          this.$el.parentNode.removeChild(this.$el);
        });
    }
  }
};
</script>

<style>

</style>