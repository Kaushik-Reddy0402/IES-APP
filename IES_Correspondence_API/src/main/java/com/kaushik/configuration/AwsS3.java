package com.kaushik.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AwsS3 {

	@Value("${aws.accessKey}")
	private static String accessKey;

	@Value("${aws.secretKey}")
	private static String secretKey;
	
	@Value("${aws.bucketName")
	public static String bucketName;

	@Bean
	AmazonS3 createClient() {
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
		return AmazonS3ClientBuilder.standard()
				  					  .withRegion(Regions.EU_NORTH_1)
				  					  .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
				  					  .build();
	}
}
