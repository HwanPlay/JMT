<template>
  <div>
    <!-- {{group}} -->
    <b-button
      @click="getNoteList(group.groupNo)"
      v-b-toggle="'co-'+group.groupNo"
    >{{group.groupName}}</b-button>
    <!-- Elements to collapse -->
    <b-collapse :id="'co-'+group.groupNo" class="mt-2">
      <h1>Note List</h1>
      <!-- <span>{{received_note_list}}</span> -->

      <div v-for="note in received_note_list" :key="note.noteNo">
        <hr>
        <!-- <span>{{note}}</span> -->
        
        <h3>{{note.createdDate}}</h3>
        <b-button @click="getNoteHTML(note.noteNo)" variant="primary"><p>{{ note.title }}</p></b-button>
        <!-- <p>{{ note }}</p> -->
        <!-- <button v-on:click="getTheSelectedOne">Get The Value</button> -->
      </div>
      <br>
      <!-- <div v-for="notes in received_note_list" :key="notes.noteNo"> -->
      <!-- </div> -->
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
      console.log(groupId);
      this.$emit('onGetNoteList', groupId);
    },
    getNoteHTML(NoteId){
      console.log('why this');
      console.log('this is note search group'+NoteId);
      this.$emit('onGetNoteHTML', NoteId);
    }
  },
};
</script>

<style>
</style>