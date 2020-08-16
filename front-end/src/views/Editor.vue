<template>
  <v-container fluid ma-0 pa-0 >
    <v-row>
      <EditorGroupList :groupList="groupList" @onGetNoteList="getNoteList" />
      <EditorNoteList :receivedNoteList="receivedNoteList" @onOpenNoteDetail="openNoteDetail" />
    </v-row>
  </v-container>
</template>

<script>
import SERVER from '../api/spring';
import axios from 'axios';


import EditorGroupList from '../components/Editor/EditorGroupList';
import EditorNoteList from '../components/Editor/EditorNoteList';

export default {
  name: 'Editor', 
  components: {
    EditorGroupList,
    EditorNoteList,
  },
  data() {
    return {
      groupList: [],
      receivedNoteList: []
    };
  },
  methods: {
    getGroupList() {
      const GROUP_URL = '/group/get/all/';

      axios
        .get(SERVER.URL + GROUP_URL + this.$store.state.userId)
        .then(res => {
          console.log(res);
          this.groupList = res.data.groups;
        })
        .catch(err => console.error(err));
    },
    getNoteList(groupId) {
      const URL = '/note/get/group/';
      axios
        .get(SERVER.URL + URL + groupId)
        .then(res => {
          this.receivedNoteList = res.data.notes;
          console.log(this.receivedNoteList);
        })
        .catch(err => console.error(err));
    },
    openNoteDetail({noteNo, groupNo}) {
      console.log(noteNo,groupNo);
      this.$router.push({ name: 'EditorDetail', query: { noteNo: noteNo, groupNo: groupNo }});
    },
  },
  mounted() {
    this.getGroupList();
  }
};
</script>

<style>

</style>