<template>
    <v-card flat style="max-width: 500px;">
      <v-card-title class="top d-flex justify-center align-center">그룹 정보 수정</v-card-title>
      <v-container>
      <v-form ref="form" lazy-validation class="ml-2 mr-2">
        <v-row>
          <v-col cols= "5">
            <v-text-field v-model="editGroupInfo.groupName" :counter="10" :rules="rules.name" label="그룹 명" required></v-text-field>
          </v-col>
        </v-row>
        <v-textarea v-model="editGroupInfo.groupIntro" :counter="50" :rules="rules.intro" label="그룹 소개" required></v-textarea>
        <v-card-actions>
        <v-btn text color="error" class="mr-4" @click="alert = !alert">
          그룹 해체
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn text color="blue" @click="submit">
          정보 수정
        </v-btn>
        </v-card-actions>
        <v-alert :value="alert" outlined dense type="error" prominent border="left" transition="scale-transition">
          그룹과 관련된 모든 정보가 삭제됩니다. <br> 그룹 삭제를 원하시면 
          <strong><span style='text-decoration: underline; cursor: pointer;' @click='destroyGroup'>
            이곳
          </span></strong>을 클릭해주세요
        </v-alert>
      </v-form>
    </v-container>
  </v-card>
</template>


<script>
import axios from 'axios';
import SERVER from '../../api/spring.js';

export default {
  name: 'CreateGroup',
  data () {
    return {
      editGroupInfo:{
        groupIntro: this.groupInfo.groupIntro,
        groupName: this.groupInfo.groupName,
      },
      rules: {
        name: [val => (val || '').length > 0 || '그룹 이름을 정해주세요'],
        intro: [val => (val || '').length > 0 || '그룹 소개를 작성해주세요'],
      },
      snackbar: false,
      alert: false,
    };
  },
  props:{
    groupInfo: Object
  },

  methods: {
    submit () {
      this.snackbar = true;
      axios.put(SERVER.URL + '/group/all/'+this.groupInfo.groupNo, this.editGroupInfo)
        .then(res => {
          console.log('EditGroupInfo!', res);
          this.$store.commit('SET_GROUP_INFO', res);
          this.reset();
          this.$emit('close');
        })
        .catch(err => console.log(err.response));
    },

    destroyGroup(){
      axios.delete(SERVER.URL+'/group/delno/'+this.groupInfo.groupNo)
        .then(() => {
          this.$router.push('/Home');
        })
        .catch(err => console.log(err.response));  
    },
  },
};
</script>

<style>
  .top {
    background-color: rgb(52, 63, 87);
    color: white;
  }
</style>