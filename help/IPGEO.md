# IPLookup 使用文档

IPLookupV2 基于IP地址获取地理位置或ASN（自治系统号码）信息。核心功能：
- [构建GeoIP Databases 文件](https://docs.geedge.net/pages/viewpage.action?pageId=39135257)
- 通过开源解析库[MaxMind DB Reader](https://github.com/maxmind/MaxMind-DB-Reader-java)生成Reader 对象

## 功能设计

因IP知识库来源不同，默认分两种类型数据库：IP-ASN 和 IP-LOCATION。具体实现类：
- IPLookup IP地址映射为国家，地区，经纬度及ASN(内置ASNLookup类) 等。
- ASNLookup IP地址映射AS号，组织及国家编码等。

## 数据库信息
GeoIP 数据库文件默认存储在 "dat目录" 中，并且在加载工具类时，如果没有指定数据库文件的路径，它将自动加载默认路径下的数据库文件。默认文件名如下：
| Name              | Filename                                                     | Description                                                  |
| ----------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 基础IP定位库      | ip.mmdb<br />ip_v4.mmdb <br />Ip_v6.mmdb                     | IP基础定位库，一般来源于系统内置或第三方购置；包括全局，v4格式和v6格式。<br />|
| 用户自定义IP定位库    | ip_private.mmdb<br />ip_private_v4.mmdb<br />ip_private_v6.mmdb | 用户自定义IP定位库，一般来源于企业或运营商内部的知识库，相对权威和可信度更高；包括全局，v4格式和v6格式。<br /> |
| 基础ASN库   | asn.mmdb<br />asn_v4.mmdb<br />asn_v6.mmdb                   | 基础ASN库，一般来源于系统内置或第三方购置；包括全局，v4格式和v6格式。 |
| 用户自定义ASN库 | asn_private.mmdb<br />asn_private_v4.mmdb<br />asn_private_v6.mmdb | 自定义IP映射ASN库，一般来源于企业或运营商内部的知识库，相对权威和可信度更高；包括全局，v4格式和v6格式。 |



## 实现方式

### 同类型不同格式数据库的加载顺序

支持同时加载相同类型多个格式的IP定位库，基于下图将产生的DBReader对象顺序存储到LIST集合中。例如：基础库同时加载了v4，v6和 All(包含v4和v6)三个文件，读取时将优先使用v4和v6 DBReader。

![默认加载顺序](help/images/ip_load_priortiy.png)



### 不同类型数据库的执行顺序

当同时指定公共与用户自定义库时，将优先使用自定义库获取IP地理位置。执行顺序如下：

![映射优先级](help/images/ip_map.jpg)


## 方法说明

**IpLookupV2** 

| Name                                                         | Type             | Description                                        |
| ------------------------------------------------------------ | ---------------- | -------------------------------------------------- |
| IpLookupV2.Builder(boolean isDefaultDB)                        | IpLookupV2.Builder |是否使用默认数据库                                                    |
| Builder.loadDataFile(String databaseFile)            | IpLookupV2.Builder | 加载基础IP定位库                                   |
| Builder.loadDataFileV4(String databaseFile)        | IpLookupV2.Builder | 加载基础IPv4定位库                                 |
| Builder.loadDataFileV6(String databaseFile)        | IpLookupV2.Builder | 加载基础IPv6定位库                                 |
| Builder.loadDataFilePrivate(String databaseFile)    | IpLookupV2.Builder | 加载用户自定义IP定位库                             |
| Builder.loadDataFilePrivateV4(String databaseFile) | IpLookupV2.Builder | 加载用户自定义IPv4定位库                           |
| Builder.loadDataFilePrivateV6(String databaseFile) | IpLookupV2.Builder | 加载用户自定义IPv6定位库                           |
| Builder.loadAsnDataFile(String databaseFile)        | IpLookupV2.Builder | 加载基础ASN库                                  |
| Builder.loadAsnDataFileV4(String databaseFile)    | IpLookupV2.Builder | 加载基础IPv4 ASN库                             |
| Builder.loadAsnDataFileV6(String databaseFile)    | IpLookupV2.Builder | 加载基础IPv6 ASN库                             |
| Builder.loadAsnDataFilePrivate(String databaseFile) | IpLookupV2.Builder | 加载用户自定义ASN库                            |
| Builder.loadAsnDataFilePrivateV4(String databaseFile) | IpLookupV2.Builder | 加载用户自定义IPv4 ASN库                       |
| Builder.loadAsnDataFilePrivateV6(String databaseFile) | IpLookupV2.Builder | 加载用户自定义IPv6 ASN库                       |
| Builder.loadDataFile(Inputstream databaseStream)            | IpLookupV2.Builder | 加载基础IP定位库                                   |
| Builder.loadDataFileV4(Inputstream databaseStream)        | IpLookupV2.Builder | 加载基础IPv4定位库                                 |
| Builder.loadDataFileV6(Inputstream databaseStream)        | IpLookupV2.Builder | 加载基础IPv6定位库                                 |
| Builder.loadDataFilePrivate(Inputstream databaseStream)    | IpLookupV2.Builder | 加载用户自定义IP定位库                             |
| Builder.loadDataFilePrivateV4(Inputstream databaseStream) | IpLookupV2.Builder | 加载用户自定义IPv4定位库                           |
| Builder.loadDataFilePrivateV6(Inputstream databaseStream) | IpLookupV2.Builder | 加载用户自定义IPv6定位库                           |
| Builder.loadAsnDataFile(Inputstream databaseStream)        | IpLookupV2.Builder | 加载基础ASN库                                  |
| Builder.loadAsnDataFileV4(Inputstream databaseStream)    | IpLookupV2.Builder | 加载基础IPv4 ASN库                             |
| Builder.loadAsnDataFileV6(Inputstream databaseStream)    | IpLookupV2.Builder | 加载基础IPv6 ASN库                             |
| Builder.loadAsnDataFilePrivate(Inputstream databaseStream) | IpLookupV2.Builder | 加载用户自定义ASN库                            |
| Builder.loadAsnDataFilePrivateV4(Inputstream databaseStream) | IpLookupV2.Builder | 加载用户自定义IPv4 ASN库                       |
| Builder.loadAsnDataFilePrivateV6(Inputstream databaseStream) | IpLookupV2.Builder | 加载用户自定义IPv6 ASN库                       |
| Builder.build()                                              | IpLookupV2         | 构建IPLookupV2对象                                      |
| countryLookup(String ip)                                     | String           | 返回国家/区域                                           |
| provinceLookup(String ip)                                     | String           | 返回省/洲                                           |
| cityLookup(String ip)                                        | String           | 返回城市            |
| administrativeAreaLookupDetail(String ip)                                        | String           | 返回到区/县的四级定位信息            |
| ispLookup(String ip)                                        | String           | 返回运营商            |
| organizationLookup(String ip)                                        | String           | 返回组织           |
| cityLookupDetail(String ip)                                  | String           | 返回到城市的3级定位信息，包含城市,省(州),国家;分隔符为英文句号"." |
| locationLookupDetail(String ip)                                  | String           | 返回地理位置信息明细，支持7级 |
| infoLookupToCSV(String ip)                                  | String           | 返回地理位置信息明细，支持7级,csv格式 |
| infoLookupToJSON(String ip)                                  | String           | 返回地理位置信息明细，支持7级,json格式 |
| infoLookup(String ip)                                  | String           | 返回地理位置信息明细，支持7级,Response 对象 |
| latLngLookup(String ip)                                      | String           | 返回经纬度，格式：维度,经度 ；分隔符为英文逗号","  |
| cityLatLngLookup(String ip)                                  | String           | 返回 cityLookup+latLngLookup信息；                 |
| asnLookup(String ip)                                         | String           | 返回ASN，调用AsnLookup工具类                     |
| asnLookupOrganization(String ip)                                   | String           | 返回所属AS组织，调用AsnLookup工具类            |
| asnLookupDetail(String ip)                                   | String           | 返回ASN及ISP信息，调用AsnLookup工具类            |

**AsnLookup** 

| Name                                                         | Type              | Description                  |
| ------------------------------------------------------------ | ----------------- | ---------------------------- |
| AsnLookup.Builder(boolean isDefaultDB)                       | AsnLookup.Builder |                              |
| Builder.loadDataFile(String databaseFile)           | AsnLookup.Builder | 加载基础ASN库             |
| Builder.loadDataFileV4(String databaseFile)       | AsnLookup.Builder | 加载基础V4 ASN库       |
| Builder.loadDataFileV6(String databaseFile)       | AsnLookup.Builder | 加载基础V6 ASN库       |
| Builder.loadDataFilePrivate(String databaseFile)   | AsnLookup.Builder | 加载用户自定义ASN库     |
| Builder.loadDataFilePrivateV4(String databaseFile) | AsnLookup.Builder | 加载用户自定义V4 ASN库 |
| Builder.loadDataFilePrivateV6(String databaseFile) | AsnLookup.Builder | 加载用户自定义V6 ASN库 |
| Builder.loadDataFile(Inputstream databaseStream)           | AsnLookup.Builder | 加载基础ASN库             |
| Builder.loadDataFileV4(Inputstream databaseStream)       | AsnLookup.Builder | 加载基础V4 ASN库       |
| Builder.loadDataFileV6(Inputstream databaseStream)       | AsnLookup.Builder | 加载基础V6 ASN库       |
| Builder.loadDataFilePrivate(Inputstream databaseStream)   | AsnLookup.Builder | 加载用户自定义ASN库     |
| Builder.loadDataFilePrivateV4(Inputstream databaseStream) | AsnLookup.Builder | 加载用户自定义V4 ASN库 |
| Builder.loadDataFilePrivateV6(Inputstream databaseStream) | AsnLookup.Builder | 加载用户自定义V6 ASN库 |
| Builder.build()                                              | AsnLookup         | 构建AsnLookup对象                |
| asnLookup(String ip)                                         | String            | 返回ASN                   |
| asnLookupOrganization(String ip)                                         | String            | 返回AS 组织                  |
| asnLookupDetail(String ip)                                   | String            | 返回ASN及ISP信息           |

## 实现举例

```java
 IpLookupV2 ipLookup =  ipLookupV2 = new IpLookupV2.Builder(false)
                    .loadDataFileV4(new FileInputStream(new File("dat/ip_v4_built_in.mmdb")))
                    .loadDataFilePrivateV4(new FileInputStream(new File("dat/ip_v4_user_defined.mmdb")))
                    .loadDataFileV6(new FileInputStream(new File("dat/ip_v6_built_in.mmdb")))
                    .loadDataFilePrivateV6(new FileInputStream(new File("dat/ip_v6_user_defined.mmdb")))
                    .loadAsnDataFile(new FileInputStream(new File("dat/asn_v4.mmdb")))
                    .loadAsnDataFileV6(new FileInputStream(new File("dat/asn_v6.mmdb")))
                    .build();

        logger.info(ipLookup.countryLookup("192.168.50.21"));
        logger.info(ipLookup.cityLookupDetail("192.168.50.21"));
        logger.info(ipLookup.latLngLookup("192.168.50.21"));
        logger.info(ipLookup.infoLookupToJSON("120.221.155.223"));
        logger.info(ipLookup.countryLookup("2001:4860:4860::8888"));
        logger.info(ipLookup.asnLookup("120.221.155.223"));
        logger.info(ipLookup.asnLookup("2001:4860:4860::8888"));
        logger.info(ipLookup.latLngLookup("119.253.39.18"));
        logger.info(ipLookup.cityLatLngLookup("120.221.155.223"));
```

