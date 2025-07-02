package cl.lcd.enums;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class LocationTypeConverter extends AbstractBeanField<LocationType, String> {

    @Override
    protected LocationType convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        return switch (value) {
            case "A", "AP" -> LocationType.AIRPORT;
            case "C", "CC" -> LocationType.CITY;
            default -> LocationType.OTHER;
        };
    }
}
