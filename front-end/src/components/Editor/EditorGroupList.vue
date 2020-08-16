<template>
  <v-col cols="3">
    <EditorGroupListProfile />
    
    <v-divider></v-divider>
    <EditorGroupListDetail :groupList="groupList" @onGetNoteList="getNoteList" />
    
  </v-col>
</template>

<script>
import EditorGroupListProfile from './EditorGroupListProfile';
import EditorGroupListDetail from './EditorGroupListDetail';

export default {
  name: 'EditorGroupList',
  components: {
    EditorGroupListProfile,
    EditorGroupListDetail
  },
  props: {
    groupList: Array,
  },
  methods: {
    getNoteList(groupId) {
      this.$emit('onGetNoteList', groupId);
    }
  }
};
</script>

<style>
</style>