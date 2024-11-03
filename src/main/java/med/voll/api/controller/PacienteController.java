package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.paciente.*;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
  @Autowired
  private PacienteRepository repository;

  @PostMapping
  @Transactional
  public ResponseEntity<DatosListaPaciente> registrar(@RequestBody @Valid DatosRegistroPaciente datos,
                                                      UriComponentsBuilder urlComponentBuilder) {
    Paciente paciente = repository.save(new Paciente(datos));
    DatosListaPaciente datosListaPaciente = new DatosListaPaciente(paciente.getId(), paciente.getNombre(), paciente.getEmail(), paciente.getDocumentoIdentidad());
    //Construccion de la url del nuevo elemento
    URI url = urlComponentBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
    return ResponseEntity.created(url).body(datosListaPaciente);
  }

  @GetMapping("/all")
  public ResponseEntity<Page<DatosListaPaciente>> listar(@PageableDefault(page = 0, size = 10, sort = {"nombre"}) Pageable paginacion) {
    return ResponseEntity.ok(repository.findByActivoTrue(paginacion).map(DatosListaPaciente::new));
  }

  @GetMapping("/{id}")
  public ResponseEntity<DatosListaPaciente> buscarUnPaciente(@PathVariable Long id){
    Paciente paciente = repository.getReferenceById(id);
    return ResponseEntity.ok(new DatosListaPaciente(paciente));
  }

  @PutMapping
  @Transactional
  public ResponseEntity<DatosListaPaciente> actualizarPaciente(@RequestBody @Valid DatosActualizacionPaciente datosActualizacionPaciente){
    Paciente paciente = repository.getReferenceById(datosActualizacionPaciente.id());
    paciente.actualizarInformacion(datosActualizacionPaciente);
    return ResponseEntity.ok(new DatosListaPaciente(paciente.getId(), paciente.getNombre(), paciente.getEmail(), paciente.getDocumentoIdentidad()));
  }

  @DeleteMapping("/{id}")
  @Transactional
  public ResponseEntity excluirPaciente(@PathVariable Long id){
    //Delete lógico
    Paciente paciente = repository.getReferenceById(id);
    paciente.desactivarPaciente();
    return ResponseEntity.noContent().build();
  }
}
