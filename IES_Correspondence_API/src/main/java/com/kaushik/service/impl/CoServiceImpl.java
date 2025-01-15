package com.kaushik.service.impl;

import java.io.File;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.kaushik.configuration.AwsS3;
import com.kaushik.entities.EligibilityEntity;
import com.kaushik.entities.NoticeEntity;
import com.kaushik.respository.AppRepo;
import com.kaushik.respository.EligibilityRepo;
import com.kaushik.respository.NoticeRepo;
import com.kaushik.service.CoService;
import com.kaushik.utils.EmailUtils;
import com.kaushik.utils.PdfUtils;

@Service
public class CoServiceImpl implements CoService {

	@Autowired
	private NoticeRepo noticeRepo;

	@Autowired
	private EligibilityRepo eligibilityRepo;

	@Autowired
	private AppRepo appRepo;

	@Autowired
	private EmailUtils emailUtils;

	@Autowired
	private PdfUtils pdfUtils;

	@Autowired
	private AmazonS3 s3Client;

	@Override
	public void processNotices() throws Exception {
		// fetch the notices from the database table
		List<NoticeEntity> notices = noticeRepo.findByNoticeStatus("PENDING");

		// based on the status of the notices generate the pdf for the notices
		for (NoticeEntity noticeEntity : notices) {
			processRecord(noticeEntity);
		}
		// store the generated pdf in the AWS S3 bucket

		// send the email by attaching the pdf to it

		// update the url and status of the notices in the notices table

	}

	private void processRecord(NoticeEntity noticeEntity) throws Exception {
		Long caseNo = noticeEntity.getCaseNo();

		EligibilityEntity eligEntity = Optional.ofNullable(eligibilityRepo.findByCaseNo(caseNo))
				.orElseThrow(() -> new IllegalArgumentException("Eligibility entity not found"));

		
		File pdfFile = switch (eligEntity.getPlanStatus()) {
						case "APPROVED" -> pdfUtils.generateApprovedPdf(eligEntity, caseNo);
						case "DENIED" -> pdfUtils.generateDeniedPdf(eligEntity, caseNo);
						default -> null;
		};

		if (pdfFile != null) {
			// Storing PDF in S3 and update the processed record
			String url = storePdfInS3(pdfFile);
			if (updateProcessedRecord(noticeEntity, url)) {
				// Sending email with the generated PDF
				appRepo.findById(caseNo).ifPresent(appEntity -> {
					try {
						emailUtils.emailSender("IES", "Application Notice", appEntity.getEmail(), pdfFile);
					} catch (Exception e) {
						throw new RuntimeException();
					}
				});
			}
		}
	}

	private boolean updateProcessedRecord(NoticeEntity noticeEntity, String url) {
		noticeEntity.setNoticeStatus("COMPLETED");
		noticeEntity.setNoticeUrl(url);
		return true;
	}

	private String storePdfInS3(File pdfFile) {
		try {
			// Generating a unique name for the file to avoid conflicts
			String fileName = System.currentTimeMillis() + "_" + pdfFile.getName();

			// Uploading the file to S3
			s3Client.putObject(new PutObjectRequest(AwsS3.bucketName, fileName, pdfFile));

			// Generating the URL for the uploaded file
			String fileUrl = s3Client.getUrl(AwsS3.bucketName, fileName).toString();

			return fileUrl;
		} catch (Exception e) {
			throw new RuntimeException("Failed to upload the file to S3", e);
		}
	}

}
