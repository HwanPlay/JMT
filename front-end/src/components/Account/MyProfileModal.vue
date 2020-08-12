<template>
  <v-card class="mx-auto">
    <v-card-title class="title font-weight-regular justify-space-between">
      <span>{{ currentTitle }}</span>
      <v-avatar color="primary lighten-2" class="subheading white--text" size="24" v-text="step"></v-avatar>
    </v-card-title>

    <v-window v-model="step">
      <v-window-item :value="1">
        <v-container>
          <v-row>
            <v-col cols="4">
              asd
            </v-col>
            <v-col cols="8">
              Name: {{this.$store.state.myName}}
              <hr/>
              userId: {{this.$store.state.userId}}
            </v-col>
          </v-row>
        </v-container>        
      </v-window-item>

      <v-window-item :value="2">
        <v-card-text>
          <v-text-field label="Password" type="password"></v-text-field>
          <v-text-field label="Confirm Password" type="password"></v-text-field>
          <span class="caption grey--text text--darken-1">Please enter a password for your account</span>
        </v-card-text>
      </v-window-item>

      <v-window-item :value="3">
        <div class="pa-4 text-center">
          <v-img
            class="mb-4"
            contain
            height="128"
            src="https://cdn.vuetifyjs.com/images/logos/v.svg"
          ></v-img>
          <h3 class="title font-weight-light mb-2">Welcome to Vuetify</h3>
          <span class="caption grey--text">Thanks for signing up!</span>
        </div>
      </v-window-item>
    </v-window>

    <v-divider></v-divider>

    <v-card-actions>
      <v-btn :disabled="step === 1" text @click="step--">Back</v-btn>
      <v-spacer></v-spacer>
      <v-btn :disabled="step === 2" color="primary" depressed @click="step++">Edit</v-btn>
      <v-btn :disabled="step === 1" color="primary" depressed @click="saveProfile">Confirm</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  name: 'MyProfileModal',
  data: () => ({
    step: 1,
  }),
  props: {
    dialog:Boolean,
  },
  watch: {
    dialog() {
      this.step=1;
    }
  },
  methods: {
    saveProfile() {
      console.log('a');
    },
  },
  computed: {
    currentTitle() {
      switch (this.step) {
      case 1:
        return 'Profile';
      case 2:
        return 'Edit Profile';
      default:
        return 'Account created';
      }
    },
  },
};
</script>