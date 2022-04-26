package fr.leblanc.gomoku;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class GomokuApplication implements CommandLineRunner
{
    public static void main(final String[] args) {
        SpringApplication.run(GomokuApplication.class, args);
    }
    
    public void run(final String... args) throws Exception {
    }
}