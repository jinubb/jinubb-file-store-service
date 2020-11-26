package com.example.filestore.model;

import org.springframework.core.io.Resource;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileResponse {
	private Resource fileResource;
	private String contentType;
}
