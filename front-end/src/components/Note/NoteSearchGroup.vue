<template>
  <div>
    <b-button @click="getNoteList(group.groupNo)" v-b-toggle.coll-1>{{group.groupName}}</b-button>
    <!-- Elements to collapse -->
    <b-collapse id="coll-1" class="mt-2">
      <div v-for="group_data in group.group_conf_data" :key="group_data.date">
        <h3>{{group_data.date}}</h3>
        <div v-for="note in group_data.note_list" :key="note.note_id">
          <b-button @click="getNoteList(note.note_id)">{{note.note_title}}</b-button>
        </div>
      </div>
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
    getNoteList(groupId) {
      console.log(groupId);
      this.$emit('onGetNoteList', groupId);
    },
  },
};
</script>

<style>
</style>