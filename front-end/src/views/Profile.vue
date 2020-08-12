<template>
  <v-container>
    <ProfileBasic />
    <div style="height: 2rem;"></div>
    <ProfilePassword />
  </v-container>
</template>

<script>
import ProfileBasic from '../components/Profile/ProfileBasic.vue';
import ProfilePassword from '../components/Profile/ProfilePassword.vue';

export default {
  name: 'Profile',
  components: {
    ProfileBasic,
    ProfilePassword
  }
};
</script>

<style>

</style>