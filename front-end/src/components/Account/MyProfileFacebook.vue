<template>
  <div class="modal-bg" >
    <v-card-title class="py-3 top">
      내 정보
      <v-spacer></v-spacer>
      <!-- <v-btn @click="submitSave" text rounded outlined style="color: white" color="#526387">Save</v-btn> -->
      <template>
        <div class="text-center ma-2">
          <v-btn text rounded outlined style="color: white" color="#526387" @click="submitSave">Save</v-btn>
          <v-snackbar
            v-model="snackbar"
          >
            {{ text }}

            <template v-slot:action="{ attrs }">
              <v-btn
                color="pink"
                text
                v-bind="attrs"
                @click="snackbar = false"
              >
                Close
              </v-btn>
            </template>
          </v-snackbar>
        </div>
      </template>
    </v-card-title>
    <div class="프로필 사진">

      <div class="img-holder d-flex justify-center">
        <v-avatar id='preview' size="164" tile>
          <img id="profileImage" :src="initImageUrl" alt="사진 자리 ㅠㅠ">
        
          <v-icon  class="profile-edit" @click="getFileInput" size="30">mdi-camera-outline</v-icon>
          <input hidden style="color:black;" id="upload" type="file" ref="file" accept="image/*" v-on:change="fileSelect()">
        </v-avatar>
      </div>

      <v-divider></v-divider>
      <div class="px-2">
        <div>
          <i class="mdi mdi-email"></i> 
          Email: {{this.$store.state.userId}}
        </div>
        <v-row>
          <v-col cols="5" class="pr-0 d-flex align-center">
            <i class="mdi mdi-account-circle"></i>UserName: 
          </v-col>
          <v-col cols="7" class="pl-0">
            <!-- <input class="inline" type="text"  v-model="user_name"> -->
            <div ref="form">
              <v-text-field
                v-model="user_name"
              ></v-text-field>
            </div> 
          </v-col>
        </v-row>
      </div>
      <!-- <div class="d-flex justify-end text-center">
        <v-btn @click="submitSave" text rounded outlined style="color: rgb(14, 23, 38)" color="#526387">Save</v-btn>
        <v-btn rounded outlined color="red" v-bind="attrs" v-on="on" dark>회원 탈퇴</v-btn>
      </div> -->

      <v-divider class="my-1"></v-divider>
      <div class="d-flex justify-space-around mt-2 pb-2">
        <!-- <ProfileDelete /> -->
        <ProfilePasswordModal @onSubmitSuccess="submitSuccess" />
      </div>
    </div>
  </div>
</template>


<script>
import axios from 'axios';
import SERVER from '../../api/spring.js';

import ProfilePasswordModal from '../Profile/ProfilePasswordModal.vue';
// import ProfileDelete from '../Profile/ProfileDelete.vue';

export default {
  name: 'MyProfileFacebook',
  components: {
    ProfilePasswordModal,
    // ProfileDelete,
  },
  data() {
    return {
      snackbar: false,
      text: 'We saved your profile!',

      imgFlag: false,
      isFileSelected: false,
      file: null,
      user_name: this.$store.state.myName,
      initImageUrl: 'http://joinmeeting.tk/images/'+this.$store.state.myPicture,
    };
  },
  props: {
    profileFlag: Boolean,
  },
  watch: {
    profileFlag: function() {
      // modal 클릭 할 때.
      this.file = null;
      this.initImageUrl = 'http://joinmeeting.tk/images/'+this.$store.state.myPicture;
      var image = document.querySelector('#profileImage');
      image.src = 'http://joinmeeting.tk/images/'+this.$store.state.myPicture;
    }
  },
  methods: {
    submitSuccess() {
      this.snackbar = true;
      this.text= 'We saved your password!';
    },
    getFileInput(){
      document.querySelector('#upload').click();
    },
    fileSelect() {
      if (event.target.files.length) { 
        this.file = this.$refs.file.files[0];
        
        var oldImage = document.querySelector('#profileImage');
        
        if (oldImage) {
          var get_file =  event.target.files;

          var preview = document.querySelector('#preview');

          oldImage.remove();
          var newImage = document.createElement('img');

          // newImage.id = 'profileImage';
          newImage.setAttribute('id', 'profileImage');
          newImage.setAttribute('style', 'width: 164px; height: 164px; object-fit: cover; border-radius: 50%;');
          /* FileReader 객체 생성 */
          var reader = new FileReader();

          /* reader 시작시 함수 구현 */
          reader.onload = (function (aImg) {

            return function (event) {
              /* base64 인코딩 된 스트링 데이터 */
              aImg.src = event.target.result;
            };
          })(newImage);

          if(get_file){
            /* 
                get_file[0] 을 읽어서 read 행위가 종료되면 loadend 이벤트가 트리거 되고 
                onload 에 설정했던 return 으로 넘어간다.
                이와 함게 base64 인코딩 된 스트링 데이터가 result 속성에 담겨진다.
            */
            reader.readAsDataURL(get_file[0]);
          }
          preview.appendChild(newImage);
        }
      }
    },
    submitSave() {
      this.snackbar = true;
      this.text= 'We saved your profile!';
      let formData = new FormData();
      
      formData.append('name', this.user_name);
      if (this.file !== null){
        formData.append('multipartFile', this.file);
      }
      
      // console.log(...formData);
      // formData.forEach(ele=> {
      //   console.log(ele[key]);
      // });


      // 파일크기 3M BYTE
      if (this.file && this.file.size > 3e6) {
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
  },
};
</script>

<style scoped>
#profileImage {
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
  height: 30px !important;
  width: 30px !important;
  bottom: 0%; /*your button position*/
  right: 0%; /*your button position*/
}

.top {
  background-color: rgb(52, 63, 87);
  color: white;
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