package cl.ucn.disc.isof.fivet.domain.service.ebean;

import cl.ucn.disc.isof.fivet.domain.model.Control;
import cl.ucn.disc.isof.fivet.domain.model.Paciente;
import cl.ucn.disc.isof.fivet.domain.model.Persona;
import cl.ucn.disc.isof.fivet.domain.service.BackendService;
import com.avaje.ebean.Ebean;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static cl.ucn.disc.isof.fivet.domain.model.Control.*;

/**
 * Clase de testing del {@link BackendService}.
 */
@Slf4j
@FixMethodOrder(MethodSorters.DEFAULT)
public class TestEbeanBackendService {

    /**
     * Todos los test deben terminar antes de 60 segundos.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(60);

    /**
     * Configuracion de la base de datos:  h2, hsql, sqlite
     * WARN: hsql no soporta ENCRYPT
     */
    private static final String DB = "h2";

    /**
     * Backend
     */
    private BackendService backendService;

    /**
     * Cronometro
     */
    private Stopwatch stopWatch;

    /**
     * Antes de cada test
     */
    @Before
    public void beforeTest() {

        stopWatch = Stopwatch.createStarted();
        log.debug("Initializing Test Suite with database: {}", DB);

        backendService = new EbeanBackendService(DB);
        backendService.initialize();
    }

    /**
     * Despues del test
     */
    @After
    public void afterTest() {

        log.debug("Test Suite done. Shutting down the database ..");
        backendService.shutdown();

        log.debug("Test finished in {}", stopWatch.toString());
    }

    /**
     * Test de la persona
     */
    @Test
    public void testPersona() {

        final String rut = "1-1";
        final String nombre = "Este es mi nombre";

        // Insert into backend
        {
            final Persona persona = Persona.builder()
                    .nombre(nombre)
                    .rut(rut)
                    .email("d@a")
                    .password("durrutia123")
                    .tipo(Persona.Tipo.CLIENTE)
                    .build();

            persona.insert();

            log.debug("Persona to insert: {}", persona);
            Assert.assertNotNull("Objeto sin id", persona.getId());
        }

        // Get from backend v1
        {
            final Persona persona = backendService.getPersona(rut);
            log.debug("Persona founded: {}", persona);
            Assert.assertNotNull("Can't find Persona", persona);
            Assert.assertNotNull("Objeto sin id", persona.getId());
            Assert.assertEquals("Nombre distintos!",nombre, persona.getNombre());
            Assert.assertNotNull("Pacientes null", persona.getPacientes());
            Assert.assertTrue("Pacientes != 0", persona.getPacientes().size() == 0);

            // Update nombre
            persona.setNombre(nombre);
            persona.update();
        }

        // Get from backend v2
        {
            final Persona persona = backendService.getPersona(rut);
            log.debug("Persona founded: {}", persona);
            Assert.assertNotNull("Can't find Persona", persona);
            Assert.assertEquals("Nombres distintos!", nombre, persona.getNombre());
        }
        //Obtener persona con el EMAIL
        {
            final Persona persona = backendService.getPersona("d@a");
            log.debug("Persona founded POR EL EMAIL: {}", persona);
            Assert.assertNotNull("Can't find Persona", persona);

        }

    }

