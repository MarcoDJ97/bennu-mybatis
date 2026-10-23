
package pe.bennu.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import pe.bennu.mapper.EmpleadoMapper;
import pe.bennu.model.Empleado;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceTest {

    @Mock
    private EmpleadoMapper mapper;

    @InjectMocks
    private EmpleadoService service;

    @Test
    void buscarPorId_cuandoExiste_devuelveEmpleado() {
        // Arrange
        int id = 1;
        Empleado esperado = new Empleado();

        when(mapper.buscarPorId(id)).thenReturn(esperado);

        // Act
        Empleado resultado = service.buscarPorId(id);

        // Assert
        assertNotNull(resultado);
        assertSame(esperado, resultado);
        verify(mapper).buscarPorId(id);
    }

    @Test
    void buscarPorId_cuandoNoExiste_devuelveNull() {
        // Arrange
        int id = 999;

        when(mapper.buscarPorId(id)).thenReturn(null);

        // Act
        Empleado resultado = service.buscarPorId(id);

        // Assert
        assertNull(resultado);
        verify(mapper).buscarPorId(id);
    }

    @Test
    void actualizar_cuandoEmpleadoNoExiste_devuelveFalse() {
        // Arrange
        int id = 999;
        Empleado empleado = new Empleado();

        when(mapper.buscarPorId(id)).thenReturn(null);

        // Act
        boolean resultado = service.actualizar(id, empleado);

        // Assert
        assertFalse(resultado);
        verify(mapper, never()).actualizar(any(Empleado.class));
    }

    @Test
    void crear_cuandoEstadoEsNulo_asignaActivo() {
        // Arrange
        Empleado empleado = new Empleado();
        empleado.setEstado(null);

        // Act
        Empleado resultado = service.crear(empleado);

        // Assert
        assertEquals("ACTIVO", resultado.getEstado());
        verify(mapper).crear(empleado);
    }
}
