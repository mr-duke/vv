package de.thro.vv.kleiderkreisel;

import de.thro.vv.kleiderkreisel.server.entities.Konto;
import de.thro.vv.kleiderkreisel.server.repositories.KontoRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class KleiderkreiselApplication {

    @Bean
    InitializingBean initialisiere (KontoRepository kontoRepo) {
        return new InitializingBean() {
            // Lege ein Demo-Konto für die Tauschplattform zur Abrechnung der Tauschgebühren an
            @Override
            public void afterPropertiesSet() throws Exception {
                Konto konto = new Konto(1L, 0L, LocalDateTime.now());
                kontoRepo.save(konto);
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(KleiderkreiselApplication.class, args);
    }

}
