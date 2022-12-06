## 组件化架构图

![image](https://github.com/jb5982857/albert/blob/master/image/architecture.jpg?raw=true)

#### app
app 壳，作为 app 入口，applicaiton 事件分发；

#### 登录注册
登录注册功能

#### 主页内容
内容列表，用户展示 list 以及多个 tab

#### 内容详情
点击具体内容，展示对应的详情

#### 聊天功能
聊天功能

#### 设置功能
设置界面功能

#### common
公共组件，用于业务和基础功能之间的通信
包含：
1. BaseActivity，BaseApplication以及各个业务组建注册 Application 生命周期
2. 各个业务服务组件之间的路由路径维护


#### 存储
database 和 mmap 等存储功能，包含对应业务功能

#### network
网络功能，包含 retrofit2 和 websocket

#### common_ui
基础 ui 库，包含主风格的 button ，toast 等

#### app_utils
包含系统信息和app信息获取

#### server_core
各个组件服务化接口在这里注册和对外提供 api

#### log
日志组件，包含本地 Log 维护 和 远程 Log 上报
