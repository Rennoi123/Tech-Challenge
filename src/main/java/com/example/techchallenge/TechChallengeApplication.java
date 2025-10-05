package com.example.techchallenge;

import com.github.lalyos.jfiglet.FigletFont;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TechChallengeApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TechChallengeApplication.class);
        app.setBannerMode(Banner.Mode.CONSOLE);
        app.setBanner((env, source, out) -> {
            try {
                String ascii = FigletFont.convertOneLine("TECH CHALLENGE V3");
                out.print("\u001B[92m");
                out.println(ascii);
                out.print("\u001B[0m");
            } catch (Exception e) {
                out.println("TECH CHALLENGE V3");
            }
        });
        app.run(args);
    }
}