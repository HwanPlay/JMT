<template>
  <div>
    <input
      type="file"
      accept="image/jpeg"
      @change="uploadImage"
    />
    <button @click="clear">aaaa</button>
    <div class="image-container">
      <img :src="this.previewImage" width="200" />
    </div>
  </div>
</template>


<script>
const url = '/home/jenkins/workspace/joinmeeting/backend/resources/upload/profile/img/200812_8a929eb81e.PNG';
const url_image = '200812_8a929eb81e.PNG';



export default {
  name: 'ProfileBasicImage',
  data() {
    return {
      previewImage: null,
    };
  },
  methods: {
    clear() {
      this.previewImage = null;
    },
    uploadImage(e) {
      const image = e.target.files[0];
      const reader = new FileReader();
      reader.readAsDataURL(image);
      reader.onload = (e) => {
        this.previewImage = e.target.result;
        console.log(this.previewImage);
      };
    },
  },
}; // missing closure added
</script>

<style>
.uploading-image {
  display: flex;
}
.image-container {
  width: 300px;
  height: 300px;
}
</style>