<template>
  <b id="roundedBox">
    <div class="nav" id="NoteNav-box" cols="1">
      <v-navigation-drawer height="100%" permanent>
        <div id="myProfile">
          <div>
            <img id="myImage" :src="'http://joinmeeting.tk/images/'+this.$store.state.myPicture">
          </div>
          <v-list-item-content class="item-content">
            <v-list-item-title class="myName">
              <h5 style="color : white;">{{ $store.state.myName }}</h5>
            </v-list-item-title>
            <v-list-item-subtitle>
              <div id="userIdBoxNote" >
                <p >{{ $store.state.userId }}</p>
              </div>
            </v-list-item-subtitle>
          </v-list-item-content>
          
          <v-btn v-show="NoteListflag" small  color="primary" id="gotoGroupList" @click="gotoGroupList">그룹목록보기</v-btn>
        </div>
        <v-divider></v-divider>
        <div v-show="groupListflag">
          <v-list nav dense>
            <v-list-item-group active-class="border" color="orange">
              <div id="v-list-item-box">
                <v-list-item
                  v-for="group in group_list"
                  :key="group.id"
                  @click="getNoteList(group.groupNo)"
                  v-b-toggle.sidebar-backdrop
                >
                  <v-badge
                    v-if="$store.state.userId === group.hostId"
                    color="red"
                    dot
                    overlap
                    offset-x="25"
                    offset-y="15"
                  >
                    <v-list-item-icon>
                      <v-icon
                        dark
                        style="margin-top : 5px; margin-left : 10px;  margin-right : -15px;"
                      >mdi-account-multiple</v-icon>
                    </v-list-item-icon>
                  </v-badge>

                  <v-badge v-else color="rgb(0, 0, 0, 0)" dot overlap offset-x="25" offset-y="15">
                    <v-list-item-icon>
                      <v-icon
                        dark
                        style="margin-top : 5px; margin-left : 10px; margin-right : -15px;"
                      >mdi-account-multiple</v-icon>
                    </v-list-item-icon>
                  </v-badge>

                  <v-list-item-content style="width:90px;">
                    <p id="groupName" style="padding-top: 5px" v-text="group.groupName"></p>
                  </v-list-item-content>
                </v-list-item>
              </div>
            </v-list-item-group>
          </v-list>
        </div>

        <div v-show="NoteListflag">
          <v-list nav dense>
            <v-list-item-group active-class="border" color="orange">
              <div id="v-list-item-box">
                <v-list-item
                  v-for="note in received_note_list"
                  :key="note.noteNo"
                  @click="getNoteHTML(note.noteNo)"
                  v-b-toggle.sidebar-backdrop
                >
                  <v-badge
                    v-if="$store.state.userId === note.title"
                    color="red"
                    dot
                    overlap
                    offset-x="25"
                    offset-y="15"
                  >
                    <v-list-item-icon>
                      <v-icon
                        dark
                        style="margin-top : 5px; margin-left : 10px;  margin-right : -15px;"
                      >mdi-note-outline</v-icon>
                    </v-list-item-icon>
                  </v-badge>

                  <v-badge v-else color="rgb(0, 0, 0, 0)" dot overlap offset-x="25" offset-y="15">
                    <v-list-item-icon>
                      <v-icon
                        dark
                        style="margin-top : 5px; margin-left : 10px; margin-right : -15px;"
                      >mdi-note-outline</v-icon>
                    </v-list-item-icon>
                  </v-badge>

                  <v-list-item-content>
                    <p id="groupName" style="padding-top: 5px" v-text="note.note_title"></p>
                  </v-list-item-content>
                </v-list-item>
              </div>
            </v-list-item-group>
          </v-list>
        </div>
      </v-navigation-drawer>
      <div v-show="NoteContent" class="px-3">
        <h2 class="text-center" style="color:black; margin-left : 40px; margin-top:30px;">Note</h2>
        <div class="row p-2">
          <div class="row p-2" id="menu_box">
            <div
              class="wrapper"
              v-b-toggle.sidebar-backdrop
              v-for="note in received_note_list"
              :key="note.noteNo"
            >
              <div class="card btn" @click="getNoteHTML(note.noteNo)">
                <!-- <img src="https://images.unsplash.com/photo-1477666250292-1419fac4c25c?auto=format&amp;fit=crop&amp;w=667&amp;q=80&amp;ixid=dW5zcGxhc2guY29tOzs7Ozs%3D" /> -->
                <div class="cardContent">
                  <div id="cardTitle">
                    <h3 style=" color:black;">{{ note.note_title }}</h3>
                  </div>
                  <div id="cardDate">
                    <p style=" color:black;">{{compute_date(note.createdDate)}}</p>
                  </div>
                </div>
                <div class="cardInfo">
                  <!-- <h3>{{ note.title }}</h3>
                  <p>{{compute_date(note.createdDate)}}</p> -->
                  <p style="font-size : 40px;">자세히 보기</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div v-show="EditContent" id="EditBox">
        <NoteEditor
          id="NoteEditorBox"
          :group_list="group_list"
          @onDeleteNote="deleteNote"
          :noteObj="noteObj"
          @onSaveNote="saveNote"
        />
        <b-col cols="0.5"></b-col>
      </div>
    </div>
  </b>
