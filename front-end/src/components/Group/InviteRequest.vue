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
            
          <v-row>
            <v-col cols="12">
                
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
  name: 'InviteRequest',
  components: {
  
  },

  props : {
    recvList : Object
  },


};
  

</script>

<style>

</style>