<template>
    <v-row style=" height: 100%; width:100%;">
      <EditorDetailSideBar :noteList="noteList" @onGetNoteHTML="getNoteHTML" />      
      <v-col cols="1"></v-col>
      <EditorTiptap :noteObj="noteObj" @onSaveNote="saveNote" @onDeleteNote="deleteNote" />
    </v-row>
</template>

<script>
import SERVER from '../api/spring';
import axios from 'axios';

import EditorDetailSideBar from '../components/Editor/EditorDetailSideBar.vue';
import EditorTiptap from '../components/Editor/EditorTiptap.vue';

export default {
  name: 'EditorDetail',
  data() {
    return {
      noteObj: {},
      noteList: [],

      groupNo: this.$route.query.groupNo,
    };
  },
  components: {
    EditorDetailSideBar,
    EditorTiptap
  },
  methods: {
    getNoteHTML(noteNo) {
      if(noteNo) {
        // 사이드바에서 올라옴. 사이드바 바꿀 필요 없음.
      } else if (this.$route.query.noteNo) {
        // 다른 view에서 온 요쳥.
        noteNo = this.$route.query.noteNo;
      } else if (noteNo === undefined) {
        return;
      }

      const URL_getNoteByNo = '/note/getno/';

      axios
        .get(SERVER.URL + URL_getNoteByNo + noteNo)
        .then(res => {
          this.noteObj = res.data;
        })
        .catch(err => {
          console.error(err);
          this.noteContent = 'axios Error is occured';
        });
    },
    getNoteList(groupNo) {
      if (this.$route.query.groupNo) {
        groupNo = this.$route.query.groupNo;
      } else if (groupNo === undefined) {
        return;
      }

      const FUNC_URL = '/note/get/group/';

      axios
        .get(SERVER.URL + FUNC_URL + groupNo)
        .then(res => {
          this.noteList = res.data.notes;
        })
        .catch(err => console.error(err));
    },
    saveNote(noteObj) {
      const URL_saveNote = '/note/';
      axios
        .put(SERVER.URL + URL_saveNote + noteObj.Id, {
          title: noteObj.Title,
          content: noteObj.Content
        })
        .then(res => {
          this.getNoteList();
        })
        .catch(err => console.error(err));
    },
    deleteNote({noteNo, groupNo}) {
      const URLDeleteNote = '/note/delno/';
      
      axios
        .delete(SERVER.URL + URLDeleteNote + noteNo + '/' + groupNo)
        .then(res => {
          if (res.data.count === 0){
            this.$router.push({name: 'Editor'});            
          } else {
            this.getNoteHTML(this.noteList[0].noteNo);
            this.$router.push({name: 'EditorDetail', query: {noteNo: this.noteList[0].noteNo, groupNo: groupNo}});
          }
          this.getNoteList();
        })
        .catch(err => {
          console.error(err);
        });
    },
  },
  mounted() {
    this.getNoteHTML();
    this.getNoteList();
  },

};
</script>

<style>

</style>