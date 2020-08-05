<template>
  <div style="height: 100%; width: 100%;">
    <div class="mainCoference">
      <div class="main_1 main_common">
        <videochat @Videochat="noteoff" />
      </div>
      <div class="main_2 main_common">
        <div class="NoteDiv" style="display:none;">
            <NoteEditor />
        </div>
      </div>
      <div class="main_3 main_common">test3</div>
  </div> 
  </div>
</template>

<script>
import Videochat from '../components/Conference/Videochat.vue';
import NoteEditor from '../components/Note/NoteEditor.vue';
import $ from 'jquery';
export default {
  name: 'Conference',
  components: {
    Videochat,
    NoteEditor
  },
  data() {
    return {
      noteBool : true, 
    };
  },
  methods: {
    noteoff() {
      if(this.noteBool == false){
        $('.NoteDiv').hide();
      }
      else{
        $('.NoteDiv').show();
      }
      this.noteBool = !this.noteBool;
      console.log(this.noteBool);
    }
  },
};
</script>

<style>
.mainCoference{
    background-color: yellow;
    border: 1px solid red;
    width: 100%;
    height: 100%;
    position: relative;
}
   
.main_1{
width: 75%;
float: left;
background-color: orangered;
position: relative;
}
.main_3{
width: 25%;
float: left;
background-color: rebeccapurple;
position: relative;
}

</style>