<template>
  <div>
      {{timerCount}}
      <p>{{ new Date() | moment('YYYY-MM-DD HH:mm:ss') }}</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      timerCount: 5,
    };
  },

  watch: {
    timerCount: {
      handler(value) {
        if (value > 0) {
          setTimeout(() => {
            this.timerCount--;
          }, 1000);
        } else if(value === 0){
            this.timerCount = 5
        }
      },
      immediate: true, // This ensures the watcher is triggered upon creation
    },
  },
};
</script>

<style>
</style>