</template>

<script>
import NoteEditor from '../../components/Note/NoteEditor';
import SERVER from '../../api/spring.js';

import axios from 'axios';
export default {
  name: 'NoteSearch',
  components: {
    NoteEditor
  },
  props: {
    propsNoteObj: Object,
  },
  watch: {
    propsNoteObj: {
      deep: true,
      handler() {
        this.noteObj = this.propsNoteObj;
        this.getNoteList(this.noteObj.groupNo);
        this.groupListflag = false;
        this.NoteListflag = true;
        this.NoteContent = false;
        this.EditContent = true;
      }
    }
  },
  methods: {
    get_group_list() {
      const FUNC_URL = '/group/get/all/';

      axios
        .get(SERVER.URL + FUNC_URL + this.USER_ID)
        .then(res => {
          this.group_list = res.data.groups;
        })
        .catch(err => console.error(err));
    },
    getNoteList(groupId) {
      const FUNC_URL = '/note/get/group/';
      // '/' + this.USER_ID
      axios
        .get(SERVER.URL + FUNC_URL + groupId)
        .then(res => {
          console.log(res);
          this.received_note_list = res.data.notes;
        })
        .catch(err => console.error(err));
      this.groupId = groupId;
    },

    getNoteHTML(NoteId) {
      console.log(NoteId);
      
      if (this.$route.params.NoteId_Cal) {
        NoteId = this.$route.params.NoteId_Cal;
      } else if (NoteId === undefined) {
        return;
      }

      const URL_getNoteByNo = '/note/getno/';

      axios
        .get(SERVER.URL + URL_getNoteByNo + NoteId)
        .then(res => {
          console.log('이거 시작');
          console.log(res.data);
          this.noteObj.Content = res.data.content;
          this.noteObj.Id = res.data.noteNo;
          this.noteObj.Title = res.data.note_title;

        })
        .catch(err => {
          console.error(err);
          this.noteContent = 'axios Error is occured';
        });
      this.groupListflag = false;
      this.NoteListflag = true;
      this.NoteContent = false;
      this.EditContent = true;
    },
    saveNote(noteObj) {
      const URL_saveNote = '/note/';

      axios
        .put(SERVER.URL + URL_saveNote + noteObj.Id, {
          title: noteObj.Title,
          content: noteObj.Content
        })
        .then(res => {
          this.alertFlag = !this.alertFlag;
          this.alertMessage = 'Save!';
          console.log('title:', noteObj.Title);
          console.log('Content:', noteObj.Content);
        })
        .catch(err => console.error(err));
      this.getNoteList(this.groupId);
    },
    deleteNote(noteId) {
      const URLDeleteNote = '/note/delno/';
      axios
        .delete(SERVER.URL + URLDeleteNote + noteId)
        .then(res => {
          this.alertFlag = !this.alertFlag;
          this.alertMessage = 'Delete!';
          console.log(res);
          console.log('delete note' + noteId);
        })
        .catch(err => console.error(err));
      this.getNoteList(this.groupId);
      
    },
    compute_date(date) {
      return date.slice(2, 10);
    },
    gotoGroupList(){
      this.groupListflag = true;
      this.NoteListflag = false;
      this.NoteContent = true;
      this.EditContent = false;
      console.log(this.groupId);
      this.getNoteList(this.groupId);
    }
  },
  mounted() {
    this.get_group_list();

  },

  data() {
    return {
      groupListflag: true,
      NoteListflag: false,
      NoteContent: true,
      EditContent: false,
      variant: 'dark',
      groupId: '',
      group_list: [],
      received_note_list: [],
      USER_ID: this.$store.state.userId,
      noteObj: {
        Content: '',
        Id: 0,
        Title: ''
      },
    };
  }
};
</script>
<style >
#roundedBox {
  margin-left: -15px;
}
#NoteNav-box {
  margin-left: 15px;
  height: 100%;
  padding: 0;
}
.v-navigation-drawer__content {
  background-color: rgb(52, 63, 87);
}
#myProfile {
  width: 100%;
  text-align: center;
}
#myImage {
  margin-top: 20px;
  display: inline-block;
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 50%;
}
#groupName {
  color: white;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 40px;
}
.note-btn:hover {
  color: red !important;
  border: 1px solid red;
  background: red !important;
}

