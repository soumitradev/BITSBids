package com.jamers.BITSBids.controllers;

import com.jamers.BITSBids.common.SeaweedFile;
import com.jamers.BITSBids.common.SeaweedFileAllocation;
import com.jamers.BITSBids.response_types.GenericResponseType;
import com.jamers.BITSBids.response_types.errors.FileUploadError;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Objects;

@Controller
public class MediaController {
	@PostMapping(
					value = "/media/upload"
	)
	public ResponseEntity<GenericResponseType> uploadFile(
					@RequestParam("files")
					MultipartFile[] files) {

		MultiValueMap<String, String> responseMap = new LinkedMultiValueMap<>();
		for (MultipartFile file : files) {
			try {
				InputStream fileStream = file.getInputStream();

				File dir = new File("tmpFiles");
				if (!dir.exists()) {
					boolean success = dir.mkdirs();
					if (!success) {
						return new ResponseEntity<GenericResponseType>(
										new GenericResponseType(
														FileUploadError.fileUploadError(),
														GenericResponseType.ResponseStatus.ERROR
										),
										HttpStatus.INTERNAL_SERVER_ERROR
						);
					}
				}
				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				fileStream.transferTo(stream);
				stream.close();
				fileStream.close();

				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

				HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
				ResponseEntity<SeaweedFileAllocation> result = restTemplate.exchange(
								System.getenv("SEAWEED_MASTER") + "/dir/assign",
								HttpMethod.GET,
								entity,
								SeaweedFileAllocation.class
				);
				SeaweedFileAllocation allocation = result.getBody();
				if (allocation == null) {
					return new ResponseEntity<GenericResponseType>(
									new GenericResponseType(FileUploadError.fileUploadError(), GenericResponseType.ResponseStatus.ERROR),
									HttpStatus.INTERNAL_SERVER_ERROR
					);
				}

				HttpHeaders uploadHeaders = new HttpHeaders();
				headers.setContentType(MediaType.MULTIPART_FORM_DATA);

				MultiValueMap<String, Object> uploadBody = new LinkedMultiValueMap<>();
				uploadBody.add("file", new FileSystemResource(serverFile));

				HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(uploadBody, uploadHeaders);


				RestTemplate uploadRestTemplate = new RestTemplate();
				SeaweedFile response = uploadRestTemplate.postForEntity(
								"http://" + allocation.url + "/" + allocation.fid,
								requestEntity,
								SeaweedFile.class
				).getBody();
				if (response == null) {
					return new ResponseEntity<GenericResponseType>(
									new GenericResponseType(FileUploadError.fileUploadError(), GenericResponseType.ResponseStatus.ERROR),
									HttpStatus.INTERNAL_SERVER_ERROR
					);
				}
				responseMap.add(response.name, System.getenv("PROD_URL") + "/f/" + allocation.fid);
			} catch (Exception e) {
				return new ResponseEntity<GenericResponseType>(
								new GenericResponseType(FileUploadError.fileUploadError(), GenericResponseType.ResponseStatus.ERROR),
								HttpStatus.INTERNAL_SERVER_ERROR
				);
			}

			File dir = new File("tmpFiles");
			if (dir.exists()) {
				if (dir.listFiles() != null) {
					for (File tempFile : Objects.requireNonNull(dir.listFiles())) {
						boolean deleted = tempFile.delete();
						if (!deleted) {
							return new ResponseEntity<GenericResponseType>(
											new GenericResponseType(
															FileUploadError.fileUploadError(),
															GenericResponseType.ResponseStatus.ERROR
											),
											HttpStatus.INTERNAL_SERVER_ERROR
							);
						}
					}
				}
			}
		}

		return new ResponseEntity<GenericResponseType>(new GenericResponseType(
						responseMap,
						GenericResponseType.ResponseStatus.SUCCESS
		), HttpStatus.CREATED);
	}
}
