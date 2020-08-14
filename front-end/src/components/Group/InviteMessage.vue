<template>
  <v-card class="mx-auto" max-width="344">
    {{ message }}
    <v-card-text>
      <div>그룹 초대 메세지</div>
      <p class="display-1 text--primary">
        {{ message.hostId }}님의 초대
      </p>
      <p>{{ message.groupName }} 그룹</p>
    </v-card-text>
    <v-card-actions>
      <v-btn text color="deep-purple accent-4" @click='removeRequest'>
        거절
      </v-btn>
      <v-spacer></v-spacer>
      <v-btn text color="deep-purple accent-4" @click='acceptInvite'>
        수락
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import axios from 'axios';
import SERVER from '../../api/spring.js';

export default {
  name: 'InviteMessage',
  props: {
    message: Object
  },
  methods:{
    acceptInvite(){
      const Info = {
        groupNo: this.Request.groupNo,
        id: this.$store.state.userId,
        nickname: this.$store.state.userId
      };
      axios.post(SERVER.URL + '/groupmember/add', Info);
      this.removeRequest();
    },
    removeRequest(){
      axios.delete(SERVER.URL + '/request/delno/' + Request.requestNo)
        .then(() => {
          this.$el.parentNode.removeChild(this.$el);
        });
    }
  }
};
</script>

<style>

</style>