package com.fup.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fup.entity.FileDB;
import com.fup.respository.FileRepository;

import org.springframework.util.StringUtils;

@Service
public class FileServiceImpl implements FileService{
	
	@Autowired
	private FileRepository fileRepo;

	@Override
	public FileDB store(String path, MultipartFile file) throws IOException {
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		String filePath = path + File.separator + fileName;	//full path
		
		File f = new File(path);		//create folder if not created
			if(!f.exists()) {
				f.mkdir();
			}
		
	    FileDB fileDB = new FileDB(fileName, file.getContentType(), file.getBytes());
	    
	    Files.copy(file.getInputStream(), Paths.get(filePath)); //throws exception

	    return fileRepo.save(fileDB);
		
		
//		String name = file.getOriginalFilename();	//file name
//		
//		
//		String filePath = path + File.separator + name;		//full path
//		
//		
//		
//		File f = new File(path);		//create folder if not created
//		if(!f.exists()) {
//			f.mkdir();
//		}
//		
//		
//		Files.copy(file.getInputStream(), Paths.get(filePath)); //throws exception		//file copy	
//		
//		FileDB fi = new FileDB();
//		fi.setName(name);
//		
//		fileRepo.save(fi);
//		
//		return name;
	}

	@Override
	public Stream<FileDB> getAllFiles() {
		
		return fileRepo.findAll().stream();
	}

	@Override
	public FileDB getFile(String id) throws IOException {
		
		return fileRepo.findById(id).get();
	}

	
}
