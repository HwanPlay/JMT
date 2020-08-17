<template>
  <v-row justify="end">
    <v-btn style="outline:none; " text @click.stop="dialog = true">
      더보기...
    </v-btn>

    <v-dialog v-model="dialog" width="500" height="500">
      <v-card>
        <v-card-title class="headline top d-flex justify-center align-center">그룹원</v-card-title>
          <div v-if="membersInfo && membersInfo.length===0">
            <v-icon color="rgb(52, 63, 87);" class="d-flex justify-center align-center mt-8" size="100">far fa-dizzy</v-icon>
            <h3 class="d-flex justify-center align-center mt-8">그룹원들이 없습니다</h3>
          </div>
          <v-card-text v-for="memberInfo in membersInfo" :key='memberInfo.id' style="padding: 5px;">
            <MemberCard :userInfo=memberInfo :groupNo=groupNo :hostId=hostId @refresh="refresh" />
          </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="rgb(255, 128, 74)" text @click="dialog = false">
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

<style>
  .top {
    background-color: rgb(52, 63, 87);
    color: white;
  }
</style>
