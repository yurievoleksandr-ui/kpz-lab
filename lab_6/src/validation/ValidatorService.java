package validation;

import annotations.FragileWeightLimit;
import annotations.MatchesPattern;
import annotations.NumericRange;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class ValidatorService implements Validator {

    @Override
    public ValidationResult validate(Object entity) {
        ValidationResult result = new ValidationResult();
        if (entity == null) {
            result.addError("entity", "Об'єкт для валідації не може бути null");
            return result;
        }

        Class<?> clazz = entity.getClass();

        // 1. Валідація рівня класу
        FragileWeightLimit classRule = clazz.getAnnotation(FragileWeightLimit.class);
        if (classRule != null) {
            validateFragileWeight(entity, clazz, classRule, result);
        }

        // 2. Валідація рівня полів (публічні та закриті)
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(entity);
            } catch (IllegalAccessException e) {
                result.addError(field.getName(), "Не вдалося прочитати значення поля");
                continue;
            }

            MatchesPattern pattern = field.getAnnotation(MatchesPattern.class);
            if (pattern != null) {
                checkPattern(field, value, pattern, result);
            }

            NumericRange range = field.getAnnotation(NumericRange.class);
            if (range != null) {
                checkRange(field, value, range, result);
            }
        }
        return result;
    }

    private void validateFragileWeight(Object entity, Class<?> clazz,
                                       FragileWeightLimit rule, ValidationResult result) {
        try {
            Field fragileField = clazz.getDeclaredField(rule.fragileField());
            Field weightField = clazz.getDeclaredField(rule.weightField());
            fragileField.setAccessible(true);
            weightField.setAccessible(true);

            boolean fragile = (Boolean) fragileField.get(entity);
            double weight = ((Number) weightField.get(entity)).doubleValue();

            if (fragile && weight > rule.maxWeight()) {
                result.addError(rule.weightField(), rule.message());
            }
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            result.addError(clazz.getSimpleName(),
                    "Помилка конфігурації правила класу: " + e.getMessage());
        }
    }

    private void checkPattern(Field field, Object value, MatchesPattern ann,
                              ValidationResult result) {
        if (!(value instanceof String) || !Pattern.matches(ann.regex(), (String) value)) {
            result.addError(field.getName(), ann.message());
        }
    }

    private void checkRange(Field field, Object value, NumericRange ann,
                            ValidationResult result) {
        if (!(value instanceof Number)) {
            result.addError(field.getName(), "Поле не є числовим");
            return;
        }
        double number = ((Number) value).doubleValue();
        if (number < ann.min() || number > ann.max()) {
            result.addError(field.getName(), ann.message());
        }
    }
}
