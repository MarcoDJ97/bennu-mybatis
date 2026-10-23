package pe.bennu.empleados_mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "pe.bennu")
@MapperScan("pe.bennu.mapper")
public class EmpleadosMybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmpleadosMybatisApplication.class, args);
    }
}