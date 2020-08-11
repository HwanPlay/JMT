<template>
  <div class="rounded text-monospace p-2">

    <div v-for="group in group_list" :key="group.groupNo">
      <div class="rounded group_btn">
        <b-button class="text-left" block variant="white" @click="getNoteList(group.groupNo)" v-b-toggle.sidebar-backdrop> <h3>{{group.groupName}}</h3></b-button>
      </div>
      <!-- <hr align="left" class="group_hr"> -->
    </div>

    <b-sidebar
      id="sidebar-backdrop"
      :backdrop-variant="variant"
      backdrop
      shadow
      width=25rem 
    >
      <div class="px-3">
        <h3 class="text-center">Notes</h3>
        <div class="row p-2">

          <b-button class="col-4" 
          v-b-toggle.sidebar-backdrop 
          v-for="note in received_note_list" :key="note.noteNo" 
          bg-variant="dark" @click="getNoteHTML(note.noteNo)" variant="primary">
            <h3>{{ note.title }}</h3>
            <h5>{{compute_date(note.createdDate)}}</h5>
          </b-button>
        </div>
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
.group_hr {
  margin: 0.3rem auto 0.3rem 0 !important;
  border-radius: 5px !important;
  width: 50% !important;
  color:white !important;
  background-color:white !important;
}
.group_btn {
  margin: 0.3rem auto 0.3rem 0 !important;
  border-radius: 5px !important; 
}
.group_btn:hover {
  background-color: #f8f9fa;
}
</style>