<template>
  <b-col cols="3" class="rounded border border-secondary">
    <div class="rounded  ">
    <h2 class="text-center">Groups</h2>
    
    <div v-if="false">
      <div v-for="group in group_list" :key="group.groupNo">
        <NoteSearchGroup
          :group="group"
          :received_note_list="received_note_list"
          @onGetNoteList="getNoteList"
          @onGetNoteHTML="getNoteHTML"
        />
      </div>
    </div>
    
    <NoteSearchSidebar
      :group_list="group_list"
      :received_note_list="received_note_list"
      @onGetNoteList="getNoteList"
      @onGetNoteHTML="getNoteHTML"
    />
    </div>
  </b-col>
</template>

<script>
import NoteSearchGroup from './NoteSearchGroup.vue';
import NoteSearchSidebar from './NoteSearchSidebar.vue';

export default {
  name: 'NoteSearch',
  components: {
    NoteSearchGroup,
    NoteSearchSidebar,
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
    },
  },
  data() {
    return {};
  },
};
</script>

<style>
.searchbar_text {

}
</style>
