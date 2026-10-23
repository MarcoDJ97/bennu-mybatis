
package pe.bennu.model;

import java.time.LocalDate;

public class AsistenciaDiaria {

    private Long idAsistencia;
    private Long idEmpleado;
    private LocalDate fecha;
    private String estadoAsistencia;
    private Integer minutosTrabajados;
    private Integer minutosTardanza;
    private Integer minutosSalidaAnticipada;
    private String observacion;

    public Long getIdAsistencia() {
        return idAsistencia;
    }

    public void setIdAsistencia(Long idAsistencia) {
        this.idAsistencia = idAsistencia;
    }

    public Long getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getEstadoAsistencia() {
        return estadoAsistencia;
    }

    public void setEstadoAsistencia(String estadoAsistencia) {
        this.estadoAsistencia = estadoAsistencia;
    }

    public Integer getMinutosTrabajados() {
        return minutosTrabajados;
    }

    public void setMinutosTrabajados(Integer minutosTrabajados) {
        this.minutosTrabajados = minutosTrabajados;
    }

    public Integer getMinutosTardanza() {
        return minutosTardanza;
    }

    public void setMinutosTardanza(Integer minutosTardanza) {
        this.minutosTardanza = minutosTardanza;
    }

    public Integer getMinutosSalidaAnticipada() {
        return minutosSalidaAnticipada;
    }

    public void setMinutosSalidaAnticipada(Integer minutosSalidaAnticipada) {
        this.minutosSalidaAnticipada = minutosSalidaAnticipada;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
