package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.medico.DatosRegistroMedico;
import med.voll.api.medico.Medico;
import med.voll.api.medico.DatosListaMedicos;
import med.voll.api.medico.MedicoRepository;

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

  @PostMapping
  public String registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico){
    System.out.println("El req llegó correctamente");

    medicoRepository.save(new Medico(datosRegistroMedico));
    return datosRegistroMedico.toString();
  }

  @GetMapping
  public Page<DatosListaMedicos> listadoMedicos(@PageableDefault(size=5, direction = Sort.Direction.ASC) Pageable paginacion){
    return medicoRepository.findAll(paginacion).map(DatosListaMedicos::new);
  }
}
