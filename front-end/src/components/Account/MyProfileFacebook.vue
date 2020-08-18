<template>
  <div class="modal-bg" >
    <h2 class="pt-3 d-flex justify-center">Profile</h2>
    <v-divider></v-divider>
    <div class="프로필 사진">
      <div style="height:90px;" class="d-flex justify-space-between px-5">
        <div 
        style="
        text-align: center;
        vertical-align: middle;
        line-height:90px;
        ">프로필사진</div>
        <div >
          <MyProfileFacebookUpload />
        </div>
      </div>
      <div class="pt-4 d-flex justify-center align-center">
        <v-avatar size="164" tile>
          <img id="profile" :src="'http://joinmeeting.tk/images/'+this.$store.state.myPicture" alt="사진 자리 ㅠㅠ">
        </v-avatar>
      </div>
      <v-divider></v-divider>
      <div class="px-2">
        <div>
          <i class="mdi mdi-email"></i> 
          Email: {{this.$store.state.userId}}
        </div>
        <div>
          <i class="mdi mdi-account-circle"></i>
          UserName: {{this.$store.state.myName}}
        </div>
      </div>
      <v-divider></v-divider>
      <div class="d-flex justify-space-around">
        <ProfileDelete />
        <ProfilePasswordModal />
      </div>
    </div>
  </div>
</template>

<script>
import ProfilePasswordModal from '../Profile/ProfilePasswordModal.vue';
import ProfileDelete from '../Profile/ProfileDelete.vue';
import MyProfileFacebookUpload from './MyProfileFacebookUpload.vue';

export default {
  name: 'MyProfileFacebook',
  components: {
    ProfilePasswordModal,
    ProfileDelete,
    MyProfileFacebookUpload,
  }
};
</script>

<style>
#profile {
  width: 200px;
  height: 200px;
  object-fit: cover;
  border-radius: 50%;
}
.modal-bg {
  background-color: brown;
}
</style>