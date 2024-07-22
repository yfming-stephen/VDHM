<template>
  <div class='tabs-view-container'>
    <router-link class="tabs-view" v-for="tag in visitedViews" :to="tag.path" :key="tag.path">
      <el-tag :closable="true" :type="isActive(tag.path)?'':'info'" @close='closeViewTabs(tag, $event)' size="medium">
        {{tag.name}}
      </el-tag>
    </router-link>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'
export default {
  name: 'TabsView',
  computed: {
    visitedViews() {
      return this.visitedViews.slice(-6)
    },
    ...mapGetters([
      'visitedViews'
    ])
  },
  methods: {
    closeViewTabs(view, $event) {
      this.delVisitedViews(view).then((views) => {
        if (this.isActive(view.path)) {
          var latestView = views.slice(-1)[0]
          if (latestView) {
            this.$router.push(latestView.path)
          } else {
            this.$router.push('/dashboard')
          }
        }
      })
      $event.preventDefault()
    },
    addViewTabs() {
      // console.log(this.$route)
      if (this.$route.name) {
        this.addVisitedViews(this.$route)
      }
    },
    isActive(path) {
      return path === this.$route.path
    },
    ...mapActions([
      'addVisitedViews',
      'delVisitedViews'
    ])
  },
  watch: {
    $route() {
      this.addViewTabs()
    }
  }
}
</script>

<style lang="scss" scoped>
.tabs-view-container {
  width: 100%;
  height: 50px;
  overflow: hidden;
  display: inline-block;
  vertical-align: top;
  margin-left: -5px;
  margin-top: -15px;
  outline: 0;
  .tabs-view {
    margin-left: 10px;
  }
}
</style>
