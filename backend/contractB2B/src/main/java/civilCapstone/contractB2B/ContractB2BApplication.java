package civilCapstone.contractB2B;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = {civilCapstone.contractB2B.chat.repository.MessageRepository.class})
public class ContractB2BApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContractB2BApplication.class, args);
	}

}

