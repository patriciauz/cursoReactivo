package co.edu.sophos.actividad1.universidad.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("programa")
public class Programa {

    @Id
    private Integer id;
    private String nombre;


}
