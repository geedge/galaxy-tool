package com.zdjizhi.utils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.InternetDomainName;

import java.util.Set;

public class FormatUtils {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 将List&lt;HashMap&lt;String,Object&gt;&gt;中HashMap的key全部转化为变量形式，<br>
	 * 如{"BRH_ID":"1234","BRH_NAME":"机构"}改为{"brhId":"1234","brhName":"机构"}
	 *
	 * @param list
	 * @return List<HashMap<String, Object>>
	 */
	public static List<HashMap<String, Object>> formatHashMapKeyInList(List<HashMap<String, Object>> list) {
		List result = new ArrayList();
		// 遍历list后进行格式化列表元素
		if (list != null) {
			for (HashMap<String, Object> h : list) {
				// 将hashMap的key转化为驼峰格式，如{"BRH_ID":"1234"}改为{"brhId":"1234"}
				result.add(formatHashMapKey(h));
			}
		}
		return result;
	}

	/**
	 * 将hashMap的key转化为驼峰格式，如{"BRH_ID":"1234","BRH_NAME":"机构"}改为{"brhId":"1234",
	 * "brhName":"机构"}
	 *
	 * @param hashMap
	 * @return HashMap<String, Object>
	 */
	public static HashMap<String, Object> formatHashMapKey(HashMap<String, Object> hashMap) {
		HashMap result = new HashMap();
		String key = null;
		// 遍历map
		for (Entry<String, Object> e : (Set<Entry<String, Object>>) hashMap.entrySet()) {
			key = (String) e.getKey();
			// 将hashMap的key转化为驼峰格式
			key = formatDBNameToVarName(key);
			// 封装为新的map
			result.put(key, e.getValue());
		}
		// 返回格式化后的map
		return result;
	}

	/**
	 * 数据库列名转化为属性名,如DEAL_ID=dealId; <br>
	 * 不能保证完全正确，如DBUTIL不会智能的转化为DBUtil,而会转化为dbutil, <br>
	 * 规则为全部转化为单词，然后首字母小写
	 *
	 * @param DBName
	 * @return String
	 */
	public static String formatDBNameToVarName(String DBName) {
		StringBuilder result = new StringBuilder("");
		// 以"_"分割
		String[] DBNameArr = DBName.split("_");
		for (int i = 0, j = DBNameArr.length; i < j; i++) {
			// 获取以"_"分割后的字符数组的每个元素的第一个字母，
			result.append(DBNameArr[i].charAt(0));
			// 将其他字符转换成小写
			result.append(DBNameArr[i].substring(1).toLowerCase());
		}
		char c0 = result.charAt(0);
		if (c0 >= 'A' && c0 <= 'Z') {
			c0 = (char) (c0 + 'a' - 'A');
		}
		result.setCharAt(0, c0);
		return result.toString();
	}

	private FormatUtils(Builder builder) {
		init(builder);
	}

	private synchronized void init(Builder builder) {
		try {

			if (builder.isDefalult) {
				logger.info("CompleteUtil begin with default PZ.");
			} else {
				logger.info("CompleteUtil begin with user-defined PZ.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final class Builder {
		boolean isDefalult;

		public Builder(boolean isDefalult) {
			this.isDefalult = isDefalult;
		}

		public FormatUtils build() {
			return new FormatUtils(this);
		}

	}

	/**
	 * 有sni通过sni获取域名，有host根据host获取域名
	 *
	 * @param sni  sni
	 * @param host host
	 * @return 顶级域名
	 */
	public static String getTopDomain(String sni, String host) {
		if (StringUtil.isNotBlank(host)) {
			return getDomainName(host);
		} else if (StringUtil.isNotBlank(sni)) {
			return getDomainName(sni);
		} else {
			return "";
		}
	}

	/**
	 * 根据url截取顶级域名
	 *
	 * @param host 网站url
	 * @return 顶级域名
	 */
	public static String getDomainName(String host) {
		String domain = "";
		domain = InternetDomainName.from(host).topPrivateDomain().toString();
		return domain;
	}

	/**
	 * 生成当前时间戳的操作
	 */
	public static int getCurrentTime() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	/**
	 * 雪花模型生成id
	 *
	 * @return
	 */
	public static long getSnowflakeId(String zookeeperIp, String kafkaTopic, long dataCenterIdNum) {

		return SnowflakeId.generateId(zookeeperIp, kafkaTopic, dataCenterIdNum);
	}

	/**
	 * 根据ip定位库的地址返回一个ipLookup工具类对象
	 * 
	 * @param ipLibrary
	 * @return ipLookup
	 */
	public static IpLookup getIpLookup(String ipLibrary) {

		IpLookup ipLookup = new IpLookup.Builder(false).loadDataFileV4(ipLibrary + "Kazakhstan.mmdb")
				.loadDataFileV6(ipLibrary + "Kazakhstan.mmdb").loadAsnDataFileV4(ipLibrary + "asn_v4.mmdb")
				.loadAsnDataFileV6(ipLibrary + "asn_v6.mmdb").build();

		return ipLookup;
	}

	/**
	 * 根据clientIp获取location信息
	 *
	 * @param ip
	 * @return
	 */
	public static String getGeoIpDetail(String ip, IpLookup ipLookup) {

		return ipLookup.cityLookupDetail(ip);
	}

	/**
	 * 根据ip获取asn信息
	 *
	 * @param ip
	 * @return asn
	 */
	public static String getGeoAsn(String ip, IpLookup ipLookup) {

		return ipLookup.asnLookup(ip, true);
	}

	/**
	 * 根据ip获取country信息
	 *
	 * @param ip
	 * @return country
	 */
	public static String getGeoIpCountry(String ip, IpLookup ipLookup) {

		return ipLookup.countryLookup(ip);
	}

	/**
	 * 根据ip去hbase中匹配对应的用户名
	 *
	 * @param clientIp
	 * @param hbaseZookeeper
	 * @param hbaseTable
	 * @return 用户名 subscriber_id
	 */
	public static String radiusMatch(String clientIp, String hbaseZookeeper, String hbaseTable) {
		return HBaseUtils.getAccount(clientIp, hbaseZookeeper, hbaseTable);
	}

	/**
	 * base64 解码
	 *
	 * @param encodedText    mail subject
	 * @param subjectCharset 编码格式
	 * @return 解码内容 / 空
	 */
	public static String base64Str(String encodedText, String subjectCharset) {
		Base64.Decoder decoder = Base64.getDecoder();
		String sub;
		try {
			if (StringUtil.isNotBlank(subjectCharset)) {
				sub = new String(decoder.decode(encodedText), subjectCharset);
			} else {
				sub = new String(decoder.decode(encodedText), "UTF-8");
			}
			return sub;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
}
