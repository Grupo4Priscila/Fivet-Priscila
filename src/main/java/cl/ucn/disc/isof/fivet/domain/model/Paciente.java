package cl.ucn.disc.isof.fivet.domain.model;

import  org.hibernate.validator.constraints.NotEmpty;
import com.avaje.ebean.annotation.EnumValue;
import com.avaje.ebean.annotation.Encrypted;
import com.durrutia.ebean.BaseModel;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Clase que representa a un Paciente de la veterinaria.
 *
 * @author Diego P. Urrutia Astorga
 * @version 20161102
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Paciente extends BaseModel {

    /**
     * Numero de la ficha
     */
    @Getter
    @Column
    private Integer numero;

    /**
     * Nombre del paciente
     */
    @Getter
    @Setter
    @Column
    private String nombre;

    /**
     * Fecha de nacimiento
     */
    @Getter
    @Setter
    @Column
    private Date fechaNacimiento;

    /**
     * Raza
     */
    @Getter
    @Setter
    @Column
    private String raza;

    /**
     * Sexo
     */
    private Sexo sexo;

    /**
     * Color
     */
    @Getter
    @Setter
    private String color;
    /**
     * Lista de  Controles
     */
    @Getter
    @Setter
    @ManyToMany
    private List<Control> controls;

    public void agregaControl(Control c){
        this.controls.add(c);
    }

    /**
     * Sexo?
     */
    public enum Sexo {
        @EnumValue("Macho")
        MACHO,

        @EnumValue("Hembra")
        HEMBRA,

        @EnumValue("Indeterminado")
        INDETERMINADO,
    }


}
