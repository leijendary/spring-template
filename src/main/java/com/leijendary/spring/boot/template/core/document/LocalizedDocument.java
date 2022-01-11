package com.leijendary.spring.boot.template.core.document;

import static com.leijendary.spring.boot.template.core.util.RequestContext.getLanguage;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.elasticsearch.annotations.FieldType.Nested;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.elasticsearch.annotations.Field;

import lombok.Data;

@Data
public abstract class LocalizedDocument<T extends LocaleDocument> {

    @Field(type = Nested, includeInParent = true)
    private Set<T> translations = new HashSet<>();

    public T getTranslation() {
        final var language = getLanguage();
        final var sorted = translations
                .stream()
                .sorted(comparingInt(LocaleDocument::getOrdinal))
                .collect(toList());

        return sorted
                .stream()
                .filter(t -> t.getLanguage().equals(language))
                .findFirst()
                .orElse(sorted.iterator().next());
    }
}
