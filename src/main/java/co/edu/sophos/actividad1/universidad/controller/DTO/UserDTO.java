package co.edu.sophos.actividad1.universidad.controller.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class UserDTO {

    private String username;
    private String token;
}
