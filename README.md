**galaxy: Geedge Networks Core Libraries for Java**

Galaxy Tools 工具类库，主要包括文件， 加密，日期，IP定位，IP地址，JSON处理和字符串处理等。

* JRE环境必须使用JDK 11
* IP定位库，Maxmind DB
* 日期类库，依赖JODA-TIME库

**增加Galaxy Tools 到你的应用中**
***
Galaxy 的 Maven group ID 是 `com.geedgenetworks` 并且 artifactId 是 `galaxy`。增加maven依赖，如下所示：

```
<dependency>
    <groupId>com.geedgenetworks</groupId>
    <artifactId>galaxy</artifactId>
    <version>2.0.0</version>
</dependency>
```

**关于Galaxy 应用**
***
IP映射使用向导，[IPGEO](help/IPGEO.md)
格式转换Formatutils向导[FormatUtils](https://git.mesalab.cn/doufenghu/galaxy-tool/wikis/FormatUtils)

**说明**
***
maven构建需添加局域网仓库URL：
```
<repository>
   <id>nexus</id>
   <name>Team Nexus Repository</name>
   <url>http://192.168.40.153:8099/content/groups/public</url>
</repository>
```
