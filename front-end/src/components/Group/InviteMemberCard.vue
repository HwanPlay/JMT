<template>
  <v-list-item>
    <v-list-item-avatar :src="require(`../../assets/profile/profile-1.png`)" color="grey" size="55"> <v-img :src="require('../../assets/profile/profile1.jpg')"></v-img></v-list-item-avatar>
    <v-list-item-content>
      <v-list-item-title class="headline">{{ user.name }}</v-list-item-title>
      <v-list-item-subtitle>{{ user.id }}</v-list-item-subtitle>
    </v-list-item-content>
    <div v-if='!isInvited' class="my-2">
      <v-btn  color="rgb(52, 63, 87)" dark style="outline: none;" @click="addMember(user)">
        추가
      </v-btn>
    </div>
    <div v-else class="my-2">
      <v-btn :disabled='true' color="rgb(52, 63, 87)" dark style="outline: none;" @click="addMember(user)">
        완료
      </v-btn>
    </div>
  </v-list-item>
</template>

<script>
import axios from 'axios';
import SERVER from '../../api/spring.js';


export default {
  name: 'InviteMemberCard',
  props: {
    user: Object,
    groupNo: Number
  },
  data(){
    return {
      isInvited: false
    };
  },
  methods:{
    addMember(userInfo){
      const info = {
        groupNo: this.groupNo,
        id: userInfo.id,
        nickname: userInfo.name
      };
      axios.post(SERVER.URL + '/groupmember/add', info)
        .then((res) => {
          console.log('newmember :', res);
          this.isInvited = true;
        })
        .catch((err) => 
          console.log(err.response));
    }
  }
};
</script>

<style>

</style>