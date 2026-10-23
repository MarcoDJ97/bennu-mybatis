
package pe.bennu.service;

import java.util.List;
import org.springframework.stereotype.Service;
import pe.bennu.mapper.EmpleadoMapper;
import pe.bennu.model.Empleado;

@Service
public class EmpleadoService {

    private final EmpleadoMapper mapper;

    public EmpleadoService(EmpleadoMapper mapper) {
        this.mapper = mapper;
    }

    public List<Empleado> listar() {
        return mapper.listar();
    }

    public Empleado buscarPorId(int id) {
        return mapper.buscarPorId(id);
    }

    public Empleado crear(Empleado empleado) {
        if (empleado.getEstado() == null
                || empleado.getEstado().isBlank()) {
            empleado.setEstado("ACTIVO");
        }

        mapper.crear(empleado);
        return empleado;
    }

    public boolean actualizar(int id, Empleado empleado) {
        if (mapper.buscarPorId(id) == null) {
            return false;
        }

        empleado.setIdEmpleado(id);
        return mapper.actualizar(empleado) > 0;
    }

    public boolean tieneCamposParaActualizar(Empleado e) {
        return e.getDni() != null
                || e.getNombres() != null
                || e.getApellidos() != null
                || e.getCorreo() != null
                || e.getFechaIngreso() != null
                || e.getFechaCese() != null
                || e.getSueldo() != null
                || e.getEstado() != null;
    }

    public boolean actualizarParcial(int id, Empleado empleado) {
        if (mapper.buscarPorId(id) == null) {
            return false;
        }

        empleado.setIdEmpleado(id);
        mapper.actualizarParcial(empleado);
        return true;
    }

    public boolean desactivar(int id) {
        return mapper.desactivar(id) > 0;
    }
}
