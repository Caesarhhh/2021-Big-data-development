<template>
  <div>
    <a-page-header
      style="border: 1px solid rgb(235, 237, 240)"
      title="SparkSql"
      :sub-title="connectStr"
    >
      <template slot="extra">
        <a-button key="1" type="primary">
          Document
        </a-button>
      </template>
      <a-descriptions size="small" :column="3">
        <a-descriptions-item label="HostAddress">
          <a-input :disabled="connectState" v-model="inputData.hostInput"></a-input>
        </a-descriptions-item>
        <a-descriptions-item label="dbname">
          <a-input :disabled="connectState" v-model="inputData.dbnameInput"></a-input>
        </a-descriptions-item>
        <a-descriptions-item label="portNum">
          <a-input :disabled="connectState" v-model="inputData.portNumInput"></a-input>
        </a-descriptions-item>
        <a-descriptions-item label="user">
          <a-input :disabled="connectState" v-model="inputData.userInput"></a-input>
        </a-descriptions-item>
        <a-descriptions-item label="password">
          <a-input :disabled="connectState" v-model="inputData.passwordInput"></a-input>
        </a-descriptions-item>
        <a-descriptions-item>
          <br/>
          <a-button :loading="connectLoading" style="background-color: green" v-if="!connectState" @click="connect">
            connect
          </a-button>
          <a-button @click="disconnect" style="background-color:red" v-if="connectState">disconnect</a-button>
        </a-descriptions-item>
      </a-descriptions>
    </a-page-header>
    <a-row v-if="connectState">
      <a-col :span="6">
        <a-menu mode="inline" :open-keys="openKeys" style="width: 100%" @openChange="onOpenChange">
          <a-sub-menu :key="database.databaseName"
                      v-for="(database,index) in databaseList.slice((databasePos-1)*databasePageVec,databasePos*databasePageVec)">
            <span slot="title"><a-icon type="database"/><span>{{ database.databaseName }}</span></span>
            <a-menu-item @click="resetColPos" v-for="table in database.tables"
                         :key="database.databaseName+table.tableName">
              <a-popover trigger="click" placement="right">
                <template slot="content">
                  <p v-for="col in table.cols.slice((colPos-1)*colPageVec,colPos*colPageVec)">
                    {{ col.columnName }}({{ col.columnType }})[{{ col.comment }}]</p>
                  <a-pagination :page-size="colPageVec" size="small" v-model="colPos" :total="table.cols.length"/>
                </template>
                <template slot="title">
                  <span>{{ table.tableName }}</span>
                </template>
                {{ table.tableName }}
              </a-popover>
            </a-menu-item>
          </a-sub-menu>
          <a-pagination :page-size="databasePageVec" size="small" v-model="databasePos" :total="databaseList.length"/>
        </a-menu>
      </a-col>
      <a-col :span="18">
        <a-textarea @keyup.enter.native="handleKey($event,1)" id="sqlInput" v-model="sqlcontent"
                    :rows="10"></a-textarea>
        <a-popover trigger="hover" placement="top">
          <template slot="content">
            Click/Alt+Enter
          </template>
          <a-button :loading="dataLoading" size="large" @click="excuteSql" id="submitSql">RUN</a-button>
        </a-popover>
        <a-table :loading="dataLoading" id="resultTable" :columns="columns" :data-source="datasource">
        </a-table>
      </a-col>
    </a-row>
  </div>
