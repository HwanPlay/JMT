<template>
  <div>
    <b-button
      @click="getNoteList(group.groupNo)"
      v-b-toggle="'co-'+group.groupNo"
    >{{group.groupName}}</b-button>

    <b-collapse :id="'co-'+group.groupNo" class="mt-2">
      <h1>Note List</h1>
      <div class="container-fluid">
        <div class="row">
          <b-button v-for="note in received_note_list" :key="note.noteNo" class="col-3" bg-variant="dark" @click="getNoteHTML(note.noteNo)" variant="primary">
            
            <span>{{compute_date(note.createdDate)}}</span>
            <p>{{ note.title }}</p>
          
          </b-button>
        </div>
      </div>
    </b-collapse>
  </div>
</template>

<script>
export default {
  name: 'NoteSearchGroup',
  components: {},
  data() {
    return {
      string_num: String,
    };
  },
  props: {
    group: Object,
    received_note_list: Array,
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
  },
};
</script>

<style>
</style>