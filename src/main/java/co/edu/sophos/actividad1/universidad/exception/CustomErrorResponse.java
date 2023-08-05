package co.edu.sophos.actividad1.universidad.exception;

public record CustomErrorResponse(Integer status, String error, String message, Long timestamp)  {
}
