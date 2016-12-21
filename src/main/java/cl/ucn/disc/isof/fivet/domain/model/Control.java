package cl.ucn.disc.isof.fivet.domain.model;

import com.durrutia.ebean.BaseModel;
import com.avaje.ebean.annotation.Encrypted;
import com.avaje.ebean.annotation.EnumValue;
import com.durrutia.ebean.BaseModel;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;



/**
 * Created by pgonz on 20-12-2016.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Control {
    /**
     * Numero de la ficha
     */
    @Getter
    @Column
    private Integer numero;

        /**
         * Fecha
         */
        @Getter
        @Setter
        @Column
        private Date fecha;

        /**
         * Proximo Control
         */
        @Getter
        @Setter
        @Column
        private Date proximoControl;

        /**
         * Temperatura de la mascota
         */
        @Getter
        @Setter
        @Column
        private Integer temperatura;
        /**
         * Peso de la mascota
         */
        @Getter
        @Setter
        @Column
        private Integer peso;
        /**
         * Altura de la mascota
         */
        @Getter
        @Setter
        @Column
        private Integer altura;
        /**
         * Diagnostico
         */
        @Getter
        @Setter
        @Column
        private String diagnostico;
        /**
         * nota
         */
        @Getter
        @Setter
        @Column
        private String nota;



        /**
         * Veterinario
         */
        @Getter
        private Persona veterinario;




}
