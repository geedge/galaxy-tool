package com.geedgenetworks.test;

import com.geedgenetworks.utils.Encodes;
import com.geedgenetworks.utils.FormatUtils;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FormatUtilTest {
	@Test
    public void testGetTopDomain() {
        Assert.assertEquals(FormatUtils.getTopPrivateDomain("https://pan.baidu.com/s/1wejv5ZceAL0NQGExvPQm-Q#list/path=%2F&parentPath=%2Fsharelink3477893581-1028235543414583"), "baidu.com");
        Assert.assertEquals(FormatUtils.getTopPrivateDomain("www.baidu.com"), "baidu.com");
        Assert.assertEquals(FormatUtils.getTopPrivateDomain("www.baidu.com.cn"), "baidu.com.cn");
        Assert.assertEquals(FormatUtils.getTopPrivateDomain("www.baidu.cc"), "baidu.cc");
        Assert.assertEquals(FormatUtils.getTopPrivateDomain("baidu.com.hk"), "baidu.com.hk");
        Assert.assertEquals(FormatUtils.getTopPrivateDomain("baidu.com.hk:80/abc.html"), "baidu.com.hk");
        Assert.assertEquals(FormatUtils.getTopPrivateDomain("&&&&Dddddsdd"), "");
        Assert.assertEquals(FormatUtils.getTopPrivateDomain("com.cn"), "");
		Assert.assertNotEquals(FormatUtils.getTopPrivateDomain("playetoms-pa.googleapis.com"), "googleapis.com");
        Assert.assertNotEquals(FormatUtils.getTopPrivateDomain("com.io"), "com.io");



	}



    @Test
    public void testGetDomain() {
        Assert.assertEquals(FormatUtils.getDomain("https://pan.baidu.com/s/1wejv5ZceAL0NQGExvPQm-Q#list/path=%2F&parentPath=%2Fsharelink3477893581-1028235543414583"), "pan.baidu.com");
        Assert.assertEquals(FormatUtils.getDomain("www.baidu.com"), "baidu.com");
        Assert.assertEquals(FormatUtils.getDomain("www.baidu.com.cn"), "baidu.com.cn");
        Assert.assertEquals(FormatUtils.getDomain("www.baidu.cc"),"baidu.cc");
        Assert.assertEquals(FormatUtils.getDomain("com.cn"), "com.cn");
        Assert.assertEquals(FormatUtils.getDomain("baidu.com.hk:80/abc.html"), "baidu.com.hk");

    }

	@Test(expected=IllegalArgumentException.class)
	public void testGetDomainExpectedException() {
		System.out.println(FormatUtils.getDomain("http:///test"));
		System.out.println(FormatUtils.getDomain("https:///test"));
		System.out.println(FormatUtils.getDomain("/test"));
		System.out.println(FormatUtils.getDomain("www./test"));
		System.out.println(FormatUtils.getDomain("http://www./test"));
	}




    @Test
    public void testBase64Decode() {
        String value = Encodes.encodeBase64("我是", "GB2312");
        Assert.assertEquals(Encodes.decodeBase64String(value, "GB2312"), "我是");

        Assert.assertEquals(Encodes.decodeBase64String( Encodes.encodeBase64("我是UTF-8")), "我是UTF-8");

    }


	@Test
	public void testGetEmailDomain() {

		Assert.assertEquals(FormatUtils.getEmailDomain("zhansan@gmail.com"), "gmail.com");
		Assert.assertEquals(FormatUtils.getEmailDomain("zhansan"), "");
		Assert.assertEquals(FormatUtils.getEmailDomain("@gmail.com"), "gmail.com");


	}

//    @Test
	public void snowFlakeIdWriteTest() throws IOException {
		FormatUtils build = new FormatUtils.Builder(false).build();
		File file = new File("D:\\123\\test.txt");
		List list = new ArrayList<Long>();
		for (int i = 1; i <= 500000; i++) {
			try {
			//单机
			long snowflakeId = build.getSnowflakeId(1, 128);
			//集群模式
//			long snowflakeId = build.getSnowflakeId("192.168.40.224:2181", 12);
//				set.add(snowflakeId);
				list.add(snowflakeId);
				if(i%10000==0) {
					FileUtils.writeLines(file, list);
				}
				System.err.println(snowflakeId);
//				Thread.sleep(1000L);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.err.println("=====完成=======");
	}

//    @Test
	public void snowFlakeIdRepeatTest() throws IOException {
		Set set = new HashSet<Long>();
		File file = new File("D:\\123\\test.txt");
		File file2 = new File("D:\\123\\test2.txt");
		List<String> readLines = FileUtils.readLines(file);
		System.out.println(readLines.size());
		for (String object : readLines) {
			set.add(object);
		}
		List<String> readLines2 = FileUtils.readLines(file2);
		System.out.println(readLines2.size());
		for (String object : readLines2) {
			set.add(object);
		}
		System.err.println(readLines.size()+readLines2.size()+"合计："+set.size());
	}
}
