
package pe.bennu.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.bennu.mapper.AsistenciaDiariaMapper;
import pe.bennu.model.AsistenciaDiaria;

@Service
public class AsistenciaDiariaService {

    private final AsistenciaDiariaMapper mapper;

    private static final Set<String> ESTADOS_VALIDOS = Set.of(
            "PENDIENTE",
            "PRESENTE",
            "TARDANZA",
            "FALTA",
            "PERMISO",
            "DESCANSO",
            "INCOMPLETA"
    );

    public AsistenciaDiariaService(AsistenciaDiariaMapper mapper) {
        this.mapper = mapper;
    }

    public List<AsistenciaDiaria> listar() {
        return mapper.listar();
    }

    public AsistenciaDiaria buscarPorId(Long id) {
        return mapper.buscarPorId(id);
    }

    @Transactional
    public AsistenciaDiaria crear(AsistenciaDiaria asistencia) {
        validarDatos(asistencia);

        if (mapper.existeEmpleado(asistencia.getIdEmpleado()) == 0) {
            throw new IllegalArgumentException(
                    "El empleado indicado no existe");
        }

        if (mapper.existeAsistenciaEnFecha(
                asistencia.getIdEmpleado(),
                asistencia.getFecha()) > 0) {
            throw new IllegalArgumentException(
                    "El empleado ya tiene una asistencia para esa fecha");
        }

        mapper.crear(asistencia);
        return asistencia;
    }

    @Transactional
    public boolean actualizar(Long id, AsistenciaDiaria asistencia) {
        if (mapper.buscarPorId(id) == null) {
            return false;
        }

        validarDatos(asistencia);

        if (mapper.existeEmpleado(asistencia.getIdEmpleado()) == 0) {
            throw new IllegalArgumentException(
                    "El empleado indicado no existe");
        }

        if (mapper.existeOtraAsistenciaEnFecha(
                asistencia.getIdEmpleado(),
                asistencia.getFecha(),
                id) > 0) {
            throw new IllegalArgumentException(
                    "El empleado ya tiene una asistencia para esa fecha");
        }

        asistencia.setIdAsistencia(id);
        return mapper.actualizar(asistencia) > 0;
    }

    @Transactional
    public boolean eliminar(Long id) {
        if (mapper.buscarPorId(id) == null) {
            return false;
        }

        return mapper.eliminar(id) > 0;
    }

    private void validarDatos(AsistenciaDiaria asistencia) {
        if (asistencia == null) {
            throw new IllegalArgumentException(
                    "Debes enviar los datos de la asistencia");
        }

        if (asistencia.getIdEmpleado() == null
                || asistencia.getIdEmpleado() <= 0) {
            throw new IllegalArgumentException(
                    "El ID del empleado es obligatorio");
        }

        if (asistencia.getFecha() == null) {
            throw new IllegalArgumentException(
                    "La fecha es obligatoria");
        }

        if (asistencia.getEstadoAsistencia() == null
                || !ESTADOS_VALIDOS.contains(
                        asistencia.getEstadoAsistencia().trim()
                                .toUpperCase())) {
            throw new IllegalArgumentException(
                    "El estado de asistencia no es válido");
        }

        asistencia.setEstadoAsistencia(
                asistencia.getEstadoAsistencia().trim().toUpperCase());

        validarMinutos("minutos trabajados",
                asistencia.getMinutosTrabajados());
        validarMinutos("minutos de tardanza",
                asistencia.getMinutosTardanza());
        validarMinutos("minutos de salida anticipada",
                asistencia.getMinutosSalidaAnticipada());

        if (asistencia.getObservacion() != null
                && asistencia.getObservacion().length() > 255) {
            throw new IllegalArgumentException(
                    "La observación no puede superar los 255 caracteres");
        }
    }

    private void validarMinutos(String campo, Integer minutos) {
        if (minutos != null && minutos < 0) {
            throw new IllegalArgumentException(
                    "Los " + campo + " no pueden ser negativos");
        }
    }

    public boolean anular(Long id, String motivo) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                "El ID de asistencia no es válido"
            );
        }

        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException(
                "Debes indicar el motivo de anulación"
            );
        }

        if (motivo.trim().length() > 150) {
            throw new IllegalArgumentException(
                "El motivo no puede superar los 150 caracteres"
            );
        }

        AsistenciaDiaria asistencia = mapper.buscarPorId(id);

        if (asistencia == null) {
            return false;
        }

        int filasActualizadas = mapper.anular(id, motivo.trim());

        if (filasActualizadas == 0) {
            throw new IllegalArgumentException(
                "La asistencia ya está anulada"
            );
        }

        return true;
    }

}
