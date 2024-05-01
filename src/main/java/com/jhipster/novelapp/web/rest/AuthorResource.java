package com.jhipster.novelapp.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import com.jhipster.novelapp.service.AuthorService;
import com.jhipster.novelapp.service.Paged;
import com.jhipster.novelapp.service.dto.AuthorDTO;
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
 * REST controller for managing {@link com.jhipster.novelapp.domain.Author}.
 */
@Path("/api/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AuthorResource {

    private final Logger log = LoggerFactory.getLogger(AuthorResource.class);

    private static final String ENTITY_NAME = "author";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    AuthorService authorService;

    /**
     * {@code POST  /authors} : Create a new author.
     *
     * @param authorDTO the authorDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new authorDTO, or with status {@code 400 (Bad Request)} if the author has already an ID.
     */
    @POST
    public Response createAuthor(@Valid AuthorDTO authorDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Author : {}", authorDTO);
        if (authorDTO.id != null) {
            throw new BadRequestAlertException("A new author cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = authorService.persistOrUpdate(authorDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /authors} : Updates an existing author.
     *
     * @param authorDTO the authorDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated authorDTO,
     * or with status {@code 400 (Bad Request)} if the authorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the authorDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateAuthor(@Valid AuthorDTO authorDTO, @PathParam("id") Long id) {
        log.debug("REST request to update Author : {}", authorDTO);
        if (authorDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = authorService.persistOrUpdate(authorDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authorDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /authors/:id} : delete the "id" author.
     *
     * @param id the id of the authorDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") Long id) {
        log.debug("REST request to delete Author : {}", id);
        authorService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /authors} : get all the authors.
     *
     * @param pageRequest the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link Response} with status {@code 200 (OK)} and the list of authors in body.
     */
    @GET
    public Response getAllAuthors(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo,
        @QueryParam(value = "eagerload") boolean eagerload
    ) {
        log.debug("REST request to get a page of Authors");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<AuthorDTO> result;
        if (eagerload) {
            result = authorService.findAllWithEagerRelationships(page);
        } else {
            result = authorService.findAll(page);
        }
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /authors/:id} : get the "id" author.
     *
     * @param id the id of the authorDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the authorDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getAuthor(@PathParam("id") Long id) {
        log.debug("REST request to get Author : {}", id);
        Optional<AuthorDTO> authorDTO = authorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(authorDTO);
    }
}
