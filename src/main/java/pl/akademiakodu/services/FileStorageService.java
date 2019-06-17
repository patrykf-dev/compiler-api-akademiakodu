package pl.akademiakodu.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.akademiakodu.compiling.JavaProject;

import java.io.*;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private static final String STORAGE =
            Paths.get("D:", "Development", "compiler-api-akademiakodu-results").toString();


    public JavaProject saveProject(int uploadId, MultipartFile[] files) throws IOException {
        String uploadPath = Paths.get(STORAGE, "project-" + formatId(uploadId)).toString();
        String sourcePath = Paths.get(uploadPath, "source").toString();
        String classPath = Paths.get(uploadPath, "validation", "classpath").toString();
        File sourceDirectory = new File(sourcePath);
        sourceDirectory.mkdirs();

        for (MultipartFile file : files) {
            saveSourceFile(file, sourcePath);
        }

        return new JavaProject(sourcePath, classPath, uploadId);
    }


    private String formatId(int idToFormat) {
        return String.format("%05d", idToFormat);
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

    private File createLogFile(String classPath, String fileName) {
        String classParent = new File(classPath).getParent();
        String path = Paths.get(classParent, "logs", fileName).toString();
        File file = new File(path);
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    public File createCompilationOutputFile(JavaProject javaProject) {
        return createLogFile(javaProject.getClassPath(), "compilation-out.txt");
    }

    public File createCompilationErrorFile(JavaProject javaProject) {
        return createLogFile(javaProject.getClassPath(), "compilation-err.txt");
    }


    public File createExecutionOutputFile(JavaProject javaProject) {
        return createLogFile(javaProject.getClassPath(), "execution-out.txt");
    }

    public File createExecutionErrorFile(JavaProject javaProject) {
        return createLogFile(javaProject.getClassPath(), "execution-err.txt");
    }
}
