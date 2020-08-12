<template>
  <!-- <v-row justify="center"> -->
    <v-dialog v-model="dialog" width="600px">
      <template v-slot:activator="{ on, attrs }">
        <v-btn v-bind="attrs" v-on="on" color="orange" rounded>
          <v-icon color="white">fas fa-user-plus</v-icon>
        </v-btn>
      </template>
      <v-card>
        <v-card-title class="mb-0 pb-0">
          <v-col cols="5" class="headline">그룹 멤버 추가</v-col>
          <v-spacer></v-spacer>
          <v-text-field v-model='inputValue' cols="3" hint="이름 검색" @keypress.enter="searchName"></v-text-field>
          <v-btn text style='outline: none' @click="searchName">
            <v-icon>fas fa-search</v-icon>
          </v-btn>
        </v-card-title>
          <v-divider style="margin: 12px;"></v-divider>
        <v-card-text style="height: 300px;">
          <div v-if='!isSearched'>
            이름을 검색해주세요
          </div>
          <div v-else-if="(isSearched) && (searchData.length)" v-for="(user, i) in searchData" :key="i">
            <InviteMemberCard :user=user :groupNo=groupNo />
          </div>
          <div v-else>
            검색 결과가 없습니다!
            <v-icon></v-icon>
          </div>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="rgb(255, 128, 74)" text @click="dialog = false">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  <!-- </v-row> -->
</template>

<script>
import axios from 'axios';

import InviteMemberCard from './InviteMemberCard.vue';

import SERVER from '../../api/spring.js';

export default {
  name: 'InviteMember',
  components:{
    InviteMemberCard
  },
  props:{
    groupNo: Number
  },
  data () {
    return {
      dialog: false,
      inputValue: '',
      searchData: {},
      isSearched: false,
      checked: false,
      inputVal : ''
    };
  },
  methods: {
    searchName(){
      this.inputVal = this.inputValue.replace('#', '*');
      this.inputVal = this.inputVal.replace('&', '*');
      if (this.inputVal.trim()){
        axios.get(SERVER.URL + '/user/findUserByName?group_no='+this.groupNo+'&name=' + this.inputVal)
          .then(res => {
            console.log(res);
            this.searchData = res.data;
            this.inputValue = '';
            this.isSearched = true;
          });
      }
    },

  },
  mounted(){
    this.isSearched = false,
    this.searchData = {};
  }
};
</script>