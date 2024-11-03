package med.voll.api.infra;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratamientoErrores {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity capturarError404(){
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity capturarError400(MethodArgumentNotValidException e){
    var errores = e.getFieldErrors().stream().map(DatosErrorValidacion::new).toList();
    return ResponseEntity.badRequest().body(errores);
  }

  private record DatosErrorValidacion(String campo, String error){
    public DatosErrorValidacion(FieldError error){
      this(error.getField(), error.getDefaultMessage());
    }
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity capturarErrorDuplicatedKeys(DataIntegrityViolationException e){
    var errores = "Entrada duplicada:\n"+e.getMostSpecificCause().getMessage();
    return ResponseEntity.badRequest().body(errores);
  }
}
