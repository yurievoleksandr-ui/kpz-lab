package validation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CachedValidationService implements Validator {

    private final Map<Class<?>, ClassValidationMetadata> cache = new ConcurrentHashMap<>();

    @Override
    public ValidationResult validate(Object entity) {
        ValidationResult result = new ValidationResult();
        if (entity == null) {
            result.addError("entity", "Об'єкт для валідації не може бути null");
            return result;
        }

        // Рефлексія виконується лише при першому зверненні до типу
        ClassValidationMetadata meta =
                cache.computeIfAbsent(entity.getClass(), ClassValidationMetadata::build);

        // 1. Правило рівня класу
        if (meta.classRule != null) {
            if (meta.classConfigError != null) {
                result.addError(meta.className, meta.classConfigError);
            } else {
                try {
                    boolean fragile = (Boolean) meta.fragileField.get(entity);
                    double weight = ((Number) meta.weightField.get(entity)).doubleValue();
                    if (fragile && weight > meta.classRule.maxWeight()) {
                        result.addError(meta.classRule.weightField(), meta.classRule.message());
                    }
                } catch (IllegalAccessException | ClassCastException e) {
                    result.addError(meta.className,
                            "Помилка конфігурації правила класу: " + e.getMessage());
                }
            }
        }

        // 2. Правила полів (лише поля з анотаціями, з кешу)
        for (ClassValidationMetadata.FieldMetadata fm : meta.fields) {
            Object value;
            try {
                value = fm.field.get(entity);
            } catch (IllegalAccessException e) {
                result.addError(fm.field.getName(), "Не вдалося прочитати значення поля");
                continue;
            }

            if (fm.patternRule != null
                    && !(value instanceof String && fm.compiledPattern.matcher((String) value).matches())) {
                result.addError(fm.field.getName(), fm.patternRule.message());
            }

            if (fm.rangeRule != null) {
                if (!(value instanceof Number)) {
                    result.addError(fm.field.getName(), "Поле не є числовим");
                } else {
                    double n = ((Number) value).doubleValue();
                    if (n < fm.rangeRule.min() || n > fm.rangeRule.max()) {
                        result.addError(fm.field.getName(), fm.rangeRule.message());
                    }
                }
            }
        }
        return result;
    }

    public int cacheSize() {
        return cache.size();
    }
}