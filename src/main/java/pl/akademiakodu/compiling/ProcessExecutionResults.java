package pl.akademiakodu.compiling;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

@AllArgsConstructor
@Getter
@Setter
public class ProcessExecutionResults {
    private Path outputFile;
    private Path errorFile;
}
