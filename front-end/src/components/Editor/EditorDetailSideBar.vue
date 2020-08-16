<template>
  <v-col cols="3">
    <EditorGroupListProfile />
    <button>그룹목록보기</button>
    <v-divider></v-divider>
    <EditorDetailSideBarNoteList />
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
  }
};
</script>

<style>

</style>