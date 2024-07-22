import types from '../mutation-types'

const app = {
  state: {
    visitedViews: []
  },
  mutations: {
    [types.ADD_VISITED_VIEWS](state, view) {
      if (state.visitedViews.some(v => v.path === view.path)) return
      state.visitedViews = [{
        name: view.name,
        path: view.path
      }, ...state.visitedViews]
    },
    [types.DEL_VISITED_VIEWS](state, view) {
      const newViews = state.visitedViews.filter(v => v.path !== view.path)
      state.visitedViews = newViews
    }
  },
  actions: {
    addVisitedViews({commit}, view) {
      commit(types.ADD_VISITED_VIEWS, view)
    },
    delVisitedViews({
      commit,
      state
    }, view) {
      return new Promise((resolve) => {
        commit(types.DEL_VISITED_VIEWS, view)
        resolve([...state.visitedViews])
      })
    }
  }
}

export default app
