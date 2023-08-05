package co.edu.sophos.actividad1.universidad.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Nota {

    @Id
    private Integer id;
    private Integer valor;
    private Integer idCurso;
    private Integer idEstudiante;
}
