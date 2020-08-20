<template>
  <div class="justify-center align-content-center">
    <v-card class="d-flex justify-center align-content-end flex-wrap mt-5" flat tile min-height="450">
      <div id="imgContainer" class="text-center rounded-circle d-inline-flex align-center justify-center ma-3">
        <v-img src="../../assets/JMT/JMTLogo.png" max-width="200px"></v-img>
      </div>
    </v-card>
    <v-card class="mt-10 d-flex justify-center align-content-center flex-wrap" flat tile min-height="50">
      <h3>JMT는 <strong>그룹 기반</strong> 화상 회의 서비스 입니다!</h3>
    </v-card>
    <v-card class="d-flex justify-center align-content-center flex-wrap" flat tile min-height="100">
      <h5>서비스를 이용하기 위해서는 그룹을 생성하시거나, 그룹 가입을 해주세요</h5>
    </v-card>

  </div>
</template>

<script>
export default {
  name: 'EmptyGroup'
};
</script>

<style>
  #imgContainer{
    background-color: rgb(187, 201, 224);
    height: 300px;
    width: 300px;
  }
  #octagon {
      width: 100px;
      height: 100px;
      background: rgb(187, 201, 224);
      position: relative;
  }
  #octagon:before {
    content: "";
    width: 100px;
    height: 0;
    position: absolute;
    top: 0;
    left: 0;
    border-bottom: 29px solid rgb(187, 201, 224);
    border-left: 29px solid white;
    border-right: 29px solid white;
  }
  #octagon:after {
    content: "";
    width: 100px;
    height: 0;
    position: absolute;
    bottom: 0;
    left: 0;
    border-top: 29px solid rgb(187, 201, 224);
    border-left: 29px solid white;
    border-right: 29px solid white;
  }  
</style>