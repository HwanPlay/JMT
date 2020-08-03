<template>
  <b-col cols="4">
    <div>
      <NoteSearchGroup v-for="GroupTitle in NoteList" :key="GroupTitle.id" />
    </div>
    
    <!-- 주석 -->
    <div>
      <!-- Via array of string IDs passed to directive value -->
      <b-button v-b-toggle=test>Toggle Collapse A and B</b-button>

      <!-- Elements to collapse -->
      <b-collapse id="collapse-a" class="mt-2">
        <p>이게 날짜</p>
        <b-card>I am collapsible content A!</b-card>
        <b-card>I am collapsible content A!</b-card>
        <b-card>I am collapsible content A!</b-card>
      </b-collapse>
      <b-collapse id="collapse-b" class="mt-2">
        <p>이게 날짜</p>
        <b-card>I am collapsible content A!</b-card>
        <b-card>I am collapsible content B!</b-card>
      </b-collapse>
    </div>

    <div>
      <b-button v-b-toggle.collapse-2 variant="primary">Toggle Collapse</b-button>
      <b-collapse id="collapse-2" class="mt-2">
        <b-card>
          <p class="card-text">Collapse contents Here</p>
          <b-card>Hello!</b-card>
          <b-card>Hello!</b-card>
          <b-card>Hello!</b-card>
        </b-card>
      </b-collapse>
    </div>
  </b-col>
</template>

<script>
import NoteSearchGroup from './NoteSearchGroup.vue';
export default {
  name: 'NoteSearch',
  components: {
    NoteSearchGroup,
  },
  props: {
    NoteList: Object,
  },
  methods: {},
  data() {
    return {
      test: ['collapse-a', 'collapse-b']
    };
  }
};
</script>

<style></style>
