<template>
  <div class="rounded text-monospace p-2">
    
    <div v-for="group in group_list" :key="group.groupNo">
      <div class="rounded group_btn">
        <b-button class="text-left" block variant="white" @click="getNoteList(group.groupNo)" v-b-toggle.sidebar-backdrop> <h3>{{group.groupName}}</h3></b-button>
      </div>
      <!-- <hr align="left" class="group_hr"> -->
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
        <h3 class="text-center" style="color:white;">Note</h3>
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
  </div>
</template>

<script>
export default {
  name: 'NoteSearchSidebar',
  props: {
    group_list: Array,
    received_note_list: Array,
  },
  data() {
    return {
      variant: 'dark'
    };
  },
  methods: {
    getNoteList(groupId) {
      this.$emit('onGetNoteList', groupId);
    },
    getNoteHTML(NoteId){
      this.$emit('onGetNoteHTML', NoteId);
    },
    compute_date(date) {
      return date.slice(2,10);
    }
  }
};
</script>

<style>
.b-sidebar-header{
  background-color: #45568a;
}
.group_hr {
  margin: 0.3rem auto 0.3rem 0 !important;
  border-radius: 5px !important;
  width: 50% !important;
  color:white !important;
  background-color:white !important;
}
.group_btn {
  margin: 0.3rem auto 0.3rem 0 !important;
  border-radius: 5px !important; 
}
.group_btn:hover {
  background-color: #f8f9fa;
}
.col-4{
  height: 200px;
  margin-bottom: 5px;
  background-color: rgba( 255, 255, 255, 0 );
  border: 0;
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