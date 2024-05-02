package com.recipebank;


import com.recipebank.config.MapperConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@Import(MapperConfig.class)
public class RecipebankApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipebankApplication.class, args);
	}

}
