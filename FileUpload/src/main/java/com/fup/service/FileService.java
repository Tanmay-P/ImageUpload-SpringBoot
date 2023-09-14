package com.fup.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

import com.fup.entity.FileDB;

public interface FileService {

	FileDB store(String path, MultipartFile file)  throws IOException;	//receives MultipartFile object, transform to FileDB object and save it to Database
			
	FileDB getFile(String id) throws IOException;	//returns a FileDB object by provided Id
	
	Stream<FileDB> getAllFiles();	//returns all stored files as list of code>FileDB objects
}
