<template>
  <!-- <v-row justify="center"> -->
    <v-dialog v-model="dialog" width="600px">
      <v-snackbar v-model="successSnackbar" :timeout="500" centered absolute center color="success">
        <span>초대장 발송 완료!</span>
      </v-snackbar>
      <v-snackbar v-model="failSnackbar" :timeout="500" centered absolute center color="error">
        <span>이미 초대장을 보냈습니다!</span>
      </v-snackbar>
      <template v-slot:activator="{ on, attrs }">
        <v-btn text style="outline:none" v-bind="attrs" v-on="on" color="white">
          <v-icon color="orange">fas fa-user-plus</v-icon>
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
          <div v-else-if="(isSearched) && searchData && (searchData.length)" v-for="user in searchData" :key="user.id">
            <InviteMemberCard :user=user :groupNo=groupNo :groupName=groupName :hostId=hostId @send="successSnackbar=true" @fail="failSnackbar=true" />
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
    groupNo: Number,
    groupName: String,
    hostId: String
  },
  data () {
    return {
      dialog: false,
      inputValue: '',
      searchData: {},
      isSearched: false,
      checked: false,
      inputVal : '',
      successSnackbar: false,
      failSnackbar : false,
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
      }},
  },
  watch:{
    dialog(){
      this.isSearched = false;
      this.SearchData = {};
    }
  }
};
</script>