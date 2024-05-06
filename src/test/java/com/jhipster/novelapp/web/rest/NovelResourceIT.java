package com.jhipster.novelapp.web.rest;

import static com.jhipster.novelapp.domain.NovelAsserts.*;
import static com.jhipster.novelapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhipster.novelapp.IntegrationTest;
import com.jhipster.novelapp.domain.Novel;
import com.jhipster.novelapp.repository.NovelRepository;
import com.jhipster.novelapp.service.NovelService;
import com.jhipster.novelapp.service.dto.NovelDTO;
import com.jhipster.novelapp.service.mapper.NovelMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link NovelResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NovelResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/novels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NovelRepository novelRepository;

    @Mock
    private NovelRepository novelRepositoryMock;

    @Autowired
    private NovelMapper novelMapper;

    @Mock
    private NovelService novelServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNovelMockMvc;

    private Novel novel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Novel createEntity(EntityManager em) {
        Novel novel = new Novel().title(DEFAULT_TITLE);
        return novel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Novel createUpdatedEntity(EntityManager em) {
        Novel novel = new Novel().title(UPDATED_TITLE);
        return novel;
    }

    @BeforeEach
    public void initTest() {
        novel = createEntity(em);
    }

    @Test
    @Transactional
    void createNovel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Novel
        NovelDTO novelDTO = novelMapper.toDto(novel);
        var returnedNovelDTO = om.readValue(
            restNovelMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(novelDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NovelDTO.class
        );

        // Validate the Novel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNovel = novelMapper.toEntity(returnedNovelDTO);
        assertNovelUpdatableFieldsEquals(returnedNovel, getPersistedNovel(returnedNovel));
    }

    @Test
    @Transactional
    void createNovelWithExistingId() throws Exception {
        // Create the Novel with an existing ID
        novel.setId(1L);
        NovelDTO novelDTO = novelMapper.toDto(novel);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNovelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(novelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Novel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        novel.setTitle(null);

        // Create the Novel, which fails.
        NovelDTO novelDTO = novelMapper.toDto(novel);

        restNovelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(novelDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNovels() throws Exception {
        // Initialize the database
        novelRepository.saveAndFlush(novel);

        // Get all the novelList
        restNovelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(novel.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNovelsWithEagerRelationshipsIsEnabled() throws Exception {
        when(novelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNovelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(novelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNovelsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(novelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNovelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(novelRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNovel() throws Exception {
        // Initialize the database
        novelRepository.saveAndFlush(novel);

        // Get the novel
        restNovelMockMvc
            .perform(get(ENTITY_API_URL_ID, novel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(novel.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }

    @Test
    @Transactional
    void getNonExistingNovel() throws Exception {
        // Get the novel
        restNovelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNovel() throws Exception {
        // Initialize the database
        novelRepository.saveAndFlush(novel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the novel
        Novel updatedNovel = novelRepository.findById(novel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNovel are not directly saved in db
        em.detach(updatedNovel);
        updatedNovel.title(UPDATED_TITLE);
        NovelDTO novelDTO = novelMapper.toDto(updatedNovel);

        restNovelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, novelDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(novelDTO))
            )
            .andExpect(status().isOk());

        // Validate the Novel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNovelToMatchAllProperties(updatedNovel);
    }

    @Test
    @Transactional
    void putNonExistingNovel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        novel.setId(longCount.incrementAndGet());

        // Create the Novel
        NovelDTO novelDTO = novelMapper.toDto(novel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNovelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, novelDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(novelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Novel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNovel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        novel.setId(longCount.incrementAndGet());

        // Create the Novel
        NovelDTO novelDTO = novelMapper.toDto(novel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNovelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(novelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Novel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNovel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        novel.setId(longCount.incrementAndGet());

        // Create the Novel
        NovelDTO novelDTO = novelMapper.toDto(novel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNovelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(novelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Novel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNovelWithPatch() throws Exception {
        // Initialize the database
        novelRepository.saveAndFlush(novel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the novel using partial update
        Novel partialUpdatedNovel = new Novel();
        partialUpdatedNovel.setId(novel.getId());

        partialUpdatedNovel.title(UPDATED_TITLE);

        restNovelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNovel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNovel))
            )
            .andExpect(status().isOk());

        // Validate the Novel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNovelUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedNovel, novel), getPersistedNovel(novel));
    }

    @Test
    @Transactional
    void fullUpdateNovelWithPatch() throws Exception {
        // Initialize the database
        novelRepository.saveAndFlush(novel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the novel using partial update
        Novel partialUpdatedNovel = new Novel();
        partialUpdatedNovel.setId(novel.getId());

        partialUpdatedNovel.title(UPDATED_TITLE);

        restNovelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNovel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNovel))
            )
            .andExpect(status().isOk());

        // Validate the Novel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNovelUpdatableFieldsEquals(partialUpdatedNovel, getPersistedNovel(partialUpdatedNovel));
    }

    @Test
    @Transactional
    void patchNonExistingNovel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        novel.setId(longCount.incrementAndGet());

        // Create the Novel
        NovelDTO novelDTO = novelMapper.toDto(novel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNovelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, novelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(novelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Novel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNovel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        novel.setId(longCount.incrementAndGet());

        // Create the Novel
        NovelDTO novelDTO = novelMapper.toDto(novel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNovelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(novelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Novel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNovel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        novel.setId(longCount.incrementAndGet());

        // Create the Novel
        NovelDTO novelDTO = novelMapper.toDto(novel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNovelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(novelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Novel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNovel() throws Exception {
        // Initialize the database
        novelRepository.saveAndFlush(novel);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the novel
        restNovelMockMvc
            .perform(delete(ENTITY_API_URL_ID, novel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return novelRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Novel getPersistedNovel(Novel novel) {
        return novelRepository.findById(novel.getId()).orElseThrow();
    }

    protected void assertPersistedNovelToMatchAllProperties(Novel expectedNovel) {
        assertNovelAllPropertiesEquals(expectedNovel, getPersistedNovel(expectedNovel));
    }

    protected void assertPersistedNovelToMatchUpdatableProperties(Novel expectedNovel) {
        assertNovelAllUpdatablePropertiesEquals(expectedNovel, getPersistedNovel(expectedNovel));
    }
}
