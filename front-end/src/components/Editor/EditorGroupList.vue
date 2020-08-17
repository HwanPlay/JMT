<template>
  <div style="width:256px; background-color: rgb(52, 63, 87); margin-left:30px;">
    <EditorGroupListProfile />
    
    <v-divider></v-divider>
    <EditorGroupListDetail :groupList="groupList" @onGetNoteList="getNoteList" />
    
  </div>
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