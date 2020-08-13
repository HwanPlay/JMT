<template>
  <div>
    <v-list-item>
      <v-list-item-avatar color="grey" size="40"><v-img :src="require('../../assets/profile/profile1.jpg')"></v-img></v-list-item-avatar>
      <v-list-item-content>
        <v-list-item-title>{{ userInfo.nickname }}</v-list-item-title>
        <v-list-item-subtitle>{{ userInfo.id }}</v-list-item-subtitle>
      </v-list-item-content>
      <div v-if='this.$store.state.userId===hostId'>
        <v-btn :disabled='isExiled' color="danger" @click="exileMember" style='outline: none;'>
          추방
        </v-btn>
        <v-btn color="danger" @click="letHost">
          호스트 임명
        </v-btn>
      </div>
    </v-list-item>
  </div>
</template>

<script>
import axios from 'axios';
import SERVER from '../../api/spring.js';


export default {
  name: 'MemberCard',
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
      axios.delete(SERVER.URL+'/groupmember/delno/'+this.groupNo+'/'+this.userInfo.id)
        .then(() => {
          this.isExiled = true;
          this.$emit('refresh');
        })
        .catch(err => console.log(err.response));
    },
    letHost(){
      axios.put(SERVER.URL+'/group/host/'+this.groupNo, {hostId : this.userInfo.id})
        .then(() => {
          this.$router.push('Home');
          console.log('Change Host');
        })
        .catch(err => console.log(err.response));
    },
  }
};
</script>

<style>

</style>