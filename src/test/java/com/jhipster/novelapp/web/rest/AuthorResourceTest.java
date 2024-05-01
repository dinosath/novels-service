package com.jhipster.novelapp.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.jhipster.novelapp.TestUtil;
import com.jhipster.novelapp.service.dto.AuthorDTO;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import jakarta.inject.Inject;
import java.util.List;
import liquibase.Liquibase;
import org.junit.jupiter.api.*;

@QuarkusTest
public class AuthorResourceTest {

    private static final TypeRef<AuthorDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<AuthorDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    String adminToken;

    AuthorDTO authorDTO;

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
    public static AuthorDTO createEntity() {
        var authorDTO = new AuthorDTO();
        authorDTO.name = DEFAULT_NAME;
        return authorDTO;
    }

    @BeforeEach
    public void initTest() {
        authorDTO = createEntity();
    }

    @Test
    public void createAuthor() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/authors")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Author
        authorDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(authorDTO)
                .when()
                .post("/api/authors")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        // Validate the Author in the database
        var authorDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/authors")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(authorDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testAuthorDTO = authorDTOList.stream().filter(it -> authorDTO.id.equals(it.id)).findFirst().get();
        assertThat(testAuthorDTO.name).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void createAuthorWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/authors")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Author with an existing ID
        authorDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(authorDTO)
            .when()
            .post("/api/authors")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Author in the database
        var authorDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/authors")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(authorDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/authors")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        authorDTO.name = null;

        // Create the Author, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(authorDTO)
            .when()
            .post("/api/authors")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Author in the database
        var authorDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/authors")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(authorDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateAuthor() {
        // Initialize the database
        authorDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(authorDTO)
                .when()
                .post("/api/authors")
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
            .get("/api/authors")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the author
        var updatedAuthorDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/authors/{id}", authorDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the author
        updatedAuthorDTO.name = UPDATED_NAME;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedAuthorDTO)
            .when()
            .put("/api/authors/" + authorDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Author in the database
        var authorDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/authors")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(authorDTOList).hasSize(databaseSizeBeforeUpdate);
        var testAuthorDTO = authorDTOList.stream().filter(it -> updatedAuthorDTO.id.equals(it.id)).findFirst().get();
        assertThat(testAuthorDTO.name).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void updateNonExistingAuthor() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/authors")
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
            .body(authorDTO)
            .when()
            .put("/api/authors/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Author in the database
        var authorDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/authors")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(authorDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteAuthor() {
        // Initialize the database
        authorDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(authorDTO)
                .when()
                .post("/api/authors")
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
            .get("/api/authors")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the author
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/authors/{id}", authorDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var authorDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/authors")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(authorDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllAuthors() {
        // Initialize the database
        authorDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(authorDTO)
                .when()
                .post("/api/authors")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        // Get all the authorList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/authors?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(authorDTO.id.intValue()))
            .body("name", hasItem(DEFAULT_NAME));
    }

    @Test
    public void getAuthor() {
        // Initialize the database
        authorDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(authorDTO)
                .when()
                .post("/api/authors")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        var response = // Get the author
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/authors/{id}", authorDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the author
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/authors/{id}", authorDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(authorDTO.id.intValue()))
            .body("name", is(DEFAULT_NAME));
    }

    @Test
    public void getNonExistingAuthor() {
        // Get the author
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/authors/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
