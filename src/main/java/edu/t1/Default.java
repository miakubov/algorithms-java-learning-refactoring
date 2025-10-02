package edu.t1;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// Аннотация @Default помечает класс и хранит ссылку на класс конфигурации
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface Default {
    Class<?> value(); // Класс с дефолтными значениями
}
