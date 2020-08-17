<template>
  <!-- <v-col cols="12" @click="openNoteDetail({'noteNo':note.noteNo, 'groupNo': note.groupNo})">
    <div>{{note.note_title}}</div>
    <div>{{note.createdDate}}</div>
  </v-col> -->
        <div style="height: 300px; margin-left:40px; margin-right: 40px;">

              <div class="card btn" style="margin-left:30px;" @click="openNoteDetail({'noteNo':note.noteNo, 'groupNo': note.groupNo})">
                <!-- <img src="https://images.unsplash.com/photo-1477666250292-1419fac4c25c?auto=format&amp;fit=crop&amp;w=667&amp;q=80&amp;ixid=dW5zcGxhc2guY29tOzs7Ozs%3D" /> -->
                <div class="cardContent">
                  <div id="cardTitle">
                    <h3 style=" color:black;">{{ note.note_title }}</h3>
                  </div>
                  <div id="cardDate">
                    <p style=" color:black;">{{note.createdDate | moment('YY-MM-DD')}}</p>
                    <p style=" color:black; margin-top:-10px;">{{note.createdDate | moment('HH:mm:ss')}}</p>
                  </div>
                </div>
                <div class="cardInfo">
                  <!-- <h3>{{ note.title }}</h3>
                  <p>{{compute_date(note.createdDate)}}</p> -->
                  <p style="font-size : 22px;">자세히 보기</p>
                </div>
              </div>
            </div>

</template>

<script>
export default {
  name: 'EditorNoteListItems',
  props: {
    note: Object
  },
  methods: {
    openNoteDetail({noteNo, groupNo}) {
      this.$emit('onOpenNoteDetail', {noteNo, groupNo});
    }
  }
};
</script>

<style>
#menu_box {
  margin-left: 10px;
  position: absolute;
  width: 25%;
  height: 40%;
  overflow-y: auto;
}
.card {
  
  text-align: center;
  float: left;
  margin-right: 20px;
  width: 150px;
  height: 240px;
  border-radius: 15px;
  padding: 1.5rem;
  background: white;
  position: relative;
  display: -webkit-box;
  display: flex;
  -webkit-box-align: end;
  align-items: flex-end;
  -webkit-transition: 0.4s ease-out;
  transition: 0.4s ease-out;
  box-shadow: 0px 7px 10px rgba(0, 0, 0, 0.5);
  
}
.card:hover {
  -webkit-transform: translateY(20px);
  transform: translateY(20px);
  
 
}
.card:hover .cardContent {
  opacity: 0;
  
}
.card:hover:before {
  opacity: 1;
  background-color: #355070;
}
.card:hover .cardInfo {
  opacity: 1;
  -webkit-transform: translateY(0px);
  transform: translateY(0px);
  position: absolute;
  float: left;
  top: 110px;
  left: 15px;
}
.card:before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  display: block;
  width: 100%;
  height: 100%;
  border-radius: 15px;
  background: rgba(0, 0, 0, 0.6);
  z-index: 2;
  -webkit-transition: 0.5s;
  transition: 0.5s;
  opacity: 0;
}
.cardContent {
  margin-top: 40px;
  width: 100%;
  display: inline-block;
  text-align: center;
  color: white;
  z-index: 3;
}
.card .cardInfo {
  position: absolute;
  z-index: 3;
  color: white;
  opacity: 0;
  -webkit-transform: translateY(30px);
  transform: translateY(30px);
  -webkit-transition: 0.5s;
  transition: 0.5s;
  top: 110px;
  left: 15px;
}

#cardTitle {
  height: 100px;
}
#cardTitle h3 {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2; /* 라인수 */
  -webkit-box-orient: vertical;
  word-wrap: break-word;
  line-height: 1.2em;
  height: 2.4em; /* line-height 가 1.2em 이고 3라인을 자르기 때문에 height는 1.2em * 3 = 3.6em */
}
</style>