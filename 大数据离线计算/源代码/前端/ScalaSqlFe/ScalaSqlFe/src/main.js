// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import 'ant-design-vue/dist/antd.css';
import axios from "axios";
import { Button,FormModel,Descriptions,Pagination,DatePicker,Input,Icon,Form,Select,Checkbox,Row,Col,PageHeader,Popover,Menu,Tag,Table,Divider,notification } from 'ant-design-vue';
Vue.config.productionTip = false
Vue.config.axios=axios
Vue.prototype.$notification=notification
Vue.component(Button.name,Button)
Vue.use(Descriptions)
Vue.use(Pagination);
Vue.use(Table);
Vue.use(DatePicker);
Vue.use(Select);
Vue.use(Divider);
Vue.use(Form);
Vue.use(Menu);
Vue.use(Row);
Vue.use(Col);
Vue.use(Popover);
Vue.use(FormModel);
Vue.use(Input);
Vue.use(Icon);
Vue.use(Checkbox);
Vue.use(PageHeader);
Vue.use(Tag)
/* eslint-disable no-new */
new Vue({
  el: '#app',
  components: { App },
  template: '<App/>'
})
