package com.zdjizhi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName AbstractIpLookup
 * @Description 对IP定位查找抽象类，规定可执行方法
 * @Author darnell
 * @Date 2018-10-9 19:20
 * @Version 1.0
 **/
public abstract class AbstractIpLookup {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected static final String UNKNOWN  =  "";
    protected  static final String LOCATION_SEPARATOR = ".";
    protected  static final String LATLNG_SEPARATOR = ",";
    protected  static final String OBJECT_SEPARATOR = "\t";
    protected  static final String PRIVATE_IP = "Private IP";

    /**
     * 给定IP库文件路径，获取国家代码
     * @param ip ip地址
     * @return String 国家代码
     */
    public abstract String countryLookup(String ip);

    /**
     *
     * 获取到城市的3级定位信息
     * @param ip ip地址，点分十进制格式,ipv4
     * @return String 国家代码,省代码,城市代码
     */
    public abstract String cityLookupDetail(String ip);


    /**
     *
     * 获取到区县的4级定位信息
     * @param ip ip地址，点分十进制格式,ipv4
     * @return String 国家代码，省，城市，区/县
     */
    public abstract String administrativeAreaLookupDetail(String ip);

    /**
     *
     * 获取位置信息明细，支持7级
     * @param ip ip地址，点分十进制格式,ipv4
     * @return String 国家代码，省，城市，区/县，街道
     */
    public abstract String locationLookupDetail(String ip);


    /**
     *
     *  获取城市信息
     *
     * @param ip ip地址
     * @return String 城市代码
     */
    public abstract String cityLookup(String ip);

    /**
     *
     *  获取所属省/州信息
     *
     * @param ip ip地址
     * @return String 州/省代码
     */
    public abstract String provinceLookup(String ip);

    /**
     *
     *  获取运营商信息
     * @param ip ip地址
     * @return String 运营商
     */
    public abstract String ispLookup(String ip);

    /**
     *
     *  获取组织信息
     * @param ip ip地址
     * @return String 组织
     */
    public abstract String organizationLookup(String ip);

    /**
     *
     * 获取经纬度信息
     *
     * @param ip ip地址
     * @return String 纬度 经度
     */
    public abstract String latLngLookup(String ip);



    /**
     *
     * 获城市与经纬度信息
     *
     * @param ip ip地址
     * @return String 省代码 纬度 经度
     */
    public abstract String cityLatLngLookup(String ip);


    /**
     *
     * 获取AS号，默认通过IP定位库获取ASN号
     * @param ip ip地址
     * @return String asn号
     */
    public abstract String asnLookup(String ip);

    /**
     *
     * 获取IP 所属AS的组织+国家代码
     * @param ip ip地址
     * @return String asn号
     */
    public abstract String asnLookupInfo(String ip);


    /**
     *
     * 获取AS号与组织信息
     * @param ip ip地址
     * @return asn号 组织名称
     */
    public abstract String asnLookupDetail(String ip);

    /**
     * Get A two-character ISO 3166-1 country code for the country associated with the location.
     * @param ip ip地址
     * @return country code
     */
    public abstract  String countryCodeLookup(String ip);

    /**
     * A string of up to three characters containing the region-portion of the ISO 3166-2 code for the first level region associated with the IP address.
     * @param ip ip地址
     * @return First Level Region Code
     */
    public abstract  String regionCodeLookup(String ip);

    /**
     * This method to return all fields for CSV.
     * [Location]\t[ISP]\t[Organization]\t[ASN]\t[Latitude,Longitude]
     * -[Location]:Country.SuperAdministrativeArea[Province].AdministrativeArea[City][.SubAdministrativeArea][.Locality][.DependentLocality][.DoubleDependentLocality]
     * -[Latitude,Longitude] : Latitude,Longitude
     * @param ip
     * @return
     */
    public abstract  String infoLookupToCSV(String ip);

    /**
     * This method to return all fields for JSON.
     * @param ip
     * @return
     */
    public abstract  String infoLookupToJson(String ip);








}
