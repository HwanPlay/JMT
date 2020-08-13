<template>
  <div style="height:4rem;">
      <b-alert
      :show="dismissCountDown"
      dismissible
      variant="white"
      @dismissed="dismissCountDown=0"
      @dismiss-count-down="countDownChanged"
    >
      <div>
          <img id="myImage" src="../../assets/profile/profile1.jpg" />
          <p>{{alertMessage}}</p>
          <p>{{dismissCountDown}}</p>
      </div>
    </b-alert>
  </div>
</template>

<script>
export default {
  name: 'NoteAlert',
  props:{
    alertFlag: Boolean,
    alertMessage: String,
  },
  data() {
    return {
      //alert
      dismissSecs: 10,
      dismissCountDown: 0,
      showDismissibleAlert: false,
    };
  },
  watch: {
    alertFlag:function() {
      console.log('alert!! on');
      this.showAlert();
    }
  },
  methods: {
    countDownChanged(dismissCountDown) {
      this.dismissCountDown = dismissCountDown;
    },
    showAlert() {
      this.dismissCountDown = this.dismissSecs;
    },
  }
};
</script>

<style>

</style>