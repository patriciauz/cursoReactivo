package co.edu.sophos.actividad1.universidad.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class Estudiante {

    @Id
    private Integer id;
    private Integer identificacion;
    private String nombreCompleto;
    private Integer idPrograma;
}
