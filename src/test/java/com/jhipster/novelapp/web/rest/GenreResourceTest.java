package com.jhipster.novelapp.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.jhipster.novelapp.TestUtil;
import com.jhipster.novelapp.service.dto.GenreDTO;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import jakarta.inject.Inject;
import java.util.List;
import liquibase.Liquibase;
import org.junit.jupiter.api.*;

@QuarkusTest
public class GenreResourceTest {

    private static final TypeRef<GenreDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<GenreDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    String adminToken;

    GenreDTO genreDTO;

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
    public static GenreDTO createEntity() {
        var genreDTO = new GenreDTO();
        genreDTO.name = DEFAULT_NAME;
        return genreDTO;
    }

    @BeforeEach
    public void initTest() {
        genreDTO = createEntity();
    }

    @Test
    public void createGenre() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/genres")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Genre
        genreDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(genreDTO)
                .when()
                .post("/api/genres")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        // Validate the Genre in the database
        var genreDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/genres")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(genreDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testGenreDTO = genreDTOList.stream().filter(it -> genreDTO.id.equals(it.id)).findFirst().get();
        assertThat(testGenreDTO.name).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void createGenreWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/genres")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Genre with an existing ID
        genreDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(genreDTO)
            .when()
            .post("/api/genres")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Genre in the database
        var genreDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/genres")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(genreDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/genres")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        genreDTO.name = null;

        // Create the Genre, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(genreDTO)
            .when()
            .post("/api/genres")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Genre in the database
        var genreDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/genres")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(genreDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateGenre() {
        // Initialize the database
        genreDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(genreDTO)
                .when()
                .post("/api/genres")
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
            .get("/api/genres")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the genre
        var updatedGenreDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/genres/{id}", genreDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the genre
        updatedGenreDTO.name = UPDATED_NAME;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedGenreDTO)
            .when()
            .put("/api/genres/" + genreDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Genre in the database
        var genreDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/genres")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(genreDTOList).hasSize(databaseSizeBeforeUpdate);
        var testGenreDTO = genreDTOList.stream().filter(it -> updatedGenreDTO.id.equals(it.id)).findFirst().get();
        assertThat(testGenreDTO.name).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void updateNonExistingGenre() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/genres")
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
            .body(genreDTO)
            .when()
            .put("/api/genres/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Genre in the database
        var genreDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/genres")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(genreDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteGenre() {
        // Initialize the database
        genreDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(genreDTO)
                .when()
                .post("/api/genres")
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
            .get("/api/genres")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the genre
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/genres/{id}", genreDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var genreDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/genres")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(genreDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllGenres() {
        // Initialize the database
        genreDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(genreDTO)
                .when()
                .post("/api/genres")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        // Get all the genreList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/genres?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(genreDTO.id.intValue()))
            .body("name", hasItem(DEFAULT_NAME));
    }

    @Test
    public void getGenre() {
        // Initialize the database
        genreDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(genreDTO)
                .when()
                .post("/api/genres")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        var response = // Get the genre
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/genres/{id}", genreDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the genre
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/genres/{id}", genreDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(genreDTO.id.intValue()))
            .body("name", is(DEFAULT_NAME));
    }

    @Test
    public void getNonExistingGenre() {
        // Get the genre
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/genres/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
