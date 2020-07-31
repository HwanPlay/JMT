<template>
  <v-dialog Dark v-model="dialog" persistent max-width="750px">
    <template v-slot:activator="{ on, attrs }">
      <v-btn color="primary" v-bind="attrs" v-on="on" class="mr-2" style="width: 92.88px; padding: 20px; color: white;">
        회원가입
      </v-btn>
    </template>
    <v-card><!-- 회원가입 폼 양식을 주려면 여기서 -->
      <v-card-title>
        <span class="headline">회원가입</span>
      </v-card-title>
      <v-card-text>
        <v-row align="center">
          <v-row justify="center">
            <v-carousel style="height: 250px; width:300px;">
              <v-carousel-item v-for="(profile,i) in profiles" :key="i" reverse-transition="fade-transition" transition="fade-transition">
                <v-row align="center" justify="center">
                  <v-img height="200" width="200" :src="profile.src"></v-img>
                </v-row>
              </v-carousel-item>
            </v-carousel>
          </v-row>
          
          <v-form style="width: 300px; margin-right: 20px;" cols="12" md="6" sm="6" ref="form" v-model="valid">
            <v-text-field style="padding-top: 20px;" v-model="name" :counter="10" :rules="nameRules" label="Name" required></v-text-field>
            <v-text-field style="padding-top: 20px;" v-model="email" :rules="emailRules" label="E-mail(ID)" required></v-text-field>
            <v-text-field type="password" style="padding-top: 20px;" v-model="password" :rules="passwordRules" label="Password" required></v-text-field>
            <v-text-field type="password" style="padding-top: 20px;" v-model="passwordConfirm" :rules="passwordConfirmRules" label="Password Confirmation" required></v-text-field>
          </v-form>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darken-1" text @click="close">Close</v-btn>
        <v-btn color="blue darken-1" text @click="close">SignUp</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
  export default {
    name: 'Signup',
    data: () => ({
      dialog: false,
      valid: true,
      name: '',
      nameRules: [
        v => !!v || '이름을 입력해주세요',
        v => (v && v.length <= 10) || '10자 이내의 이름을 입력해주세요',
      ],
      email: '',
      emailRules: [
        v => !!v || 'E-mail을 입력해주세요',
        v => /.+@.+\..+/.test(v) || '바른 E-mail 형식을 입력해주세요',
      ],
      password: "",
      passwordRules: [
        v => !!v || "패스워드를 입력해주세요",
        v => (v && v.length >= 8) || "8자 이상의 비밀번호를 입력해주세요"
      ],
      passwordConfirm: "",
      profiles: [
        { src: require('../assets/profile/profile1.jpg')},
        { src: require('../assets/profile/profile2.png')},
        { src: require('../assets/profile/profile3.png')},
        { src: require('../assets/profile/profile4.png')},
        { src: require('../assets/profile/profile5.png')}
      ],
    }),
    methods: {
      close(){
        this.dialog = false;
        this.resetValidation();
      },
      resetValidation(){
        this.$refs.form.reset()
      }
    },
    computed: {
      passwordConfirmRules(){
        return (this.password === this.passwordConfirm) || Array('패스워드가 일치하지 않습니다.')
      }
    },
  }
</script>