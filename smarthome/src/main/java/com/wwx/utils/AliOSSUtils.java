package com.wwx.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

/**
 * 阿里云 OSS 工具类
 */
@Component
public class AliOSSUtils {

    // @Value("${aliyun.oss.endpoint}")
    // private String endpoint;
    // @Value("${aliyun.oss.accessKeyId}")
    // private String accessKeyId;
    // @Value("${aliyun.oss.accessKeySecret}")
    // private String accessKeySecrect;
    // @Value("${aliyun.oss.bucketName}")
    // private String bucketName;

    @Autowired
    private AliOSSProperties aliOSSProperties;
    /**
     * 实现上传图片到OSS
     */
    public String upload(MultipartFile file) throws IOException {
        // 获取阿里云OSS的配置信息
        String endpoint = aliOSSProperties.getEndpoint();
        String accessKeyId = aliOSSProperties.getAccessKeyId();
        String accessKeySecret = aliOSSProperties.getAccessKeySecret();
        String bucketName = aliOSSProperties.getBucketName();
        // 获取上传的文件的输入流
        InputStream inputStream = file.getInputStream();

        // 避免文件覆盖
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        //上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, fileName, inputStream);

        //文件访问路径
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;
        // 关闭ossClient
        ossClient.shutdown();
        return url;// 把上传到oss的路径返回
    }

}
