<template>
  <div id="app">
    <JMTlogo />
  </div>
</template>

<script>
import JMTlogo from './components/JMTlogo.vue';

export default {
  name: 'App',
  components: {
    JMTlogo,
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
