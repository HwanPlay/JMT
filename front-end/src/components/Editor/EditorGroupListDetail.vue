<template>
  <div class="group-list" style="padding:0;">
    <!-- <ul>
        <li v-for="group in groupList" :key="group.id" v-text="group.groupName" @click="getNoteList(group.groupNo)"></li>
    </ul>-->
    <v-list nav dense id="item-box">
      <v-list-item-group active-class="border" color="orange">
        <div>
          <v-list-item
            v-for="group in groupList"
            :key="group.id"
            @click="getNoteList(group.groupNo)"
            v-b-toggle.sidebar-backdrop
          >
            <v-list-item-icon style="margin-left:10px">
              <v-icon dark>mdi-account-multiple</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-badge
                v-if="$store.state.userId === group.hostId"
                color="red"
                content="HOST"
                overlap
                offset-x="45"
                offset-y="20"
              >
                <div id="NameText" v-text="group.groupName"></div>
              </v-badge>
              <v-badge v-else color="rgb(0, 0, 0, 0)" dot overlap>
                <div id="NameText" v-text="group.groupName"></div>
              </v-badge>
              <!-- <div id="groupNameText" v-text="group.groupName"></div> -->
            </v-list-item-content>
          </v-list-item>
        </div>
      </v-list-item-group>
    </v-list>
  </div>
</template>

<script>
export default {
  name: 'EditorGroupListDetail',
  props: {
    groupList: Array
  },
  methods: {
    getNoteList(groupId) {
      this.$emit('onGetNoteList', groupId);
      if (this.$route.query.groupNo !== groupId) {
        this.$router.push({ name: 'Editor', query: { groupNo: groupId } });
      }
    }
  }
};
</script>

<style>
#item-box {
  background-color: rgb(52, 63, 87);
}
#NameText {
  color: white;
  width: 100px;
  font-size: 20px;
  left: -20px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>