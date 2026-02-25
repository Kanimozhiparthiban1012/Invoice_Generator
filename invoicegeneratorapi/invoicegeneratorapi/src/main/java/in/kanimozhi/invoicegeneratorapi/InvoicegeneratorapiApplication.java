package in.kanimozhi.invoicegeneratorapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class InvoicegeneratorapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoicegeneratorapiApplication.class, args);
	}

}
