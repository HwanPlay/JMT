<template>
  
  <div class="profile-mage" style="text-align:center;">
    <div >
      <img style="width: 100px;  height: 100px;" id="myImage" :src="'http://joinmeeting.tk/images/'+this.$store.state.myPicture" />
    </div>
    <v-list-item-content class="item-content">
      <v-list-item-title class="myName">
        <h5 style="color : white;">{{ $store.state.myName }}</h5>
      </v-list-item-title>
      <v-list-item-subtitle>
        <div id="userIdBoxNote">
          <p style="color:white;">{{ $store.state.userId }}</p>
        </div>
      </v-list-item-subtitle>
    </v-list-item-content>
  </div>
</template>

<script>
export default {
  name: 'EditorGroupListProfile',
};
</script>

<style>
#myImage {
  margin-top: 20px;
  display: inline-block;
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 50%;
}
</style>