<template>
  <b-col cols="3" style="padding: 0" class="rounded">
    <v-navigation-drawer height="100%" permanent>
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
            v-for="(group, i) in this.$store.state.myGroups"
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
      
    </v-navigation-drawer>

    <b-sidebar
      id="sidebar-backdrop"
      backdrop
      shadow
      no-header
      width="25rem"
    >
      <div class="px-3">
        <div style="height: 3rem;"></div>
        <h3 class="text-center">Notes</h3>
        <div class="row p-2">
          <b-button
            class="col-4 note-btn"
            v-b-toggle.sidebar-backdrop
            v-for="note in received_note_list"
            :key="note.noteNo"
            @click="getNoteHTML(note.noteNo)"
          >
            <h5>{{ note.title }}</h5>
            <span>{{compute_date(note.createdDate)}}</span>
          </b-button>
        </div>
      </div>
    </b-sidebar>
  </b-col>
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
.nav {
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
</style>