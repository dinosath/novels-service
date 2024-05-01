package com.jhipster.novelapp.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import com.jhipster.novelapp.service.NovelService;
import com.jhipster.novelapp.service.Paged;
import com.jhipster.novelapp.service.dto.NovelDTO;
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
 * REST controller for managing {@link com.jhipster.novelapp.domain.Novel}.
 */
@Path("/api/novels")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class NovelResource {

    private final Logger log = LoggerFactory.getLogger(NovelResource.class);

    private static final String ENTITY_NAME = "novel";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    NovelService novelService;

    /**
     * {@code POST  /novels} : Create a new novel.
     *
     * @param novelDTO the novelDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new novelDTO, or with status {@code 400 (Bad Request)} if the novel has already an ID.
     */
    @POST
    public Response createNovel(@Valid NovelDTO novelDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Novel : {}", novelDTO);
        if (novelDTO.id != null) {
            throw new BadRequestAlertException("A new novel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = novelService.persistOrUpdate(novelDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /novels} : Updates an existing novel.
     *
     * @param novelDTO the novelDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated novelDTO,
     * or with status {@code 400 (Bad Request)} if the novelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the novelDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateNovel(@Valid NovelDTO novelDTO, @PathParam("id") Long id) {
        log.debug("REST request to update Novel : {}", novelDTO);
        if (novelDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = novelService.persistOrUpdate(novelDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, novelDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /novels/:id} : delete the "id" novel.
     *
     * @param id the id of the novelDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteNovel(@PathParam("id") Long id) {
        log.debug("REST request to delete Novel : {}", id);
        novelService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /novels} : get all the novels.
     *
     * @param pageRequest the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link Response} with status {@code 200 (OK)} and the list of novels in body.
     */
    @GET
    public Response getAllNovels(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo,
        @QueryParam(value = "eagerload") boolean eagerload
    ) {
        log.debug("REST request to get a page of Novels");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<NovelDTO> result;
        if (eagerload) {
            result = novelService.findAllWithEagerRelationships(page);
        } else {
            result = novelService.findAll(page);
        }
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /novels/:id} : get the "id" novel.
     *
     * @param id the id of the novelDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the novelDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getNovel(@PathParam("id") Long id) {
        log.debug("REST request to get Novel : {}", id);
        Optional<NovelDTO> novelDTO = novelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(novelDTO);
    }
}
