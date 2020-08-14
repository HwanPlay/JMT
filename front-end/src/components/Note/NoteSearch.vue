<template>
  <b id="roundedBox">
    <!-- <v-navigation-drawer height="100%" permanent>
      <div id="myProfile">
        <div>
          <img id="myImage" src="../../assets/profile/profile1.jpg" />
        </div>
        <v-list-item-content class="item-content">
          <v-list-item-title class="myName">
            <h2 style="color : white">{{ $store.state.myName }}</h2>
          </v-list-item-title>
          <v-list-item-subtitle>
            <h5 style="color : white">{{ $store.state.userId }}</h5>
          </v-list-item-subtitle>
        </v-list-item-content>
      </div>
      <v-divider></v-divider>

      <v-list nav dense>
        <v-list-item-group active-class="border"  color="orange">
          <v-list-item
            v-for="(group, i) in group_list"
            :key="i"
            @click="getNoteList(group.groupNo)"
            v-b-toggle.sidebar-backdrop
          >
            <v-list-item-icon>
              <v-icon style="margin-top : 5px; margin-left : 10px;  margin-right : -15px;">mdi-account-multiple</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <h4 id="groupNameText" style="left : -30px;" v-text="group.groupName"></h4>
            </v-list-item-content>
          </v-list-item>
        </v-list-item-group>
      </v-list>
      
    </v-navigation-drawer> -->
      <div class="nav" id="NotNav-box" cols="2">
      <v-navigation-drawer height="100%" permanent>
        <div id='myProfile'>
          <div>
          <img id="myImage" src="../../assets/profile/profile1.jpg">
          </div>
               <v-list-item-content class="item-content">
                <v-list-item-title class="myName">
                  <h5>{{ $store.state.myName }}</h5>
                </v-list-item-title>
                <v-list-item-subtitle>
                  <div id="userIdBox">
                    <p>{{ $store.state.userId }}</p>
                  </div>
                </v-list-item-subtitle>
              </v-list-item-content> 
        </div>
        <v-divider></v-divider>
        <v-list nav dense>
          <v-list-item-group  v-model="group" active-class="border"  color="orange">
            <div id="v-list-item-box" >
            <v-list-item
            v-for="(group, i) in group_list"
            :key="i"
            @click="getNoteList(group.groupNo)"
            v-b-toggle.sidebar-backdrop
          >
              <v-badge v-if="$store.state.userId === group.hostId" color="red" dot overlap offset-x="25" offset-y="15">
                <v-list-item-icon>
                  <v-icon dark style="margin-top : 5px; margin-left : 10px;  margin-right : -15px;">mdi-account-multiple</v-icon>
                </v-list-item-icon>
              </v-badge>
          
              <v-badge v-else color="rgb(0, 0, 0, 0)" dot overlap offset-x="25" offset-y="15">
                <v-list-item-icon>
                  <v-icon dark style="margin-top : 5px; margin-left : 10px; margin-right : -15px;">mdi-account-multiple</v-icon>
                </v-list-item-icon>
              </v-badge>

              <v-list-item-content>
                <p id="groupNameText" style="padding-top: 5px" v-text="group.groupName"></p>
              </v-list-item-content>
            </v-list-item>
            </div>
          </v-list-item-group>
          


        </v-list>
      </v-navigation-drawer>
    </div>

    <b-sidebar
      id="sidebar-backdrop"
      :backdrop-variant="variant"
      backdrop
      shadow
      no-header
      width=25rem 
    >
      <div class="px-3">
        <div style="height: 3rem;"></div>
        <h3 class="text-center">Note</h3>
        <div class="row p-2">

          <!-- <b-button class="col-4" 
          v-b-toggle.sidebar-backdrop 
          v-for="note in received_note_list" :key="note.noteNo" 
           @click="getNoteHTML(note.noteNo)" > -->
          <div class="row p-2" id="menu_box">

          <div class="menu__container" v-b-toggle.sidebar-backdrop  v-for="note in received_note_list" :key="note.noteNo" 
           @click="getNoteHTML(note.noteNo)">
            <div class="menu">{{ note.title }}</div>
            <span style="color : white;">{{compute_date(note.createdDate)}}</span>
          </div>
          </div>
            <!-- <h5>{{ note.title }}</h5>
            <span>{{compute_date(note.createdDate)}}</span> -->
          <!-- </b-button> -->
        </div>
      </div>
    </b-sidebar>
  </b>
</template>

<script>
export default {
  name: 'NoteSearch',
  components: {},
  props: {
    group_list: Array,
    received_note_list: Array,
  },
  methods: {
    getNoteList(groupId) {
      this.$emit('onGetNoteList', groupId);
    },
    getNoteHTML(NoteId) {
      this.$emit('onGetNoteHTML', NoteId);
    },
    compute_date(date) {
      return date.slice(2,10);
    }
  },
  data() {
    return {};
  },
};
</script>
<style >
#roundedBox{
  margin-left: -15px;
}
#NotNav-box {
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
.groupNameText {
  color: white;
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
  font-family: -apple-system, BlinkMacSystemFont,;
  padding: 0 10px;
  box-sizing: border-box;
  height: 100%;
  background-image: linear-gradient(-20deg, #a592e4 0%, #3e5085 100%, #3133a0 100%);
}
#menu_box{
  position:absolute;
  top: -50px;
  width: 100%;
  
}

.menu__container {
  
  width: 90px;
  height: 120px;
  margin-left: 24px;
  margin-right: 30px;
  margin-top: -20px;
  margin-bottom: -100px;
  border-radius: 5px;
  cursor: pointer;
  box-shadow: 0 10px 40px 2px rgba(0,0,0,0.4);
  background:rgb(255, 255, 255);
  transform-origin: center center 0px;
  transition: all 0.5s ease-out;
  animation-name: animate;
  animation-duration: 3s;
  animation-iteration-count: infinite;
  transform: perspective(1000px) rotateX(35deg) rotateY(4deg) rotateZ(-30deg);
  z-index: 1;
}
.menu__container:hover {
  animation: unset;
  margin-top: 18vh;
  transform: perspective(1000px) rotateX(0deg) rotateY(0deg) rotateZ(0deg);
}
.menu__container .menu {
  display: flex;
  justify-content: space-around;
  align-items: center;
  max-width: 100%;
  height: 100%;
  color: rgb(0, 0, 0);
}

@-moz-keyframes animate {
  0% {
    margin-top: 20vh;
  }
  50% {
    margin-top: 18vh;
  }
  100% {
    margin-top: 20vh;
  }
}
@-webkit-keyframes animate {
  0% {
    margin-top: 20vh;
  }
  50% {
    margin-top: 18vh;
  }
  100% {
    margin-top: 20vh;
  }
}
@-o-keyframes animate {
  0% {
    margin-top: 20vh;
  }
  50% {
    margin-top: 18vh;
  }
  100% {
    margin-top: 20vh;
  }
}
@keyframes animate {
  0% {
    margin-top: 20vh;
  }
  50% {
    margin-top: 18vh;
  }
  100% {
    margin-top: 20vh;
  }
}
</style>