</template>
<script>
  import axios from 'axios';

  export default {
    data() {
      return {
        rootSubmenuKeys: ['table1', 'table2'],
        openKeys: [],
        databaseList: [],
        sqlcontent: "",
        resultdata: [],
        columns: [],
        datasource: [],
        dataLoading: false,
        tabLoading: true,
        connectLoading: false,
        inputData: {
          hostInput: "",
          dbnameInput: "",
          portNumInput: "",
          userInput: "",
          passwordInput: "",
        },
        databasePageVec: 11,
        colPageVec: 10,
        connectStr: "there is no connection",
        connectState: false,
        databasePos: 1,
        colPos: 1
      };
    },
    watch: {
      resultdata(val, oldVal) {
        this.columns = []
        this.datasource = []
        let dataArr = []
        let datanum = 0
        for (let i = 0; i < val.cols.length; i++) {
          this.columns.push({
            title: val.cols[i].columnName,
            dataIndex: val.cols[i].columnName
          })
          datanum = val.cols[i].datas.length
          dataArr.push(val.cols[i].datas)
        }
        for (let i = 0; i < datanum; i++) {
          let tempdict = {}
          for (let j = 0; j < val.cols.length; j++) {
            tempdict[this.columns[j].title] = dataArr[j][i]
          }
          this.datasource.push(tempdict)
        }
      }
    },
    methods: {
      handleKey(e, type) {
        if (e.altKey && e.keyCode === 13) {
          this.excuteSql()
        }
      },
      resetColPos() {
        this.colPos = 1
      },
      connect() {
        let param = {
          "databaseName": this.inputData.dbnameInput,
          "hostAddress": this.inputData.hostInput,
          "portNum": this.inputData.portNumInput,
          "user": this.inputData.userInput,
          "password": this.inputData.passwordInput
        }
        this.connectLoading = true
        axios({
          method: "get",
          url: "/api/testConnection",
          params: param,
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(res => {
          this.connectLoading = false
          localStorage.setItem("sparkInputData", JSON.stringify(this.inputData))
          if (res.data.data == "success") {
            this.connectState = true
            this.connectStr = this.inputData.userInput + "@" + this.inputData.hostInput + "/" + this.inputData.dbnameInput
            this.getAllDatabases()
          } else {
            this.$notification['error']({
              message: "connecting error!",
              description: res.data.data,
              duration: 4.5,
              placement: 'bottomLeft'
            })
          }
        })
      },
      disconnect() {
        this.connectState = false
        this.connectStr = "there is no connection"
      },
      onOpenChange(openKeys) {
        let param = {
          "databaseName": openKeys[openKeys.length - 1],
          "hostAddress": this.inputData.hostInput,
          "portNum": this.inputData.portNumInput,
          "user": this.inputData.userInput,
          "password": this.inputData.passwordInput
        }
        let index = -1
        for (let j = 0; j < this.databaseList.length; j++) {
          if (this.databaseList[j].databaseName == param.databaseName) {
            index = j
          }
        }
        if (index != -1 && this.databaseList[index].tables.length == 0) {
          axios({
            method: "get",
            url: "/api/getAllTables",
            params: param,
            headers: {
              'Content-Type': 'application/json;charset=UTF-8'
            }
          }).then(ress => {
            let tablelist = ress.data.data
            this.databaseList[index].tables = tablelist
          })
        }
        const latestOpenKey = openKeys.find(key => this.openKeys.indexOf(key) === -1);
        if (this.rootSubmenuKeys.indexOf(latestOpenKey) === -1) {
          this.openKeys = openKeys;
        } else {
          this.openKeys = latestOpenKey ? [latestOpenKey] : [];
        }
      },
      excuteSql() {
        if (this.dataLoading) {
          return
        }
        if (!this.connectState) {
          alert("there is no connection!")
          return
        }
        let param = {
          "sqlcontent": this.sqlcontent,
          "databaseName": this.inputData.dbnameInput,
          "hostAddress": this.inputData.hostInput,
          "portNum": this.inputData.portNumInput,
          "user": this.inputData.userInput,
          "password": this.inputData.passwordInput
        }
        this.dataLoading = true
        axios({
          method: "get",
          url: "/api/sqlexcute",
          params: param,
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(res => {
          this.dataLoading = false
          if (res.data.status == 200) {
            this.resultdata = res.data.data
          }
          if (res.data.status == 400) {
            this.resultdata={cols:[]}
            this.$notification['error']({
              message: "sql excute error!",
              description: res.data.data,
              duration: 4.5,
              placement: 'bottomLeft'
            })
          }
        })
      },
      getAllDatabases() {
        let param = {
          "databaseName": this.inputData.dbnameInput,
          "hostAddress": this.inputData.hostInput,
          "portNum": this.inputData.portNumInput,
          "user": this.inputData.userInput,
          "password": this.inputData.passwordInput
        }
        axios({
          method: "get",
          url: "/api/getAllDatabases",
          params: param,
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(async res => {
          this.databaseList = []
          let resdata = res.data.data.datas
          for (let i = 0; i < resdata.length; i++) {
            let temp = {
              databaseName: resdata[i],
              tables: []
            }
            this.databaseList.push(temp)
          }
        })
      }
    },
    mounted() {
      let inputData = JSON.parse(localStorage.getItem("sparkInputData"))
      if (inputData != null) {
        this.inputData = inputData
      }
    }
  };
</script>
<style scoped>
  #sqlInput {
    left: 5%;
    position: absolute;
    width: 90%;
    top: 20px;
  }

  #submitSql {
    position: absolute;
    right: 7%;
    width: 10%;
    top: 250px;
  }

  #resultTable {
    width: 96%;
    left: 2%;
    position: absolute;
    top: 300px;
  }
</style>
