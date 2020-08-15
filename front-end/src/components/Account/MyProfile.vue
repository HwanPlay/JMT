<template>
  <div class="text-center" style="height: 100%">
    <v-dialog v-model="dialog" width="500">
      <template v-slot:activator="{ on, attrs }">
          <v-btn text v-bind="attrs" v-on="on" class="mr-2" style="height: 100%; width:100%; outline:none;">
            <v-icon size="30">mdi-account</v-icon>
          </v-btn>
      </template>

      <v-card>
        <v-container>
          <v-row>
            <v-col cols="4">
              <v-avatar class="profile"  size="164" tile>
                <img :src="picture_URL" alt="사진 자리 ㅠㅠ">
              </v-avatar>

              <!-- <v-img src="http://joinmeeting.tk/images/200814_2f4510ea76.jpg" aspect-ratio="1" alt="사진 자리 ㅠㅠ"></v-img>
              <img src="http://joinmeeting.tk/images/200814_2f4510ea76.jpg" aspect-ratio="1" alt="사진 자리 ㅠㅠ"> -->

            </v-col>
            <v-col cols="8">
              Name: {{this.$store.state.myName}}
              <hr/>
              userId: {{this.$store.state.userId}}
            </v-col>
          </v-row>
        </v-container>        

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <router-link to="/Profile">
            <v-btn color="primary" @click="dialog = false" text >
              Edit
            </v-btn>  
          </router-link>
          <v-btn color="danger" text @click="dialog = false">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
export default {
  name: 'MyProfile',
  data() {
    return {
      dialog: false,
      picture_URL: 'http://joinmeeting.tk/images/' + this.$store.state.myPicture,
    };
  },
  computed: {
  },
  methods: {
  }
};
</script>