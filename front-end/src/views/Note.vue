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
      :group_list="group_list"
      @onDeleteNote="deleteNote"
      :noteObj="noteObj"
      @onSaveNote="saveNote"
      />
      <b-col cols="1"></b-col>

    </b-row>
  </b-container>
</template>

<script>
import NoteEditor from '../components/Note/NoteEditor.vue';
import NoteSearch from '../components/Note/NoteSearch.vue';
import SERVER from '../api/spring.js';

import axios from 'axios';

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
      USER_ID: this.$store.state.userId,

      noteObj: {
        Content: '',
        Id: 0,
        Title: '',
      },
      
    };
  },
  created() {
    this.getNoteHTML();
  },
  methods: {
    // api 추가
    get_group_list() {
      const FUNC_URL = '/group/get/all/';

      axios
        .get(SERVER.URL + FUNC_URL + this.USER_ID)
        .then((res) => {
          this.group_list = res.data.groups;
        })
        .catch((err) => console.error(err));
    },
    getNoteList(groupId) {
      const FUNC_URL = '/note/get/group/';
      // '/' + this.USER_ID
      axios
        .get(SERVER.URL + FUNC_URL + groupId )
        .then((res)=>{
          this.received_note_list = res.data.notes;
        })
        .catch((err) => console.error(err));
    },
    getNoteHTML(NoteId) {
      if (this.$route.params.NoteId_Cal) {
        NoteId = this.$route.params.NoteId_Cal;
      }else if(NoteId === undefined) {
        return ;
      }

      const URL_getNoteByNo = '/note/getno/';

      axios
        .get(SERVER.URL+URL_getNoteByNo+NoteId)
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
      const URL_saveNote = '/note/';
            
      axios.put(SERVER.URL + URL_saveNote + noteObj.Id,{
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
    //   axios.put(SERVER.URL+URLContentEdit+noteId, {
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
    //   axios.put(SERVER.URL+URLTitleEdit+noteId, {
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
      const URLDeleteNote = '/note/delno/';
      axios.delete(SERVER.URL+URLDeleteNote+noteId)
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
