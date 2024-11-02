package med.voll.api.paciente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.direccion.Direccion;

@Table(name = "pacientes")
@Entity(name = "Paciente")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Paciente {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String nombre;
  private String email;
  private String documentoIdentidad;
  private String telefono;
  private Boolean activo;

  @Embedded
  private Direccion direccion;

  public Paciente(DatosRegistroPaciente datos) {
    this.nombre = datos.nombre();
    this.email = datos.email();
    this.telefono = datos.telefono();
    this.activo = true;
    this.documentoIdentidad = datos.documentoIdentidad();
    this.direccion = new Direccion(datos.direccion());
  }

  public void atualizarInformacion(DatosActualizacionPaciente datos) {
    if (datos.nombre() != null)
      this.nombre = datos.nombre();

    if (datos.telefono() != null)
      this.telefono = datos.telefono();

    if (datos.direccion() != null)
      direccion.actualizarDatos(datos.direccion());
  }

  public void desactivarPaciente(){
    this.activo = false;
  }

}
