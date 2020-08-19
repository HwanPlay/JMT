<template>
  <!-- <ul>
    <li v-for="note in noteList" :key="note.noteNo" v-text="note.note_title"
      @click="getNoteHTML( {noteNo:note.noteNo, groupNo:note.groupNo} )"
    ></li>
  </ul> -->
      <v-list nav dense id="NoteItemBox" style="padding:0;">
            <v-list-item-group active-class="border" color="orange">
              <div id="v-list-item-box">
                <v-list-item
                  v-for="note in noteList"
                  :key="note.noteNo"
                  @click="getNoteHTML({noteNo:note.noteNo, groupNo:note.groupNo})"
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
                    <p id="NoteName" style="padding-top: 5px" v-text="note.note_title"></p>
                  </v-list-item-content>
                </v-list-item>
              </div>
            </v-list-item-group>
          </v-list>
</template>

<script>
export default {
  name: 'EditorDetailSideBarNoteList',
  props: {
    noteList:Array,
  },
  methods: {
    getNoteHTML({noteNo, groupNo}) {
      if (this.$route.query.noteNo === noteNo) {
        return;
      }
      // this.$emit('onGetNoteHTML', noteNo);
      this.$router.push({name: 'EditorDetail', query: {noteNo: noteNo, groupNo: groupNo}});
    }
  }
};
</script>

<style>
#v-list-item-box {
  background-color: rgb(52, 63, 87);
  margin-left: 10px;
  margin-right: 10px;
  
}
#NoteItemBox {
  background-color: rgb(52, 63, 87);
  height:400px;
  overflow: auto;
}
#NoteName{
  color: white;
}
</style>