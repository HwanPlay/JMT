<template>
  <v-row style="height: 85%; width: 100%; margin-top:64px;">
    <!-- 좌츨 버튼 부분 -->
    <v-col cols="7" style="height: 100%;">
      <v-row style="height: 100%; margin-right: 60px;" align="center" justify="end">
        <v-col class="text-center" cols="12" sm="4">
          <div class="my-2">
            <v-btn class="rounded-xl" dark depressed style="height: 130px; width: 130px;" color="warning">
              <v-icon size="60">fas fa-video</v-icon>
            </v-btn>
          </div>
          <h5 style="margin-top: 20px;">새 회의</h5>
          <div class="my-2">
            <v-btn class="rounded-xl" dark depressed style="margin-top: 50px; height: 130px; width: 130px;" color="blue darken-1" @click="goToGroup">
              <v-icon size="60">fas fa-user-friends</v-icon>
            </v-btn>
          </div> 
          <h5 style="margin-top: 20px;">내 그룹</h5>
        </v-col>
        <v-col class="text-center" cols="12" sm="4">
          <div class="my-2">
            <v-btn class="rounded-xl" dark depressed style="height: 130px; width: 130px;" color="blue darken-1">
              <v-icon size="60">fas fa-plus-square</v-icon>
            </v-btn>
          </div>
          <h5 style="margin-top: 20px;">새 그룹</h5>
          <div class="my-2 underbox">
            <v-btn class="rounded-xl" dark depressed style="margin-top: 50px; height: 130px; width: 130px;" color="blue darken-1" @click="goToNote">
              <v-icon size="60">fas fa-file-alt</v-icon>
            </v-btn>
          </div>
          <h5 style="margin-top: 20px;">내 노트</h5>
        </v-col>
      </v-row>
    </v-col>

    <!-- 우측 시계 + 일정 버튼 -->
    <v-col cols="4" style="height: 90%; width: 100%;">
      <v-row style="height: 100%; width: 100%; margin-top: 20px;;" align="start" justify="center">

        <v-card class="mx-auto" width="100%">
          <v-img class="white--text align-end" height="200px" src="https://cdn.vuetifyjs.com/images/cards/docks.jpg">
            <h3>10 : 22</h3>
          </v-img>

          <h4><v-card-subtitle class="pb-0">현재 회의중</v-card-subtitle></h4>

          <v-card-text class="text--primary">
            <div>Whitehaven Beach</div>

            <div>Whitsunday Island, Whitsunday Islands</div>
          </v-card-text>

          <v-card-actions>
            <v-btn color="orange" text>Share</v-btn>

            <v-btn color="orange" text>Explore</v-btn>
          </v-card-actions>
        </v-card>

      </v-row>
    </v-col>
  </v-row>
</template>

<script>
export default {
  name: "Home",
  methods:{
    goToGroup(){
      this.$emit('goToGroup')
    },
    goToNote(){
      this.$emit('goToNote')
    }
  }
};
</script>

<style scoped>
v-col {
  height: 100%;
  padding: 0px;
}
.box1 {
  height: 100%;
  background-color: rgb(52, 63, 87);
  box-shadow: rgba(0, 0, 0, 0.1) 8px 0px 16px;
}
.blue-btns:hover{
  size: 110%;
}
</style>