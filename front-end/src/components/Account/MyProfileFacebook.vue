<template>
  <div class="modal-bg" >
    <h2 class="pt-3 d-flex justify-center">Profile</h2>
    <v-divider></v-divider>
    <div class="프로필 사진">

       <div class="pt-4 d-flex justify-center align-center">
        <v-avatar size="164" tile>
          <img id="profile" :src="'http://joinmeeting.tk/images/'+this.$store.state.myPicture" alt="사진 자리 ㅠㅠ">
        </v-avatar>
      </div>


      <div>
        <input id="file" type="file" ref="file" accept="image/*" v-on:change="fileSelect()">
            <v-btn @click="submitSave" text color="#526387" class="align-self-end">Save</v-btn>
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
import axios from 'axios';
import SERVER from '../../api/spring.js';

import ProfilePasswordModal from '../Profile/ProfilePasswordModal.vue';
import ProfileDelete from '../Profile/ProfileDelete.vue';
// import MyProfileFacebookUpload from './MyProfileFacebookUpload.vue';

export default {
  name: 'MyProfileFacebook',
  components: {
    ProfilePasswordModal,
    ProfileDelete,
    // MyProfileFacebookUpload,
  },
  data() {
    return {
      file:''
    };
  },
  methdos: {
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
              this.makeAlert({'alertColor': 'primary', 'alertMessage': 'Success'});
            } else {
              this.makeAlert({'alertColor': 'danger', 'alertMessage': 'fail to edit'});
            }
          })
          .catch(err=>console.error(err));
      }

    },
  }
};
</script>

<style>
.file-upload label {
    display: inline-block;
    padding: .5em .75em;
    color: #999;
    font-size: inherit;
    line-height: normal;
    vertical-align: middle;
    background-color: #fdfdfd;
    cursor: pointer;
    border: 1px solid #ebebeb;
    border-bottom-color: #e2e2e2;
    border-radius: .25em;
}
.file-upload input[type="file"] {  /* 파일 필드 숨기기 */
    position: absolute;
    width: 1px;
    height: 1px;
    padding: 0;
    margin: -1px;
    overflow: hidden;
    clip:rect(0,0,0,0);
    border: 0;
}

.file-upload input[type="file"] {
    position: absolute;
    width: 1px;
    height: 1px;
    padding: 0;
    margin: -1px;
    overflow: hidden;
    clip:rect(0,0,0,0);
    border: 0;
}

.file-upload label {
    display: inline-block;
    padding: .5em .75em;
    color: #999;
    font-size: inherit;
    line-height: normal;
    vertical-align: middle;
    background-color: #fdfdfd;
    cursor: pointer;
    border: 1px solid #ebebeb;
    border-bottom-color: #e2e2e2;
    border-radius: .25em;
}

/* named upload */
.file-upload .upload-name {
    display: inline-block;
    padding: .5em .75em;  /* label의 패딩값과 일치 */
    font-size: inherit;
    font-family: inherit;
    line-height: normal;
    vertical-align: middle;
    background-color: #f5f5f5;
    border: 1px solid #ebebeb;
    border-bottom-color: #e2e2e2;
    border-radius: .25em;
    -webkit-appearance: none; /* 네이티브 외형 감추기 */
    -moz-appearance: none;
    appearance: none;
}
/* imaged preview */
.file-upload .upload-display {  /* 이미지가 표시될 지역 */
    margin-bottom: 5px;
}

@media(min-width: 768px) { 
    .file-upload .upload-display {
        display: inline-block;
        margin-right: 5px;
        margin-bottom: 0;
    }
}

.file-upload .upload-thumb-wrap {  /* 추가될 이미지를 감싸는 요소 */
    display: inline-block;
    width: 54px;
    padding: 2px;
    vertical-align: middle;
    border: 1px solid #ddd;
    border-radius: 5px;
    background-color: #fff;
}

.file-upload .upload-display img {  /* 추가될 이미지 */
    display: block;
    max-width: 100%;
    width: 100% \9;
    height: auto;
}



#profile {
  width: 200px;
  height: 200px;
  object-fit: cover;
  border-radius: 50%;
}
.modal-bg {
  background-color: rgb(14, 23, 38);
}
div {
  color: white;
}

</style>