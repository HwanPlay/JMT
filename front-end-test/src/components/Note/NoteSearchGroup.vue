<template>
  <div>
    <b-button v-b-toggle.group.group_id>{{group.group_title}}</b-button>

    <!-- Elements to collapse -->
    <b-collapse :id="group.group_id" class="mt-2">
      <p>{{group.group_conf_data.date}}</p>
      <b-button
        v-for="note in group.group_conf_data.note_list"
        :key="note.note_id"
        v-text="note.note_title"
        @click="onGetNote(note.note_id)"
      ></b-button>
    </b-collapse>
  </div>
</template>

<script>
export default {
  name: 'NoteSearchGroup',
  components: {},
  props: {
    group: Object,
  },
  methods: {
    onGetNote(noteId) {
      this.emit('GetNote', noteId);
    },
  },
};
</script>

<style>
</style>