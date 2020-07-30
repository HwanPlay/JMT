<template>
  <b-container fluid>
    <b-row>
      <NoteSearch />
      <NoteEditor 
        :receivedHTML=receivedHTML
        @onUploadHTML=UploadHTML
       />
    </b-row>
  </b-container>
</template>

<script>
import NoteEditor from "../components/Note/NoteEditor.vue";
import NoteSearch from "../components/Note/NoteSearch.vue";

import axios from "axios";
const SERVER_URL = "https://localhost:8080/";

export default {
  name: "Note",
  components: {
    NoteEditor,
    NoteSearch,
  },
  data() {
    return {
      NoteList: null,
      receivedHTML: '',
      dataHTML : '',
    };
  },
  methods: {
    getNoteList() {
      axios.post(SERVER_URL, "DATA", "CONFIG")
      .then(res=>{
        this.receivedHTML = res.value
      });
    },
    UploadHTML(res) {
      console.log(res)
      // axios로 요청 보낼것
    }
  },
};
</script>

