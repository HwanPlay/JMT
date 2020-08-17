<template>
  <div style="padding: 20px; width:72%; height:92%;">
    <h2>Note</h2>
    <div style="width:100%; height:100%; ">

    <v-row style="margin-left:90px; margin-top:20px; height: 100%; overflow-y :scroll; ">
      <EditorNoteListItems  v-for="note in receivedNoteList" :key="note.noteNo" :note="note" @onOpenNoteDetail="openNoteDetail" />
    </v-row> 
    </div>
  </div>
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