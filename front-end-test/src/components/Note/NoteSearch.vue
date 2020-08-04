<template>
  <b-col cols="4">
    <div>
      <NoteSearchGroup :v-for="group in group_list" :key="group.group_id"
      @GetNote="onGetNote" />
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
    group_list: Object,
  },
  methods: {
    GetNote(noteId){
      this.emit('onGetNote', noteId);
    } 
  },
  data() {
    return {
      test: ['collapse-a', 'collapse-b']
    };
  }
};
</script>

<style></style>
