<template>
    <div class="text-center" style="height: 100%">
    <v-dialog v-model="dialog" width="500">
      <template v-slot:activator="{ on, attrs }">
          <v-btn text v-bind="attrs" v-on="on" class="mr-2" style="height: 100%; width:100%; outline:none;">
            <v-badge color="rgb(255, 128, 74)" content="this.$store.state.requests.length" v-if="haveRequests">
              <v-icon size="30">mdi-bell</v-icon>
            </v-badge>
            <v-icon v-else size="30">mdi-bell</v-icon>
          </v-btn>
      </template>

      <v-card>
        <v-card-title class="top justify-center align-content-center">초대 메세지</v-card-title>
        <div v-if="haveRequests">
          <v-container v-for="request in this.$state.store.requests" :key="request.requestNo">
            <RequestCard :Request=request />
          </v-container>        
        </div>
        <div v-else class="justify-center align-content-center d-flex mt-5">
          <v-col cols="2" class="pr-0 ml-5">
            <v-icon size="50">far fa-envelope-open</v-icon>
          </v-col>
          <v-col cols="10" class="pl-0 align-self-center mt-2">
            다른 그룹으로 부터 온 초대 메세지가 없습니다!
          </v-col>
        </div>


        <!-- <v-divider></v-divider> -->

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="danger" text @click="dialog = false" style="outline: none;">Close</v-btn>
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
    dialog: false,
  }),

  computed: {
    haveRequests() {
      return this.$store.getters.haveRequests;
    }
  }
};
  

</script>

<style>
  .top {
    /* background-color: rgb(52, 63, 87); */
    /* color: white; */
    background-color: white;
    color: rgb(52, 63, 87);
  }
</style>