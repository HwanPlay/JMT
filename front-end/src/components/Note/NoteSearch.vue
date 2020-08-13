<template>
  <b-col cols="3" class="rounded border border-secondary">
    <h2 class="text-center">Groups</h2>
    
    <NoteSearchSidebar
      :group_list="group_list"
      :received_note_list="received_note_list"
      @onGetNoteList="getNoteList"
      @onGetNoteHTML="getNoteHTML"
    />
  </b-col>
</template>

<script>
import NoteSearchSidebar from './NoteSearchSidebar.vue';

export default {
  name: 'NoteSearch',
  components: {
    NoteSearchSidebar,
  },
  props: {
    group_list: Array,
    received_note_list: Array,
  },
  methods: {
    getNoteList(groupId) {
      this.$emit('onGetNoteList', groupId);
    },
    getNoteHTML(NoteId) {
      this.$emit('onGetNoteHTML', NoteId);
    },
  },
  data() {
    return {};
  },
};
</script>

<style>
</style>
