<template>
  <b-container style="margin-top: 4rem" fluid>
    <b-row>
      <NoteSearch 
        :group_list="group_list" 
        @onGetNoteList="getNoteList" 
        :received_note_list="received_note_list" 
        @onGetNoteHTML="getNoteHTML"/>
      <NoteEditor 
      @onEditNoteHTML="editNoteHTML" 
      :noteId="noteId"
      :noteContent="noteContent"
      />
    </b-row>
  </b-container>
</template>

<script>
import NoteEditor from '../components/Note/NoteEditor.vue';
import NoteSearch from '../components/Note/NoteSearch.vue';

import axios from 'axios';
const SERVER_URL = 'http://localhost:8080/';

export default {
  name: 'Note',
  components: {
    NoteEditor,
    NoteSearch,
  },
  data() {
    return {      
      group_list: [],
      received_note_list: [],

      noteContent: '',
      noteId: 0,
    };
  },
  methods: {
    // api 추가
    get_group_list() {
      const URL = 'videoconference/api/group/gethost/';
      const ID = 'lwh1992@naver.com/';

      axios
        .get(SERVER_URL + URL + ID)
        .then((res) => {
          console.log('this is group'+res.data.groups);
          this.group_list = res.data.groups;
          console.log(this.group_list);
        })
        .catch((err) => console.error(err));
    },
    getNoteList(groupId) {
      console.log(groupId);
      
      const URL = 'videoconference/api/note/get/group/';
      const groupNoAndId = '/lwh1992@naver.com';
      axios
        .get(SERVER_URL + URL + groupId + groupNoAndId)
        .then((res)=>{
          this.received_note_list = res.data.notes;
          console.log(res);
        })
        .catch((err) => console.error(err));
    },
    getNoteHTML(NoteId) {
      console.log('this is note.vue', NoteId);
      const URL_getNoteByNo = 'videoconference/api/note/getno/';

      axios
        .get(SERVER_URL+URL_getNoteByNo+NoteId)
        .then((res)=> {
          this.noteContent = res.data.content;
          this.noteId = NoteId;
          console.log(res);
        })
        .catch((err)=> {
          console.error(err);
          this.noteContent = 'axios Error is occured';
          this.noteId = NoteId;
        });
    },
    editNoteHTML([noteId, noteContent]) {
      // 이거 url 수정할 것 contenet임.
      const URLEdit = 'videoconference/api/note/content/';
      console.log(noteId);
      axios.put(SERVER_URL+URLEdit+noteId, {
        'content': noteContent,
      }).then((res)=>{
        console.log(noteContent);
        alert('upload 성공');
      })
        .catch((err)=>{
          console.log('error뜸');
          console.error(err);
        });
    }
  },
  mounted() {
    this.get_group_list();
  },
};
</script>
