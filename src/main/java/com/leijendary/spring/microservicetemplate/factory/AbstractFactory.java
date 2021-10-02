package com.leijendary.spring.microservicetemplate.factory;

import com.leijendary.spring.microservicetemplate.data.LocalizedData;
import com.leijendary.spring.microservicetemplate.model.LocaleCopy;
import com.leijendary.spring.microservicetemplate.model.LocaleModel;
import com.leijendary.spring.microservicetemplate.model.LocalizedCopy;
import com.leijendary.spring.microservicetemplate.model.LocalizedModel;
import org.modelmapper.ModelMapper;

import static com.leijendary.spring.microservicetemplate.util.SpringContext.getBean;
import static java.util.stream.Collectors.toSet;

public abstract class AbstractFactory {

    protected static final ModelMapper MAPPER = getBean(ModelMapper.class);

    public static <D extends LocalizedData<?>,
            M extends LocalizedModel<T>,
            T extends LocaleModel> void translations(final D data, final M model, final Class<T> type) {
        final var translations = data.getTranslations()
                .stream()
                .map(translation -> MAPPER.map(translation, type))
                .collect(toSet());

        model.setTranslations(translations);
    }

    public static <D extends LocalizedData<?>,
            M extends LocalizedCopy<T>,
            T extends LocaleCopy> void translations(final D data, final M model, final Class<T> type) {
        final var translations = data.getTranslations()
                .stream()
                .map(translation -> MAPPER.map(translation, type))
                .collect(toSet());

        model.setTranslations(translations);
    }
}
