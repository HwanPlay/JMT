<template>
  <v-row>
    <v-col cols="4">
      사진
    </v-col>
    <v-col cols="8">
      data
      <div>
        <div>{{this.$store.state.userId}}</div>
        <div>{{this.$store.state.myName}}</div>
        <div>3</div>
      </div>
    </v-col>
  </v-row>
</template>

<script>
export default {

};
</script>

<style>

</style>