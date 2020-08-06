<template>
  <div>
    <div v-for="group in group_list" :key="group.groupNo">
      <b-button @click="getNoteList(group.groupNo)" v-b-toggle.sidebar-backdrop>{{group.groupName}}</b-button>
    </div>

    <b-sidebar
      id="sidebar-backdrop"
      title="Note List"
      :backdrop-variant="variant"
      backdrop
      shadow
    >
      <div class="px-3 py-2">
       <b-button v-b-toggle.sidebar-backdrop v-for="note in received_note_list" :key="note.noteNo" class="col-4" bg-variant="dark" @click="getNoteHTML(note.noteNo)" variant="primary">
          <span>{{compute_date(note.createdDate)}}</span>
          <p>{{ note.title }}</p>
        </b-button>
      </div>
    </b-sidebar>
  </div>
</template>

<script>
export default {
  name: 'NoteSearchSidebar',
  props: {
    group_list: Array,
    received_note_list: Array,
  },
  data() {
    return {
      variant: 'dark'
    };
  },
  methods: {
    getNoteList(groupId) {
      this.$emit('onGetNoteList', groupId);
    },
    getNoteHTML(NoteId){
      this.$emit('onGetNoteHTML', NoteId);
    },
    compute_date(date) {
      return date.slice(2,10);
    }
  }
};
</script>

<style>

</style>