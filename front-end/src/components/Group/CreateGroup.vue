<template>
  <v-card flat style="max-width: 500px; max-height: 500px;">
    <v-card-title class="top d-flex justify-center align-center">그룹 생성하기</v-card-title>
    <v-container>
      <v-form ref="form" lazy-validation class="ml-2 mr-2">
        <v-row>
          <v-col cols= "5">
            <v-text-field v-model="createGroupInfo.groupName" :counter="10" :rules="rules.name" label="그룹 명" required></v-text-field>
          </v-col>
          <v-spacer></v-spacer>
          <v-col cols= "6">
            <!-- <div class="hostId">{{ $store.state.userId }}</div> -->
          </v-col>
        </v-row>
        <v-textarea v-model="createGroupInfo.groupIntro" :counter="80" :rules="rules.intro" label="그룹 소개" required></v-textarea>
        <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn text color="error" class="mr-4" @click="reset">
          초기화
        </v-btn>
        <v-btn :disabled="!valid" text color="blue" @click="submit">
          그룹 생성
        </v-btn>
        </v-card-actions>

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
      createGroupInfo:{
        groupIntro: '',
        groupName: '',
        hostId: this.$store.state.userId
      },
      rules: {
        name: [val => (val || '').length > 0 || '그룹 이름을 정해주세요'],
        intro: [val => (val || '').length > 0 || '그룹 소개를 작성해주세요'],
      },
    };
  },

  methods: {
    reset () {
      this.$refs.form.reset();
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
    }
  },
  mounted(){
    this.reset();
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
</style>