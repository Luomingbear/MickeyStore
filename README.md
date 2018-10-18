# MickeyStore
基于sqlite的key-value数据存储工具

## 依赖
下载代码到自己的java目录下，由于使用了Gson进行json格式化处理，所以还需要引用Gson，在gradle文件的`dependencies`里面添加
```xml
implementation 'com.google.code.gson:gson:2.8.5'
```
## 使用
- 初始化
推荐在application里面执行初始化。
```java
MickeyStore.getInstance().init(Context context);
```

- 设置数据
```java
MickeyStore.getInstance().setData(String key, Object value, OnMickeyStoreListener listener);
```

- 获取数据
```java
MickeyStore.getInstance().getData(String key, OnMickeyStoreListener listener);
```

- 移除数据
```java
MickeyStore.getInstance().removeData(String key, OnMickeyStoreListener listener)
```

- 清空数据
```java
MickeyStore.getInstance().clear(String key, OnMickeyStoreListener listener)
```
