<template>
  <v-main class="main">
    <v-container class="fill-height container" fluid>
      <v-row align="left" justify="left">

        <!-- 로그인 -->
        <v-col cols="12" sm="8" md="5">
              <v-spacer></v-spacer>
              <v-form>
                <h4 style="color: rgb(229, 235, 239);">모두를 위한 화상회의 플랫폼</h4>
                <h6 style="margin-top: 20px; color: rgb(187, 201, 224);">서비스를 시작하려면 로그인을 해주세요.</h6>
                <v-text-field filled color="rgb(187, 201, 224)" background-color="rgb(52, 63, 87)" label="ID" name="login" style="margin-top: 30px;" append-icon="mdi-account" type="text"></v-text-field>
                <v-text-field filled color="rgb(187, 201, 224)" background-color="rgb(52, 63, 87)" id="password" label="Password" name="password" append-icon="mdi-lock" type="password"></v-text-field>
              </v-form>
            <div class="float-left">
              <v-spacer></v-spacer>
                <v-btn class="mr-2" style="padding: 20px; background-color:rgb(244, 67, 72); color: white;" >로그인</v-btn>
                <v-btn class="mr-2" style="padding: 20px; background-color:rgb(39, 53, 82); color: white;">회원가입</v-btn>
                <v-btn class="mr-2" style="padding: 20px; background-color:#1A237E; color: white;" @click="developer">개발자</v-btn>
            </div>
        </v-col>

        <!-- 캐러셀 -->
        <v-spacer></v-spacer>
        <v-col cols="12" md="6" style="padding: 12px 0px;">
          <v-spacer></v-spacer>
          <v-carousel :continuous="true" :cycle="cycle" :show-arrows="false" hide-delimiter-background delimiter-icon="mdi-minus" height="100%">
            <v-carousel-item v-for="(slide, i) in slides" :key="i">
              <v-row class="fill-height float-left" style="margin-left: 15px; " align="center" justify="center">
                <v-icon size=200>{{ icons[i] }}</v-icon>
                <h3 style="margin-bottom: 40px;">{{ slides[i] }} </h3>
              </v-row>
            </v-carousel-item>
          </v-carousel>
        </v-col>
      </v-row>
    </v-container>
  </v-main>
</template>

<script>
  export default {
    name: "Login",
    
    data (){
      {
      return {
        icons: [
          "fas fa-solar-panel fas-4x",
          "far fa-edit far-4x",
          "fas fa-font fa-4x",
        ],
        cycle: true,
        slides: [
          '전 세계 사람들과, 언제 어디서든!',
          '강의를 보면서 필기도 가능합니다!',
          "사용자 친화적인 UI로 누구나 간편하게!",
        ],
      }
      }
    },

    methods:{
      developer(){
        this.$emit("loginConfirm")
      },
    }
  }
</script>

<style>
  .container{
    margin: auto;
    padding: 0 60px;
  }

</style>