“Segoe UI”,
Roboto,
Helvetica,
Arial,
sans-serif,
*,
*:before,
*:after {
  box-sizing: inherit;
  padding: 0;
  margin: 0;
}
.px-3 {
  font-family: -apple-system, BlinkMacSystemFont;
  padding: 0 10px;
  box-sizing: border-box;
  height: 100%;
  /* background-image: linear-gradient(-20deg, #a592e4 0%, #3e5085 100%, #3133a0 100%); */
  background-color: white;
}
#menu_box {
  margin-left: 10px;
  position: absolute;
  width: 80%;
  height: 85%;
  overflow-y: auto;
}

.wrapper {
  display: -webkit-box;
  display: flex;
  height: 300px;
  margin-left: 30px;
  justify-content: space-around;
}
#userIdBoxNote{
  display: inline-block;
  word-wrap: break-word;
}

#userIdBoxNote p {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 200px;
  height: 20px;
  font-size: 16px;
  color: white;
  font-weight:400;
}
 .card {
  
  text-align: center;
  float: left;
  margin-right: 20px;
  width: 150px;
  height: 240px;
  border-radius: 15px;
  padding: 1.5rem;
  background: white;
  position: relative;
  display: -webkit-box;
  display: flex;
  -webkit-box-align: end;
  align-items: flex-end;
  -webkit-transition: 0.4s ease-out;
  transition: 0.4s ease-out;
  box-shadow: 0px 7px 10px rgba(0, 0, 0, 0.5);
  
}

.card:hover {
  -webkit-transform: translateY(20px);
  transform: translateY(20px);
  
 
}
.card:hover .cardContent {
  opacity: 0;
  
}

.card:hover:before {
  opacity: 1;
  background-color: #355070;
}

.card:hover .cardInfo {
  opacity: 1;
  -webkit-transform: translateY(0px);
  transform: translateY(0px);
  position: absolute;
  float: left;
  top: 50px;
  left: 2px;
}

.card:before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  display: block;
  width: 100%;
  height: 100%;
  border-radius: 15px;
  background: rgba(0, 0, 0, 0.6);
  z-index: 2;
  -webkit-transition: 0.5s;
  transition: 0.5s;
  opacity: 0;
}

.card img {
  width: 100%;
  height: 100%;
  -o-object-fit: cover;
  object-fit: cover;
  position: absolute;
  top: 0;
  left: 0;
  border-radius: 15px;
}

.card .cardInfo {
  position: absolute;
  z-index: 3;
  color: white;
  opacity: 0;
  -webkit-transform: translateY(30px);
  transform: translateY(30px);
  -webkit-transition: 0.5s;
  transition: 0.5s;
  top: 50px;
  left: 2px;
}

.card .cardInfo h1 {
  margin: 0px;
}

.card .cardInfo p {
  letter-spacing: 1px;
  font-size: 15px;
  margin-top: 8px;
  top: 50px;
  left: 2px;
}

.cardContent {
  margin-top: 40px;
  width: 100%;
  display: inline-block;
  text-align: center;
  color: white;
  z-index: 3;
}
#cardTitle {
  height: 100px;
}
#cardTitle h3 {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2; /* 라인수 */
  -webkit-box-orient: vertical;
  word-wrap: break-word;
  line-height: 1.2em;
  height: 2.4em; /* line-height 가 1.2em 이고 3라인을 자르기 때문에 height는 1.2em * 3 = 3.6em */
}

#EditBox {
  position: absolute;
  margin-top: 60px;
  left: 280px;
  width: 75%;
}
</style>