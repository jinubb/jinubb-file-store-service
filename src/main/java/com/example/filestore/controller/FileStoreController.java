package com.example.filestore.controller;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.common.ResponseContainer;
import com.example.filestore.entity.File;
import com.example.filestore.model.FileResponse;
import com.example.filestore.service.FileStoreService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/filestore/api")
public class FileStoreController {
	
	private static Logger logger = LoggerFactory.getLogger(FileStoreController.class);

	@Autowired
	private FileStoreService service;
	
	@ApiOperation(value = "Download file")
	@GetMapping(value = "/download/{fileId}")
	public ResponseEntity<Resource> downloadFile(@PathVariable("fileId")Long fileId) throws MalformedURLException, IOException {
		FileResponse res = service.getFileById(fileId);
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(res.getContentType()))
				//요청시 다운로드
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + res.getFileResource().getFilename() + "\"")
                .body(res.getFileResource());
	}
	
	@ApiOperation(value = "Get file")
	@GetMapping(value = "/get/{fileId}")
	public ResponseEntity<Resource> getFile(@PathVariable("fileId")Long fileId) throws MalformedURLException, IOException {
		FileResponse res = service.getFileById(fileId);
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(res.getContentType()))
                .body(res.getFileResource());
	}
	
	@ApiOperation(value = "Upload file")
	@PostMapping("/upload")
	public ResponseContainer<File> uploadloadFile(@RequestParam MultipartFile uploadFile) {
		ResponseContainer<File> response = ResponseContainer.emptyResponse();		
		try {
			response.setPayload(service.uploadFile(uploadFile));
		} catch(Exception e) {
			logger.error("upload:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	
	@ApiOperation(value = "Delete file")
	@DeleteMapping(value = "/delete/{fileId}")
	public ResponseContainer<File> deleteFile(@PathVariable("fileId")Long fileId) {
		ResponseContainer<File> response = ResponseContainer.emptyResponse();		
		try {
			response.setPayload(service.deleteFile(fileId));
		} catch(Exception e) {
			logger.error("delete:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	
	@ApiOperation(value = "File list")
	@GetMapping(value = "/info/list")
	public ResponseContainer<List<File>> infoFileList() {
		ResponseContainer<List<File>> response = ResponseContainer.emptyResponse();		
		try {
			response.setPayload(service.infoFileList());
		} catch(Exception e) {
			logger.error("list:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	
	@ApiOperation(value = "File detail")
	@GetMapping(value = "/info/{fileId}/detail")
	public ResponseContainer<File> infoFileDetail(@PathVariable("fileId")Long fileId) {
		ResponseContainer<File> response = ResponseContainer.emptyResponse();		
		try {
			response.setPayload(service.infoFileDetail(fileId));
		} catch(Exception e) {
			logger.error("detail:\n{}",e);
			response.setError(e);
		}
		return response;
	}
}
