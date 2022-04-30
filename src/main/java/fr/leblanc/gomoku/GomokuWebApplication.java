package fr.leblanc.gomoku;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class GomokuWebApplication implements CommandLineRunner
{
    public static void main(final String[] args) {
        SpringApplication.run(GomokuWebApplication.class, args);
    }
    
    public void run(final String... args) throws Exception {
    	//start
    }
}