<template>
  <v-container fluid ma-0 pa-0 >
    <v-row>
      <EditorDetailSideBar :noteList="noteList" @onGetNoteHTML="getNoteHTML" />      
      <EditorTiptap :noteObj="noteObj" @onSaveNote="saveNote" @onDeleteNote="deleteNote" />
    </v-row>
  </v-container>
</template>

<script>
import SERVER from '../api/spring';
import axios from 'axios';

import EditorDetailSideBar from '../components/Editor/EditorDetailSideBar.vue';
import EditorTiptap from '../components/Editor/EditorTiptap';

export default {
  name: 'EditorDetail',
  data() {
    return {
      noteObj: {},
      noteList: [],
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
        console.log('1번');
      } else if (this.$route.query.noteNo) {
        // 다른 view에서 온 요쳥.
        noteNo = this.$route.query.noteNo;
        console.log('2번');
      } else if (noteNo === undefined) {
        return;
      }

      const URL_getNoteByNo = '/note/getno/';

      axios
        .get(SERVER.URL + URL_getNoteByNo + noteNo)
        .then(res => {
          console.log(res.data);
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
          console.log('getNoteList',res);
          this.noteList = res.data.notes;
          console.log(this.noteList);
        })
        .catch(err => console.error(err));
    },
    saveNote(noteObj) {
      const URL_saveNote = '/note/';
      console.log(noteObj);
      axios
        .put(SERVER.URL + URL_saveNote + noteObj.Id, {
          title: noteObj.Title,
          content: noteObj.Content
        })
        .then(res => {
          console.log(res);
          this.getNoteList();
        })
        .catch(err => console.error(err));
    },
    deleteNote(noteId) {
      const URLDeleteNote = '/note/delno/';
      console.log(this.noteList, this.noteObj);
      
      axios
        .delete(SERVER.URL + URLDeleteNote + noteId)
        .then(res => {
          console.log('del res', res);
          this.getNoteList(this.groupId);
          console.log(this.noteList);
          if (this.noteList && this.noteList.length !== 0){
            this.getNoteHTML(this.noteList[0].noteNo);
          } else {
            this.$router.push({name: 'Editor'});            
          }
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