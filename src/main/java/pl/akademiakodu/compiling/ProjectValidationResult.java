package pl.akademiakodu.compiling;

public enum ProjectValidationResult {
    COMPILATION_ERROR(0),
    EXECUTION_ERROR(1),
    INVALID_RESULT(2),
    VALID_RESULT(3),
    UPLOADED(4),
    SERVER_ERROR(5),
    COMPILATION_TIMEOUT(6),
    EXECUTION_TIMEOUT(7);


    ProjectValidationResult(int id) {
        this.id = id;
    }

    private int id;
}
