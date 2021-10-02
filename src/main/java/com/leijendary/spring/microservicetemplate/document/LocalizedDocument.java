package com.leijendary.spring.microservicetemplate.document;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.HashSet;
import java.util.Set;

import static com.leijendary.spring.microservicetemplate.util.RequestContext.getLanguage;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.elasticsearch.annotations.FieldType.Nested;

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
