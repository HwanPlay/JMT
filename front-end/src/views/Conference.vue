<template>
  <div style="height: 100%; width: 100%;">
      <v-row>
        <v-col class="box1" cols="8">
          <videochat />
        </v-col>
        <v-col class="box1" cols="4">
          <NoteEditor />
        </v-col>
      </v-row>
  </div>
</template>

<script>
import Videochat from '../components/Conference/Videochat.vue';
import NoteEditor from '../components/Note/NoteEditor.vue';

export default {
  name: 'Conference',
  components: {
    Videochat,
    NoteEditor
  }
};
</script>

<style>

</style>