<template>
  <v-row class="d-flex justify-center">
    <v-col cols="12" lg="3" >
      <v-img class="img-center" height="200" width="200" src="../assets/JMT_logo.jpg"></v-img>
    </v-col>
    <v-col cols="12" lg="9" class="col-center">
      <span class="title">Join MeeTing & Typing</span>
    </v-col>      
  </v-row>
</template>

<script>
export default {

}
</script>

<style>
.img-center {
  display: block;
  margin-left: auto;
  margin-right: auto;
}
.col-center {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  font-size: xx-large;
}
.title {
  font-size: 5rem;
}
</style>