package com.Jedi.OnePlacementServer.services.Impl;

import com.Jedi.OnePlacementServer.entities.User;
import com.Jedi.OnePlacementServer.exceptions.ResourceNotFoundException;
import com.Jedi.OnePlacementServer.repositories.UserRepo;
import com.Jedi.OnePlacementServer.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public String uploadImage(String path, MultipartFile file, Integer uId) throws IOException {
        // Full Path
        String fPath=path+File.separator+uId;

        // Create Folder (if not created)
        File f = new File(path);
        if(!f.exists()) f.mkdir();

        // copy file
        Files.copy(file.getInputStream(), Paths.get(fPath), StandardCopyOption.REPLACE_EXISTING);
        updateUser(uId,fPath, "I");

        return uId.toString();
    }

    @Override
    public String uploadResume(String path, MultipartFile file, Integer uId) throws IOException {
        String fileName = file.getOriginalFilename();
        String randomUUID = UUID.randomUUID().toString();
        assert fileName != null;
        String fName_rand = randomUUID.concat(fileName.substring(fileName.lastIndexOf('.')));

        String fPath = path+File.separator+fName_rand;

        File f = new File(path);
        if(!f.exists()) f.mkdir();

        // copy file
        Files.copy(file.getInputStream(), Paths.get(fPath));
        updateUser(uId,fPath, "R");

        return fileName;
    }

    @Override
    public String retrieveImage(Integer uId) {
        User user = this.userRepo.findById(uId)
                .orElseThrow(()-> new ResourceNotFoundException("User","Id",uId));
        return user.getProfilePath();
    }

    @Override
    public String retrieveResume(Integer uId) {
        User user = this.userRepo.findById(uId)
                .orElseThrow(()-> new ResourceNotFoundException("User","Id",uId));
        return user.getResumePath();
    }

    private void updateUser(Integer uId, String fPath, String from){
        System.out.println("I CAME HERE " + uId);
        User user = this.userRepo.findById(uId)
                .orElseThrow(()-> new ResourceNotFoundException("User","Id",uId));

        if(from.matches("I"))
            user.setProfilePath(fPath);
        else
            user.setResumePath(fPath);
        this.userRepo.save(user);
    }
}