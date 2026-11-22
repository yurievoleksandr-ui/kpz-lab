import model.Parcel;
import validation.ValidationResult;
import validation.ValidatorService;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws Exception {
        ValidatorService validator = new ValidatorService();

        Parcel valid = new Parcel("RA123456789UA", 3.2, true,
                "Чернівці", LocalDate.of(2026, 11, 25));

        Parcel invalid = new Parcel("AB123", 50.0, true,
                "Львів", LocalDate.of(2026, 11, 25));

        print("Валідне відправлення", valid, validator.validate(valid));
        print("Невалідне відправлення", invalid, validator.validate(invalid));

        // Завдання 3
        // Benchmark.run();   // розкоментуйте, щоб запустити бенчмарк

        // Завдання 4
        ComponentEngine.run("components");
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