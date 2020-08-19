<template>
  
  <!-- <div style="padding: 20px; width:76%; height:92%; floa"> -->
    <v-row style=" position : absolute; left : 300px; width:80%; height: 85%; margin-top:10px;">

    <div style="width:100%; height:100%; ">

    <v-row style="margin-left:15px; margin-top:20px; width:100%; height: 100%; overflow-y :scroll; ">
      <EditorNoteListItems   v-for="note in receivedNoteList" :key="note.noteNo" :note="note" @onOpenNoteDetail="openNoteDetail" />
    </v-row> 
    </div>
    </v-row>
  <!-- </div> -->
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