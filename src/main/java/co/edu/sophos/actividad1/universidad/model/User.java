package co.edu.sophos.actividad1.universidad.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder(toBuilder = true)
public class User {

    @Id
    private Integer id;
    private String username;
    private String password;
    private String role;
}
