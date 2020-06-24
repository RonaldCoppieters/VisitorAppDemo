package be.pxl.VisitorsApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@ServletComponentScan
public class VisitorsApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication
				.run(VisitorsApplication.class, args);
	}

}
