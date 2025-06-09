package com.kaushik.service.impl;

import java.io.File;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.kaushik.configuration.AwsConfiguration;
import com.kaushik.entities.AppEntity;
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

	@Autowired
	private AwsConfiguration awsProperties;

	@Override
	public void processNotices() throws Exception {
		// fetch the notices from the database table
		List<NoticeEntity> notices = noticeRepo.findByNoticeStatus("PENDING");

		// based on the status of the notices generate the pdf for the notices
		for (NoticeEntity noticeEntity : notices) {
			processRecord(noticeEntity);
		}
	
	}

	private void processRecord(NoticeEntity noticeEntity) throws Exception {
		Long caseNo = noticeEntity.getCaseNo();
		AppEntity appEntity = appRepo.findById(caseNo).get();

		EligibilityEntity eligEntity = Optional.ofNullable(eligibilityRepo.findByCaseNo(appEntity))
				.orElseThrow(() -> new IllegalArgumentException("Eligibility entity not found for case no: " + caseNo));

		// Generate PDF based on plan status
		File pdfFile = switch (eligEntity.getPlanStatus()) {
		case "APPROVED" -> pdfUtils.generateApprovedPdf(eligEntity, caseNo);
		case "DENIED" -> pdfUtils.generateDeniedPdf(eligEntity, caseNo);
		default -> null;
		};

		if (pdfFile != null) {

			// Upload to S3 (assume s3Service.upload returns a public URL)
			String url = upload(pdfFile);

			// Update notice record with URL and status
			if (updateProcessedRecord(noticeEntity, url)) {
				// Send email with PDF attached
				try {
					emailUtils.emailSender("IES", "Application Notice", "mpjuturu71@gmail.com", pdfFile);
				} catch (Exception e) {
					throw new RuntimeException("Failed to send email", e);
				}
			}
		}
	}

	private boolean updateProcessedRecord(NoticeEntity noticeEntity, String url) {
		noticeEntity.setNoticeStatus("COMPLETED");
	    noticeEntity.setNoticeUrl(url);
		noticeRepo.save(noticeEntity); 
		return true;
	}

	private String upload(File pdfFile) {
		try {
            String fileName = UUID.randomUUID() + "_" + pdfFile.getName();
            String bucketName = awsProperties.getS3().getBucketName();

            PutObjectRequest request = new PutObjectRequest(bucketName, fileName, pdfFile);
                    

            s3Client.putObject(request);

            return s3Client.getUrl(bucketName, fileName).toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
	}

}
