package com.fup.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fup.entity.FileDB;
import com.fup.response.ResponseFile;
import com.fup.response.ResponseMessage;
import com.fup.service.FileServiceImpl;


@RestController
@RequestMapping("/file")
@CrossOrigin(origins = "http://localhost:3000/")
public class FileController {

	@Autowired
	private FileServiceImpl fileService;
	
	@Value("${project.image}")
	private String path;
	
	@PostMapping(value = "/upload",  consumes="multipart/form-data")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("image") MultipartFile image) {
	
		String message = "";
	    try {
	      //fileService.store(path,image);
	      message = "Uploaded the file successfully: " + image.getOriginalFilename();
	      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	    } catch (Exception e) {
	      message = "Could not upload the file: " + image.getOriginalFilename() + "!";
	      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	    }
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<ResponseFile>> getFiles() {
		
		List<ResponseFile> files = fileService.getAllFiles().map(dbFile -> {
		      String fileDownloadUri = ServletUriComponentsBuilder
		          .fromCurrentContextPath()
		          .path("/images/")
		          .path(dbFile.getId())
		          .toUriString();

		      return new ResponseFile(
		    	  dbFile.getId(),
		          dbFile.getName(),
		          fileDownloadUri,
		          dbFile.getType(),
		          dbFile.getData().length);
		    }).collect(Collectors.toList());

		    return ResponseEntity.status(HttpStatus.OK).body(files);
		
		//return new ResponseEntity<>(fileService.getAllFiles(), HttpStatus.OK);
	}
	
	@GetMapping(value="/{id}", produces=MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> getFile(@PathVariable String id) throws IOException {
	    FileDB fileDB = fileService.getFile(id);

	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
	        .body(fileDB.getData());
	}
	
}
