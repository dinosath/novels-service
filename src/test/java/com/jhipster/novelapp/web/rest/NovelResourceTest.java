package com.jhipster.novelapp.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.jhipster.novelapp.TestUtil;
import com.jhipster.novelapp.service.dto.NovelDTO;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import jakarta.inject.Inject;
import java.util.List;
import liquibase.Liquibase;
import org.junit.jupiter.api.*;

@QuarkusTest
public class NovelResourceTest {

    private static final TypeRef<NovelDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<NovelDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    String adminToken;

    NovelDTO novelDTO;

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
    public static NovelDTO createEntity() {
        var novelDTO = new NovelDTO();
        novelDTO.title = DEFAULT_TITLE;
        return novelDTO;
    }

    @BeforeEach
    public void initTest() {
        novelDTO = createEntity();
    }

    @Test
    public void createNovel() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/novels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Novel
        novelDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(novelDTO)
                .when()
                .post("/api/novels")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        // Validate the Novel in the database
        var novelDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/novels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(novelDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testNovelDTO = novelDTOList.stream().filter(it -> novelDTO.id.equals(it.id)).findFirst().get();
        assertThat(testNovelDTO.title).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    public void createNovelWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/novels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Novel with an existing ID
        novelDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(novelDTO)
            .when()
            .post("/api/novels")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Novel in the database
        var novelDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/novels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(novelDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkTitleIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/novels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        novelDTO.title = null;

        // Create the Novel, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(novelDTO)
            .when()
            .post("/api/novels")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Novel in the database
        var novelDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/novels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(novelDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateNovel() {
        // Initialize the database
        novelDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(novelDTO)
                .when()
                .post("/api/novels")
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
            .get("/api/novels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the novel
        var updatedNovelDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/novels/{id}", novelDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the novel
        updatedNovelDTO.title = UPDATED_TITLE;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedNovelDTO)
            .when()
            .put("/api/novels/" + novelDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Novel in the database
        var novelDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/novels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(novelDTOList).hasSize(databaseSizeBeforeUpdate);
        var testNovelDTO = novelDTOList.stream().filter(it -> updatedNovelDTO.id.equals(it.id)).findFirst().get();
        assertThat(testNovelDTO.title).isEqualTo(UPDATED_TITLE);
    }

    @Test
    public void updateNonExistingNovel() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/novels")
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
            .body(novelDTO)
            .when()
            .put("/api/novels/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Novel in the database
        var novelDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/novels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(novelDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteNovel() {
        // Initialize the database
        novelDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(novelDTO)
                .when()
                .post("/api/novels")
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
            .get("/api/novels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the novel
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/novels/{id}", novelDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var novelDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/novels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(novelDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllNovels() {
        // Initialize the database
        novelDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(novelDTO)
                .when()
                .post("/api/novels")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        // Get all the novelList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/novels?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(novelDTO.id.intValue()))
            .body("title", hasItem(DEFAULT_TITLE));
    }

    @Test
    public void getNovel() {
        // Initialize the database
        novelDTO =
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(novelDTO)
                .when()
                .post("/api/novels")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract()
                .as(ENTITY_TYPE);

        var response = // Get the novel
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/novels/{id}", novelDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the novel
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/novels/{id}", novelDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(novelDTO.id.intValue()))
            .body("title", is(DEFAULT_TITLE));
    }

    @Test
    public void getNonExistingNovel() {
        // Get the novel
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/novels/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
