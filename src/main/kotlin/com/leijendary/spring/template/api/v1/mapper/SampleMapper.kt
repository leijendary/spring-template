package com.leijendary.spring.template.api.v1.mapper

import com.leijendary.spring.template.api.v1.model.SampleMessage
import com.leijendary.spring.template.api.v1.model.SampleRequest
import com.leijendary.spring.template.api.v1.model.SampleResponse
import com.leijendary.spring.template.api.v1.model.SampleSearchResponse
import com.leijendary.spring.template.document.SampleDocument
import com.leijendary.spring.template.entity.SampleTable
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers.getMapper

@Mapper
interface SampleMapper {
    companion object {
        val INSTANCE: SampleMapper = getMapper(SampleMapper::class.java)
    }

    fun toResponse(sampleTable: SampleTable): SampleResponse

    @Mappings(
        Mapping(source = "field1", target = "column1"),
        Mapping(source = "field2", target = "column2")
    )
    fun toEntity(sampleRequest: SampleRequest): SampleTable

    fun toEntity(sampleMessage: SampleMessage): SampleTable

    @Mappings(
        Mapping(source = "translation.name", target = "name"),
        Mapping(source = "translation.description", target = "description")
    )
    fun toSearchResponse(sampleDocument: SampleDocument): SampleSearchResponse

    fun toDocument(sampleTable: SampleTable): SampleDocument

    @Mappings(
        Mapping(source = "field1", target = "column1"),
        Mapping(source = "field2", target = "column2")
    )
    fun update(sampleRequest: SampleRequest, @MappingTarget sampleTable: SampleTable)

    fun update(sampleTable: SampleTable, @MappingTarget sampleDocument: SampleDocument)

    fun toMessage(sampleTable: SampleTable): SampleMessage
}
