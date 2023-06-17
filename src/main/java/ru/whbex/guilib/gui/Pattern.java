package ru.whbex.guilib.gui;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Pattern {
    String[] value() default {"#########"};
}
