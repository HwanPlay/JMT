<template>
  <v-row justify="end">
    <v-btn class="mr-2" style="outline: none;" text @click.stop="dialog = true">
      더보기...
    </v-btn>

    <v-dialog v-model="dialog" width="500" height="500">
      <v-card>
        <v-card-title class="headline">Members</v-card-title>
        <v-divider></v-divider>
          <div v-if="membersInfo.length===0">
            아싸..
          </div>
          <v-card-text v-for="(memberInfo, i) in membersInfo" :key=i style="padding: 5px;">
            <memberCard :userInfo = memberInfo />
          </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" text @click="dialog = false">
            Disagree
          </v-btn>
          <v-btn color="green darken-1" text @click="dialog = false">
            Agree
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import memberCard from './memberCard.vue';

export default {
  name: 'Signup',
  components:{
    memberCard,
  },
  props:{
    membersInfo: Array,
  },
  data () {
    return{
      dialog: false,
    };
  }
};
</script>
