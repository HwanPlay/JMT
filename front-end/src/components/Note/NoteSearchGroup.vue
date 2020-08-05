<template>
  <div>
    {{group}}
    <b-button @click="getNoteList(group.groupNo)" v-b-toggle="'co-'+group.groupNo" >{{group.groupName}}</b-button>
    <!-- Elements to collapse -->
    <b-collapse :id="'co-'+group.groupNo" class="mt-2">
        <h1>asdsad</h1>
      <div v-for="notes in received_note_list" :key="notes.noteNo">
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
      console.log(groupId);
      this.$emit('onGetNoteList', groupId);
    },
    makeString(num) {
      console.log(num);
      console.log(typeof num.toString());
      this.string_num = num.toString();
      return this.string_num;
    }
  },
};
</script>

<style>
</style>