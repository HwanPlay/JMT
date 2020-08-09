<template>
  <b-container fluid>
    <b-row>
      <NoteSearch 
        :group_list="group_list" 
        @onGetNoteList="getNoteList" 
        :received_note_list="received_note_list" 
        @onGetNoteHTML="getNoteHTML"/>
      <b-col cols="1"></b-col>
      
      <NoteEditor 
      @onDeleteNote="deleteNote"
      :noteObj="noteObj"
      @onSaveNote="saveNote"
      />
    </b-row>
  </b-container>
</template>

<script>
import NoteEditor from '../components/Note/NoteEditor.vue';
import NoteSearch from '../components/Note/NoteSearch.vue';

import axios from 'axios';
const SERVER_URL = 'http://localhost:8080/videoconference/api/';
const USER_ID = this.$store.state.userId;

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

      noteObj: {
        Content: '',
        Id: 0,
        Title: '',
      }
    };
  },
  methods: {
    // api 추가
    get_group_list() {
      const FUNC_URL = 'group/get/all/';

      axios
        .get(SERVER_URL + FUNC_URL + USER_ID)
        .then((res) => {
          this.group_list = res.data.groups;
        })
        .catch((err) => console.error(err));
    },
    getNoteList(groupId) {
      const FUNC_URL = 'note/get/group/';
      axios
        .get(SERVER_URL + FUNC_URL + groupId + '/' + USER_ID)
        .then((res)=>{
          this.received_note_list = res.data.notes;
        })
        .catch((err) => console.error(err));
    },
    getNoteHTML(NoteId) {
      const URL_getNoteByNo = 'note/getno/';

      axios
        .get(SERVER_URL+URL_getNoteByNo+NoteId)
        .then((res)=> {
          console.log(res.data);
          this.noteObj.Content = res.data.content;
          this.noteObj.Id = res.data.noteNo;
          this.noteObj.Title = res.data.title;
        })
        .catch((err)=> {
          console.error(err);
          this.noteContent = 'axios Error is occured';
        });
    },
    saveNote(noteObj) {
      const URL_saveNote = 'note/';
            
      axios.put(SERVER_URL + URL_saveNote + noteObj.Id,{
        'title': noteObj.Title,
        'content': noteObj.Content
      }).then((res)=>{
        console.log('title:', noteObj.Title);
        console.log('Content:', noteObj.Content);
      }).catch((err)=> console.error(err));      
    },


    // editNoteContent([noteId, noteContent]) {
    //   // 이거 url 수정할 것 contenet임.
    //   const URLContentEdit = 'videoconference/api/note/content/';
    //   console.log(noteId);
    //   axios.put(SERVER_URL+URLContentEdit+noteId, {
    //     'content': noteContent,
    //   }).then((res)=>{
    //     console.log(res);
    //     // alert('content edit 성공');
    //   })
    //     .catch((err)=>{
    //       console.log('error뜸');
    //       console.error(err);
    //     });
    // },
    // editNoteTitle([noteId, noteTitle]) {
    //   const URLTitleEdit = 'videoconference/api/note/title/';
    //   axios.put(SERVER_URL+URLTitleEdit+noteId, {
    //     'title': noteTitle,
    //   }).then((res)=>{
    //     console.log(res);
    //     // alert('title edit 성공');
    //   })
    //     .catch((err)=>{
    //       console.log('error뜸');
    //       console.error(err);
    //     });
    // },

    deleteNote(noteId) {
      const URLDeleteNote = 'api/note/delno/';
      axios.delete(SERVER_URL+URLDeleteNote+noteId)
        .then((res)=> {
          console.log(res);
          console.log('delete note' + noteId);
        })
        .catch((err)=>console.error(err));
    }
  },
  mounted() {
    this.get_group_list();
  },
};
</script>
