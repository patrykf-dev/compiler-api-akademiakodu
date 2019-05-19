package pl.akademiakodu.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.akademiakodu.models.JavaProject;

import java.io.*;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private static final String STORAGE =
            Paths.get("D:", "Development", "compiler-api-akademiakodu-results").toString();


    public File createOutputFile(JavaProject javaProject) throws IOException {
        String path = Paths.get(javaProject.getClassPath(), "result-out.txt").toString();
        File file = new File(path);
        file.getParentFile().mkdir();
        file.createNewFile();
        return file;
    }

    public File createErrorFile(JavaProject javaProject) throws IOException {
        String path = Paths.get(javaProject.getClassPath(), "result-err.txt").toString();
        File file = new File(path);
        file.getParentFile().mkdir();
        file.createNewFile();
        return file;
    }

    public JavaProject saveProject(int uploadId, MultipartFile[] files) throws IOException {
        String sourcePath = Paths.get(STORAGE, "uploads", JavaProject.formatId(uploadId) + "-source").toString();
        String classPath = Paths.get(STORAGE, "output", JavaProject.formatId(uploadId) + "-compilation").toString();
        File sourceDirectory = new File(sourcePath);
        sourceDirectory.mkdirs();

        for (MultipartFile file : files) {
            saveSourceFile(file, sourcePath);
        }

        return new JavaProject(sourcePath, classPath, uploadId);
    }

    private void saveSourceFile(MultipartFile file, String sourcePath) throws IOException {
        String[] childDirectories = extractChildDirectories(file);
        String pathname = Paths.get(sourcePath, childDirectories).toString();
        new File(pathname).mkdirs();
        String fileName = file.getOriginalFilename();
        OutputStream out = new FileOutputStream(new File(Paths.get(pathname, fileName).toString()));
        out.write(file.getBytes());
        out.close();
    }

    private String[] extractChildDirectories(MultipartFile file) {
        String javaPackageKeyWord = "package";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(javaPackageKeyWord)) {
                    int startIndex = line.indexOf(javaPackageKeyWord) + javaPackageKeyWord.length() + 1;
                    int endIndex = line.indexOf(";");
                    String packages = line.substring(startIndex, endIndex);
                    return packages.split("\\.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
