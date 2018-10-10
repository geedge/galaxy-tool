package com.zdjizhi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName AbstractIpLookup
 * @Description 对IP定位查找抽象类，规定可执行方法
 * @Author dell
 * @Date 2018-10-9 19:20
 * @Version 1.0
 **/
public abstract class AbstractIpLookup {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected static final String UNKNOW  =  "N/A";
    /**
     * 给定IP库文件路径，获取国家代码
     * @param ip ip地址
     * @return String 国家代码
     */
    public abstract String countryLookup(String ip);

    /**
     *
     * 获取详细定位信息
     *
     * @param ip ip地址，点分十进制格式,ipv4
     * @return String 城市代码 省代码 国家代码
     */
    public abstract String cityLookupDetail(String ip);


    /**
     *
     *  获取所属省或市信息
     *
     * @param ip ip地址
     * @return String 城市|省代码
     */
    public abstract String cityLookup(String ip);


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
     * 获取AS号
     * @param ip ip地址
     * @return String 组织名称
     */
    public abstract String asnLookup(String ip);


    /**
     *
     * 获取AS号与组织信息
     * @param ip ip地址
     * @return 组织名称
     */
    public abstract String asnLookupDetail(String ip);



}
