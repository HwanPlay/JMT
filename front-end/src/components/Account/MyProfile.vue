<template>
  <v-dialog Dark v-model="dialog" persistent max-width="650px">
    <template v-slot:activator="{ on, attrs }">
      <v-btn v-bind="attrs" v-on="on" color="rgb(14, 23, 38)" class="mr-0" style="height: 97%; width: 76px;">
        <v-icon size=30>fas fa-user</v-icon>
      </v-btn>
    </template>
    <v-card><!-- 회원가입 폼 양식을 주려면 여기서 -->
      <v-container style="height: 100%;">
        <v-card-title>
          <span class="headline"><h1>My Profile</h1></span>
        </v-card-title>
        <hr class="mt-0">
        <v-row>
          <v-col cols="4" justify="center">
            <v-btn text fab height="125" width="125" class="rounded-circle ml-10" style="outline: none;">
              <!-- <v-badge color="rgb(229, 235, 239)" offset-x="40" offset-y="20" bordered bottom icon="fa-edit"> -->
              <v-img :src="require('../../assets/profile/profile1.jpg')" class="rounded-circle" height="125" width="125"></v-img>
              <!-- </v-badge> -->
            </v-btn>
          </v-col>      
          <v-col cols="8">
            <h2 style="color: rgb(4, 8, 19);">김싸피</h2>
            <h4 style="color: rgb(52, 63, 87);">SSAFY3@ssafy.com</h4>
          </v-col>
        </v-row>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="close">Close</v-btn>
          <!-- <v-btn color="blue darken-1" text @click="close">Edit</v-btn> -->
        </v-card-actions>
      </v-container>
    </v-card>

  </v-dialog>
</template>

<script>
  export default {
    name: 'Signup',
    data() {
      return {
        dialog: false,
      }
    },
    methods: {
      close(){
        this.dialog = false;
        this.resetValidation();
      },
    },
  }
</script>