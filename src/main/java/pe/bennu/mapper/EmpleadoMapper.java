
package pe.bennu.mapper;

import java.util.List;
import org.apache.ibatis.annotations.*;
import pe.bennu.model.Empleado;

@Mapper
public interface EmpleadoMapper {

    @Select("""
        SELECT id_empleado AS idEmpleado,
               DNI AS dni,
               nombres,
               apellidos,
               correo,
               fecha_ingreso AS fechaIngreso,
               fecha_cese AS fechaCese,
               sueldo,
               estado
        FROM BE_EMPLEADO
        ORDER BY id_empleado
        """)
    List<Empleado> listar();

    @Select("""
        SELECT id_empleado AS idEmpleado,
               DNI AS dni,
               nombres,
               apellidos,
               correo,
               fecha_ingreso AS fechaIngreso,
               fecha_cese AS fechaCese,
               sueldo,
               estado
        FROM BE_EMPLEADO
        WHERE id_empleado = #{id}
        """)
    Empleado buscarPorId(int id);

    @Insert("""
        INSERT INTO BE_EMPLEADO
        (DNI, nombres, apellidos, correo,
         fecha_ingreso, sueldo, estado)
        VALUES
        (#{dni}, #{nombres}, #{apellidos}, #{correo},
         #{fechaIngreso}, #{sueldo}, #{estado})
        """)
    @Options(useGeneratedKeys = true,
             keyProperty = "idEmpleado",
             keyColumn = "id_empleado")
    int crear(Empleado empleado);

    @Update("""
        UPDATE BE_EMPLEADO
        SET DNI = #{dni},
            nombres = #{nombres},
            apellidos = #{apellidos},
            correo = #{correo},
            fecha_ingreso = #{fechaIngreso},
            sueldo = #{sueldo},
            estado = #{estado}
        WHERE id_empleado = #{idEmpleado}
        """)
    int actualizar(Empleado empleado);

    @Update("""
        <script>
        UPDATE BE_EMPLEADO
        <set>
            <if test="empleado.dni != null">
                DNI = #{empleado.dni},
            </if>
            <if test="empleado.nombres != null">
                nombres = #{empleado.nombres},
            </if>
            <if test="empleado.apellidos != null">
                apellidos = #{empleado.apellidos},
            </if>
            <if test="empleado.correo != null">
                correo = #{empleado.correo},
            </if>
            <if test="empleado.fechaIngreso != null">
                fecha_ingreso = #{empleado.fechaIngreso},
            </if>
            <if test="empleado.fechaCese != null">
                fecha_cese = #{empleado.fechaCese},
            </if>
            <if test="empleado.sueldo != null">
                sueldo = #{empleado.sueldo},
            </if>
            <if test="empleado.estado != null">
                estado = #{empleado.estado},
            </if>
        </set>
        WHERE id_empleado = #{empleado.idEmpleado}
        </script>
        """)
    int actualizarParcial(@Param("empleado") Empleado empleado);

    @Update("""
        UPDATE BE_EMPLEADO
        SET estado = 'INACTIVO'
        WHERE id_empleado = #{id}
          AND estado = 'ACTIVO'
        """)
    int desactivar(int id);
}
