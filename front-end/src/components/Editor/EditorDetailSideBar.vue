<template>
  <v-col cols="3">
    <EditorGroupListProfile />
    <button @click="goToGroupList">그룹목록보기</button>
    <v-divider></v-divider>

    <EditorDetailSideBarNoteList :noteList="noteList" @onGetNoteHTML="getNoteHTML" />
    
  </v-col>
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
    noteList: Array,
  },
  methods: {
    getNoteHTML(noteNo) {
      this.$emit('onGetNoteHTML', noteNo);
    },
    goToGroupList() {
      console.log(this.noteList[0].groupNo);
      this.$router.push({name: 'Editor', query: {groupNo: this.noteList[0].groupNo}});

      console.log('그룹 목록');
    }
  }
};
</script>

<style>

</style>