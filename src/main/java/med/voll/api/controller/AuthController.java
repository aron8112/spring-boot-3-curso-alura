package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.user.DatosAuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {

  @Autowired
  private AuthenticationManager authManager;

  @PostMapping
  public ResponseEntity autenticarUser(@RequestBody @Valid DatosAuthUser datosAuthUser){
    Authentication token = new UsernamePasswordAuthenticationToken(
        datosAuthUser.username(),
        datosAuthUser.password());
    authManager.authenticate(token);
    return ResponseEntity.ok().build();
  }

}
