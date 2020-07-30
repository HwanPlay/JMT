<template>
  <v-main fluid style="with: 100%; height: 100%; padding: 0px; margin-left: 20px;">
    <v-row align="left" justify="left">

      <!-- 로그인 -->
      <v-col cols="12" sm="8" md="5" style= "margin-bottom: 100px; margin-left: 30px;">
        <v-img :src="require('../../assets/profile/profile1.jpg')" class="rounded-circle"></v-img>
        <!-- <div class="float-left">
          <v-spacer></v-spacer>
            <v-btn class="mr-2" style="padding: 20px; background-color:rgb(244, 67, 72); color: white;" >로그인</v-btn>
            <Signup>회원가입</Signup>
        </div>
        <div style="margin-top: 80px;">
          <div style="font-size: 13px; line-height: 18px; color:rgb(121, 134, 163);">
            계정 / 비밀번호가 기억이 안나신다면 
            <span style="text-decoration: underline; cursor: pointer;">이곳</span>을 클릭해주세요
          </div> -->
        <!-- </div> -->
      </v-col>
    </v-row>
  </v-main>
</template>

<script>

export default {
  name: "MyProfile",

}
</script>

<style>

</style>