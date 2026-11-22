package validation;

import annotations.FragileWeightLimit;
import annotations.MatchesPattern;
import annotations.NumericRange;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ClassValidationMetadata {

    /** Метадані одного поля, що має хоча б одну анотацію. */
    public static class FieldMetadata {
        final Field field;
        final MatchesPattern patternRule;
        final Pattern compiledPattern;   // скомпільований один раз
        final NumericRange rangeRule;

        FieldMetadata(Field field, MatchesPattern patternRule, NumericRange rangeRule) {
            this.field = field;
            this.patternRule = patternRule;
            this.compiledPattern = patternRule != null ? Pattern.compile(patternRule.regex()) : null;
            this.rangeRule = rangeRule;
        }
    }

    final String className;
    final FragileWeightLimit classRule;
    final Field fragileField;
    final Field weightField;
    final String classConfigError;       // не null, якщо правило класу налаштоване невірно
    final List<FieldMetadata> fields = new ArrayList<>();

    private ClassValidationMetadata(Class<?> clazz) {
        this.className = clazz.getSimpleName();
        this.classRule = clazz.getAnnotation(FragileWeightLimit.class);

        Field fragile = null;
        Field weight = null;
        String error = null;
        if (classRule != null) {
            try {
                fragile = clazz.getDeclaredField(classRule.fragileField());
                weight = clazz.getDeclaredField(classRule.weightField());
                fragile.setAccessible(true);
                weight.setAccessible(true);
            } catch (NoSuchFieldException e) {
                error = "Помилка конфігурації правила класу: " + e.getMessage();
            }
        }
        this.fragileField = fragile;
        this.weightField = weight;
        this.classConfigError = error;

        for (Field field : clazz.getDeclaredFields()) {
            MatchesPattern p = field.getAnnotation(MatchesPattern.class);
            NumericRange r = field.getAnnotation(NumericRange.class);
            if (p != null || r != null) {
                field.setAccessible(true);
                fields.add(new FieldMetadata(field, p, r));
            }
        }
    }

    /** Одноразовий «дорогий» розбір метаструктури типу. */
    public static ClassValidationMetadata build(Class<?> clazz) {
        return new ClassValidationMetadata(clazz);
    }
}