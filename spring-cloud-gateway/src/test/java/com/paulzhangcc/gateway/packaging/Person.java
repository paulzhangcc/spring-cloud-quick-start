package com.paulzhangcc.gateway.packaging;

import java.util.function.Function;

/**
 * @author paul
 * @description
 * @date 2019/3/20
 */
public class Person {
    private String name;
    private Integer age;

    public static Person create() {
        return new Person();
    }

    public Person config() {
        System.out.println(1);
        return this;
    }

    public Person name(Function<? super Person, ? extends Person> function) {
        return new PersonConfig(this, function);
    }

    public Person age(Function<? super Person, ? extends Person> function) {
        return new PersonConfig(this, function);
    }

    public Person build() {
        return config();
    }

    public static void main(String[] args) {
        Person build = Person.create().age((person) -> {
            person.age = 18;
            System.out.println(2);
            return person;
        }).name((person) -> {
            person.name = "zjf";
            System.out.println(3);
            return person;
        }).build();
    }


}
