package com.jhipster.novelapp.service;

import com.jhipster.novelapp.config.JHipsterProperties;
import com.jhipster.novelapp.service.dto.ManagementInfoDTO;
import io.quarkus.runtime.configuration.ProfileManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Provides information for management/info resource
 */
@ApplicationScoped
public class ManagementInfoService {

    private final JHipsterProperties jHipsterProperties;

    @Inject
    public ManagementInfoService(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    public ManagementInfoDTO getManagementInfo() {
        var info = new ManagementInfoDTO();
        if (jHipsterProperties.info().swagger().enable()) {
            info.activeProfiles.add("swagger");
        }
        info.activeProfiles.add(ProfileManager.getActiveProfile());
        info.displayRibbonOnProfiles = ProfileManager.getActiveProfile();
        return info;
    }
}
