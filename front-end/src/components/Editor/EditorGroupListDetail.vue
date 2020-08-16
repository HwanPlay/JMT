<template>
  <div class="group-list">
      <ul>
        <li v-for="group in groupList" :key="group.id" v-text="group.groupName" @click="getNoteList(group.groupNo)"></li>
      </ul>
    </div>
</template>

<script>
export default {
  name: 'EditorGroupListDetail',
  props: {
    groupList: Array,
  },
  methods: {
    getNoteList(groupId) {
      this.$emit('onGetNoteList', groupId);
    }
  }
};
</script>

<style>

</style>