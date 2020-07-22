<template>
  <v-container fluid style="height: 100%; padding: 0px">
    <v-card>
      <v-row style="height: 100%">
        <v-col class="box1" cols="16">
          <v-tabs vertical>
            <v-tab class="box1">
              <!-- <v-icon left></v-icon> -->
              App 세팅
            </v-tab>
            <v-tab>
              <!-- <v-icon left></v-icon> -->
              개발자
            </v-tab>
          </v-tabs>
        </v-col>
        <v-col class="box2" cols="8">
          <div>
            <v-icon style="color: white">fas fa-globe-asia</v-icon>
          </div>
        </v-col>
      </v-row>
    </v-card>
  </v-container>
</template>

<script>
export default {

}
</script>

<style>
  v-col {
    height: 100%;
    padding: 0px;
  }
  .box1 {
    height: 100%;
    background-color: rgb(19, 28, 46);
    box-shadow: rgba(0, 0, 0, 0.1) 8px 0px 16px;

  }
  v-tab {
    height: 100%;
    background-color: rgb(19, 28, 46);
    box-shadow: rgba(0, 0, 0, 0.1) 8px 0px 16px;
  }
  .box2 {
    background: rgb(32, 43, 67);
    color: rgb(229, 235, 239);
    height: 100%;  
  }
</style>