**galaxy: zdjizhi Core Libraries for Java**

Galaxy Tools 是中电积至关于java的核心工具类库，主要包括文件类， 加密类，日期类，IP定位，IP工具处理类，JSON处理和字符串处理工具类等。

使用Galaxy注意如下：

    * JRE环境必须使用JDK 1.8 或更高版本
    * IP定位库，依赖GEOIP库，目前有两种GEOIP及GEOIP2
    * 日期类库，依赖JODA-TIME库

**增加Galaxy Tools 到你的应用中**
***
Galaxy 的 Maven group ID 是 `com.zdjizhi` 并且 artifactId 是 `galaxy`。增加maven依赖，如下所示：

```
<dependency>
    <groupId>com.zdjizhi</groupId>
    <artifactId>galaxy</artifactId>
    <version>1.0.1/version>
</dependency>
```

**关于Galaxy 应用**
***

IP定位使用向导，[IpLookup](/releases/docs)


**说明**
***
目前工具类库未支持外网，maven构建时请自行添加局域网仓库URL，具体如下：
```
<repository>
   <id>nexus</id>
   <name>Team Nexus Repository</name>
   <url>http://192.168.10.125:8099/content/groups/public</url>
</repository>
```