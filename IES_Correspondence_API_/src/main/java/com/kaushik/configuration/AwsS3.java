package com.kaushik.configuration;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AwsS3 {
	
	@Autowired
	private AwsConfiguration awsProperties;

	@Bean
	AmazonS3 amazonS3() {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsProperties.getAccessKey(),
				awsProperties.getSecretKey());

		return AmazonS3ClientBuilder.standard().withRegion(awsProperties.getRegion())
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
	}
}
