<template>
  <v-row style="height: 100%; width:100%;">
    <div style="height: 100%; width:100%;">
      <v-row  style=" height: 100%; width:100%;">
        <EditorGroupList :groupList="groupList" @onGetNoteList="getNoteList" />

        <v-col v-if="noGroup" class="justify-center align-content-center">
          <v-card class="d-flex justify-center align-content-end flex-wrap mt-5" flat tile min-height="500">
            <div id="imgContainer" class="text-center rounded-circle d-inline-flex align-center justify-center ma-3">
              <v-img src="../assets/JMT/JMTLogo.png" max-width="200px"></v-img>
            </div>
          </v-card>
          <v-card class="mt-10 d-flex justify-center align-content-center flex-wrap" flat tile min-height="50">
            <h3>JMT는 <strong>그룹 기반</strong> 화상 회의 서비스 입니다!</h3>
          </v-card>
          <v-card class="d-flex justify-center align-content-center flex-wrap" flat tile min-height="100">
            <h5>서비스를 이용하기 위해서는 그룹을 생성하시거나, 그룹 가입을 해주세요</h5>
          </v-card>
        </v-col>

        <v-col v-else-if="isEmpty" class="justify-center align-content-center">
          <v-card class="d-flex justify-center align-content-end flex-wrap mt-5" flat tile min-height="500">
            <div id="imgContainer" class="text-center rounded-circle d-inline-flex align-center justify-center ma-3">
              <v-img src="../assets/JMT/JMTLogo.png" max-width="200px"></v-img>
            </div>
          </v-card>
          <v-card class="mt-10 d-flex justify-center align-content-center flex-wrap" flat tile min-height="50">
            <h3 cla ss="title mb-2">해당 그룹에서 작성된 노트가 존재하지 않습니다!</h3>
          </v-card>
          <v-card class="d-flex justify-center align-content-center flex-wrap" flat tile min-height="100">
            <h4>화상회의를 진행하시면서, 노트를 작성해보세요</h4>
          </v-card>
        </v-col>
        <div v-else>
          <EditorNoteList style="float : left;" :receivedNoteList="receivedNoteList" @onOpenNoteDetail="openNoteDetail" />
        </div>
      </v-row>
   </div>
  </v-row>
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
      isEmpty: false,
      noGroup: false,
    };
  },
  methods: {
    getGroupList() {
      const GROUP_URL = '/group/get/all/';
      
      axios
        .get(SERVER.URL + GROUP_URL + this.$store.state.userId)
        .then(res => {
          console.log('hihi', res.data.groups.length);
          if(res.data.groups.length == 0){
            this.noGroup = true;
          }
          this.groupList = res.data.groups;
          console.log(this.noGroup);
        })
        .catch(err => console.error(err));
    },
    getNoteList(groupId) {
      const URL = '/note/get/group/';
      
      if(groupId) {
        console.log('groupNo가 있을때');
        this.isEmpty = false;
      } else if (this.$route.query.groupNo){
        this.isEmpty = false;
        groupId = this.$route.query.groupNo;
      } else {
        this.initGetNoteList();
        this.isEmtpy = true;
        return;
      }

      axios
        .get(SERVER.URL + URL + groupId)
        .then(res => {
          this.receivedNoteList = res.data.notes;
          if (!this.receivedNoteList.length){
            this.isEmpty = true;
          }else{
            this.isEmpty = false;
          }
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
  #imgContainer{
    background-color: rgb(187, 201, 224);
    height: 300px;
    width: 300px;
  }
</style>