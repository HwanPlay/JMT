<template>
  <div style="height: 100%; width: 100%; padding-top: 64px;">
    <v-row style="margin: 0px; height: 100%; width: 100%;">
      <v-col class="box1" cols="3">
        <v-row style="height:100%;" align="start" justify="end">
          <v-btn-toggle mandatory style="background-color: rgb(52, 63, 87)" v-model="icon">
            <v-col class="text-center" cols="12" sm="4">
              <div class="my-2">
                <v-btn class="select-btn" text x-large value="left">Profile</v-btn>
              </div>
              <div class="my-2">
                <v-btn right class="select-btn" text x-large value="center">Group</v-btn>
              </div>
              <div class="my-2">
                <v-btn right class="select-btn" text x-large value="right">Meeting</v-btn>
              </div>
              <div class="my-2">
                <v-btn class="select-btn" text x-large value="dis">Something</v-btn>
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
  .select-btn{
    color:white;
    width: 510%;
  }
</style>