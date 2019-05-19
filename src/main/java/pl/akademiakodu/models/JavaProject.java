package pl.akademiakodu.models;

import lombok.Getter;
import lombok.ToString;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Getter
@ToString
public class JavaProject {
    private int id;
    private String sourcePath;
    private String classPath;
    private String mainClassPath;
    private String mainClassFullName;
    private static final String MAIN_CLASS_FILE = "Hello.java";

    public JavaProject(String sourcePath, String classPath, int id) {
        this.id = id;
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

    public static String formatId(int idToFormat) {
        return String.format("%05d", idToFormat);
    }

    public String getFormattedId() {
        return formatId(this.id);
    }
}
