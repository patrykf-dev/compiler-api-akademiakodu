package pl.akademiakodu.models;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String expectedResultPath;
    private Date updateDate;
    private boolean isActive;
}
