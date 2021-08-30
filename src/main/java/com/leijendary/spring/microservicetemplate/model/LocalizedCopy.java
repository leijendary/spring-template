package com.leijendary.spring.microservicetemplate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

import static com.leijendary.spring.microservicetemplate.util.RequestContext.getLanguage;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Data
@MappedSuperclass
public abstract class LocalizedCopy<R, T extends LocaleCopy<R>> {

    @Id
    private long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "reference", fetch = EAGER, cascade = ALL)
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
