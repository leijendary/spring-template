package com.leijendary.spring.microservicetemplate.repository.provider;

import com.leijendary.spring.microservicetemplate.model.LocaleModel;
import com.leijendary.spring.microservicetemplate.model.LocalizedModel;
import com.leijendary.spring.microservicetemplate.repository.LocaleAwareRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.query.EscapeCharacter;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public class LocaleAwareRepositoryProvider<R extends LocalizedModel<R, T>, T extends LocaleModel<R>,
        ID extends Serializable> extends SimpleJpaRepository<R, ID> implements LocaleAwareRepository<R, T, ID> {

    private final Class<R> domainClass;
    private final EntityManager entityManager;
    private final JpaEntityInformation<R, ?> entityInformation;
    private final PersistenceProvider provider;

    @Nullable
    private CrudMethodMetadata metadata;

    private EscapeCharacter escapeCharacter = EscapeCharacter.DEFAULT;

    public LocaleAwareRepositoryProvider(final JpaEntityInformation<R, ?> entityInformation,
                                         final EntityManager entityManager) {
        super(entityInformation, entityManager);

        this.entityManager = entityManager;
        this.entityInformation = entityInformation;
        this.domainClass = entityInformation.getJavaType();
        this.provider = PersistenceProvider.fromEntityManager(entityManager);
    }

    public LocaleAwareRepositoryProvider(final Class<R> domainClass, final EntityManager entityManager) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager), entityManager);
    }

    @Override
    public void setRepositoryMethodMetadata(@NonNull final CrudMethodMetadata crudMethodMetadata) {
        this.metadata = crudMethodMetadata;
    }

    @Override
    public void setEscapeCharacter(@NonNull final EscapeCharacter escapeCharacter) {
        this.escapeCharacter = escapeCharacter;
    }

    @Override
    public Optional<R> findTranslatedById(ID id, String language) {
        return Optional.ofNullable(entityManager.find(domainClass, id));
    }

    @Override
    public Optional<R> findTranslatedById(ID id) {
        return Optional.empty();
    }

    @Override
    public Page<R> findTranslatedAll(Specification<R> specification, Pageable pageable, String language) {
        return null;
    }

    @Override
    public Page<R> findTranslatedAll(Pageable pageable, String language) {
        return null;
    }

    @Override
    public Page<R> findTranslatedAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<R> findTranslatedAll(String language) {
        return null;
    }

    @Override
    public List<R> findTranslatedAll() {
        return null;
    }
}
