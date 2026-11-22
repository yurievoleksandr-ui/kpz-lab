package lab6;

import lab6.model.Parcel;
import lab6.validation.ValidationResult;
import lab6.validation.ValidatorService;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        ValidatorService validator = new ValidatorService();

        Parcel valid = new Parcel("RA123456789UA", 3.2, true,
                "Чернівці", LocalDate.of(2026, 11, 25));

        Parcel invalid = new Parcel("AB123", 50.0, true,
                "Львів", LocalDate.of(2026, 11, 25));

        print("Валідне відправлення", valid, validator.validate(valid));
        print("Невалідне відправлення", invalid, validator.validate(invalid));
    }

    private static void print(String title, Object entity, ValidationResult result) {
        System.out.println("=== " + title + " ===");
        System.out.println(entity);
        System.out.println("IsValid: " + result.isValid());
        if (!result.isValid()) {
            System.out.println("Помилки:");
            result.getErrors().forEach(e -> System.out.println("  - " + e));
        }
        System.out.println();
    }
}