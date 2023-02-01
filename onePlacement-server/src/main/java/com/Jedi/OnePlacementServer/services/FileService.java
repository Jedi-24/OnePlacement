package com.Jedi.OnePlacementServer.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String uploadImage(String path, MultipartFile file, Integer uId) throws IOException; // returns file's name;
    String uploadResume(String path, MultipartFile file, Integer uId) throws IOException;
    String retrieveImage(Integer uId);
    String retrieveResume(Integer uId);
}