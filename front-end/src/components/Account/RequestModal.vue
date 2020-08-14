<template>
    <div class="text-center" style="height: 100%">
    <v-dialog v-model="dialog" width="500">
      <template v-slot:activator="{ on, attrs }">
          <v-btn @click='getRequest' text v-bind="attrs" v-on="on" class="mr-2" style="height: 100%; width:100%; outline:none;">
            <v-icon size="30">mdi-bell</v-icon>
          </v-btn>
      </template>

      <v-card>
        <v-container v-for="request in requests" :key="request.requestNo">
          <RequestCard :Request=request />
        </v-container>        

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="danger" text @click="dialog = false">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import SERVER from '../../api/spring.js';
import axios from 'axios';

import RequestCard from './RequestCard.vue';

export default {
  name: 'RequestMessage',
  components: {
    RequestCard
  },

  data : () => ({
    requests : null,
    dialog: false,
  }),

  methods: {
    getRequest() {
      this.dialog = true;
      axios.get(SERVER.URL + '/request/getuser/' + this.$store.state.userId)
        .then(res => {
          this.requests = res.data.requests;
        })
        .catch(err => console.log(err.response));
    }
  },

  // mounted() {
  //   this.getRequest();
  // }


};
  

</script>

<style>

</style>