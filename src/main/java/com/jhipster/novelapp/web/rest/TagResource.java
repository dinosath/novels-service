package com.jhipster.novelapp.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import com.jhipster.novelapp.service.Paged;
import com.jhipster.novelapp.service.TagService;
import com.jhipster.novelapp.service.dto.TagDTO;
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
 * REST controller for managing {@link com.jhipster.novelapp.domain.Tag}.
 */
@Path("/api/tags")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TagResource {

    private final Logger log = LoggerFactory.getLogger(TagResource.class);

    private static final String ENTITY_NAME = "tag";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    TagService tagService;

    /**
     * {@code POST  /tags} : Create a new tag.
     *
     * @param tagDTO the tagDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new tagDTO, or with status {@code 400 (Bad Request)} if the tag has already an ID.
     */
    @POST
    public Response createTag(@Valid TagDTO tagDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Tag : {}", tagDTO);
        if (tagDTO.id != null) {
            throw new BadRequestAlertException("A new tag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = tagService.persistOrUpdate(tagDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /tags} : Updates an existing tag.
     *
     * @param tagDTO the tagDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated tagDTO,
     * or with status {@code 400 (Bad Request)} if the tagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tagDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateTag(@Valid TagDTO tagDTO, @PathParam("id") Long id) {
        log.debug("REST request to update Tag : {}", tagDTO);
        if (tagDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = tagService.persistOrUpdate(tagDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tagDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /tags/:id} : delete the "id" tag.
     *
     * @param id the id of the tagDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteTag(@PathParam("id") Long id) {
        log.debug("REST request to delete Tag : {}", id);
        tagService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /tags} : get all the tags.
     *
     * @param pageRequest the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link Response} with status {@code 200 (OK)} and the list of tags in body.
     */
    @GET
    public Response getAllTags(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo,
        @QueryParam(value = "eagerload") boolean eagerload
    ) {
        log.debug("REST request to get a page of Tags");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<TagDTO> result;
        if (eagerload) {
            result = tagService.findAllWithEagerRelationships(page);
        } else {
            result = tagService.findAll(page);
        }
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /tags/:id} : get the "id" tag.
     *
     * @param id the id of the tagDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the tagDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getTag(@PathParam("id") Long id) {
        log.debug("REST request to get Tag : {}", id);
        Optional<TagDTO> tagDTO = tagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tagDTO);
    }
}
