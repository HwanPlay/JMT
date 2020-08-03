<template>
  <div style="height: 100%; width: 100%;">
    <v-row style="margin: 0px; height: 100%; width: 100%; padding-top: 64px;">
      <v-col class="box1" cols="3">
        <v-row style="height:100%;" align="start" justify="end">
          <v-btn-toggle mandatory style="background-color: rgb(52, 63, 87)">
            <v-col class="text-center" cols="12" sm="4">
              <div class="my-2">
                <v-btn class="select-btn" text x-large @click="toggle0" value="left">Profile</v-btn>
              </div>
              <div class="my-2">
                <v-btn right class="select-btn" text @click="toggle1" x-large value="center">Group</v-btn>
              </div>
              <div class="my-2">
                <v-btn right class="select-btn" text @click="toggle2" x-large value="right">Meeting</v-btn>
              </div>
              <div class="my-2">
                <v-btn class="select-btn" text @click="toggle3" x-large value="dis">Something</v-btn>
              </div>
            </v-col>
          </v-btn-toggle>
        </v-row>
      </v-col>
      <v-col class="box2" cols="9" style="padding: 0px;">
        <!-- <v-img style="height: 101%; margin-top: -1px;" :src="require('./background.jpg')"> -->
        <v-window v-model="onboarding" vertical style="width: 100%; height: 100%">
          <v-window-item v-for="n in length" :key="`card-${n}`" style="height: 100%;">
            <v-row class="fill-height" align="center" justify="center" style="margin: 0px;">
              <div v-if="n==1">
                <MyProfile />
              </div>
              <div v-else-if="n==2">
                <Group />
              </div>
              <div v-else-if="n==3">
                <Meeting />
              </div>
              <div v-else>
                <Something />
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
import MyProfile from '../components/Group/MyProfile.vue';
import Group from '../components/Group/Group.vue';
import Something from '../components/Group/Something.vue';
import Meeting from '../components/Group/Meeting.vue';

export default {
  name: 'Information',
  components: {
    MyProfile,
    Group,
    Meeting,
    Something
  },
  data () {
    return {
      length: 4,
      onboarding: 0
    };
  },
  methods: {
    toggle0 () {
      this.onboarding = 0;
    },
    toggle1 () {
      this.onboarding = 1;
    },
    toggle2 () {
      this.onboarding = 2;
    },
    toggle3 () {
      this.onboarding = 3;
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
.box2 {
  height: 100%;
}
.select-btn {
  color: white;
  width: 510%;
}
</style>
