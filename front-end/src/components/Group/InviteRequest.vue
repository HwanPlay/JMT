<template>
    <div class="text-center" style="height: 100%">
    <v-dialog v-model="dialog" width="500">
      <template v-slot:activator="{ on, attrs }">
          <v-btn text v-bind="attrs" v-on="on" class="mr-2" style="height: 100%; width:100%; outline:none;">
            <v-icon size="30">mdi-bell</v-icon>
          </v-btn>
      </template>

      <v-card>
        <v-container>
            
          <v-row v-for="(item, i) in requests" :key="i">
            <v-list-item-content>
              {{item}}
            </v-list-item-content>
            <v-divider></v-divider>
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
import SERVER from '../../api/spring.js';
import axios from 'axios';

export default {
  name: 'InviteRequest',
  components: {
  },

  data : () => ({
    requests : null,
  }),

  methods: {
    getRequest() {
      axios.get(SERVER.URL + '/getuser/' + this.$store.state.userId)
        .then(res => {
          this.requests = res.data.requests;
        })
        .catch(err => console.log(err.response));
    }
  }


};
  

</script>

<style>

</style>