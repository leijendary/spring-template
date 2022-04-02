package com.leijendary.spring.boot.template.api.v1.mapper

import com.leijendary.spring.boot.template.api.v1.data.SampleMessage
import com.leijendary.spring.boot.template.api.v1.data.SampleRequest
import com.leijendary.spring.boot.template.api.v1.data.SampleResponse
import com.leijendary.spring.boot.template.api.v1.data.SampleSearchResponse
import com.leijendary.spring.boot.template.document.SampleDocument
import com.leijendary.spring.boot.template.model.SampleTable
import com.leijendary.spring.boot.template.projection.SampleTableProjection
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

    fun toResponse(sampleTableProjection: SampleTableProjection): SampleResponse

    @Mappings(
        Mapping(source = "field1", target = "column1"),
        Mapping(source = "field2", target = "column2")
    )
    fun toEntity(sampleRequestV1: SampleRequest): SampleTable

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