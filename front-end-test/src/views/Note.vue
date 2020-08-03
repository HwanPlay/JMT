<template>
  <b-container style="margin-top: 4rem" fluid>
    <b-row>
      <NoteSearch :NoteList="NoteList" />
      <NoteEditor :receivedHTML="receivedHTML" @onUploadHTML="UploadHTML" />
    </b-row>
  </b-container>
</template>

<script>
import NoteEditor from '../components/Note/NoteEditor.vue';
import NoteSearch from '../components/Note/NoteSearch.vue';

import axios from 'axios';
const SERVER_URL = 'https://localhost:8080/';

export default {
  name: 'Note',
  components: {
    NoteEditor,
    NoteSearch,
  },
  data() {
    return {
      NoteList: null,
      receivedHTML: '',
      dataHTML: '',
    };
  },
  methods: {
    getNoteList() {
      axios
        .post(SERVER_URL, 'DATA', 'CONFIG')
        .then((res) => {
          console.log(res);
          this.NoteList = res;
        })
        .catch((err) => {
          console.error(err);
        });
    },
    UploadHTML(res) {
      console.log(res);
      // axios로 요청 보낼것
    },
  },
};
</script>
