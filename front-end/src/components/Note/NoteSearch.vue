<template>
  <b-col cols="4">
    <div>
      <div v-for="group in group_list" :key="group.groupNo">
        <NoteSearchGroup @onGetNoteList="getNoteList" :group="group" />
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
    notes_list: Array,
  },
  methods: {
    getNoteList(groupId) {
      this.$emit('onGetNoteList', groupId);
    }
  },
  data() {
    return {
    };
  }
};
</script>

<style></style>
