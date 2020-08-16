<template>
  <v-col cols="4" @click="openNoteDetail({'noteNo':note.noteNo, 'groupNo': note.groupNo})">
    <div>{{note.note_title}}</div>
    <div>{{note.createdDate}}</div>
  </v-col>
</template>

<script>
export default {
  name: 'EditorNoteListItems',
  props: {
    note: Object
  },
  methods: {
    openNoteDetail({noteNo, groupNo}) {
      this.$emit('onOpenNoteDetail', {noteNo, groupNo});
    }
  }
};
</script>

<style>

</style>