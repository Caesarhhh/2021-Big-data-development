<template>
  <div id="app">
    <SqlPage></SqlPage>
  </div>
</template>

<script>
  import SqlPage from "./components/SqlPage";
  export default {
    name: 'App',
    components: {
      SqlPage:SqlPage
    }
  }
</script>

<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 0px;
  }
</style>
