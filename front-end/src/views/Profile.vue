<template>
  <v-container style="width: 500px">
    <ProfileBasicCard />
    <div style="height: 2rem;"></div>
    <ProfilePassword />
  </v-container>
</template>

<script>
import ProfileBasicCard from '../components/Profile/ProfileBasicCard.vue';
import ProfilePassword from '../components/Profile/ProfilePassword.vue';

export default {
  name: 'Profile',
  components: {
    ProfileBasicCard,
    ProfilePassword
  }
};
</script>

<style>

</style>