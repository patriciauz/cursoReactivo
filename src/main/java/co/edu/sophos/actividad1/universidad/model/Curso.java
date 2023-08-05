package co.edu.sophos.actividad1.universidad.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Curso {

    @Id
    private Integer Id;
    private String nombre;
}
