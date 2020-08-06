<template>
  <v-dialog Dark v-model="dialog" persistent max-width="500px">
    <template v-slot:activator="{ on, attrs }">
      <v-btn v-if="(groupInfo.hostId === this.$store.state.userId) && !groupInfo.hasMeeting"
             color="danger" v-bind="attrs" v-on="on" class="mr-2" style="width: 92.88px; padding: 20px; color: white; outline:none;">
        관리
      </v-btn>
      <!-- <v-btn v-if="(groupInfo.hostId === this.$store.state.userId) && !groupInfo.hasMeeting"
             class="mr-2" style="outline: none;" text @click.stop="dialog = true">
        관리
      </v-btn> -->
    </template>
    <v-card>
      <v-container style="height: 100%;">
        <v-card-title>
          <span class="headline"><h1>그룹 관리</h1></span>
          <div class="ml-5 mt-5" style="height: 99%; width: 35%">
            <v-spacer></v-spacer>
            <v-toolbar dense style="width: 90%">
              <v-text-field append-icon="fas fa-search" hint="그룹원 검색"></v-text-field>
            </v-toolbar>
          </div>
        </v-card-title>
        <hr class="mt-0">
        <v-row>
          멤버결과창
        </v-row>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" style="outline: none;" text @click="close">Close</v-btn>
        </v-card-actions>
      </v-container>
    </v-card>

  </v-dialog>
</template>

<script>
export default {
  name: 'Signup',
  data () {
    return {
      dialog: false
    };
  },
  methods: {
    close () {
      this.dialog = false;
    }
  }
};
</script>