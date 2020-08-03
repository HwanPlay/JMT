<template>
  <div>
      <NoteSearchGroupButton :GroupTitle=GroupTitle @getNote="onGetNote" />



    <!-- 주석 -->

      <b-button v-b-toggle.note_list>{{GroupTitle}}</b-button>

      <!-- Elements to collapse -->
      <b-collapse id="note_list" class="mt-2">
        <p>이게 날짜</p>
        <b-card>I am collapsible content A!</b-card>
        <b-card>I am collapsible content A!</b-card>
        <b-card>I am collapsible content A!</b-card>
      </b-collapse>
      
      <b-collapse id="collapse-b" class="mt-2">
        <p>이게 날짜</p>
        <b-card>I am collapsible content A!</b-card>
        <b-card>I am collapsible content B!</b-card>
      </b-collapse>
    </div>
</template>

<script>
import NoteSearchGroupButton from './NoteSearchGroupButton.vue';
export default {
  name: 'NoteSearchGroup',
  components: {
    NoteSearchGroupButton
  },
  props: {
    GroupTitle: Object,
  },
  methods: {
    onGetNote(noteId) {
      this.emit('GetNote', noteId)
    }
  }
};
</script>

<style>
</style>