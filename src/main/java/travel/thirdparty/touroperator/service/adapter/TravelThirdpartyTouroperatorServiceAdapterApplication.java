package travel.thirdparty.touroperator.service.adapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"travel.thirdparty.touroperator.service.adapter", "travel.thirdparty.touroperator.service.adapter.config"})
public class TravelThirdpartyTouroperatorServiceAdapterApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelThirdpartyTouroperatorServiceAdapterApplication.class, args);
	}

}
