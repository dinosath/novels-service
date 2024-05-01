package com.jhipster.novelapp.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.jhipster.novelapp.TestUtil;
import com.jhipster.novelapp.service.dto.ChapterDTO;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import jakarta.inject.Inject;
import java.util.List;
import liquibase.Liquibase;
import org.junit.jupiter.api.*;

@QuarkusTest
public class ChapterResourceTest {

    private static final TypeRef<ChapterDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<ChapterDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    String adminToken;

    ChapterDTO chapterDTO;

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
    public static ChapterDTO createEntity() {
        var chapterDTO = new ChapterDTO();
        chapterDTO.title = DEFAULT_TITLE;
        chapterDTO.content = DEFAULT_CONTENT;
        return chapterDTO;
    }

    @BeforeEach
    public void initTest() {
        chapterDTO = createEntity();
    }

    @Test
    public void createChapter() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/chapters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Chapter
        chapterDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(chapterDTO)
                .when()
                .post("/api/chapters")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        // Validate the Chapter in the database
        var chapterDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/chapters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(chapterDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testChapterDTO = chapterDTOList.stream().filter(it -> chapterDTO.id.equals(it.id)).findFirst().get();
        assertThat(testChapterDTO.title).isEqualTo(DEFAULT_TITLE);
        assertThat(testChapterDTO.content).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    public void createChapterWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/chapters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Chapter with an existing ID
        chapterDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(chapterDTO)
            .when()
            .post("/api/chapters")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Chapter in the database
        var chapterDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/chapters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(chapterDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkTitleIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/chapters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        chapterDTO.title = null;

        // Create the Chapter, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(chapterDTO)
            .when()
            .post("/api/chapters")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Chapter in the database
        var chapterDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/chapters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(chapterDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateChapter() {
        // Initialize the database
        chapterDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(chapterDTO)
                .when()
                .post("/api/chapters")
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
            .get("/api/chapters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the chapter
        var updatedChapterDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/chapters/{id}", chapterDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the chapter
        updatedChapterDTO.title = UPDATED_TITLE;
        updatedChapterDTO.content = UPDATED_CONTENT;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedChapterDTO)
            .when()
            .put("/api/chapters/" + chapterDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Chapter in the database
        var chapterDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/chapters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(chapterDTOList).hasSize(databaseSizeBeforeUpdate);
        var testChapterDTO = chapterDTOList.stream().filter(it -> updatedChapterDTO.id.equals(it.id)).findFirst().get();
        assertThat(testChapterDTO.title).isEqualTo(UPDATED_TITLE);
        assertThat(testChapterDTO.content).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    public void updateNonExistingChapter() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/chapters")
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
            .body(chapterDTO)
            .when()
            .put("/api/chapters/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Chapter in the database
        var chapterDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/chapters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(chapterDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteChapter() {
        // Initialize the database
        chapterDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(chapterDTO)
                .when()
                .post("/api/chapters")
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
            .get("/api/chapters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the chapter
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/chapters/{id}", chapterDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var chapterDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/chapters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(chapterDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllChapters() {
        // Initialize the database
        chapterDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(chapterDTO)
                .when()
                .post("/api/chapters")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        // Get all the chapterList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/chapters?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(chapterDTO.id.intValue()))
            .body("title", hasItem(DEFAULT_TITLE))
            .body("content", hasItem(DEFAULT_CONTENT.toString()));
    }

    @Test
    public void getChapter() {
        // Initialize the database
        chapterDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(chapterDTO)
                .when()
                .post("/api/chapters")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        var response = // Get the chapter
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/chapters/{id}", chapterDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the chapter
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/chapters/{id}", chapterDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(chapterDTO.id.intValue()))
            .body("title", is(DEFAULT_TITLE))
            .body("content", is(DEFAULT_CONTENT.toString()));
    }

    @Test
    public void getNonExistingChapter() {
        // Get the chapter
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/chapters/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
