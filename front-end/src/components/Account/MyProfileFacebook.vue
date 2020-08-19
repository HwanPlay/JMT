<template>
  <div class="modal-bg" >
    <h2 class="py-3 d-flex justify-center profile-title">Profile</h2>
    <div class="프로필 사진">

      <div class="img-holder d-flex justify-center">
        <v-avatar size="164" tile>
          <img id="profile" :src="'http://joinmeeting.tk/images/'+this.$store.state.myPicture" alt="사진 자리 ㅠㅠ">
          <v-icon class="profile-edit" @click="getFileInput" size="30">mdi-camera-outline</v-icon>
          <input hidden style="color:black;" id="file" type="file" ref="file" accept="image/*" v-on:change="fileSelect()">
        </v-avatar>
      </div>


      <div class=" d-flex justify-end">

        <div v-if="file">
          <label for="input-file">Image:</label>
          <span id="input-file">{{file.name}}</span>       
        </div>
        <!-- <div class="btn btn-primary" @click="getFileInput" >Image Upload</div> -->
      </div>

      <v-divider></v-divider>
      <div class="px-2">
        <div>
          <i class="mdi mdi-email"></i> 
          Email: {{this.$store.state.userId}}
        </div>
        <div>
          <i class="mdi mdi-account-circle"></i>
          UserName: <input class="inline" type="text"  v-model="user_name">
          <!-- <v-form ref="form">
            <v-text-field v-model="user_name" :counter="max" :rules="rules" label="User Name"></v-text-field>
          </v-form> -->
        </div>
      </div>
      <v-btn @click="submitSave" text color="#526387" class="align-self-end">Save</v-btn>

      <v-divider></v-divider>
      <div class="d-flex justify-space-around">
        <ProfileDelete />
        <ProfilePasswordModal />
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import SERVER from '../../api/spring.js';

import ProfilePasswordModal from '../Profile/ProfilePasswordModal.vue';
import ProfileDelete from '../Profile/ProfileDelete.vue';

export default {
  name: 'MyProfileFacebook',
  components: {
    ProfilePasswordModal,
    ProfileDelete,
  },
  data() {
    return {
      file:'',
      user_name: this.$store.state.myName,
    };
  },
  props: {
    profileFlag: Boolean,
  },
  watch: {
    profileFlag: function() {
      console.log(this.profileFlag);
      this.file = '';
    }
  },
  methods: {
    getFileInput(){
      document.querySelector('#file').click();
    },
    fileSelect() {
      console.log(this.$refs);
      this.file = this.$refs.file.files[0];
      console.log(this.file);
    },
    submitSave() {
      let formData = new FormData();

      formData.append('name', this.user_name);
      formData.append('multipartFile', this.file);
      
      // 파일크기 3M BYTE
      if (this.file.size > 3e6) {
        this.makeAlert({'alertColor': 'danger', 'alertMessage': 'Please choose less than 3MB'});
      } else {
        // file upload 가능
        axios
          .post(SERVER.URL + '/user/modify',formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          })
          .then((res) => {
            console.log(res);
            if(res.status === 200){
              this.$store.commit('SET_MY_PROFILE', res);
              // this.makeAlert({'alertColor': 'primary', 'alertMessage': 'Success'});
            } else {
              // this.makeAlert({'alertColor': 'danger', 'alertMessage': 'fail to edit'});
            }
          })
          .catch(err=>console.error(err));
      }

    },
  }
};
</script>

<style scoped>
#profile {
  width: 164px;
  height: 164px;
  object-fit: cover;
  border-radius: 50%;
}
.img-holder{
  position: relative;
}
.img-holder .profile-edit{
  position: absolute;
  bottom: -40%; /*your button position*/
  right: -40%; /*your button position*/
}


.profile-title {
  background-color: rgb(14, 23, 38);
  color: white;
}
/* .profile-edit{
  position: relative;
  float: left;
  right: 0;
  bottom: 0;  
} */
</style>