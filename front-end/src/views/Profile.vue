<template>
  <v-container style="width: 500px">
    <ProfileBasicCard />
    <div style="height: 2rem;"></div>
    <ProfilePassword />
    <ProfileDelete />
  </v-container>
</template>

<script>
import ProfileBasicCard from '../components/Profile/ProfileBasicCard.vue';
import ProfilePassword from '../components/Profile/ProfilePassword.vue';
import ProfileDelete from '../components/Profile/ProfileDelete.vue';
export default {
  name: 'Profile',
  components: {
    ProfileBasicCard,
    ProfilePassword,
    ProfileDelete,
  }
};
</script>

<style>

</style>