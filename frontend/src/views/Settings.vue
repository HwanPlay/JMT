<template>
  <v-container fluid style="height: 100%; padding: 0px; margin: 0px;">
    <v-row style="height: 100%">
      <v-col class="box1" cols="5">
      </v-col>
      <v-col class="box2" cols="8" v-bind:style="{ backgroundImage: 'url(' + backImageUrl +')'}">
        <div>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {

}
</script>

<style>
  v-col {
    height: 100%;
    padding: 0px;
  }
  .box1 {
    height: 100%;
    background-color: rgb(52, 63, 87);
    box-shadow: rgba(0, 0, 0, 0.1) 8px 0px 16px;

  }
  v-tab {
    height: 100%;
    background-color: rgb(19, 28, 46);
    box-shadow: rgba(0, 0, 0, 0.1) 8px 0px 16px;
  }
  .box2 {
    background: rgb(32, 43, 67);
    color: rgb(229, 235, 239);
    height: 100%;  
  }
</style>