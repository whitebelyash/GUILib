package ru.whbex.guilib.gui;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
public @interface Pattern {
    String[] pattern() default {"#########"};
}
