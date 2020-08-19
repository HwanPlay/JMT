<template>
  <v-card flat style="max-width: 500px; height: 95%;">
    <v-card-title class="top d-flex justify-center align-center">그룹 생성하기</v-card-title>
      <v-container class="p-4">

        <b-form-group label="이름" label-for="input-1">
          <b-form-input
            id="input-1"
            v-model="createGroupInfo.groupName"
            required
            :state="createGroupInfo.groupName.length < 11 && 0 < createGroupInfo.groupName.length"
            placeholder="그룹 이름">
          </b-form-input>
        </b-form-group>
        
        <b-form-group id="input-group-2" class="mt-7 " label="그룹 소개" label-for="input-2">
          <b-form-textarea
            id="input-2"
            v-model="createGroupInfo.groupIntro"
            required
            :state="createGroupInfo.groupIntro.length < 51 && 0 < createGroupInfo.groupIntro.length"
            placeholder="그룹 소개"
            rows="5" 
          ></b-form-textarea>
        </b-form-group>

        <v-card-actions class="p-0">
          <v-btn class="mt-4" color="rgb(255, 128, 74)" block style="outline: none" :disabled="!valid" @click="submit">
            <div style="color:white">CREATE</div>
          </v-btn>
        </v-card-actions>

      </v-container>
  </v-card>
</template>

<script>
import axios from 'axios';
import SERVER from '../../api/spring.js';


export default {
  name: 'CreateGroup',
  props:{
    modalOn: Boolean,
  },
  data () {
    return {
      step: 1,
      createGroupInfo:{
        groupIntro: '',
        groupName: '',
        hostId: this.$store.state.userId,
      },
    };
  },

  methods: {
    reset () {
      this.createGroupInfo.groupIntro = '';
      this.createGroupInfo.groupName = '';
    },
    submit () {
      console.log(this.createGroupInfo);
      axios.post(SERVER.URL + '/group/add', this.createGroupInfo)
        .then(res => {
          console.log('CreateGroup!', res);
          this.$store.commit('SET_GROUP_INFO', res);
          this.reset();
          this.$emit('close');
        })
        .catch(err => console.log(err.response));
    },
  },
  computed:{
    valid (){
      return (this.createGroupInfo.groupIntro && this.createGroupInfo.groupName);
    },
  },
  mounted(){
    this.reset();
  },
  watch:{
    modalOn(){
      this.reset();
    }
  }
};
</script>

<style>
  .top {
    background-color: rgb(52, 63, 87);
    color: white;
  }
  .hostId{
    border-bottom-width: 10px;
  }
  .form-control:focus {
    border-color: rgb(255, 128, 74);
    box-shadow: 0 0 0 0.2rem rgba(255, 128, 74, 0.3);
  }
  
</style>