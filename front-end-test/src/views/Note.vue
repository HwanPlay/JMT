<template>
  <b-container style="margin-top: 4rem" fluid>
    <b-row>
      <NoteSearch :group_list="group_list" @onGetNote="GetNote" />
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
      group_list: [
        {
          group_id: 1,
          group_title: '그룹 이름',
          group_conf_data: [
            {
              date: '1월 1일',
              note_list: [
                {
                  note_id: 3,
                  note_title: '주제A',
                },
                {
                  note_id: 5,
                  note_title: '주제B',
                },
              ],
            },
            {
              date: '1월5일',
              note_list: [
                {
                  note_id: 4,
                  note_title: '주제c',
                },
                {
                  note_id: 7,
                  note_title: '주제d',
                },
              ],
            },
          ],
        },
      ],
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
    GetNote(noteId) {
      axios
        .get(SERVER_URL, noteId)
        .then((res) => {
          console.log(res);
          this.receivedHTML = res.value;
        })
        .catch((err) => console.error(err));
    },
  },
};
</script>
