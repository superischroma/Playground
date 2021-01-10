package me.superischroma.playground.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command
{
    String name() default "";
    String description() default "";
    String usage() default "<command>";
    String aliases() default "";
}