# URouter 说明
## 一、引入


## 二、URouter类说明
### 1、初始化
定义：
```kotlin
fun init(context: Context)
```
举例
```kotlin
URouter.instance.init(this)
```

### 2、打开页面
定义：
```kotlin
fun start(
        context: Context,
        path: String, params: Map<String, Any?>? = null, flag: Int = URouterFlag.DEFAULT
    ): IURouterContainer?
```
| 参数        | 说明    |  
| :-----:   | :-----:   |
| path       |    路径，URouterPlugin的path参数，找不到会异常   |
| params     |   传入参数    |
| flag        |    DEFAULT -> 默认堆栈形式 ，CLEAR_TOP -> 如果存在，请清楚顶部以上的   |

举例
```kotlin
URouter.instance.start(context, "A", params, URouterFlag.CLEAR_TOP)
```

### 3、关闭页面
定义：
```kotlin
fun finish(container: IURouterContainer, backParams: Map<String, Any?>? = null)
```
| 参数        | 说明    |  
| :-----:   | :-----:   |
| backParams     |   关闭之后，传给下面容器的参数    |

举例
```kotlin
URouter.instance.finish(this, params)
```

## 三、IURouterContainer 接口说明
```kotlin
//当容器被添加时
fun added(params: Map<String, Any?>?)

//当容器被移除时
fun removed()

//当容器不在栈顶时
fun background()

//当容器重新回到栈顶时
fun forward(params: Map<String, Any?>?)

//当使用 CLEAR_TOP 或者 SINGLE_TASK 时，栈内存在该容器，则会不会重新创建容器，会走这个方法告知参数
fun onNewParams(params: Map<String, Any?>?)

//物理返回键按下时，调用这个方法
// 返回值
// true -> 拦截
// false -> 不拦截
fun onPhysicalBackPressed(): Boolean
```
