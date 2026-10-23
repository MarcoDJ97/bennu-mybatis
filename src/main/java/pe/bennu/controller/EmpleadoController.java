
package pe.bennu;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.bennu.model.Empleado;
import pe.bennu.service.EmpleadoService;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final EmpleadoService service;

    public EmpleadoController(EmpleadoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Empleado> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> buscar(@PathVariable int id) {
        Empleado empleado = service.buscarPorId(id);

        if (empleado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(empleado);
    }

    @PostMapping
    public ResponseEntity<Empleado> crear(
            @RequestBody Empleado empleado) {
        Empleado creado = service.crear(empleado);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(
            @PathVariable int id,
            @RequestBody Empleado empleado) {

        if (!service.actualizar(id, empleado)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcial(
            @PathVariable int id,
            @RequestBody Empleado empleado) {

        if (!service.tieneCamposParaActualizar(empleado)) {
            return ResponseEntity.badRequest()
                    .body("Debes enviar al menos un campo para actualizar.");
        }

        if (!service.actualizarParcial(id, empleado)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(
            @PathVariable int id) {

        if (!service.desactivar(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
