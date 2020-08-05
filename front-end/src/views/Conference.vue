<template>
  <div style="height: 100%; width: 100%;">
    <div class="mainCoference">
      <div class="main_1 main_common">
        <videochat />
      </div>
    </div>
    <div class="main_2 main_common">
      <Sharescreen />
    </div>
  </div>
</template>

<script>
import Videochat from '../components/Conference/Videochat.vue';
import Sharescreen from '../components/Conference/Sharescreen.vue';

export default {
  name: 'Conference',
  components: {
    Videochat,
    Sharescreen
  },
};
</script>

<style>
.mainCoference{
    position: relative; 
    top:0;
    left:0; 
    height: 100%; 
    width: 100%;
}
   
.main_1{
  width: 100%;
  height: 100%;
  float: left;
  background-color: orangered;
  position: relative;
}


</style>