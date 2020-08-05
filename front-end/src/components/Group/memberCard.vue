<template>
  <div>
    <v-list-item>
      <v-list-item-avatar :src="require('../../assets/profile/profile1.jpg')" color="grey" size="55"> <v-img :src="require('../../assets/profile/profile1.jpg')"></v-img></v-list-item-avatar>
      <v-list-item-content>
        <v-list-item-title class="headline">{{ userInfo.nickname }}</v-list-item-title>
        <v-list-item-subtitle>{{ userInfo.id }}</v-list-item-subtitle>
      </v-list-item-content>
    </v-list-item>
  </div>
</template>

<script>
export default {
  name: 'memberCard',
  props:{
    userInfo: Object
  },
};
</script>

<style>

</style>