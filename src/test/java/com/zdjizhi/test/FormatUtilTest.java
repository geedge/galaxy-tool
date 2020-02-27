package com.zdjizhi.test;

import com.google.common.net.InternetDomainName;
import com.zdjizhi.utils.Encodes;
import com.zdjizhi.utils.FormatUtils;
import com.zdjizhi.utils.StringUtil;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;
import java.util.Base64;

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
        Assert.assertEquals(FormatUtils.getTopPrivateDomain("comcn"), "");

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


    @Test
    public void testBase64Decode() {
        String value = Encodes.encodeBase64("我是", "GB2312");
        Assert.assertEquals(Encodes.decodeBase64String(value, "GB2312"), "我是");

        Assert.assertEquals(Encodes.decodeBase64String( Encodes.encodeBase64("我是UTF-8")), "我是UTF-8");

    }
}
