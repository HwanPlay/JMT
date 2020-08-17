<template>
  <!-- <v-col cols="3"> -->
  
  <div style="width:256px; background-color: rgb(52, 63, 87); margin-left:15px;">
    <EditorGroupListProfile />
    <div style="text-align:center;">
    <v-btn  small  color="primary" id="goToGroupList" @click="goToGroupList">그룹목록보기</v-btn>
    </div>
    <v-divider></v-divider>

    <EditorDetailSideBarNoteList   :noteList="noteList" @onGetNoteHTML="getNoteHTML" />
  </div>

  <!-- </v-col> -->
</template>

<script>
import EditorGroupListProfile from './EditorGroupListProfile';
import EditorDetailSideBarNoteList from './EditorDetailSideBarNoteList';

export default {
  name: 'EditorDetailSideBar',
  components: {
    EditorGroupListProfile,
    EditorDetailSideBarNoteList
  },
  props: {
    noteList: Array
  },
  methods: {
    getNoteHTML(noteNo) {
      this.$emit('onGetNoteHTML', noteNo);
    },
    goToGroupList() {
      console.log(this.noteList[0].groupNo);
      this.$router.push({
        name: 'Editor',
        query: { groupNo: this.noteList[0].groupNo }
      });

      console.log('그룹 목록');
    }
  }
};
</script>

<style>
</style>