<template>
  <div class="text-center">
    <v-dialog v-model="dialog" >
      <template v-slot:activator="{ on, attrs }">
        <v-btn class="text-center" text small color="error" v-bind="attrs" v-on="on">Delete Account</v-btn>
      </template>

      <v-card style="width: 500px">
        <v-card-title class="headline grey lighten-2">DELETE ACCOUNT</v-card-title>

        <v-card-text>Input Your Password</v-card-text>
        <v-text-field
            v-model="password"
            label="Password"
            required
          ></v-text-field>
        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="SubmitDeleteAccount">Submit</v-btn>
          <v-btn color="danger" text @click="dialog = false">Cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import axios from 'axios';
import SERVER from '../../api/spring.js';

export default {
  name: 'ProfileDelete',
  data() {
    return {
      dialog: false,
      password: ''
    };
  },
  methods: {
    SubmitDeleteAccount() {
      this.dialog = false;
      axios.delete(SERVER.URL + '/user/delUser/' + this.$store.state.userId).then().catch();
    }
  }
};
</script>

<style scoped>
.btn-box {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>