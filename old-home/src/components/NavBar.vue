<template>
  <div>
    <v-app-bar
    class="navbar-color"
      color=" accent-4"
      dark
    >
      <v-toolbar-title>다운로드 페이지</v-toolbar-title>

      <v-spacer></v-spacer>

      <v-btn v-if="false" class="nav-btn">
        Sign in
      </v-btn>

      <!-- <v-btn class="nav-btn"> -->
        <!-- Download -->
      <!-- </v-btn> -->
    </v-app-bar>
  </div>
</template>

<style scoped>
.nav-btn:hover {
  color: rgb(255, 128, 74);
}
.navbar-color {
  background: rgb(4, 8, 19);
}
</style>