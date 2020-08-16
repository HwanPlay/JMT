<template>
  <v-row justify="end">
    <v-btn text @click.stop="dialog = true">
      더보기...
    </v-btn>

    <v-dialog v-model="dialog" width="500" height="500">
      <v-card>
        <v-card-title class="headline">Members</v-card-title>
        <v-divider></v-divider>
          <div v-if="membersInfo && membersInfo.length===0">
            아싸..
          </div>
          <v-card-text v-for="memberInfo in membersInfo" :key='memberInfo.id' style="padding: 5px;">
            <MemberCard :userInfo=memberInfo :groupNo=groupNo :hostId=hostId @refresh="refresh" />
          </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" text @click="dialog = false">
            Close
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import MemberCard from './MemberCard.vue';

export default {
  name: 'Signup',
  components:{
    MemberCard,
  },
  props:{
    membersInfo: Array,
    groupNo: Number,
    hostId: String,
  },
  methods: {
    refresh(){
      this.$emit('refresh');
    }
  },
  data () {
    return{
      dialog: false,
    };
  },
};
</script>
