package de.thro.vv.kleiderkreisel;

import javax.persistence.AttributeConverter;
import java.time.LocalDate;

public class DatumsConverter implements AttributeConverter<LocalDate, String > {
    @Override
    public String convertToDatabaseColumn(LocalDate attribute) {
        return null;
    }

    @Override
    public LocalDate convertToEntityAttribute(String dbData) {
        return null;
    }
}
