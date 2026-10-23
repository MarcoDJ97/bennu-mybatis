
package pe.bennu.controller;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import pe.bennu.model.AsistenciaDiaria;
import pe.bennu.service.AsistenciaDiariaService;

@RestController
@RequestMapping("/api/asistencias")
public class AsistenciaDiariaController {

    private final AsistenciaDiariaService service;

    public AsistenciaDiariaController(
            AsistenciaDiariaService service) {
        this.service = service;
    }

    @GetMapping
    public List<AsistenciaDiaria> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsistenciaDiaria> buscarPorId(
            @PathVariable Long id) {
        AsistenciaDiaria asistencia = service.buscarPorId(id);

        if (asistencia == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(asistencia);
    }

    @PostMapping
    public ResponseEntity<AsistenciaDiaria> crear(
            @RequestBody AsistenciaDiaria asistencia) {
        try {
            AsistenciaDiaria creada = service.crear(asistencia);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se pudo registrar la asistencia por una restricción de la base de datos");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsistenciaDiaria> actualizar(
            @PathVariable Long id,
            @RequestBody AsistenciaDiaria asistencia) {
        try {
            boolean actualizada = service.actualizar(id, asistencia);

            if (!actualizada) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(service.buscarPorId(id));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se pudo actualizar por una restricción de la base de datos");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            boolean eliminado = service.eliminar(id);

            if (!eliminado) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar la asistencia porque tiene registros relacionados");
        }
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<?> anular(
            @PathVariable Long id,
            @RequestBody Map<String, String> datos) {

        try {
            String motivo = datos.get("motivo");

            boolean anulado = service.anular(id, motivo);

            if (!anulado) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(
                Map.of(
                    "mensaje", "Asistencia anulada correctamente",
                    "idAsistencia", id
                )
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
            );
        }
    }
}
