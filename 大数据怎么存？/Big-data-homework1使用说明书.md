## Big-data-homework1使用说明书

* 程序配置

  config.S3Config.java中可配置程序基本参数

  ``` 
  bucketName:所需关联的桶的名称
  baseFilePath:本地关联的文件夹地址
  accessKey:S3 accessKey
  secretKey:S3 secretKey
  serviceEndpoint:S3服务器地址
  signingRegion:S3服务器注册地区
  partSize:大文件传输分块大小
  threadSize:大文件和小文件的大小分界线
  RefreshInternal_Second:定时任务刷新间隔
  cachePath:缓存信息保存地址
  filterFile:文件传输过程中需要忽略的文件名
  Overdue_Second:缓存文件过期时间
  ```

* 程序启动

  可从S3Excutor.main()中直接执行程序执行器，也可从Homework1Application.java.main()中执行程序执行器，控制台中打印出程序执行进度信息

* 程序意外故障

  程序执行时如果遇到意外（如网络问题等）可直接中断程序，重启程序后执行器将会读取缓存信息，重新执行被中断的任务

