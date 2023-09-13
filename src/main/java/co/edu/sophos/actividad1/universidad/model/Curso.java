package co.edu.sophos.actividad1.universidad.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
@Data
@AllArgsConstructor
public class Curso {

    @Id
    private Integer Id;
    private String nombre;
}
