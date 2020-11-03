package com.zdjizhi.test;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.google.common.collect.Maps;
import com.zdjizhi.utils.StringUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.Map;

public class S3Test {
    @Test
    public void testInit() {
        String endpoint = "192.168.40.224:9098";
        String accessKey = "accessKey";
        String secretKey = "secretKey";
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "us-east-1"))
                .withChunkedEncodingDisabled(true)
                .build();

        Assert.assertNotNull(s3);

        s3.shutdown();

    }


    @Test
    public void testPutObject() {
        String endpoint = "http://192.168.40.224:9098/hos";
        String accessKey = "accessKey";
        String secretKey = "secretKey";
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "us-east-1"))
                .withChunkedEncodingDisabled(true)
                .build();


        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("txt/html");
        Map<String, String> userMap = Maps.newHashMap();

        //userMap.put("x-hos-meta-message", "{\"name\":\"我是元数据\"}");
       // metadata.setUserMetadata(userMap);
        metadata.setHeader("x-hos-meta-message", "{\"name\":\"我是元数据\"}");
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File("/Users/darnell/Downloads/存储配额.pdf"));
            PutObjectResult result = s3.putObject("file", "storage.pdf", inputStream, metadata);

            System.out.println(result.getMetadata().toString());
            Assert.assertNotNull(result.getContentMd5());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        s3.shutdown();

    }


    @Test
    public void testGetObject() {

        String endpoint = "http://192.168.40.224:9098/hos";
        String accessKey = "accessKey";
        String secretKey = "secretKey";
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "us-east-1"))
                .withChunkedEncodingDisabled(true)
                .build();

        S3Object object = s3.getObject("file", "storage.pdf");
        System.out.println(object.getObjectMetadata().getContentType());
        System.out.println(object.getObjectMetadata().getRawMetadataValue("x-hos-meta-message"));


       InputStream in = null;
       OutputStream out = null;
        try {


            try {
                in = new BufferedInputStream(object.getObjectContent(),	1024);
                out = new BufferedOutputStream(new FileOutputStream(new File("/Users/darnell/Downloads/" + StringUtil.createUUID() + object.getKey())),1024);
                byte[] buffer = new byte[1024];

                int length = -1;
                while((length = in.read(buffer)) != -1)
                {
                    out.write(buffer, 0, length);
                }

                out.flush();
            } finally {
                if (null != in) {
                    in.close();
                }
                if (null != out) {
                    out.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("上传文件时出错！！！");
        }


        s3.shutdown();






    }

}