    @Test
    public void testListaGetPaciente(){
        // Insert into backend
        {
            final Paciente p1 = Paciente.builder()
                    .nombre("paciente1")
                    .numero(1)
                    .sexo(Paciente.Sexo.MACHO)
                    .raza("Poodle")
                    .color("blanco")
                    .build();

            p1.insert();

            log.debug("Paciente to insert: {}", p1);
            Assert.assertNotNull("Objeto sin id", p1.getNumero());

            final Paciente p2 = Paciente.builder()
                    .nombre("paciente2")
                    .numero(2)
                    .sexo(Paciente.Sexo.MACHO)
                    .raza("Poodle")
                    .color("azul")
                    .build();

            p2.insert();

            log.debug("Paciente to insert: {}", p2);
            Assert.assertNotNull("Objeto sin id", p2.getNumero());
        }
        final List<Paciente> paciente = backendService.getPacientes();
        log.debug("Lista de pacientes founded: {}", paciente);
        Assert.assertNotNull("Lista distinta de null", paciente);
        Assert.assertTrue("Pacientes encontrados 2...", paciente.size() == 2);



    }
    @Test
    public void testGetPacientePorNumero(){
        //Insertar 2 pacientes
        {
            final Paciente p1 = Paciente.builder()
                    .nombre("paciente1")
                    .numero(1)
                    .sexo(Paciente.Sexo.MACHO)
                    .raza("Poodle")
                    .color("blanco")
                    .build();

            p1.insert();

            log.debug("Paciente to insert: {}", p1);
            Assert.assertNotNull("Objeto sin id", p1.getNumero());

            final Paciente p2 = Paciente.builder()
                    .nombre("paciente2")
                    .numero(2)
                    .sexo(Paciente.Sexo.MACHO)
                    .raza("Poodle")
                    .color("azul")
                    .build();

            p2.insert();

            log.debug("Paciente to insert: {}", p2);
            Assert.assertNotNull("Objeto sin id", p2.getNumero());
        }
        //PAciente con el numero 1
        final Paciente paciente = backendService.getPaciente(1);
        log.debug("Pacientes founded: {}", paciente);
        Assert.assertNotNull("Can't find Paciente", paciente);
        Assert.assertNotNull("Objeto sin id", paciente.getNumero());
        Assert.assertEquals("Nombre distintos!","paciente1", paciente.getNombre());

        //no existe paciente con numero 10
        final Paciente paciente1 = backendService.getPaciente(10);
        log.debug("Pacientes founded: {}", paciente1);
        Assert.assertNull("Paciente null", paciente1);

    }
    @Test
    public void testListaGetControlesPorVeterinarioRut(){
        //Insertar un veterinario
        final Persona p1=Persona.builder()
                .nombre("Vet1")
                .rut("1-1")
                .email("a@a")
                .password("qwerty")
                .tipo(Persona.Tipo.VETERINARIO)
                .build();
        p1.insert();


        //Insertar 2 controles
        {
            final Control c1 = Control.builder()
                    .numero(1)
                    .altura(123)
                    .diagnostico("Cojo")
                    .fecha(Date.from(Instant.EPOCH))
                    .temperatura(25)
                    .veterinario(p1)
                    .build();
            Ebean.save(c1);
            //c1.insert();

            log.debug("Control to insert: {}", c1);
            Assert.assertNotNull("Objeto sin id", c1.getNumero());
            final Control c2 = Control.builder()
                    .numero(2)
                    .altura(123)
                    .diagnostico("Ciego")
                    .fecha(Date.from(Instant.EPOCH))
                    .temperatura(18)
                    .veterinario(p1)
                    .build();
            Ebean.save(c2);
            //c1.insert();

            log.debug("Control to insert: {}", c2);
            Assert.assertNotNull("Objeto sin id", c2.getNumero());

        }






    }
    @Test
    public void testGetPacientePorNombre(){
        //Insertar 3 pacientes 2 con nombre pep&
        {
            final Paciente p1=Paciente.builder()
                    .numero(1)
                    .nombre("pepinillo")
                    .build();
            final Paciente p2=Paciente.builder()
                    .numero(2)
                    .nombre("peponazo")
                    .build();
            final Paciente p3=Paciente.builder()
                    .numero(3)
                    .nombre("perilla")
                    .build();

            p1.insert();
            p2.insert();
            p3.insert();
        }
        //Encontro 2 pacientes peponazo y pepinillo
        List<Paciente> listaPaciente = backendService.getPacientesPorNombre("pep");
        Assert.assertTrue("No es una lista vacia",listaPaciente!=null);
        Assert.assertTrue("Lista con 2 pacientes",listaPaciente.size()==2);

        //Ningun paciente con ese nombre
        List<Paciente> listaPaciente1 = backendService.getPacientesPorNombre("NoHay");
        Assert.assertTrue("No es una lista vacia",listaPaciente1!=null);
        Assert.assertTrue("Lista con 0 pacientes",listaPaciente1.size()==0);


    }
    @Test
    public void testInsertarControl(){


    }


}
