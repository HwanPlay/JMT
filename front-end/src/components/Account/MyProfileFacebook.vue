<template>
  <div class="modal-bg" >
    <h2 class="py-3 d-flex justify-center profile-title">Profile</h2>
    <div class="프로필 사진">

      <div class="img-holder d-flex justify-center">
        <v-avatar id="preview" size="164" tile>
          <img id="profile" :src="'http://joinmeeting.tk/images/'+this.$store.state.myPicture" alt="사진 자리 ㅠㅠ">
          
          <v-icon class="profile-edit" @click="getFileInput" size="30">mdi-camera-outline</v-icon>
          <input hidden style="color:black;" id="upload" type="file" ref="file" accept="image/*" v-on:change="fileSelect()">
        </v-avatar>
      </div>
      
      <!-- <input type='file' id='upload' name='upload'/> -->
      <!-- <div v-if="imgFlag" id='preview' size="164" tile> -->
        <!-- 미리보기 공간 -->
      <!-- </div> -->

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
      imgFlag: false,
      file:'',
      user_name: this.$store.state.myName,
    };
  },
  props: {
    profileFlag: Boolean,
  },
  watch: {
    profileFlag: function() {
      this.file = '';
      this.imgFlag = false;
    }
  },
  methods: {
    getFileInput(){
      document.querySelector('#upload').click(); 
      this.imgFlag = true;
    },
    fileSelect() {
      console.log(this.$refs);
      this.file = this.$refs.file.files[0];
      console.log(typeof this.file);
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
  },
  mounted() {    
    var upload = document.querySelector('#upload');
    upload.addEventListener('change',function (e) {
      var preview = document.querySelector('#preview');
      var get_file = e.target.files;
      
      var oldImage = document.querySelector('#Img');
      if (oldImage) {
        oldImage.remove();
      }


      var image = document.createElement('img');
      image.id = 'Img';
      image.style.height = '100px';
      image.style.width = '100px';
      /* FileReader 객체 생성 */
      var reader = new FileReader();

      /* reader 시작시 함수 구현 */
      reader.onload = (function (aImg) {
        console.log(1);

        return function (e) {
          console.log(3);
          /* base64 인코딩 된 스트링 데이터 */
          aImg.src = e.target.result;
        };
      })(image);

      if(get_file){
        /* 
            get_file[0] 을 읽어서 read 행위가 종료되면 loadend 이벤트가 트리거 되고 
            onload 에 설정했던 return 으로 넘어간다.
            이와 함게 base64 인코딩 된 스트링 데이터가 result 속성에 담겨진다.
        */
        reader.readAsDataURL(get_file[0]);
        console.log(2);
      }
      
      

      preview.appendChild(image);
      // image.id = 'oldImage';

      // preview.replaceChild()
    });
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