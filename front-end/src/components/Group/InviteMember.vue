<template>
  <v-row justify="center">
    <v-dialog v-model="dialog" width="600px">
      <template v-slot:activator="{ on, attrs }">
        <v-btn color="danger" v-bind="attrs" v-on="on">
          <v-icon>fas fa-user-plus</v-icon>
        </v-btn>
        Add Member
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
            
            <v-list-item>
              <v-list-item-avatar :src="require(`../../assets/profile/profile-${i%4}.png`)" color="grey" size="55"> <v-img :src="require('../../assets/profile/profile1.jpg')"></v-img></v-list-item-avatar>
              <v-list-item-content>
                <v-list-item-title class="headline">{{ user.name }}</v-list-item-title>
                <v-list-item-subtitle>{{ user.id }}</v-list-item-subtitle>
              </v-list-item-content>
              <div class="my-2">
                <v-btn color="rgb(52, 63, 87)" dark style="outline: none;" @click="addMember(user)">
                  추가
                </v-btn>
              </div>
            </v-list-item>

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
  </v-row>
</template>

<script>
import axios from 'axios';

const SERVER_URL = 'http://localhost:8080/videoconference/api/';

export default {
  name: 'InviteMember',
  props:{
    groupNo: String
  },
  data () {
    return {
      dialog: false,
      inputValue: '',
      searchData: {},
      isSearched: false,
    };
  },
  methods: {
    searchName(){
      axios.get(SERVER_URL + 'user/findUserByName/' + this.inputValue)
        .then(res => {
          this.searchData = res.data;
          this.inputValue = '';
          this.isSearched = true;
        });
    },
    addMember(userInfo){
      const info = {
        groupNo: this.groupNo,
        id: userInfo.id,
        nickname: userInfo.name
      };
      axios.post(SERVER_URL + 'groupmember/add', info)
        .then((res) => {
          console.log('newmember :', res);
        })
        .catch((err) => 
          console.log(err.response));
    }
  },
  mounted(){
    this.isSearched = false,
    this.searchData = {};
  }
};
</script>