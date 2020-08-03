<template>
  <div style="height: 100%; width: 100%;">
    <v-row style="margin: 0px; height: 100%; width: 100%; padding-top: 64px;">
      <v-col class="box1" cols="2">
        <v-row style="height:100%; width: 100%;" align="start" justify="end">
          <v-btn-toggle mandatory style="background-color: rgb(52, 63, 87)">
            <v-col class="text-center" cols="12" sm="5">
              <div class="my-2">
                <v-btn class="select-btn" style="outline: none;" text x-large @click="toggle0" value="left">Group1</v-btn>
              </div>
              <div class="my-2">
                <v-btn right class="select-btn" style="outline: none;" text @click="toggle1" x-large value="center">Group2</v-btn>
              </div>
              <div class="my-2">
                <v-btn right class="select-btn" style="outline: none;" text @click="toggle2" x-large value="right">Group3</v-btn>
              </div>
              <div class="my-2">
                <v-btn class="select-btn" style="outline: none;" text @click="toggle3" x-large value="dis">Group4</v-btn>
              </div>
            </v-col>
          </v-btn-toggle>
        </v-row>
      </v-col>
      <v-col class="box2" cols="10" style="padding: 0px;">
        <!-- <v-img style="height: 101%; margin-top: -1px;" :src="require('./background.jpg')"> -->
        <v-window v-model="onboarding" vertical style="width: 100%; height: 100%">
          <v-window-item v-for="n in length" :key="`card-${n}`" style="height: 100%;">
            <v-row class="fill-height" style="margin: 0px; width: 100%;">
              <div style="width: 100%; margin-left: 15px;" v-if="n==1">
                <Group1 />
              </div>
              <div style="width: 100%;" v-else-if="n==2">
                <Group2 />
              </div>
              <div style="width: 100%" v-else-if="n==3">
                <Group3 />
              </div>
              <div style="width: 100%" v-else>
                <Group4 />
              </div>
            </v-row>
          </v-window-item>
        </v-window>
        <!-- </v-img> -->
      </v-col>
    </v-row>
  </div>
</template>

<script>
import Group1 from '../components/Group/Group1.vue';
import Group2 from '../components/Group/Group2.vue';
import Group3 from '../components/Group/Group3.vue';
import Group4 from '../components/Group/Group4.vue';

export default {
  name: 'Information',
  components:{
    Group1,
    Group2,
    Group3,
    Group4,
  },
  data() {
    return {
      length: 4,
      onboarding: 0,
    };
  },
  methods: {
    toggle0(){
      this.onboarding = 0;
    },
    toggle1(){
      this.onboarding = 1;
    },
    toggle2(){
      this.onboarding = 2;
    },
    toggle3(){
      this.onboarding = 3;
    },
  },
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
.box2 {
  height: 100%;
}
.select-btn {
  color: white;
  width: 510%;
}
</style>