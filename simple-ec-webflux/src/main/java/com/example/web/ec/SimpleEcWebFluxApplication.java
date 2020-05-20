package com.example.web.ec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class SimpleEcWebFluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleEcWebFluxApplication.class, args);
	}

}
