package com.leijendary.spring.microservicetemplate.mapper;

import com.leijendary.spring.microservicetemplate.util.NumberUtil;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper
public interface NumberMapper {

    NumberMapper INSTANCE = getMapper(NumberMapper.class);

    default BigDecimal toBigDecimal(final ByteBuffer byteBuffer) {
        return NumberUtil.toBigDecimal(byteBuffer);
    }

    default ByteBuffer toByteBuffer(final BigDecimal bigDecimal) {
        return NumberUtil.toByteBuffer(bigDecimal);
    }
}
