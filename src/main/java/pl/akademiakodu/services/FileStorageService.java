package pl.akademiakodu.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.akademiakodu.models.JavaProject;

import java.io.*;

@Service
public class FileStorageService {

    private static final String FILES_STORAGE = "D:\\Development\\compiler-api-akademiakodu-results\\";


    public File createOutputFile(JavaProject javaProject) throws IOException {
        String path = javaProject.getClassPath() + "\\result-out.txt";
        File file = new File(path);
        file.getParentFile().mkdir();
        file.createNewFile();
        return file;
    }

    public File createErrorFile(JavaProject javaProject) throws IOException {
        String path = javaProject.getClassPath() + "\\result-err.txt";
        File file = new File(path);
        file.getParentFile().mkdir();
        file.createNewFile();
        return file;
    }

    public JavaProject saveProject(int uploadId, MultipartFile[] files) throws IOException {
        String sourcePath = FILES_STORAGE + "uploads\\" + JavaProject.formatId(uploadId) + "-source";
        String classPath = FILES_STORAGE + "output\\" + JavaProject.formatId(uploadId) + "-compilation";
        File sourceDirectory = new File(sourcePath);
        sourceDirectory.mkdirs();

        for (MultipartFile file : files) {
            saveSourceFile(file, sourcePath);
        }

        return new JavaProject(sourcePath, classPath, uploadId);
    }

    private void saveSourceFile(MultipartFile file, String sourcePath) throws IOException {
        String childDirectories = extractChildDirectories(file);
        new File(sourcePath + childDirectories).mkdirs();
        String fileName = file.getOriginalFilename();
        OutputStream out = new FileOutputStream(new File(sourcePath + childDirectories + fileName));
        out.write(file.getBytes());
        out.close();
    }

    private String extractChildDirectories(MultipartFile file) {
        String javaPackageKeyWord = "package";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(javaPackageKeyWord)) {
                    int startIndex = line.indexOf(javaPackageKeyWord) + javaPackageKeyWord.length() + 1;
                    int endIndex = line.indexOf(";");
                    String packages = line.substring(startIndex, endIndex);
                    return "\\" + packages.replace(".", "\\") + "\\";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
