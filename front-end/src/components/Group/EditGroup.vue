<template>
  <v-card flat style="max-width: 500px; height: 100%">
    <v-card-title class="top d-flex justify-center align-center">그룹 정보 수정</v-card-title>
      <v-container class="p-4">

      <b-form-group label="이름" label-for="input-1">
        <b-form-input
          id="input-1"
          v-model="editGroupInfo.groupName"
          required
          :state="editGroupInfo.groupName.length < 9 && 0 < editGroupInfo.groupName.length"
          placeholder="그룹 이름">
        </b-form-input>
      </b-form-group>
      
      <b-form-group id="input-group-2" class="mt-7 " label="그룹 소개" label-for="input-2">
        <b-form-textarea
          id="input-2"
          v-model="editGroupInfo.groupIntro"
          required
          :state="editGroupInfo.groupIntro.length < 51 && 0 < editGroupInfo.groupIntro.length"
          placeholder="그룹 소개"
          rows="5" 
        ></b-form-textarea>
      </b-form-group>
        <v-btn class="mt-4" color="rgb(255, 128, 74)" block style="outline:none" :disabled="!valid" @click="submit">
          <div style="color: white">수정</div>
        </v-btn>
        <v-btn class="mt-4" color="rgb(187, 201, 224)" block style="outline:none" :disabled="!valid" @click="alert=!alert">
          <div style="color: white">해체</div>
        </v-btn>
        <v-alert class="mt-6" :value="alert" outlined dense type="error" prominent border="left" transition="scale-transition">
          그룹과 관련된 모든 정보가 삭제됩니다. <br> 그룹 삭제를 원하시면 
          <strong><span style='text-decoration: underline; cursor: pointer;' @click='destroyGroup'>
            이곳
          </span></strong>을 클릭해주세요
        </v-alert>
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
    groupInfo: Object,
    onModal: Boolean,
  },

  methods: {
    submit () {
      this.snackbar = true;
      axios.put(SERVER.URL + '/group/all/'+this.groupInfo.groupNo, this.editGroupInfo)
        .then(res => {
          this.$store.commit('SET_GROUP_INFO', res);
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
  computed: {
    valid(){
      return (this.editGroupInfo.groupIntro && this.editGroupInfo.groupName);
    }
  },
  watch:{
    onModal(){
      this.editGroupInfo.groupIntro = this.groupInfo.groupIntro;
      this.groupName = this.groupInfo.groupName;
    }
  }
};
</script>

<style>
  .top {
    background-color: rgb(52, 63, 87);
    color: white;
  }
  .form-control:focus {
    border-color: rgb(255, 128, 74);
    box-shadow: 0 0 0 0.2rem rgba(255, 128, 74, 0.3);
  }
</style>