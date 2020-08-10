<template>
  <v-card flat style="width: 500px; height: 500px;">
    <v-container>
      <v-snackbar v-model="snackbar" absolute top right color="success">
        <span>Registration successful!</span>
        <v-icon dark>mdi-checkbox-marked-circle</v-icon>
      </v-snackbar>
      <v-card-title>그룹 생성하기</v-card-title>
      <v-form ref="form" lazy-validation class="ml-2 mr-2">
        <v-row>
          <v-col cols= "5">
            <v-text-field v-model="createGroupInfo.groupName" :rules="rules.name" label="그룹 명" required></v-text-field>
          </v-col>
        </v-row>
        <v-textarea v-model="createGroupInfo.groupIntro" :rules="rules.intro" label="그룹 소개" required></v-textarea>
        <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn text color="error" class="mr-4" @click="reset">
          초기화
        </v-btn>
        <v-btn text color="blue" @click="submit">
          그룹 생성
        </v-btn>
        </v-card-actions>

      </v-form>
    </v-container>
  </v-card>
</template>

<script>
import axios from 'axios';

export default {
  name: 'createGroup',
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
      snackbar: false,
    };
  },

  methods: {
    reset () {
      this.$refs.form.reset();
    },
    submit () {
      this.snackbar = true;
      console.log(this.createGroupInfo);
      const SERVER_URL = 'http://localhost:8080/videoconference/api';
      axios.post(SERVER_URL + '/group/add', this.createGroupInfo)
        .then(res => {
          console.log('createGroup!', res);
          this.$router.push('/Home').catch(()=>{});
          this.reset();
          this.$emit('close');
        })
        .catch(err => console.log(err.response));
    },
  },
  computed:{
    
  }
};
</script>

<style>

</style>