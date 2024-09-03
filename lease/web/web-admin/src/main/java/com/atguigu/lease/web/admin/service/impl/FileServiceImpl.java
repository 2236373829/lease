package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.common.minio.MinioProperties;
import com.atguigu.lease.web.admin.service.FileService;
import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioProperties minioProperties;

    @Override
    public String uploadFile(MultipartFile file) throws ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        // 判断bucket是否存在
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.getBucketName())
                .build());
        if (!bucketExists) {
            //创建bucket
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .build());
        }

        // 设置bucket权限（自己读写，所有人读）
        minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                .bucket(minioProperties.getBucketName())
                .config(this.createBucketPolicyConfig(minioProperties.getBucketName()))
                .build());

        // 上传文件
        String fileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                + "/" + UUID.randomUUID() + file.getOriginalFilename();

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minioProperties.getBucketName())
                .stream(file.getInputStream(), file.getSize(), -1)
                .object(fileName)
                .contentType(file.getContentType())
                .build());

        // String url = minioProperties.getEndpoint() + "/" + minioProperties.getBucketName() + "/" + fileName;

        // String.join(delimiter, elements) delimiter: 分隔符 elements: 可变长度元素
        return String.join("/", minioProperties.getEndpoint(), minioProperties.getBucketName(), fileName);
    }

    // minio bucket权限
    private String createBucketPolicyConfig(String bucketName) {
        return """
                {
                     "Statement" : [{
                         "Action" : "s3:GetObject",
                         "Effect" : "Allow",
                         "Principal" : "*",
                         "Resource" : "arn:aws:s3:::%s/*"
                     }],
                     "Version" : "2012-10-17"
                }
                """.formatted(bucketName);
    }
}
