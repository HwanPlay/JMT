<template>
  <div>    
    <NavBar />
    
    <v-container style="margin-top: 4rem;">
      <JMTlogo />
      <div style="height: 4rem;"></div>
      <Download />
      <IntroduceJMT />
    </v-container>
  </div>
</template>

<script>
import JMTlogo from './components/JMTlogo';
import NavBar from './components/NavBar';
import IntroduceJMT from './components/IntroduceJMT';
import Download from './components/Download';

export default {
  name: 'App',

  components: {
    JMTlogo,
    NavBar,
    IntroduceJMT,
    Download
  },

  data: () => ({
    //
  }),
};
</script>
