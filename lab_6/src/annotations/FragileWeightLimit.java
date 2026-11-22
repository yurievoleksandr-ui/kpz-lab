package lab6.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Рівень класу: якщо відправлення крихке, його вага не повинна перевищувати maxWeight.
 * Імена полів передаються як метадані, щоб валідатор не був жорстко прив'язаний до Parcel.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FragileWeightLimit {
    String fragileField() default "fragile";
    String weightField() default "weight";
    double maxWeight();
    String message() default "Крихке відправлення перевищує допустимий ліміт ваги";
}