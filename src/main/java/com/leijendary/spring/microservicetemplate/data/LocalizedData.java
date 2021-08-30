package com.leijendary.spring.microservicetemplate.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.LinkedHashSet;

import static com.leijendary.spring.microservicetemplate.util.RequestContext.getLanguage;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

@Data
public abstract class LocalizedData<T extends LocaleData> implements Serializable {

    private long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private LinkedHashSet<T> translations = new LinkedHashSet<>();

    public T getTranslation() {
        final var language = getLanguage();
        final var sorted = translations
                .stream()
                .sorted(comparingInt(LocaleData::getOrdinal))
                .collect(toList());

        return sorted
                .stream()
                .filter(t -> t.getLanguage().equals(language))
                .findFirst()
                .orElse(sorted.iterator().next());
    }
}
