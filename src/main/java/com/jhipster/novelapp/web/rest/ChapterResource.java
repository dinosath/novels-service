package com.jhipster.novelapp.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import com.jhipster.novelapp.service.ChapterService;
import com.jhipster.novelapp.service.Paged;
import com.jhipster.novelapp.service.dto.ChapterDTO;
import com.jhipster.novelapp.web.rest.errors.BadRequestAlertException;
import com.jhipster.novelapp.web.rest.vm.PageRequestVM;
import com.jhipster.novelapp.web.rest.vm.SortRequestVM;
import com.jhipster.novelapp.web.util.HeaderUtil;
import com.jhipster.novelapp.web.util.PaginationUtil;
import com.jhipster.novelapp.web.util.ResponseUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for managing {@link com.jhipster.novelapp.domain.Chapter}.
 */
@Path("/api/chapters")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ChapterResource {

    private final Logger log = LoggerFactory.getLogger(ChapterResource.class);

    private static final String ENTITY_NAME = "chapter";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    ChapterService chapterService;

    /**
     * {@code POST  /chapters} : Create a new chapter.
     *
     * @param chapterDTO the chapterDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new chapterDTO, or with status {@code 400 (Bad Request)} if the chapter has already an ID.
     */
    @POST
    public Response createChapter(@Valid ChapterDTO chapterDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Chapter : {}", chapterDTO);
        if (chapterDTO.id != null) {
            throw new BadRequestAlertException("A new chapter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = chapterService.persistOrUpdate(chapterDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /chapters} : Updates an existing chapter.
     *
     * @param chapterDTO the chapterDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated chapterDTO,
     * or with status {@code 400 (Bad Request)} if the chapterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chapterDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateChapter(@Valid ChapterDTO chapterDTO, @PathParam("id") Long id) {
        log.debug("REST request to update Chapter : {}", chapterDTO);
        if (chapterDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = chapterService.persistOrUpdate(chapterDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chapterDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /chapters/:id} : delete the "id" chapter.
     *
     * @param id the id of the chapterDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteChapter(@PathParam("id") Long id) {
        log.debug("REST request to delete Chapter : {}", id);
        chapterService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /chapters} : get all the chapters.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of chapters in body.
     */
    @GET
    public Response getAllChapters(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) {
        log.debug("REST request to get a page of Chapters");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<ChapterDTO> result = chapterService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /chapters/:id} : get the "id" chapter.
     *
     * @param id the id of the chapterDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the chapterDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getChapter(@PathParam("id") Long id) {
        log.debug("REST request to get Chapter : {}", id);
        Optional<ChapterDTO> chapterDTO = chapterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chapterDTO);
    }
}
