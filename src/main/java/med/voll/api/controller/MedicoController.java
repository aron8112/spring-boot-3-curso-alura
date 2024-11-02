package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import med.voll.api.medico.*;

import org.hibernate.annotations.SoftDelete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/medicos")
public class MedicoController {

  @Autowired
  private MedicoRepository medicoRepository;

  @PostMapping("/log")
  public String registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico){
    System.out.println("El req llegó correctamente");

    medicoRepository.save(new Medico(datosRegistroMedico));
    return datosRegistroMedico.toString();
  }

  @GetMapping("/all")
  public Page<DatosListaMedicos> listadoMedicos(@PageableDefault(size=5, direction = Sort.Direction.ASC) Pageable paginacion){
//    return medicoRepository.findAll(paginacion).map(DatosListaMedicos::new);
    return medicoRepository.findByActivoTrue(paginacion).map(DatosListaMedicos::new);
  }

  @GetMapping("/{id}")
  public DatosListaMedicos buscarUnMedico(@PathVariable Long id){
//    return medicoRepository.findAll(paginacion).map(DatosListaMedicos::new);
    Medico medico = medicoRepository.getReferenceById(id);
    return new DatosListaMedicos(medico);
  }

  @PutMapping
  @Transactional
  public void actualizarMedico(@RequestBody @Valid DatosActualizacionMedico datosActualizacionMedico){
    Medico medico = medicoRepository.getReferenceById(datosActualizacionMedico.id());
    medico.actualizarDatos(datosActualizacionMedico);
  }

  @DeleteMapping("/{id}")
  @Transactional
  public void excluirMedico(@PathVariable Long id){
    //Delete lógico
    Medico medico = medicoRepository.getReferenceById(id);
    medico.desactivarMedico();
  }

  // ELiminar en la base de datos
  //  public void excluirMedico(@PathVariable Long id){
  //    Medico medico = medicoRepository.getReferenceById(id);
  //    medicoRepository.delete(medico);
  //  }
}
