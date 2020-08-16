<template>
  <ul>
    <li v-for="note in noteList" :key="note.noteNo" v-text="note.note_title"
      @click="getNoteHTML(note.noteNo)"
    ></li>
  </ul>
</template>

<script>
export default {
  name: 'EditorDetailSideBarNoteList',
  props: {
    noteList:Array,
  },
  methods: {
    getNoteHTML(noteNo) {
      console.log(noteNo);
      this.$emit('onGetNoteHTML', noteNo);
    }
  }
};
</script>

<style>

</style>