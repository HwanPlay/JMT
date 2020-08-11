<template>
  <div>
    <v-row>
      <v-col cols=5></v-col>
      <v-col cols=2>
        <v-btn class="text-center" @click="showForm = !showForm" text small color="error">Password Change</v-btn>
      </v-col>
      <v-col cols=5></v-col>
    </v-row>
    <div v-if="showForm">
      <ProfilePasswordForm />
    </div>
  </div>
</template>

<script>
import ProfilePasswordForm from './ProfilePasswordForm.vue';

export default {
  name: 'ProfilePassword',
  data() {
    return {
      showForm: false,
    };
  },
  components: {
    ProfilePasswordForm,
  }
};
</script>

<style>

</style>