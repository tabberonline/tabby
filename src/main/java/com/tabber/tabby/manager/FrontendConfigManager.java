package com.tabber.tabby.manager;

import com.tabber.tabby.constants.TabbyConstants;
import com.tabber.tabby.entity.FrontendConfigurationEntity;
import com.tabber.tabby.respository.FrontendConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrontendConfigManager {

    @Autowired
    FrontendConfigurationRepository frontendConfigurationRepository;

    @Cacheable(value = TabbyConstants.FRONTEND_CONFIG,key="#pageType.concat('_').concat(#key)",unless = "#result == null")
    public FrontendConfigurationEntity findFeConfigurationByPageTypeAndKey(String pageType, String key){
        return frontendConfigurationRepository.getTopByPageTypeAndKey(pageType,key);
    }

    @Cacheable(value = TabbyConstants.FRONTEND_CONFIG,key="'all'",unless = "#result == null")
    public List<FrontendConfigurationEntity> findAllFEConfiguration(){
        return frontendConfigurationRepository.getAll();
    }
}
