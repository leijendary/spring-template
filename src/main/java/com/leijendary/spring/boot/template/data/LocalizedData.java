package com.leijendary.spring.boot.template.data;

import static com.leijendary.spring.boot.template.util.RequestContext.getLanguage;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.Data;

@Data
public abstract class LocalizedData<T extends LocaleData> implements Serializable {

    private UUID id;
    private Set<T> translations = new HashSet<>();

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
