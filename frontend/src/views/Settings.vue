<template>
  <div style="height: 100%; width: 100%; padding-top: 64px;">
    <v-row style="margin: 0px; height: 100%; width: 100%;">
      <v-col class="box1" cols="3">
        <v-row style="height:100%;" align="start" justify="end">
          <v-btn-toggle mandatory style="background-color: rgb(52, 63, 87)">
            <v-col class="text-center" cols="12" sm="4">
              <div class="my-2">
                <v-btn class="select-btn" dark text x-large @click="toggle0">About JMT</v-btn>
              </div>
              <div class="my-2">
                <v-btn right class="select-btn" dark text x-large @click="toggle1">About Us</v-btn>
              </div>
            </v-col>
          </v-btn-toggle>
        </v-row>
      </v-col>
      <v-col cols="8"></v-col>
    </v-row>
  </div>
</template>

<script>
export default {
  name: 'Settings',
  data(){
    return {
      length: 2,
      onboarding: 0,
    };
  },
  methods: {
    toggle0 () {
      this.onboarding = 0;
    },
    toggle1 () {
      this.onboarding = 1;
    }
  }
}
</script>

<style>
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
    background: rgb(32, 43, 67);
    color: rgb(229, 235, 239);
    height: 100%;  
  }
  .select-btn{
    color: white;
    width: 510%;
  }
</style>