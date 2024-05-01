package com.jhipster.novelapp.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.jhipster.novelapp.TestUtil;
import com.jhipster.novelapp.service.dto.TagDTO;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import jakarta.inject.Inject;
import java.util.List;
import liquibase.Liquibase;
import org.junit.jupiter.api.*;

@QuarkusTest
public class TagResourceTest {

    private static final TypeRef<TagDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<TagDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    String adminToken;

    TagDTO tagDTO;

    @Inject
    LiquibaseFactory liquibaseFactory;

    @BeforeAll
    static void jsonMapper() {
        RestAssured.config =
            RestAssured.config().objectMapperConfig(objectMapperConfig().defaultObjectMapper(TestUtil.jsonbObjectMapper()));
    }

    @BeforeEach
    public void authenticateAdmin() {
        this.adminToken = TestUtil.getAdminToken();
    }

    @BeforeEach
    public void databaseFixture() {
        try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
            liquibase.dropAll();
            liquibase.validate();
            liquibase.update(liquibaseFactory.createContexts(), liquibaseFactory.createLabels());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TagDTO createEntity() {
        var tagDTO = new TagDTO();
        tagDTO.name = DEFAULT_NAME;
        return tagDTO;
    }

    @BeforeEach
    public void initTest() {
        tagDTO = createEntity();
    }

    @Test
    public void createTag() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tags")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Tag
        tagDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(tagDTO)
                .when()
                .post("/api/tags")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        // Validate the Tag in the database
        var tagDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tags")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(tagDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testTagDTO = tagDTOList.stream().filter(it -> tagDTO.id.equals(it.id)).findFirst().get();
        assertThat(testTagDTO.name).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void createTagWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tags")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Tag with an existing ID
        tagDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(tagDTO)
            .when()
            .post("/api/tags")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Tag in the database
        var tagDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tags")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(tagDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tags")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        tagDTO.name = null;

        // Create the Tag, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(tagDTO)
            .when()
            .post("/api/tags")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Tag in the database
        var tagDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tags")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(tagDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateTag() {
        // Initialize the database
        tagDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(tagDTO)
                .when()
                .post("/api/tags")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tags")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the tag
        var updatedTagDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tags/{id}", tagDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the tag
        updatedTagDTO.name = UPDATED_NAME;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedTagDTO)
            .when()
            .put("/api/tags/" + tagDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Tag in the database
        var tagDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tags")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(tagDTOList).hasSize(databaseSizeBeforeUpdate);
        var testTagDTO = tagDTOList.stream().filter(it -> updatedTagDTO.id.equals(it.id)).findFirst().get();
        assertThat(testTagDTO.name).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void updateNonExistingTag() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tags")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(tagDTO)
            .when()
            .put("/api/tags/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Tag in the database
        var tagDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tags")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(tagDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteTag() {
        // Initialize the database
        tagDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(tagDTO)
                .when()
                .post("/api/tags")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tags")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the tag
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/tags/{id}", tagDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var tagDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tags")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(tagDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllTags() {
        // Initialize the database
        tagDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(tagDTO)
                .when()
                .post("/api/tags")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        // Get all the tagList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tags?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(tagDTO.id.intValue()))
            .body("name", hasItem(DEFAULT_NAME));
    }

    @Test
    public void getTag() {
        // Initialize the database
        tagDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(tagDTO)
                .when()
                .post("/api/tags")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        var response = // Get the tag
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/tags/{id}", tagDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the tag
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tags/{id}", tagDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(tagDTO.id.intValue()))
            .body("name", is(DEFAULT_NAME));
    }

    @Test
    public void getNonExistingTag() {
        // Get the tag
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tags/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
