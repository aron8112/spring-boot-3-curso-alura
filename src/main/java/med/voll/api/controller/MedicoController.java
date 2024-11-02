package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import med.voll.api.direccion.DatosDireccion;
import med.voll.api.medico.*;

import org.apache.coyote.Response;
import org.hibernate.annotations.SoftDelete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/medicos")
public class MedicoController {

  @Autowired
  private MedicoRepository medicoRepository;

  //Retorno de post
  //Status 201
  //direccion del nuevo elemento, ej: http://localhost:4000/medicos/{id}
  @PostMapping("/log")
  public ResponseEntity<DatosResActualizMedico> registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico,
                                        UriComponentsBuilder urlComponentBuilder){
    Medico medico = medicoRepository.save(new Medico(datosRegistroMedico));
    DatosResActualizMedico datosRespuestaMedico = new DatosResActualizMedico(
        medico.getId(), medico.getNombre(), medico.getEmail(), medico.getTelefono(), medico.getDocumento(),
        new DatosDireccion(
            medico.getDireccion().getCalle(),
            medico.getDireccion().getDistrito(),
            medico.getDireccion().getCiudad(),
            medico.getDireccion().getNumero(),
            medico.getDireccion().getComplemento()
        ));
    //Construccion de la url del nuevo elemento
    URI url = urlComponentBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
    return ResponseEntity.created(url).body(datosRespuestaMedico);
  }

  @GetMapping("/all")
  public ResponseEntity<Page<DatosListaMedicos>> listadoMedicos(@PageableDefault(size=5, direction = Sort.Direction.ASC) Pageable paginacion){
    return ResponseEntity.ok(medicoRepository.findByActivoTrue(paginacion).map(DatosListaMedicos::new));
  }

  @GetMapping("/{id}")
  public ResponseEntity<DatosListaMedicos> buscarUnMedico(@PathVariable Long id){
    Medico medico = medicoRepository.getReferenceById(id);
    DatosListaMedicos datosListaMedicos = new DatosListaMedicos(medico);
    return ResponseEntity.ok(datosListaMedicos);
  }

  @PutMapping
  @Transactional
  public ResponseEntity actualizarMedico(@RequestBody @Valid DatosActualizacionMedico datosActualizacionMedico){
    Medico medico = medicoRepository.getReferenceById(datosActualizacionMedico.id());
    medico.actualizarDatos(datosActualizacionMedico);
    return ResponseEntity.ok(new DatosResActualizMedico(
        medico.getId(), medico.getNombre(), medico.getEmail(), medico.getTelefono(), medico.getDocumento(),
        new DatosDireccion(
            medico.getDireccion().getCalle(),
            medico.getDireccion().getDistrito(),
            medico.getDireccion().getCiudad(),
            medico.getDireccion().getNumero(),
            medico.getDireccion().getComplemento()
            )));
  }

  @DeleteMapping("/{id}")
  @Transactional
  public ResponseEntity excluirMedico(@PathVariable Long id){
    //Delete lógico
    Medico medico = medicoRepository.getReferenceById(id);
    medico.desactivarMedico();
    return ResponseEntity.noContent().build();
  }

  // ELiminar en la base de datos
  //  public void excluirMedico(@PathVariable Long id){
  //    Medico medico = medicoRepository.getReferenceById(id);
  //    medicoRepository.delete(medico);
  //  }
}
