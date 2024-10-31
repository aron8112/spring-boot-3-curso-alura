package med.voll.api.medico;

public record DatosListaMedicos(
    String nombre,
    String especialidad,
    String documento,
    String email
) {
  public DatosListaMedicos(Medico medico){
    this(medico.getNombre(), medico.getEspecialidad().toString(), medico.getDocumento(), medico.getEmail());
  }
}
