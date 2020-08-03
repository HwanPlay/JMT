<template>
  <div>
    <b-button v-b-toggle.GroupTitle.id>{{GroupTitle}}</b-button>



    <!-- 주석 -->

    <!-- Elements to collapse -->
    <b-collapse :id="GroupTitle.id" class="mt-2">
      <p>{{GroupTitle.date}}</p>
      <b-card v-for="note in subject" :key="note.id" v-text="note.text"></b-card>      
    </b-collapse>

    <b-collapse id="collapse-b" class="mt-2">
      <p>이게 날짜</p>
      <b-card>I am collapsible content A!</b-card>
      <b-card>I am collapsible content B!</b-card>
    </b-collapse>
  </div>
</template>

<script>
export default {
  name: 'NoteSearchGroupButton',
  props: {
    GroupTitle: Object,
  },
  data() {
      return {
          subject: this.GroupTitle.list
      }
  }
};
</script>

<style>
</style>