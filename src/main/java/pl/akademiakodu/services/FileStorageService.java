package pl.akademiakodu.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.akademiakodu.compiling.ProjectDetails;

import java.io.*;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private static final String STORAGE =
            Paths.get("D:", "Development", "compiler-api-akademiakodu-results").toString();


    public ProjectDetails saveProject(long uploadId, MultipartFile[] files) throws IOException {
        String uploadPath = Paths.get(STORAGE, "project-" + formatId(uploadId)).toString();
        String sourcePath = Paths.get(uploadPath, "source").toString();
        String classPath = Paths.get(uploadPath, "validation", "classpath").toString();
        File sourceDirectory = new File(sourcePath);
        sourceDirectory.mkdirs();

        for (MultipartFile file : files) {
            saveSourceFile(file, sourcePath);
        }

        return new ProjectDetails(sourcePath, classPath);
    }


    private String formatId(long idToFormat) {
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


    public File createCompilationOutputFile(ProjectDetails projectDetails) {
        return createLogFile(projectDetails.getClassPath(), "compilation-out.txt");
    }

    public File createCompilationErrorFile(ProjectDetails projectDetails) {
        return createLogFile(projectDetails.getClassPath(), "compilation-err.txt");
    }


    public File createExecutionOutputFile(ProjectDetails projectDetails) {
        return createLogFile(projectDetails.getClassPath(), "execution-out.txt");
    }

    public File createExecutionErrorFile(ProjectDetails projectDetails) {
        return createLogFile(projectDetails.getClassPath(), "execution-err.txt");
    }
}
