<template>
  <div>
    <button class="menubar__button" v-b-toggle.sidebar-backdrop>
      <b-icon-files v-b-tooltip.hover title="File List" class="h3 mb-2 border rounded"></b-icon-files>
    </button>

    <b-sidebar
      id="sidebar-backdrop"
      title="Your Notes"
      :backdrop-variant="variant"
      backdrop
      shadow
    >
      <div class="px-3 py-2">
        이게 내용
      </div>
    </b-sidebar>
  </div>
</template>

<script>
export default {
  data () {
    return {

    };
  }
};
</script>
