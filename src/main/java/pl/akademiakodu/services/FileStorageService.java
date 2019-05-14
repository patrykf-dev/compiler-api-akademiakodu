package pl.akademiakodu.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {
    private static final String UPLOADED_FILES_STORAGE = "D:\\Development\\compiler-api-akademiakodu-results\\";

    public FileStorageService() {}

    public Path saveUploadedFile(byte[] fileContent, String originalFilename) throws IOException {
        Path path = Paths.get(UPLOADED_FILES_STORAGE + originalFilename);
        Files.write(path, fileContent);
        return path;
    }

    public String getOutputPathForFile() {
        return UPLOADED_FILES_STORAGE + "output\\result.txt";
    }
}
