package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.DatosActualizacionMedico;
import med.voll.api.medico.DatosListaMedicos;
import med.voll.api.medico.Medico;
import med.voll.api.paciente.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
  @Autowired
  private PacienteRepository repository;

  @PostMapping
  @Transactional
  public void registrar(@RequestBody @Valid DatosRegistroPaciente datos) {
    repository.save(new Paciente(datos));
  }

  @GetMapping
  public Page<DatosListaPaciente> listar(@PageableDefault(page = 0, size = 10, sort = {"nombre"}) Pageable paginacion) {
    return repository.findByActivoTrue(paginacion).map(DatosListaPaciente::new);
  }

  @GetMapping("/{id}")
  public DatosListaPaciente buscarUnPaciente(@PathVariable Long id){
    Paciente paciente = repository.getReferenceById(id);
    return new DatosListaPaciente(paciente);
  }

  @PutMapping
  @Transactional
  public void actualizarPaciente(@RequestBody @Valid DatosActualizacionPaciente datosActualizacionPaciente){
    Paciente paciente = repository.getReferenceById(datosActualizacionPaciente.id());
    paciente.atualizarInformacion(datosActualizacionPaciente);
  }

  @DeleteMapping("/{id}")
  @Transactional
  public void excluirPaciente(@PathVariable Long id){
    //Delete lógico
    Paciente paciente = repository.getReferenceById(id);
    paciente.desactivarPaciente();
  }
}
