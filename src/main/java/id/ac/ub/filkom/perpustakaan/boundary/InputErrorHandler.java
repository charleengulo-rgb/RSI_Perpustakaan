package id.ac.ub.filkom.perpustakaan.boundary;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class InputErrorHandler {
  @ExceptionHandler({
    MethodArgumentTypeMismatchException.class,
    MissingServletRequestParameterException.class
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String inputTidakValid() {
    return "input-error";
  }
}
