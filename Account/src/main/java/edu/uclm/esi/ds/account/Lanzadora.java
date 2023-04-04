package edu.uclm.esi.ds.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class Lanzadora {
	public static void main(String[] args) {
		SpringApplication.run(Lanzadora.class, args);
	}
}
