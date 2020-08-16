<template>
  <v-card flat style="width: 500px; height: 500px;">
    <v-container>
      <v-card-title>회의 시작하기</v-card-title>
      <v-divider></v-divider>
      <div v-if="sGroups && sGroups.length===0">
        당신의 그룹이 없습니다!
      </div>
      <v-card-text v-for="sGroup in sGroups" :key='sGroup.groupNo' style='padding: 5px;'>
        <GroupCard :groupInfo=sGroup />
        <br>
      </v-card-text>
        <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn text color="error" class="mr-4" @click="closeModal">
          닫기
        </v-btn>
        </v-card-actions>
    </v-container>
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
  data(){
    return {
      sGroups : []
    };
  },
  methods:{
    closeModal(){
      this.$emit('closeModal');
    }
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

</style>