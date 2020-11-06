import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/11/05 11:05
 */
@ComponentScan("com.yxt")
@MapperScan("com.yxt.crud.mapper")
@EnableAutoConfiguration
public class CrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudApplication.class, args);
	}
}


