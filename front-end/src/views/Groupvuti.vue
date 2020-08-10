<template>
  <v-row style="width: 100%">
    <v-col cols="3">
      <v-card
        class="mr-auto"
        height="100%"
        tile
      >
        <v-navigation-drawer style="height: 100%;" permanent stateless>
          <v-list>
            <v-list-item>
              <v-list-item-avatar>
                <v-img src="../assets/profile/profile1.jpg"></v-img>
              </v-list-item-avatar>
            </v-list-item>

            <v-list-item link> <!-- @click="$router.push('/MyProfile')" -->
              <v-list-item-content>
                <v-list-item-title class="title">{{ $store.state.myName }}</v-list-item-title>
                <v-list-item-subtitle>{{ $store.state.userId }}</v-list-item-subtitle>
              </v-list-item-content>
              <!-- <v-list-item-action>
                <v-icon>mdi-menu-down</v-icon>
              </v-list-item-action> -->
            </v-list-item>
          </v-list>
          <v-divider></v-divider>
          <v-list
            nav
            dense
          >
            <v-list-item-group v-model="group" color="primary">
              <v-list-item
                v-for="(group, i) in this.$store.state.myGroups"
                :key="i" @click="toggle(i)"
              >
                <v-list-item-icon>
                  <v-icon>mdi-account-multiple</v-icon>
                </v-list-item-icon>

                <v-list-item-content>
                  <v-list-item-title v-text="group.groupName"></v-list-item-title>
                </v-list-item-content>

              </v-list-item>
            </v-list-item-group>
            
            <v-btn
              @click="modalOn= !modalOn"
              width="100%"
              text
            >
            <v-icon class="mr-2">mdi-account-multiple-plus</v-icon>
            <p>그룹 만들기</p>
            </v-btn>
            <v-dialog v-model="modalOn" max-width="500px">
              <createGroup @close="closeModal" />
            </v-dialog>

          </v-list>
        </v-navigation-drawer>
      </v-card>
    </v-col>

    <v-col cols="9">
      <!-- <Group1 :groupInfo="this.$store.state.myGroups[this.onboarding]" /> -->
      <GroupContent :groupInfo="this.$store.state.myGroups[this.onboarding]" />
    </v-col>
  </v-row>
</template>

<script>
// import Group1 from '../components/Group/Group1.vue';
import GroupContent from '../components/Group/GroupContent.vue';
import createGroup from '../components/Group/createGroup.vue';

export default {
  name: 'Groups',
  components: {
    // Group1,
    GroupContent,
    createGroup,
  },
  data() {
    return {
      group: 0,
      modalOn: false,
      onboarding: 0,
    };
  },
  methods: {
    toggle(i) {
      console.log('change!',i);
      this.onboarding = i;
    },
    createGroup() {
      this.$store.state.groupModalOn = true;
      console.log(this.$store.state.groupModalOn);
    },
    closeModal() {
      this.modalOn = false;
    },
  },
  mounted() {
    this.$store.dispatch('getGroupInfo');
  },
};
</script>

<style>

</style>