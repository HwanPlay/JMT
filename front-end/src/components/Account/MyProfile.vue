<template>
  <v-dialog Dark v-model="dialog" persistent max-width="650px">
    <template v-slot:activator="{ on, attrs }">
      <v-btn v-bind="attrs" v-on="on" color="rgb(14, 23, 38)" class="mr-0" style="height: 100%; width: 76px; outline: none;">
        <v-icon size=30>fas fa-user</v-icon>
      </v-btn>
    </template>

  <v-card class="mx-auto" style="width: 100%; height: 100%;">
      <v-card-title class="title font-weight-regular justify-space-between">
        <span>{{ currentTitle }}</span>
      </v-card-title>

      <v-window v-model="step">
        <v-window-item :value="1">
          <v-row>
          <v-col cols="4" justify="center">
            <v-btn text fab height="125" width="125" class="rounded-circle ml-10" style="outline: none;">
              <v-img :src="require('../../assets/profile/profile1.jpg')" class="rounded-circle" height="125" width="125"></v-img>
            </v-btn>
          </v-col>
          <v-col cols="8" style="margin-top: 20px; padding-right: 20px;">
            <h2 style="color: rgb(4, 8, 19);">{{ $store.state.myName }}</h2>
            <h4 style="color: rgb(52, 63, 87);">{{ $store.state.userId }}</h4>
          </v-col>
        </v-row>
        </v-window-item>

        <v-window-item :value="2">
          <v-card-text>
            <v-text-field label="Password" type="password"></v-text-field>
            <v-text-field label="Confirm Password" type="password"></v-text-field>
            <span class="caption grey--text text--darken-1">
              Please enter a password for your account
            </span>
          </v-card-text>
        </v-window-item>

        <v-window-item :value="3">
          <div class="pa-4 text-center">
            <v-img class="mb-4" contain height="128" src="https://cdn.vuetifyjs.com/images/logos/v.svg"></v-img>
            <h3 class="title font-weight-light mb-2">Welcome to Vuetify</h3>
            <span class="caption grey--text">Thanks for signing up!</span>
          </div>
        </v-window-item>
      </v-window>

      <v-divider></v-divider>

      <card-actions>
        <v-spacer></v-spacer>
        <v-btn :disabled="step === 3" color="primary" depressed @click="step++">
          Next
        </v-btn>
        <v-btn color="blue darken-1" style="outline: none;" text @click="close">Close</v-btn>
      </card-actions>
    </v-card>


  </v-dialog>
</template>

<script>
export default {
  name: 'Signup',
  data () {
    return {
      dialog: false,
      step: 1,
    };
  },
  methods: {
    close () {
      this.dialog = false;
      this.step = 1;
    }
  },
  computed: {
    currentTitle(){
      switch(this.step){
      case 1: return 'My Profile';
      case 2: return 'Edit Profile';
      default : return '수정 완료!';
      }
    }
  }
};
</script>
