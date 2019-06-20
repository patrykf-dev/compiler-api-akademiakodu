package pl.akademiakodu.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.akademiakodu.compiling.ProjectDetails;
import pl.akademiakodu.models.UploadedProject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class FileStorageService {

    private static final String RESULTS_STORAGE =
            Paths.get("D:", "Development", "compiler-api-akademiakodu-results").toString();
    private static final String TASKS_STORAGE =
            Paths.get("D:", "Development", "compiler-api-akademiakodu-tasks").toString();


    public ProjectDetails saveProject(long uploadId, MultipartFile[] files) throws IOException {
        String uploadPath = Paths.get(RESULTS_STORAGE, "project-" + formatId(uploadId)).toString();
        String sourcePath = Paths.get(uploadPath, "source").toString();
        String classPath = Paths.get(uploadPath, "validation", "classpath").toString();
        File sourceDirectory = new File(sourcePath);
        sourceDirectory.mkdirs();

        for (MultipartFile file : files) {
            saveSourceFile(file, sourcePath);
        }

        return new ProjectDetails(sourcePath, classPath);
    }


    public String getExpectedOutput(UploadedProject uploadedProject) throws IOException {
        int taskId = uploadedProject.getTask().getId();
        Path taskPath = getTaskPath(taskId);
        return loadTextFileContent(taskPath);
    }

    public String getUserOutput(UploadedProject uploadedProject) throws IOException {
        Path executionOutput = Paths.get(uploadedProject.getExecutionOutPath());
        return loadTextFileContent(executionOutput);
    }

    private String loadTextFileContent(Path filePath) throws IOException {
        StringBuilder fileContent = new StringBuilder();
        Stream<String> stream = Files.lines(filePath);
        stream.forEach(s -> fileContent.append(s));
        return fileContent.toString();
    }


    private Path getTaskPath(int taskId) {
        String fileName = "task-" + String.format("%03d", taskId) + ".txt";
        return Paths.get(TASKS_STORAGE, fileName);
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
