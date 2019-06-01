package de.thro.vv.kleiderkreisel.server.repositories;

import de.thro.vv.kleiderkreisel.server.entities.Kleidung;
import org.springframework.data.repository.CrudRepository;

public interface KleidungRepository extends CrudRepository<Kleidung, Long> {
}
