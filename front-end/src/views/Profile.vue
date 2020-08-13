<template>
  <v-container style="width: 500px">
    <div>
      <h4>프로필 수정</h4>
      <h6>프로필 사진과 별명을 수정 하실 수 있습니다.</h6>
    </div>
    <div style="height: 2rem;"></div>
    <ProfileBasicCard />
    <div style="height: 2rem;"></div>
    <v-row>
      <ProfileDelete />
      <ProfilePasswordModal />
    </v-row>
  </v-container>
</template>

<script>
import ProfileBasicCard from '../components/Profile/ProfileBasicCard.vue';
import ProfilePasswordModal from '../components/Profile/ProfilePasswordModal.vue';
import ProfileDelete from '../components/Profile/ProfileDelete.vue';
export default {
  name: 'Profile',
  components: {
    ProfileBasicCard,
    ProfilePasswordModal,
    ProfileDelete,
  }
};
</script>

<style>

</style>