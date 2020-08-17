<template>
  <v-col cols="9">
    <h2>Note</h2>
    <v-row>
      <EditorNoteListItems v-for="note in receivedNoteList" :key="note.noteNo" :note="note" @onOpenNoteDetail="openNoteDetail" />
    </v-row>
  </v-col>
</template>

<script>
import EditorNoteListItems from './EditorNoteListItems';

export default {
  name: 'EditorNoteList',
  components: {
    EditorNoteListItems,
  },
  props: {
    receivedNoteList: Array,
  },
  methods: {
    openNoteDetail({noteNo, groupNo}) {
      this.$emit('onOpenNoteDetail', {noteNo, groupNo});
    },
  }
};
</script>

<style>

</style>