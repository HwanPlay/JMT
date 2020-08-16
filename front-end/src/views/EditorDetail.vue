<template>
  <v-container fluid ma-0 pa-0 >
    <v-row>
      <EditorDetailSideBar />      
    </v-row>
  </v-container>
</template>

<script>
import SERVER from '../api/spring';
import axios from 'axios';

import EditorDetailSideBar from '../components/Editor/EditorDetailSideBar';

export default {
  name: 'EditorDetail',
  data() {
    return {
      noteObj: {
        id: 0,
        title: '',
        content: '',
      }
    };
  },
  components: {
    EditorDetailSideBar,
  },
  methods: {
    getNoteHTML(noteId) {

      if (this.$route.query.noteId) {
        noteId = this.$route.query.noteId;
      } else if (noteId === undefined) {
        return;
      }

      const URL_getNoteByNo = '/note/getno/';

      axios
        .get(SERVER.URL + URL_getNoteByNo + noteId)
        .then(res => {
          console.log(res.data);
          this.noteObj.Content = res.data.content;
          this.noteObj.Id = res.data.noteNo;
          this.noteObj.Title = res.data.note_title;

        })
        .catch(err => {
          console.error(err);
          this.noteContent = 'axios Error is occured';
        });
    },
  },
  mounted() {
    this.getNoteHTML();
  }
};
</script>

<style>

</style>