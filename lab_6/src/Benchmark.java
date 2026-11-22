import model.Parcel;
import validation.CachedValidationService;
import validation.Validator;
import validation.ValidatorService;

import java.time.LocalDate;
import java.util.Random;

public class Benchmark {

    private static final Random RANDOM = new Random(42); // фіксований seed для відтворюваності

    public static void run() {
        int[] sizes = {100_000, 500_000};

        // Прогрів JIT на окремих екземплярах
        Parcel[] warm = generate(20_000);
        measure(new ValidatorService(), warm);
        measure(new CachedValidationService(), warm);

        System.out.println("=== Бенчмарк валідації ===");
        System.out.printf("%-22s | %-10s | %-10s | %-14s | %-8s%n",
                "Підхід", "Ітерацій", "Час, мс", "нс / виклик", "Валідних");
        System.out.println("-".repeat(78));

        for (int n : sizes) {
            Parcel[] data = generate(n);

            long[] base = measure(new ValidatorService(), data);
            long[] cached = measure(new CachedValidationService(), data);

            printRow("Без кешування", n, base);
            printRow("З кешуванням", n, cached);
            System.out.printf("Прискорення: x%.2f%n", (double) base[0] / cached[0]);
            System.out.println("-".repeat(78));
        }
    }

    private static long[] measure(Validator validator, Parcel[] data) {
        int valid = 0;
        long start = System.nanoTime();
        for (Parcel p : data) {
            if (validator.validate(p).isValid()) {
                valid++;
            }
        }
        long elapsed = System.nanoTime() - start;
        return new long[]{elapsed, valid};
    }

    private static void printRow(String name, int n, long[] r) {
        System.out.printf("%-22s | %-10d | %-10.2f | %-14d | %-8d%n",
                name, n, r[0] / 1_000_000.0, r[0] / n, r[1]);
    }

    private static Parcel[] generate(int count) {
        Parcel[] result = new Parcel[count];
        for (int i = 0; i < count; i++) {
            result[i] = randomParcel();
        }
        return result;
    }

    private static Parcel randomParcel() {
        String tracking;
        if (RANDOM.nextInt(4) == 0) {
            // ~25% некоректних трек-номерів
            tracking = letters(1 + RANDOM.nextInt(4)) + RANDOM.nextInt(1000);
        } else {
            tracking = letters(2) + String.format("%09d", RANDOM.nextInt(1_000_000_000)) + letters(2);
        }
        double weight = Math.round(RANDOM.nextDouble() * 400) / 10.0; // 0.0 .. 40.0
        boolean fragile = RANDOM.nextBoolean();
        String destination = "City" + RANDOM.nextInt(100);
        LocalDate date = LocalDate.of(2026, 1, 1).plusDays(RANDOM.nextInt(365));
        return new Parcel(tracking, weight, fragile, destination, date);
    }

    private static String letters(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append((char) ('A' + RANDOM.nextInt(26)));
        }
        return sb.toString();
    }
}