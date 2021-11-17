package com.leijendary.spring.boot.template.mapper;

import static org.mapstruct.factory.Mappers.getMapper;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

import com.leijendary.spring.boot.template.util.NumberUtil;

import org.mapstruct.Mapper;

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
