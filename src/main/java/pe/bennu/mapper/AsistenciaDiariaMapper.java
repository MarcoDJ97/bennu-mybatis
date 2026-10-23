
package pe.bennu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import pe.bennu.model.AsistenciaDiaria;

@Mapper
public interface AsistenciaDiariaMapper {

    @Select("""
        SELECT
            id_asistencia AS idAsistencia,
            id_empleado AS idEmpleado,
            fecha,
            estado_asistencia AS estadoAsistencia,
            minutos_trabajados AS minutosTrabajados,
            minutos_tardanza AS minutosTardanza,
            minutos_salida_anticipada AS minutosSalidaAnticipada,
            observacion
        FROM BE_ASISTENCIA_DIARIA
        WHERE observacion IS NULL
        OR LEFT(observacion, 9) <> '[ANULADA]'
        ORDER BY fecha DESC, id_asistencia DESC
        """)
    List<AsistenciaDiaria> listar();

    @Select("""
        SELECT
            id_asistencia AS idAsistencia,
            id_empleado AS idEmpleado,
            fecha,
            estado_asistencia AS estadoAsistencia,
            minutos_trabajados AS minutosTrabajados,
            minutos_tardanza AS minutosTardanza,
            minutos_salida_anticipada AS minutosSalidaAnticipada,
            observacion
        FROM BE_ASISTENCIA_DIARIA
        WHERE id_asistencia = #{id}
        """)
    AsistenciaDiaria buscarPorId(@Param("id") Long id);

    @Select("""
        SELECT COUNT(*)
        FROM BE_EMPLEADO
        WHERE id_empleado = #{idEmpleado}
        """)
    int existeEmpleado(@Param("idEmpleado") Long idEmpleado);

    @Select("""
        SELECT COUNT(*)
        FROM BE_ASISTENCIA_DIARIA
        WHERE id_empleado = #{idEmpleado}
          AND fecha = #{fecha}
        """)
    int existeAsistenciaEnFecha(
            @Param("idEmpleado") Long idEmpleado,
            @Param("fecha") java.time.LocalDate fecha);

    @Insert("""
        INSERT INTO BE_ASISTENCIA_DIARIA (
            id_empleado,
            fecha,
            estado_asistencia,
            minutos_trabajados,
            minutos_tardanza,
            minutos_salida_anticipada,
            observacion
        ) VALUES (
            #{idEmpleado},
            #{fecha},
            #{estadoAsistencia},
            #{minutosTrabajados},
            #{minutosTardanza},
            #{minutosSalidaAnticipada},
            #{observacion}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "idAsistencia",
             keyColumn = "id_asistencia")
    int crear(AsistenciaDiaria asistencia);

    @Update("""
        UPDATE BE_ASISTENCIA_DIARIA
        SET id_empleado = #{idEmpleado},
            fecha = #{fecha},
            estado_asistencia = #{estadoAsistencia},
            minutos_trabajados = #{minutosTrabajados},
            minutos_tardanza = #{minutosTardanza},
            minutos_salida_anticipada = #{minutosSalidaAnticipada},
            observacion = #{observacion}
        WHERE id_asistencia = #{idAsistencia}
        """)
    int actualizar(AsistenciaDiaria asistencia);

    @Delete("""
        DELETE FROM BE_ASISTENCIA_DIARIA
        WHERE id_asistencia = #{id}
        """)
    int eliminar(@Param("id") Long id);


    @Select("""
    SELECT COUNT(*)
    FROM BE_ASISTENCIA_DIARIA
    WHERE id_empleado = #{idEmpleado}
      AND fecha = #{fecha}
      AND id_asistencia <> #{idAsistencia}
    """)
    int existeOtraAsistenciaEnFecha(
            @Param("idEmpleado") Long idEmpleado,
            @Param("fecha") java.time.LocalDate fecha,
            @Param("idAsistencia") Long idAsistencia);

    @Update("""
    UPDATE BE_ASISTENCIA_DIARIA
    SET observacion = LEFT(
        CONCAT(
            '[ANULADA] Motivo: ',
            #{motivo},
            CASE
                WHEN observacion IS NOT NULL
                    AND TRIM(observacion) <> ''
                THEN CONCAT(' | Observación previa: ', observacion)
                ELSE ''
            END
        ),
        255
    )
    WHERE id_asistencia = #{id}
    AND (
        observacion IS NULL
        OR observacion NOT LIKE '[ANULADA]%'
    )
    """)
    int anular(
        @Param("id") Long id,
        @Param("motivo") String motivo
    );       
}
