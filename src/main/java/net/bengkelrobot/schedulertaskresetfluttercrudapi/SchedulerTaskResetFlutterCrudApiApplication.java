package net.bengkelrobot.schedulertaskresetfluttercrudapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SchedulerTaskResetFlutterCrudApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedulerTaskResetFlutterCrudApiApplication.class, args);
	}

}
