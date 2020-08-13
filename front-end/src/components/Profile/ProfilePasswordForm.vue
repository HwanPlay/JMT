<template>
  <v-form>  
    <v-container fluid>
      <v-row>
        <v-col cols="12">
          <v-text-field
            v-model="existingPassword"
            :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
            :rules="[rules.required, rules.min]"
            :type="show1 ? 'text' : 'password'"
            name="input-10-1"
            label="Existing Password"
            hint="At least 8 characters"
            @click:append="show1 = !show1"
          ></v-text-field>
        </v-col>

        <v-col cols="12">
          <v-text-field
            v-model="newPassword1"
            :append-icon="show2 ? 'mdi-eye' : 'mdi-eye-off'"
            :rules="[rules.required, rules.min, rules.passwordNoteSame]"
            :type="show2 ? 'text' : 'password'"
            name="input-10-2"
            label="New Password"
            hint="At least 8 characters"
            value="wqfasds"
            class="input-group--focused"
            @click:append="show2 = !show2"
          ></v-text-field>
        </v-col>

        <v-col cols="12">
          <v-text-field
            v-model="newPassword2"
            :append-icon="show3 ? 'mdi-eye' : 'mdi-eye-off'"
            :rules="[rules.required, rules.min, rules.passwordMatch]"
            :type="show3 ? 'text' : 'password'"
            name="input-10-2"
            label="Check New password"
            hint="At least 8 characters"
            value="wqfasds"
            class="input-group--focused"
            @click:append="show3 = !show3"
          ></v-text-field>
        </v-col>
        
        <v-col cols="12" class="d-flex flex-row-reverse">
          <v-btn color="red" text @click="closePassword">Cancel</v-btn>
          <v-btn @click="SubmitPassword" text color="primary">Submit</v-btn>
        </v-col>
      </v-row>
    </v-container>    
  </v-form>
</template>

<script>
import axios from 'axios';
import SERVER from '../../api/spring.js';

export default {
  data() {
    return {
      
      show1: false,
      show2: false,
      show3: false,

      existingPassword: '',
      newPassword1: '',
      newPassword2: '',

      rules: {
        required: (value) => !!value || 'Required.',
        min: (v) => v.length >= 8 || 'Min 8 characters',        
        passwordNoteSame: () => (this.existingPassword !== this.newPassword1) || 'Type diffierent password',
        passwordMatch: () => (this.newPassword1 === this.newPassword2) || 'Check your new password, Password is not same'
      },
    };
  },
  methods: {
    closePassword() {
      this.$emit('onClosePassword');
    },
    SubmitPassword() {
      if (this.existingPassword !== this.newPassword1 && this.newPassword1===this.newPassword2){
        axios.post(SERVER.URL + '/user/modifyPw',{
          'oldPw': this.existingPassword,
          'newPw': this.newPassword1
        }).then((res)=>{
          if (res.data === 'success'){
            this.$emit('onSubmitSuccess');
          } else {
            this.$emit('onSubmitWrongPassword');
          }
        }).catch(
          err=>console.error(err)
        );
      } else {
        this.$emit('onSubmitDifferentPassword');
      }
      this.clearPassword();
    },
    clearPassword() {
      this.existingPassword = '';
      this.newPassword1 = '';
      this.newPassword2 = '';
    },

    
  }
};
</script>