<template>
  <div>
    <div>
      프로필 편집
    </div>
    <div>
      <div>
        <span>프로필 사진</span>
        <button>추가</button>
      </div>
      
      <div>
        <img class="profile-image" :src="'http://joinmeeting.tk/images/'+this.$store.state.myPicture" alt="">

      </div>
    </div>

  </div>
</template>

<script>
export default {

};
</script>

<style>
.profile-image {
   margin-top: 20px;
  display: inline-block;
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 50%;
}

</style>