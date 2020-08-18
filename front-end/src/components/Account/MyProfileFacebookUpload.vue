<template>
  <v-file-input
  :class="input-white"
    v-model="files"
    placeholder="Upload your documents"
    label="File input"
    multiple
    prepend-icon="mdi-paperclip"
    color="white"
    loading="white"
    
  >
    <template v-slot:selection="{ text }">
      <v-chipF :class="input-white" loading="white" class="input-white" background-color="white" small label color="white">{{ text }}</v-chipF>
    </template>
  </v-file-input>
</template>

<script>
export default {
  data: () => ({
    files: [],
  }),
};
</script>

<style scoped>
.input-white{
  color: white;
}
div {
  color: white;
}
</style>