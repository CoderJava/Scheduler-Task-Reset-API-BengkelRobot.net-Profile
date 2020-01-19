package net.bengkelrobot.schedulertaskresetfluttercrudapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableScheduling
@RestController
public class SchedulerTaskResetFlutterCrudApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulerTaskResetFlutterCrudApiApplication.class, args);
    }

    @RequestMapping("/")
    public String index() {
        return "Server is running";
    }

}
