<template>
  <v-row class='fill-height'>
    <v-col>
      <v-sheet height='64'>
        <v-toolbar flat color='white'>
          <v-btn outlined class='mr-4' color='grey darken-2' @click='setToday'>Today</v-btn>

          <v-spacer></v-spacer>
          
          <v-btn fab text small color='grey darken-2' @click='prev'>
            <v-icon small>mdi-chevron-left</v-icon>
          </v-btn>
          <v-toolbar-title v-if='$refs.calendar'>{{ $refs.calendar.title }}</v-toolbar-title>
          <v-btn fab text small color='grey darken-2' @click='next'>
            <v-icon small>mdi-chevron-right</v-icon>
          </v-btn>
          
          <v-spacer></v-spacer>
          
          <v-menu bottom right>
            <template v-slot:activator='{ on, attrs }'>
              <v-btn outlined color='grey darken-2' v-bind='attrs' v-on='on'>
                <span>{{ typeToLabel[type] }}</span>
                <v-icon right>mdi-menu-down</v-icon>
              </v-btn>
            </template>
            <v-list>
              <v-list-item @click="type = 'day'">
                <v-list-item-title>Day</v-list-item-title>
              </v-list-item>
              <v-list-item @click="type = 'week'">
                <v-list-item-title>Week</v-list-item-title>
              </v-list-item>
              <v-list-item @click="type = 'month'">
                <v-list-item-title>Month</v-list-item-title>
              </v-list-item>
              <v-list-item @click="type = '4day'">
                <v-list-item-title>4 days</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </v-toolbar>
      </v-sheet>
      <v-sheet height='600'>
        <v-calendar
          ref='calendar'
          v-model='focus'
          color='primary'
          :events='events'
          :event-color='getEventColor'
          :type='type'
          @click:event='showEvent'
          @click:more='viewDay'
          @click:date='viewDay'
          @change='updateRange'
        ></v-calendar>
        <v-menu
          v-model='selectedOpen'
          :close-on-content-click='false'
          :activator='selectedElement'
          offset-x
        >
          <v-card color='grey lighten-4' min-width='350px' flat>
            <v-toolbar :color='selectedEvent.color' dark>
              <v-toolbar-title v-html='selectedEvent.name'></v-toolbar-title>
              <v-spacer></v-spacer>
                <router-link :to="{ name: 'Note', params: { NoteId_Cal: selectedEvent.id }}">
                  <v-btn icon>
                    <v-icon>mdi-pencil</v-icon>
                  </v-btn>
                </router-link>
                <!-- <v-icon>mdi-dots-vertical</v-icon> -->
                <v-btn icon @click='selectedOpen = false'>
                  <v-icon>mdi-close-box-outline</v-icon>
                </v-btn>
            </v-toolbar>
            <!-- <v-card-text>
              <span v-html='selectedEvent'></span>
              <p v-html='selectedEvent.start'></p>
              <p v-html='selectedEvent.end'></p>
            </v-card-text> -->
            <!-- <v-card-actions>
              <v-btn text color='secondary' @click='selectedOpen = false'>Cancel</v-btn>
            </v-card-actions> -->
          </v-card>
        </v-menu>
      </v-sheet>
    </v-col>
  </v-row>
</template>

<script>
import SERVER from '../../api/spring.js';
import axios from 'axios';
export default {
  data: () => ({
    focus: '',
    type: 'month',
    typeToLabel: {
      month: 'Month',
      week: 'Week',
      day: 'Day',
      '4day': '4 Days',
    },
    selectedEvent: {},
    selectedElement: null,
    selectedOpen: false,
    events: [],
    colors: [
      'blue',
      'indigo',
      'deep-purple',
      'cyan',
      'green',
      'orange',
      'grey darken-1',
    ],
  }),
  mounted() {
    this.$refs.calendar.checkChange();
  },
  methods: {
    viewDay({ date }) {
      this.focus = date;
      this.type = 'day';
    },
    getEventColor(event) {
      return event.color;
    },
    setToday() {
      this.focus = '';
    },
    prev() {
      this.$refs.calendar.prev();
    },
    next() {
      this.$refs.calendar.next();
    },
    showEvent({ nativeEvent, event }) {
      const open = () => {
        this.selectedEvent = event;
        this.selectedElement = nativeEvent.target;
        setTimeout(() => (this.selectedOpen = true), 10);
      };

      if (this.selectedOpen) {
        this.selectedOpen = false;
        setTimeout(open, 10);
      } else {
        open();
      }

      nativeEvent.stopPropagation();
    },
    updateRange({ start, end }) {
      const events = [];

      const groupIds = [];
      const noteList = [];
      const calendarData = [];
      
      axios
        .get(SERVER.URL + '/group/get/all/' + this.$store.state.userId)
        .then((res) => {
          // console.log('res:');
          // console.log(res.data.groups);
          res.data.groups.forEach(element=>{
            
            // console.log(element);
            groupIds.push(element.groupNo);
            const axios_note = axios.get(SERVER.URL +'/note/get/group/'+element.groupNo);

            noteList.push(axios_note);
          });
          axios.all(noteList).then(axios.spread((...res)=>{
            res.forEach(ele=>{
              // console.log('ele:',ele);
              ele.data.notes.forEach(note => {
                // console.log('note:');
                // console.log(note);
                calendarData.push({
                  name: note.title,
                  start: note.meeting_start_time,
                  end: note.meeting_end_time,
                  color: this.colors[this.rnd(0, this.colors.length - 1)],
                  timed: false,
                  id: note.noteNo
                });
              });
            });
          })).catch(err=>console.error(err));
        });
      this.events = calendarData;
    },
    rnd(a, b) {
      return Math.floor((b - a + 1) * Math.random()) + a;
    },
  },
};
</script>