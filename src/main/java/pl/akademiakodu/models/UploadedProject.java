package pl.akademiakodu.models;

import lombok.Getter;
import lombok.Setter;
import pl.akademiakodu.compiling.ProjectDetails;
import pl.akademiakodu.compiling.ProjectValidationResult;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "uploaded_projects")
public class UploadedProject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Enumerated(EnumType.STRING)
    private ProjectValidationResult validationResult;

    @Transient
    private ProjectDetails projectDetails;

    private Date uploadDate;
    private String sourcePath;
    private String compilationOutPath;
    private String compilationErrorPath;
    private String executionOutPath;
    private String executionErrorPath;
}
