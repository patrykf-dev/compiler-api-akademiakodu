package pl.akademiakodu.compiling;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.akademiakodu.models.UploadedProject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Getter
@Setter
@ToString
public class ProjectDetails {
    private String sourcePath;
    private String classPath;
    private String mainClassPath;
    private String mainClassFullName;

    private static final String MAIN_CLASS_FILE = "Hello.java";

    public ProjectDetails(String sourcePath, String classPath) {
        this.sourcePath = sourcePath;
        this.classPath = classPath;
        this.mainClassPath = findMainClassPath(sourcePath);
        this.mainClassFullName = extractMainClassName();
    }

    private String findMainClassPath(String sourcePath) {
        Optional<Path> mainClassFile = null;
        try {
            mainClassFile = Files.walk(Paths.get(sourcePath))
                    .filter(file -> file.getFileName().toString().equals(MAIN_CLASS_FILE)).findAny();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mainClassFile.get().toString();
    }

    private String extractMainClassName() {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(mainClassPath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("package")) {
                    String packageName = line.substring(line.indexOf(" ") + 1, line.indexOf(";")).trim();
                    String className = MAIN_CLASS_FILE.replace(".java", "");
                    return packageName + "." + className;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
