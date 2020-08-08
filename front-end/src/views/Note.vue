<template>
  <b-container fluid>
    <b-row>
      <NoteSearch 
        :group_list="group_list" 
        @onGetNoteList="getNoteList" 
        :received_note_list="received_note_list" 
        @onGetNoteHTML="getNoteHTML"/>
      
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
      const URL = 'videoconference/api/group/get/all/';
      const ID = 'lwh1992@naver.com/';

      axios
        .get(SERVER_URL + URL + ID)
        .then((res) => {
          this.group_list = res.data.groups;
        })
        .catch((err) => console.error(err));
    },
    getNoteList(groupId) {
      const URL = 'videoconference/api/note/get/group/';
      const groupNoAndId = '/lwh1992@naver.com';
      axios
        .get(SERVER_URL + URL + groupId + groupNoAndId)
        .then((res)=>{
          this.received_note_list = res.data.notes;
        })
        .catch((err) => console.error(err));
    },
    getNoteHTML(NoteId) {
      const URL_getNoteByNo = 'videoconference/api/note/getno/';

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
      const URL_saveNoteTitle = 'videoconference/api/note/title/';
      const URL_saveNoteContent = 'videoconference/api/note/content/';
      const note_ID = noteObj.Id;
      
      // console.log(noteObj.Title);
      // console.log(noteObj.Content);
      
      axios.put(SERVER_URL + URL_saveNoteTitle + note_ID,{
        'title': noteObj.Title
      }).then((res)=>{
        console.log('title:', noteObj.Title);
      }).catch((err)=> console.error(err));

      axios.put(SERVER_URL + URL_saveNoteContent + note_ID,{
        'content': noteObj.Content
      }).then((res)=>{
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
      const URLDeleteNote = 'videoconference/api/note/delno/';
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
