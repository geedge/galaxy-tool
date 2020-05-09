# Galaxy IPGEO 使用文档

Galaxy IPGEO 即数据平台下的galaxy-tool支持的IP定位功能。最终的数据格式为MaxMind DB 二进制格式（mmdb），当然我们也支持提供CSV或SQL数据库表的数据格式（只要满足schema属性约束），数据平台将会在合适时间进行转换为二进制格式。

## 工具类信息

因ASN与IP位置信息的差异性，将IPGEO分为两个工具类：

- ASNLookup 针对IP映射ASN工具类
- IPLookup 支持IP映射国家、地区、经纬度、ASN信息（支持ASNLookup部分功能）

## 数据库

所属数据库默认处于dat目录下，加载文件将以mmdb结尾。不指定加载数据库情况下，默认文件名如下：

| Name              | Filename                                                     | Description                                                  |
| ----------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 公共IP定位库      | ip.mmdb<br />ip_v4.mmdb <br />Ip_v6.mmdb                     | 公共IP定位库，包含All，v4，v6<br />来自于第三方购买或其它开源数据； |
| 自定义IP定位库    | ip_private.mmdb<br />ip_private_v4.mmdb<br />ip_private_v6.mmdb | 自定义IP定位库，包含All，v4，v6<br />来自与用户预置的自定义数据，一般权威性较高。 |
| 公共IP映射ASN库   | asn.mmdb<br />asn_v4.mmdb<br />asn_v6.mmdb                   | 公共IP映射ASN库，包含All，v4，v6<br />来自于第三方购买或其它开源数据； |
| 自定义IP映射ASN库 | asn_private.mmdb<br />asn_private_v4.mmdb<br />asn_private_v6.mmdb | 自定义IP映射ASN库，包含All，v4，v6<br />来自与用户预置的自定义数据，一般权威性较高。 |



## 执行方案

### 数据库加载优先级

> 相同类型库：具有相同特征的库，来自不同数据源或类型。例如ip.mmdb 可能包含 ip_v4.mmdb 与 ip_v6.mmdb，需要指定依赖的数据源执行顺序。

如上所述，若同时加载多个相同类型库，系统根据IP特点会优先获取V4或V6库.

![Image text](./ip_load_priortiy.png)



### 执行顺序

当同时指定公共与用户自定义库时，系统优先通过自定义库进行映射匹配。执行顺序如下：

![](/Users/darnell/docs/ip_map.png)

## 功能说明

**IpLookup** 

| Name                                                      | Type             | Description                |
| --------------------------------------------------------- | ---------------- | -------------------------- |
| IpLookup.Builder(boolean isDefaultDB)                     | IpLookup.Builder |                            |
| Builder.loadDataFile(String ipDatabasePublicFile)         | IpLookup.Builder | 加载公共IP定位库文件       |
| Builder.loadDataFilePrivate(String ipDatabasePrivateFile) | IpLookup.Builder | 加载用户自定义IP定位库文件 |
|                                                           |                  |                            |
|                                                           |                  |                            |

## 实现举例

```java
IpLookup ipLookup =  new IpLookup.Builder(false)
               .loadDataFile("dat/ip.mmdb")
               .loadDataFilePrivateV4("dat/ip_private_v4.mmdb")
               .loadDataFilePrivateV6("dat/ip_v6.mmdb")
               .loadAsnDataFile("dat/asn_v4.mmdb")
               .loadAsnDataFileV6("dat/asn_v6.mmdb")
               .build();

        logger.info(ipLookup.countryLookup("192.168.50.21"));
        logger.info(ipLookup.cityLookupDetail("192.168.50.21"));
        logger.info(ipLookup.latLngLookup("192.168.50.21"));
        logger.info(ipLookup.cityLookupDetail("120.221.155.223"));
        logger.info(ipLookup.countryLookup("2001:4860:4860::8888"));
        logger.info(ipLookup.asnLookup("120.221.155.223"));
        logger.info(ipLookup.asnLookup("2001:4860:4860::8888"));
        logger.info(ipLookup.latLngLookup("119.253.39.18"));
        logger.info(ipLookup.cityLatLngLookup("120.221.155.223"));
```

