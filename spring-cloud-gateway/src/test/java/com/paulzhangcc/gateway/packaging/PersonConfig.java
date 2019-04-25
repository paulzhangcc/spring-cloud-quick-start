package com.paulzhangcc.gateway.packaging;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author paul
 * @description
 * @date 2019/3/20
 */
public class PersonConfig extends Person {
    final Person source;

    final Function<? super Person, ? extends Person> personConfiguration;

    public PersonConfig(Person source,Function<? super Person, ? extends Person> personConfiguration) {
        this.source = Objects.requireNonNull(source, "source");
        this.personConfiguration = personConfiguration;
    }

    public Person config(){
        return Objects.requireNonNull(personConfiguration.apply(source.config()),
                "Configuration");
    }
}
