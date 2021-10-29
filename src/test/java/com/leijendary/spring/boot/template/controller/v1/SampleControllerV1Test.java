package com.leijendary.spring.boot.template.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leijendary.spring.boot.core.data.response.DataResponse;
import com.leijendary.spring.boot.core.data.response.ErrorResponse;
import com.leijendary.spring.boot.template.ApplicationTests;
import com.leijendary.spring.boot.template.data.response.v1.SampleResponseV1;
import com.leijendary.spring.boot.template.repository.SampleTableRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;

import static com.leijendary.spring.boot.core.util.JsonUtil.toClass;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SampleControllerV1Test extends ApplicationTests {

    @Autowired
    private SampleTableRepository sampleTableRepository;

    @Test
    @Order(1)
    public void create_ValidationField1_Fail() throws Exception {
        final var request = new HashMap<String, String>();
        request.put("field1", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        request.put("field2", "1");

        ResponseEntity<DataResponse> response = null;

        try {
            response = restTemplateWithToken("urn:sample:create:v1")
                    .build()
                    .postForEntity("/api/v1", request, DataResponse.class);
        } catch (HttpClientErrorException.BadRequest exception) {
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

            String body = exception.getResponseBodyAsString();
            final var errorResponse = toClass(body, ErrorResponse.class);
            final var errorCount = errorResponse.getErrors()
                    .stream()
                    .filter(e -> (e.getCode().equals("validation.maxLength")
                            && e.getSource().equals("field1")
                            && e.getMessage().equals("Exceeds limit of 50 characters")))
                    .count();
            final var meta = errorResponse.getMeta();
            final var links = errorResponse.getLinks();

            assertEquals(0, sampleTableRepository.count());
            assertEquals(1, errorCount);
            assertEquals(HttpStatus.BAD_REQUEST.value(), meta.get("status"));
            assertNotNull(links.get("self"));
        }

        assertNull(response);
    }

    @Test
    @Order(2)
    public void create_ValidationField2_Fail() throws JsonProcessingException {
        final var field1Value = "Valid Field 1 Value";
        final var field2Value = "THIS IS NOT VALID";
        final var request = new HashMap<String, String>();
        request.put("field1", field1Value);
        request.put("field2", field2Value);

        ResponseEntity<DataResponse> response = null;

        try {
            response = restTemplateWithToken("urn:sample:create:v1")
                    .build()
                    .postForEntity("/api/v1", request, DataResponse.class);
        } catch (HttpClientErrorException.BadRequest exception) {
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

            String body = exception.getResponseBodyAsString();
            final var errorResponse = toClass(body, ErrorResponse.class);
            final var errorCount = errorResponse.getErrors()
                    .stream()
                    .filter(e -> e.getCode().equals("error.invalidFormat")
                            && e.getSource().equals("field2")
                            && e.getMessage().equals("Field field2 with the value of '" + field2Value +
                            "' is not a valid format of int"))
                    .count();
            final var meta = errorResponse.getMeta();
            final var links = errorResponse.getLinks();

            assertEquals(0, sampleTableRepository.count());
            assertEquals(1, errorCount);
            assertEquals(HttpStatus.BAD_REQUEST.value(), meta.get("status"));
            assertNotNull(links.get("self"));
        }

        assertNull(response);
    }

    @Test
    @Order(3)
    public void create_Validation_Success() {
        final var field1Value = "Valid Field 1 Value";
        final var field2Value = "11111";
        final var request = new HashMap<String, String>();
        request.put("field1", field1Value);
        request.put("field2", field2Value);

        final var response = restTemplateWithToken("urn:sample:create:v1")
                .build()
                .postForEntity("/api/v1", request, DataResponse.class);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());

        final var dataResponse = response.getBody();

        assertNotNull(dataResponse);

        final var data = toClass(dataResponse.getData(), SampleResponseV1.class);

        assertNotEquals(0, data.getId());
        assertEquals(field1Value, data.getColumn1());
        assertEquals(field2Value, data.getColumn2());
        assertNotNull(data.getCreatedBy());
        assertNotNull(data.getCreatedDate());
        assertNotNull(data.getLastModifiedBy());
        assertNotNull(data.getLastModifiedDate());

        final var meta = dataResponse.getMeta();
        final var status = (int) meta.get("status");
        final var type = (String) meta.get("type");
        final var object = (String) meta.get("object");
        final var links = dataResponse.getLinks();

        assertEquals(HttpStatus.CREATED.value(), status);
        assertEquals("object", type);
        assertEquals(SampleResponseV1.class.getSimpleName(), object);
        assertNotNull(links.get("self"));
    }

    @Test
    @Order(4)
    public void update_NotExisting_Fail() {
        final var request = new HashMap<String, String>();
        request.put("field1", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        request.put("field2", "1");

        final var entity = new HttpEntity<>(request);

        assertThrows(HttpClientErrorException.NotFound.class,
                () -> restTemplateWithToken("urn:sample:update:v1")
                        .build()
                        .exchange("/api/v1/" + new SecureRandom().nextLong(), HttpMethod.PUT, entity,
                                DataResponse.class));

        final var sampleTable = sampleTableRepository.findAll().stream().findFirst().get();

        assertNotEquals(request.get("field1"), sampleTable.getColumn1());
        assertNotEquals(request.get("field2"), String.valueOf(sampleTable.getColumn2()));
        assertEquals(sampleTable.getCreatedDate(), sampleTable.getLastModifiedDate());
    }

    @Test
    @Order(5)
    public void update_ValidationField1_Fail() throws Exception {
        final var request = new HashMap<String, String>();
        request.put("field1", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        request.put("field2", "1");

        var sampleTable = sampleTableRepository.findAll()
                .stream()
                .findFirst()
                .get();
        final var entity = new HttpEntity<>(request);
        ResponseEntity<DataResponse> response = null;

        try {
            response = restTemplateWithToken("urn:sample:update:v1")
                    .build()
                    .exchange("/api/v1/" + sampleTable.getId(), HttpMethod.PUT, entity, DataResponse.class);
        } catch (HttpClientErrorException.BadRequest exception) {
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

            String body = exception.getResponseBodyAsString();
            final var errorResponse = toClass(body, ErrorResponse.class);
            final var errorCount = errorResponse.getErrors()
                    .stream()
                    .filter(e -> (e.getCode().equals("validation.maxLength")
                            && e.getSource().equals("field1")
                            && e.getMessage().equals("Exceeds limit of 50 characters")))
                    .count();
            final var meta = errorResponse.getMeta();
            final var links = errorResponse.getLinks();

            assertEquals(1, errorCount);
            assertEquals(HttpStatus.BAD_REQUEST.value(), meta.get("status"));
            assertNotNull(links.get("self"));
        }

        sampleTable = sampleTableRepository.findAll().stream().findFirst().get();

        assertNotEquals(request.get("field1"), sampleTable.getColumn1());
        assertNotEquals(request.get("field2"), String.valueOf(sampleTable.getColumn2()));
        assertEquals(sampleTable.getCreatedDate(), sampleTable.getLastModifiedDate());

        assertNull(response);
    }

    @Test
    @Order(6)
    public void update_ValidationField2_Fail() throws JsonProcessingException {
        final var field1Value = "Valid Field 1 Updated Value";
        final var field2Value = "THIS IS NOT VALID";
        final var request = new HashMap<String, String>();
        request.put("field1", field1Value);
        request.put("field2", field2Value);

        var sampleTable = sampleTableRepository.findAll()
                .stream()
                .findFirst()
                .get();
        final var entity = new HttpEntity<>(request);
        ResponseEntity<DataResponse> response = null;

        try {
            response = restTemplateWithToken("urn:sample:update:v1")
                    .build()
                    .exchange("/api/v1/" + sampleTable.getId(), HttpMethod.PUT, entity, DataResponse.class);
        } catch (HttpClientErrorException.BadRequest exception) {
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

            String body = exception.getResponseBodyAsString();
            final var errorResponse = toClass(body, ErrorResponse.class);
            final var errorCount = errorResponse.getErrors()
                    .stream()
                    .filter(e -> e.getCode().equals("error.invalidFormat")
                            && e.getSource().equals("field2")
                            && e.getMessage().equals("Field field2 with the value of '" + field2Value +
                            "' is not a valid format of int"))
                    .count();
            final var meta = errorResponse.getMeta();
            final var links = errorResponse.getLinks();

            assertEquals(1, errorCount);
            assertEquals(HttpStatus.BAD_REQUEST.value(), meta.get("status"));
            assertNotNull(links.get("self"));
        }

        sampleTable = sampleTableRepository.findAll().stream().findFirst().get();

        assertNotEquals(request.get("field1"), sampleTable.getColumn1());
        assertNotEquals(request.get("field2"), String.valueOf(sampleTable.getColumn2()));
        assertEquals(sampleTable.getCreatedDate(), sampleTable.getLastModifiedDate());

        assertNull(response);
    }

    @Test
    @Order(7)
    public void update_Validation_Success() {
        final var field1Value = "Valid Field 1 Updated Value";
        final var field2Value = "22222";
        final var request = new HashMap<String, String>();
        request.put("field1", field1Value);
        request.put("field2", field2Value);

        final var entity = new HttpEntity<>(request);
        final var sampleTable = sampleTableRepository.findAll()
                .stream()
                .findFirst()
                .get();
        final var response = restTemplateWithToken("urn:sample:update:v1")
                .build()
                .exchange("/api/v1/" + sampleTable.getId(), HttpMethod.PUT, entity, DataResponse.class);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

        final var dataResponse = response.getBody();

        assertNotNull(dataResponse);

        final var data = toClass(dataResponse.getData(), SampleResponseV1.class);

        assertEquals(1, sampleTableRepository.count());
        assertEquals(field1Value, data.getColumn1());
        assertEquals(field2Value, data.getColumn2());
        assertNotEquals(data.getCreatedDate(), data.getLastModifiedDate());

        final var meta = dataResponse.getMeta();
        final var status = (int) meta.get("status");
        final var type = (String) meta.get("type");
        final var object = (String) meta.get("object");
        final var links = dataResponse.getLinks();

        assertEquals(HttpStatus.OK.value(), status);
        assertEquals("object", type);
        assertEquals(SampleResponseV1.class.getSimpleName(), object);
        assertNotNull(links.get("self"));
    }

    @Test
    @Order(8)
    public void list_HasContent_Success() {
        final var query = "Updated";
        final var page = 0;
        final var size = 10;
        final var sorts = new Object[] { "column2,asc", "column1,desc" };
        final var uri = UriComponentsBuilder.fromPath("/api/v1")
                .queryParam("query", query)
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sort", sorts)
                .toUriString();
        final var response = restTemplateWithToken("urn:sample:list:v1")
                .build()
                .getForEntity(uri, DataResponse.class);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

        final var dataResponse = response.getBody();

        assertNotNull(dataResponse);

        final var data = toClass(dataResponse.getData(), List.class);

        assertEquals(1, data.size());

        data.forEach(d -> {
            final var sampleResponse = toClass(d, SampleResponseV1.class);

            assertNotEquals(0, sampleResponse.getId());
            assertNotNull(sampleResponse.getColumn1());
            assertNotNull(sampleResponse.getColumn2());
            assertNotNull(sampleResponse.getCreatedDate());
            assertNotNull(sampleResponse.getCreatedBy());
            assertNotNull(sampleResponse.getLastModifiedDate());
            assertNotNull(sampleResponse.getLastModifiedBy());
        });

        final var meta = dataResponse.getMeta();
        final var status = (int) meta.get("status");
        final var type = (String) meta.get("type");
        final var object = (String) meta.get("object");
        final var links = dataResponse.getLinks();

        assertEquals(HttpStatus.OK.value(), status);
        assertEquals("array", type);
        assertEquals(SampleResponseV1.class.getSimpleName(), object);
        assertNotNull(links.get("self"));
    }

    @Test
    @Order(9)
    public void delete_NonExisting_Fail() {
        assertThrows(HttpClientErrorException.NotFound.class,
                () -> restTemplateWithToken("urn:sample:delete:v1")
                        .build()
                        .delete("/api/v1/" + new SecureRandom().nextLong()));
    }

    @Test
    @Order(10)
    public void get_NonExisting_Fail() {
        final var sampleTable = sampleTableRepository.findAll()
                .stream()
                .findFirst()
                .get();

        assertThrows(HttpClientErrorException.NotFound.class,
                () -> restTemplateWithToken("urn:sample:get:v1")
                        .build()
                        .getForEntity("/api/v1/" + new SecureRandom().nextLong(), DataResponse.class));
    }

    @Test
    @Order(11)
    public void get_Existing_Success() {
        assertEquals(1, sampleTableRepository.count());

        final var sampleTable = sampleTableRepository.findAll()
                .stream()
                .findFirst()
                .get();
        final var field1Value = "Valid Field 1 Updated Value";
        final var field2Value = "22222";

        final var response = restTemplateWithToken("urn:sample:get:v1")
                .build()
                .getForEntity("/api/v1/" + sampleTable.getId(), DataResponse.class);
        final var dataResponse = response.getBody();

        assertNotNull(dataResponse);

        final var sampleResponse = toClass(dataResponse.getData(), SampleResponseV1.class);

        assertEquals(sampleTable.getId(), sampleResponse.getId());
        assertEquals(field1Value, sampleResponse.getColumn1());
        assertEquals(field2Value, sampleResponse.getColumn2());
        assertNotNull(sampleResponse.getCreatedDate());
        assertNotNull(sampleResponse.getCreatedBy());
        assertNotNull(sampleResponse.getLastModifiedDate());
        assertNotNull(sampleResponse.getLastModifiedBy());

        final var meta = dataResponse.getMeta();
        final var status = (int) meta.get("status");
        final var type = (String) meta.get("type");
        final var object = (String) meta.get("object");
        final var links = dataResponse.getLinks();

        assertEquals(HttpStatus.OK.value(), status);
        assertEquals("object", type);
        assertEquals(SampleResponseV1.class.getSimpleName(), object);
        assertNotNull(links.get("self"));
    }

    @Test
    @Order(12)
    public void delete_Existing_Success() {
        final var sampleTable = sampleTableRepository.findAll()
                .stream()
                .findFirst()
                .get();

        restTemplateWithToken("urn:sample:delete:v1")
                .build()
                .delete("/api/v1/" + sampleTable.getId());
    }

    @Test
    @Order(13)
    public void count_Existing_Zero() {
        assertEquals(0, sampleTableRepository.count());
    }
}
