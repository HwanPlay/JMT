<template>
  <b-col cols="4">
    <div>
      <div v-for="group in group_list" :key="group.groupNo">
        <NoteSearchGroup @onGetNoteList="getNoteList" :group="group" :received_note_list="received_note_list" @onGetNoteHTML="getNoteHTML" />
      </div>
    </div>    
  </b-col>
</template>

<script>
import NoteSearchGroup from './NoteSearchGroup.vue';
export default {
  name: 'NoteSearch',
  components: {
    NoteSearchGroup,
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
    }
  },
  data() {
    return {
    };
  }
};
</script>

<style></style>
