package fr.swansky.ioccontainer.tests.swansJPA;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "TableName")
@Entity
public class User {
    @Id
    @Column(length = 100)
    String name;
    @Column(length = 100)
    String pseudo;
    @Column(length = 310)
    String email;

    @Lob
    String description;

}
