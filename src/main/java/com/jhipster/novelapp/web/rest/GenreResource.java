package com.jhipster.novelapp.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import com.jhipster.novelapp.service.GenreService;
import com.jhipster.novelapp.service.Paged;
import com.jhipster.novelapp.service.dto.GenreDTO;
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
 * REST controller for managing {@link com.jhipster.novelapp.domain.Genre}.
 */
@Path("/api/genres")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class GenreResource {

    private final Logger log = LoggerFactory.getLogger(GenreResource.class);

    private static final String ENTITY_NAME = "genre";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    GenreService genreService;

    /**
     * {@code POST  /genres} : Create a new genre.
     *
     * @param genreDTO the genreDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new genreDTO, or with status {@code 400 (Bad Request)} if the genre has already an ID.
     */
    @POST
    public Response createGenre(@Valid GenreDTO genreDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Genre : {}", genreDTO);
        if (genreDTO.id != null) {
            throw new BadRequestAlertException("A new genre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = genreService.persistOrUpdate(genreDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /genres} : Updates an existing genre.
     *
     * @param genreDTO the genreDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated genreDTO,
     * or with status {@code 400 (Bad Request)} if the genreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the genreDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateGenre(@Valid GenreDTO genreDTO, @PathParam("id") Long id) {
        log.debug("REST request to update Genre : {}", genreDTO);
        if (genreDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = genreService.persistOrUpdate(genreDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, genreDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /genres/:id} : delete the "id" genre.
     *
     * @param id the id of the genreDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteGenre(@PathParam("id") Long id) {
        log.debug("REST request to delete Genre : {}", id);
        genreService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /genres} : get all the genres.
     *
     * @param pageRequest the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link Response} with status {@code 200 (OK)} and the list of genres in body.
     */
    @GET
    public Response getAllGenres(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo,
        @QueryParam(value = "eagerload") boolean eagerload
    ) {
        log.debug("REST request to get a page of Genres");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<GenreDTO> result;
        if (eagerload) {
            result = genreService.findAllWithEagerRelationships(page);
        } else {
            result = genreService.findAll(page);
        }
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /genres/:id} : get the "id" genre.
     *
     * @param id the id of the genreDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the genreDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getGenre(@PathParam("id") Long id) {
        log.debug("REST request to get Genre : {}", id);
        Optional<GenreDTO> genreDTO = genreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(genreDTO);
    }
}
