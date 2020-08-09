<template>
  <!-- <div style="height: 100%; width: 100%;"> -->
    <v-row style="margin: 0px; height: 100%; width: 100%;">
      <v-col class="box1" cols="2" style="padding: 12px 0px 12px 0px;">
        <v-col cols="12" style="background-color: rgb(52, 63, 87); padding: 12px 0px 12px 0px;">
          <div  class="y-col" v-if="!!this.$store.state.myGroups">
            <div
              style="width: 100%;"
              class="my-4"
              v-for="(group, i) in this.$store.state.myGroups"
              :key="i"
            >
              <v-btn
                class="select-btn"
                style="outline: none; width: 100%;"
                @click="toggle(i)"
                x-large
                text
                value="left"
              >
                <h3>{{ group.groupName.substring(0, 9) }}</h3>
              </v-btn>
            </div>
          </div>
          <v-btn
            class="select-btn"
            style="outline: none; width: 100%;"
            @click="modalOn=true"
            x-large
            text
            value="left"
          >
            <h3>그룹 추가</h3>
          </v-btn>
          <v-dialog v-model="modalOn" max-width="500px">
            <createGroup @close="closeModal" />
          </v-dialog>
        </v-col>
      </v-col>
      <v-col class="box2" cols="10" style="padding: 0px;">
        <!-- <v-img style="height: 101%; margin-top: -1px;" :src="require('./background.jpg')"> -->
        <Group1 :groupInfo="this.$store.state.myGroups[this.onboarding]" />
        <!-- </v-img> -->
      </v-col>
    </v-row>
  <!-- </div> -->
</template>

<script>
import Group1 from '../components/Group/Group1.vue';
import createGroup from '../components/Group/createGroup.vue';

export default {
  name: 'Groups',
  components: {
    Group1,
    createGroup,
  },
  data() {
    return {
      modalOn: false,
      onboarding: 0,
    };
  },
  methods: {
    toggle(i) {
      this.onboarding = i;
    },
    createGroup() {
      this.$store.state.groupModalOn = true;
      console.log(this.$store.state.groupModalOn);
    },
    closeModal() {
      this.modalOn = false;
    },
  },
  mounted() {
    this.$store.dispatch('getGroupInfo');
  },
};
</script>

<style scoped>
v-col {
  height: 100%;
  padding: 0px;
}
.y-col{
  overflow-y: scroll;
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