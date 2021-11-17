package com.leijendary.spring.boot.template.model;

import static com.leijendary.spring.boot.template.util.RequestContext.getLanguage;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;
import static javax.persistence.FetchType.EAGER;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class LocalizedCopy<T extends LocaleCopy> extends AppModel {

    @Id
    private UUID id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ElementCollection(fetch = EAGER)
    @CollectionTable(joinColumns = @JoinColumn(name = "id"))
    private Set<T> translations = new HashSet<>();

    public T getTranslation() {
        final var language = getLanguage();
        final var sorted = translations
                .stream()
                .sorted(comparingInt(LocaleCopy::getOrdinal))
                .collect(toList());

        return sorted
                .stream()
                .filter(t -> t.getLanguage().equals(language))
                .findFirst()
                .orElse(sorted.iterator().next());
    }
}
