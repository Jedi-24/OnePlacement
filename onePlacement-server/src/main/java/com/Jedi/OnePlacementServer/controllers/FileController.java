package com.Jedi.OnePlacementServer.controllers;

import com.Jedi.OnePlacementServer.payloads.FileResponse;
import com.Jedi.OnePlacementServer.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController @RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String imgFolderPath;
    @Value("${project.resume}")
    private String resumeFolderPath;

    @PostMapping("/upload/image")
    public ResponseEntity<FileResponse> imageUpload(@RequestParam("image") MultipartFile image, @RequestParam("userID") Integer uId) throws IOException {
        String imageName = null;
        try {
            imageName = this.fileService.uploadImage(imgFolderPath, image, uId);
        } catch (IOException e){
            return new ResponseEntity<FileResponse>(new FileResponse(null, "Error in uploading Image"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<FileResponse>(new FileResponse(imageName, "Image is Successfully uploaded"), HttpStatus.OK);
    }

    @PostMapping("/upload/resume")
    public ResponseEntity<FileResponse> uploadResume(@RequestParam("resume") MultipartFile resume, @RequestParam("userID") Integer uId) throws IOException {
        String resumeName = null;
        try{
            resumeName = this.fileService.uploadResume(resumeFolderPath,resume, uId);
        } catch (IOException e){
            return new ResponseEntity<FileResponse>(new FileResponse(null,"Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<FileResponse>(new FileResponse(resumeName,"Success"), HttpStatus.OK);
    }

    @GetMapping("/retrieve/image/{userId}")
    public ResponseEntity<FileResponse> retrieveImage(@PathVariable("userId") Integer uId) throws IOException {
        String fPath = this.fileService.retrieveImage(uId);
        File file = new File(fPath);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=profile.jpeg");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(fPath);
        // this resource contains a bytes array;
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path)); // readAllBytes gives me a bytes[];
//        File rFile = new File(resource.getURI());
//        Map<String, byte[]> ret = new HashMap<>();
//        ret.put("Jedi", resource.getByteArray());
//        return ResponseEntity.ok().headers(header).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource.getByteArray());
//        return ResponseEntity.ok().headers(header).contentLength(file.length()).contentType(MediaType.IMAGE_JPEG).body(DatatypeConverter.printBase64Binary());
        FileResponse fileResponse = new FileResponse();
        fileResponse.setFileName(DatatypeConverter.printBase64Binary(resource.getByteArray()));
        fileResponse.setMessage("success ? eh");
        return ResponseEntity.ok().headers(header).body(fileResponse);
    }

    @GetMapping("/retrieve/resume/{userId}")
    public ResponseEntity<Resource> retrieveResume(@PathVariable("userId") Integer uId) throws IOException {
        String fPath = this.fileService.retrieveResume(uId);
        File file = new File(fPath);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=resume.pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(fPath);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok().headers(header).contentLength(file.length()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }
}