<template>
  <div style="height: 100%; width: 100%;">
    <v-row style="margin: 0px; height: 100%; width: 100%;">
      <v-col class="box1" cols="4">
        <v-row style="height:100%;" align="center" justify="center">
        </v-row>
      </v-col>
      <v-col cols="8">
        
      </v-col>
    </v-row>
  </div>

</template>

<script>
export default {
  name: "Home",
};
</script>

<style scoped>
v-col {
  height: 100%;
  padding: 0px;
}
.box1 {
  height: 100%;
  background-color: rgb(52, 63, 87);
  box-shadow: rgba(0, 0, 0, 0.1) 8px 0px 16px;
}
.box2 {
  /* background-color: #0B4F6C; */
  background-color: blueviolet;
  height: 100%;
}
</style>