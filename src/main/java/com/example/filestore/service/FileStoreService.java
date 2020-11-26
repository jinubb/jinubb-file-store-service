package com.example.filestore.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.filestore.entity.File;
import com.example.filestore.model.FileResponse;
import com.example.filestore.repository.FileRepository;

@Service
public class FileStoreService {
	
	@Autowired
	private FileRepository fileRepo;
	
	private Path baseFilePath;
	
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@Autowired
    public FileStoreService(FileStorePath path) {
        baseFilePath = Paths.get(path.getFileBasePath()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(baseFilePath);
        }catch(Exception e) {
            throw new RuntimeException("파일을 업로드할 디렉토리를 생성하지 못했습니다.", e);
        }
    }

	public FileResponse getFileById(Long fileId) throws IOException, MalformedURLException {
		FileResponse res = new FileResponse();
        Resource fileResource = new UrlResource(getFilePath(findFileById(fileId)).toUri());
        if(!fileResource.exists()) {
        	throw new RuntimeException("파일이 존재하지 않습니다.");
        }
        res.setFileResource(fileResource);
        res.setContentType(httpServletRequest.getServletContext().getMimeType(fileResource.getFile().getAbsolutePath()));
		return res;
	}

	private File findFileById(Long fileId) {
		return fileRepo.findById(fileId).orElseThrow(() -> new RuntimeException("등록되지 않은 파일 id입니다."));
	}

	public File uploadFile(MultipartFile uploadFile) throws IOException {
		String fileName = StringUtils.cleanPath(uploadFile.getOriginalFilename());
		isValidFileName(fileName);
        Path filePath = baseFilePath.resolve(fileName);
        Files.copy(uploadFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return saveFile(fileName, uploadFile.getSize());
	}

	private File saveFile(String fileName, long size) {
		File file = new File();
		file.setName(fileName);
		file.setSize(Long.valueOf(size));
		return fileRepo.save(file);
	}

	private boolean isValidFileName(String fileName) {
		if(fileName.contains("..")) {
			throw new RuntimeException(String.format("%s 파일명에 부적합한 문자가 포함되어 있습니다.", fileName));
		}
		return true;
	}

	public File deleteFile(Long fileId) throws IOException {
		File file = findFileById(fileId);
		fileRepo.delete(file);
		Files.delete(getFilePath(file));
		return file;
	}

	private Path getFilePath(File file) {
		return baseFilePath.resolve(file.getName()).normalize();
	}

	public List<File> infoFileList() {
		return fileRepo.findAll();
	}

	public File infoFileDetail(Long fileId) {
		return findFileById(fileId);
	}
}
