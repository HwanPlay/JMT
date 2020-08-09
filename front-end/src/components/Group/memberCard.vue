<template>
  <div>
    <v-list-item>
      <v-list-item-avatar :src="require('../../assets/profile/profile1.jpg')" color="grey" size="55"> <v-img :src="require('../../assets/profile/profile1.jpg')"></v-img></v-list-item-avatar>
      <v-list-item-content>
        <v-list-item-title class="headline">{{ userInfo.nickname }}</v-list-item-title>
        <v-list-item-subtitle>{{ userInfo.id }}</v-list-item-subtitle>
      </v-list-item-content>
    </v-list-item>
    <div v-if='this.$store.state.userId===hostId'>
      <v-btn :disabled='isExiled' color="danger" @click="exileMember">
        추방
      </v-btn>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

const SERVER_URL = 'http://localhost:8080/videoconference/api/';


export default {
  name: 'memberCard',
  props:{
    userInfo: Object,
    groupNo: Number,
    hostId: String,
  },
  data(){
    return {
      isExiled: false,
    };
  },
  methods:{
    exileMember(){
      axios.delete(SERVER_URL+'groupmember/delno/'+this.groupNo+'/'+this.userInfo.id)
        .then(res => {
          console.log(res, 'exile');
          this.isExiled = true;
        })
        .catch(err => console.log(err.response));
    }
  }
};
</script>

<style>

</style>