package com.shoescms.common.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.shoescms.common.model.FileEntity;
import com.shoescms.common.model.repositories.FileRepository;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Component
public class FileUploadService {

    private final Bucket DEFAULT_BUCKET;
    private final String STORAGE_URL = "https://firebasestorage.googleapis.com/v0/b/shoes-f5194.appspot.com/o/";
    private final String DEFAULT_PATH = "tmp/";
    private final FileRepository fileRepository;

    public FileUploadService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
        this.DEFAULT_BUCKET = StorageClient.getInstance().bucket();
//        this.DEFAULT_BUCKET = null;
    }

    private String generatePath(String path, String fileName) {
        if (ObjectUtils.isEmpty(path))
            return DEFAULT_PATH + fileName;
        else
            return path + "/" + fileName;
    }

    private String generateUniqueFileName(String ext) {
        String filename = "";
        long millis = System.currentTimeMillis();
        String rndchars = RandomStringUtils.randomAlphanumeric(16);
        filename = rndchars + "_" + millis + "." + ext;
        return filename;
    }

    public FileEntity uploadFile(String path, MultipartFile file) throws IOException {
        String filePath = generatePath(path, generateUniqueFileName(FilenameUtils.getExtension(file.getOriginalFilename())));
        System.out.println("file path: "+ filePath);
        DEFAULT_BUCKET.create(filePath, file.getInputStream(), file.getContentType());
        return fileRepository.saveAndFlush(new FileEntity(STORAGE_URL + filePath.replaceAll("/", "%2F") + "?alt=media", file.getOriginalFilename(), filePath));
    }
}
