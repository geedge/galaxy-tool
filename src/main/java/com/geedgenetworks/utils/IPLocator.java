package com.geedgenetworks.utils;

import com.alibaba.fastjson2.JSONObject;
import com.geedgenetworks.model.LocationResponse;

public interface IPLocator {

    /**
     * 给定IP库文件路径，获取国家代码
     * @param ip ip地址
     * @return String 国家代码
     */
    String countryLookup(String ip);

    /**
     *
     *  获取所属省/州信息
     *
     * @param ip ip地址
     * @return String 州/省代码
     */
     String provinceLookup(String ip);

    /**
     *
     *  获取城市信息
     *
     * @param ip ip地址
     * @return String 城市代码
     */
    String cityLookup(String ip);

    /**
     *
     * 获取到区县的4级定位信息
     * @param ip ip地址，点分十进制格式,ipv4
     * @return String 国家代码，省，城市，区/县
     */
    String administrativeAreaLookupDetail(String ip);


    /**
     *
     *  获取运营商信息
     * @param ip ip地址
     * @return String 运营商
     */
    String ispLookup(String ip);

    /**
     *
     *  获取组织信息
     * @param ip ip地址
     * @return String 组织
     */
    String organizationLookup(String ip);



    /**
     *
     * 获取经纬度信息
     *
     * @param ip ip地址
     * @return String 纬度 经度
     */
    String latLngLookup(String ip);

    /**
     *
     * 获城市与经纬度信息
     *
     * @param ip ip地址
     * @return String 省代码 纬度 经度
     */
    String cityLatLngLookup(String ip);


    /**
     *
     * 获取到城市的3级定位信息
     * @param ip ip地址，点分十进制格式,ipv4
     * @return String 国家代码,省代码,城市代码
     */
    String cityLookupDetail(String ip);

    /**
     *
     * 获取位置信息明细，支持7级
     * @param ip ip地址，点分十进制格式,ipv4
     * @return String 国家代码，省，城市，区/县，街道
     */
    String locationLookupDetail(String ip);

    /**
     * Get A two-character ISO 3166-1 country code for the country associated with the location.
     * @param ip ip地址
     * @return country code
     */
    String countryCodeLookup(String ip);

    /**
     * A string of up to three characters containing the region-portion of the ISO 3166-2 code for the first level region associated with the IP address.
     * @param ip ip地址
     * @return First Level Region Code
     */
    String regionCodeLookup(String ip);

    /**
     * This method to return all fields for CSV.
     * [Location]\t[ISP]\t[Organization]\t[ASN]\t[Latitude,Longitude]
     * -[Location]:Country.SuperAdministrativeArea[Province].AdministrativeArea[City][.SubAdministrativeArea][.Locality][.DependentLocality][.DoubleDependentLocality]
     * -[Latitude,Longitude] : Latitude,Longitude
     * @param ip
     * @return
     */
    String infoLookupToCSV(String ip);

    /**
     * This method to return all fields for JSON String.
     * @param ip
     * @return
     */
     String infoLookupToJSONString(String ip);

    /**
     * This method to return all fields for JSON Object.
     * @param ip
     * @return
     */
    JSONObject infoLookupToJSON(String ip);

    /**
     * This method returns all fields for the MMDB Response Object.
     * @param ip
     * @return
     */
    LocationResponse infoLookup(String ip);

    /**
     *
     * 获取AS号，默认通过IP定位库获取ASN号
     * @param ip ip地址
     * @return String asn号
     */
    String asnLookup(String ip);

    /**
     *
     * 获取IP 所属AS的组织
     * @param ip ip地址
     * @return String asn号
     */
   String asnLookupOrganization(String ip);


    /**
     *
     * 获取AS号与组织信息
     * @param ip ip地址
     * @return asn号 组织名称
     */
   String asnLookupDetail(String ip);

    /**
     *
     * 获取IP 所属AS的组织+国家代码
     * @param ip ip地址
     * @return String asn号
     */
    String asnLookupInfo(String ip);

}
