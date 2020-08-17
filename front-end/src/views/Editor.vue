<template>
  <v-container fluid ma-0 pa-0 >
    <v-row>
      <EditorGroupList :groupList="groupList" @onGetNoteList="getNoteList" />
      <EditorNoteList :receivedNoteList="receivedNoteList" @onOpenNoteDetail="openNoteDetail" />
    </v-row>
  </v-container>
</template>

<script>
import SERVER from '../api/spring';
import axios from 'axios';


import EditorGroupList from '../components/Editor/EditorGroupList';
import EditorNoteList from '../components/Editor/EditorNoteList';

export default {
  name: 'Editor', 
  components: {
    EditorGroupList,
    EditorNoteList,
  },
  data() {
    return {
      groupList: [],
      receivedNoteList: [],
      currentGroup: 0,
    };
  },
  methods: {
    getGroupList() {
      const GROUP_URL = '/group/get/all/';
      
      axios
        .get(SERVER.URL + GROUP_URL + this.$store.state.userId)
        .then(res => {
          console.log(res);
          this.groupList = res.data.groups;
        })
        .catch(err => console.error(err));
    },
    getNoteList(groupId) {
      const URL = '/note/get/group/';
      
      if(groupId) {
        console.log('groupNo가 있을때');
      } else if (this.$route.query.groupNo){
        groupId = this.$route.query.groupNo;
      } else {
        this.initGetNoteList();
        return;
      }

      axios
        .get(SERVER.URL + URL + groupId)
        .then(res => {
          this.receivedNoteList = res.data.notes;
          console.log(this.receivedNoteList);
          this.currentGroup = groupId;
        })
        .catch(err => console.error(err));
    },
    openNoteDetail({noteNo, groupNo}) {
      console.log(noteNo,groupNo);
      // currentGroup를 groupNo로 고쳐야 함.
      this.$router.push({ name: 'EditorDetail', query: { noteNo: noteNo, groupNo: this.currentGroup }});
    },
    initGetNoteList() {
      const GROUP_URL = '/group/get/all/';
      
      axios
        .get(SERVER.URL + GROUP_URL + this.$store.state.userId)
        .then(res => {
          this.groupList = res.data.groups;
          console.log(this.groupList);
          this.getNoteList(this.groupList[0].groupNo);
        })
        .catch(err => console.error(err));
    }
  },
  mounted() {
    this.getGroupList();
    this.getNoteList();
  }
};
</script>

<style>

</style>