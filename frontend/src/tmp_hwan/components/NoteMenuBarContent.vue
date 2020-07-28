<template>
  <div>
    <b-button class="menubar__button" v-b-toggle.sidebar-backdrop>Note List</b-button>

    <b-sidebar
      id="sidebar-backdrop"
      title="Your Name"
      :backdrop-variant="variant"
      backdrop
      shadow
    >
      <div class="px-3 py-2">
          여기에 Note List
          
        <b-form-group label="Backdrop variant" label-for="backdrop-variant">
          <b-form-select id="backdrop-variant" v-model="variant" :options="variants"></b-form-select>
        </b-form-group>
      </div>
    </b-sidebar>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        variant: 'dark',
        variants: [
          'transparent',
          'white',
          'light',
          'dark',
          'primary',
          'secondary',
          'success',
          'danger',
          'warning',
          'info',
        ]
      }
    }
  }
</script>