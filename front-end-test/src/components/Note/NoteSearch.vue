<template>
  <b-col cols="4">
    <div>
      <div v-for="group in group_list" :key="group.group_id">
        <NoteSearchGroup :group="group" />
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
  },
  methods: {
    GetNote(noteId){
      this.emit('onGetNote', noteId);
    } 
  },
  data() {
    return {
    };
  }
};
</script>

<style></style>
