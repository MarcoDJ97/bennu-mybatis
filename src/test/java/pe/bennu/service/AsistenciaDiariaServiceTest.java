
package pe.bennu.service;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pe.bennu.mapper.AsistenciaDiariaMapper;
import pe.bennu.model.AsistenciaDiaria;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsistenciaDiariaServiceTest {

    @Mock
    private AsistenciaDiariaMapper mapper;

    @InjectMocks
    private AsistenciaDiariaService service;

    private AsistenciaDiaria crearAsistencia() {
        AsistenciaDiaria a = new AsistenciaDiaria();
        a.setIdEmpleado(1L);
        a.setFecha(LocalDate.of(2026, 10, 22));
        a.setEstadoAsistencia("PRESENTE");
        a.setMinutosTrabajados(480);
        a.setMinutosTardanza(0);
        a.setMinutosSalidaAnticipada(0);
        a.setObservacion("Asistencia de prueba");
        return a;
    }

    @Test
    void listar_devuelveLaListaDelMapper() {
        List<AsistenciaDiaria> esperadas =
                List.of(crearAsistencia());

        when(mapper.listar()).thenReturn(esperadas);

        List<AsistenciaDiaria> resultado = service.listar();

        assertEquals(esperadas, resultado);
        verify(mapper).listar();
    }

    @Test
    void buscarPorId_cuandoExiste_devuelveAsistencia() {
        AsistenciaDiaria esperada = crearAsistencia();
        esperada.setIdAsistencia(1L);

        when(mapper.buscarPorId(1L)).thenReturn(esperada);

        AsistenciaDiaria resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdAsistencia());
        assertEquals("PRESENTE", resultado.getEstadoAsistencia());
    }

    @Test
    void buscarPorId_cuandoNoExiste_devuelveNull() {
        when(mapper.buscarPorId(999L)).thenReturn(null);

        AsistenciaDiaria resultado = service.buscarPorId(999L);

        assertNull(resultado);
    }

    @Test
    void crear_cuandoDatosSonValidos_registraAsistencia() {
        AsistenciaDiaria asistencia = crearAsistencia();

        when(mapper.existeEmpleado(1L)).thenReturn(1);
        when(mapper.existeAsistenciaEnFecha(
                1L, asistencia.getFecha())).thenReturn(0);
        when(mapper.crear(asistencia)).thenReturn(1);

        AsistenciaDiaria resultado = service.crear(asistencia);

        assertSame(asistencia, resultado);
        verify(mapper).crear(asistencia);
    }

    @Test
    void crear_cuandoEmpleadoNoExiste_lanzaError() {
        AsistenciaDiaria asistencia = crearAsistencia();

        when(mapper.existeEmpleado(1L)).thenReturn(0);

        assertThrows(IllegalArgumentException.class,
                () -> service.crear(asistencia));

        verify(mapper, never()).crear(any(AsistenciaDiaria.class));
    }

    @Test
    void crear_cuandoAsistenciaEstaDuplicada_lanzaError() {
        AsistenciaDiaria asistencia = crearAsistencia();

        when(mapper.existeEmpleado(1L)).thenReturn(1);
        when(mapper.existeAsistenciaEnFecha(
                1L, asistencia.getFecha())).thenReturn(1);

        assertThrows(IllegalArgumentException.class,
                () -> service.crear(asistencia));

        verify(mapper, never()).crear(any(AsistenciaDiaria.class));
    }

    @Test
    void crear_cuandoMinutosSonNegativos_lanzaError() {
        AsistenciaDiaria asistencia = crearAsistencia();
        asistencia.setMinutosTrabajados(-10);

        assertThrows(IllegalArgumentException.class,
                () -> service.crear(asistencia));

        verify(mapper, never()).crear(any(AsistenciaDiaria.class));
    }

    @Test
    void actualizar_cuandoAsistenciaNoExiste_devuelveFalse() {
        when(mapper.buscarPorId(999L)).thenReturn(null);

        boolean resultado =
                service.actualizar(999L, crearAsistencia());

        assertFalse(resultado);
        verify(mapper, never()).actualizar(
                any(AsistenciaDiaria.class));
    }

    @Test
    void eliminar_cuandoAsistenciaNoExiste_devuelveFalse() {
        when(mapper.buscarPorId(999L)).thenReturn(null);

        boolean resultado = service.eliminar(999L);

        assertFalse(resultado);
        verify(mapper, never()).eliminar(999L);
    }

    @Test
    void anularAsistenciaExistente() {
        AsistenciaDiaria asistencia = new AsistenciaDiaria();
        asistencia.setIdAsistencia(1L);

        when(mapper.buscarPorId(1L)).thenReturn(asistencia);
        when(mapper.anular(1L, "Registro ingresado por error"))
                .thenReturn(1);

        boolean resultado = service.anular(
            1L,
            "Registro ingresado por error"
        );

        assertTrue(resultado);

        verify(mapper).anular(
            1L,
            "Registro ingresado por error"
        );
    }

}
