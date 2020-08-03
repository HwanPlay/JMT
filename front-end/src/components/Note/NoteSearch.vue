<template>
  <b-col cols="4">
    <div>
      <b-card no-body>
        <b-tabs pills card vertical>
          <b-tab title="Group A" active>
              <p>1월 1일</p>

              <b-button variant="outline-primary">Button</b-button>
              <b-button variant="outline-primary">Button</b-button>
              <b-button variant="outline-primary">Button</b-button>
              <p>1월 4일</p>
              <b-button variant="outline-primary">Button</b-button>
              <b-button variant="outline-primary">Button</b-button>

          </b-tab>
          <b-tab title="Group B">
            <b-card-text>Tab contents 2</b-card-text>
          </b-tab>
          <b-tab title="Group C">
            <b-card-text>Tab contents 3</b-card-text>
          </b-tab>
        </b-tabs>
      </b-card>
    </div>
  </b-col>
</template>

<script>
export default {
  name: 'NoteSearch',
  components: {
  }
};
</script>

<style>
</style>
