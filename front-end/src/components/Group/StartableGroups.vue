<template>
  <v-card flat style="width: 100%; height: 100%;" >
    <v-card-title class="top align-ccenter justify-center">회의 시작하기</v-card-title>
    <v-container>
      <div v-if="sGroups && sGroups.length===0">
        당신의 그룹이 없습니다!
      </div>
      <v-card-text v-for="sGroup in sGroups" :key='sGroup.groupNo' style='padding: 5px;'>
        <GroupCard :groupInfo=sGroup :meetingModalOn=meetingModalOn />
        <br>
      </v-card-text>
      </v-container>
        <v-card-actions>
          <v-spacer></v-spacer>
        <v-btn text color="error" class="mr-4" @click="closeModal">닫기</v-btn>
      </v-card-actions>
  </v-card>
</template>

<script>
import axios from 'axios';

import SERVER from '../../api/spring.js';
import GroupCard from './GroupCard.vue';

export default {
  name: 'StartableGroups',
  components: {
    GroupCard,
  },
  props:{
    meetingModalOn: Boolean,
  },
  data(){
    return {
      sGroups : [],
    };
  },
  methods:{
    closeModal(){
      this.$emit('closeModal');
    },
  },
  mounted() {
    axios.get(SERVER.URL+'/group/gethost/'+this.$store.state.userId)
      .then(res => {
        console.log('watch : ', res.data);
        this.sGroups = res.data.groups;
      })
      .catch(error => console.log(error.response));
  }
};
</script>

<style>
  .top {
    background-color: rgb(52, 63, 87);
    color: white;
  }
</style>