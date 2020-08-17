<template>
  <div>
    <div v-if="this.$store.state.userId===userInfo.id">
      <v-window v-model='step'>
        <v-window-item :value="1">
          <v-list-item class="pl-0">
            <v-badge 
              v-if="userInfo.id === this.$store.state.userId"
              color="green" content="It's Me" left overlap offset-x="45" offset-y="15"
            >
              <v-list-item-avatar color="grey" size="40">
                <v-img :src="require('../../assets/profile/profile1.jpg')"></v-img>
              </v-list-item-avatar>
            </v-badge>

            <v-badge v-else dot color="rgb(0, 0, 0, 0)">
              <v-list-item-avatar color="grey" size="40">
                <v-img :src="require('../../assets/profile/profile1.jpg')"></v-img>
              </v-list-item-avatar>
            </v-badge>
            
            <v-list-item-content>
              <v-list-item-title>{{ nickName }}</v-list-item-title>
              <v-list-item-subtitle>{{ userInfo.id }}</v-list-item-subtitle>
            </v-list-item-content>

            <v-btn text color="rgb(255, 128, 74)" @click="step++" style='outline: none;'>
              닉네임 변경
            </v-btn>
          </v-list-item>
          </v-window-item>
          <v-window-item :value="2">
            <v-list-item class="pl-0">
            <v-text-field class="ml-5" v-model="newNickname" :counter="10" label="새 닉네임" required></v-text-field>
            <v-btn text color='rgb(255, 128, 74)' style='outline: none;' @click='changeNickname'>닉네임 변경</v-btn>
            <v-btn text color='red' style='outline: none;' @click='step--'>취소</v-btn>
            </v-list-item>
          </v-window-item>
      </v-window>
    </div>


    <v-list-item v-else class="pl-0">
      <v-badge 
        v-if="userInfo.id === this.$store.state.userId"
        color="green" content="It's Me" left overlap offset-x="45" offset-y="15"
      >
        <v-list-item-avatar color="grey" size="40">
          <v-img :src="require('../../assets/profile/profile1.jpg')"></v-img>
        </v-list-item-avatar>
      </v-badge>

      <v-badge v-else dot color="rgb(0, 0, 0, 0)">
        <v-list-item-avatar color="grey" size="40">
          <v-img :src="require('../../assets/profile/profile1.jpg')"></v-img>
        </v-list-item-avatar>
      </v-badge>
      
      <v-list-item-content>
        <v-list-item-title>{{ nickName }}</v-list-item-title>
        <v-list-item-subtitle>{{ userInfo.id }}</v-list-item-subtitle>
      </v-list-item-content>

      <div v-if='this.$store.state.userId===hostId'>
        <v-btn text :disabled='isExiled' color="red" @click="exileMember" style='outline: none;'>
          추방
        </v-btn>
        <v-btn text color="blue" @click="letHost" style="outline: none">
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
      step : 1,
      newNickname: this.userInfo.nickname,
      nickName : this.userInfo.nickname,
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
    changeNickname(){
      console.log(this.nickName, this.newNickname);
      axios.put(SERVER.URL+'/groupmember/name/'+this.userInfo.groupMemberNo, { nickname: this.newNickname })
        .then(() => { 
          this.nickName = this.newNickname;
          this.step = 1;
        });
    }
  }
};
</script>

<style>

</style>