package com.tabber.tabby.service;

import com.tabber.tabby.dto.PortfolioRequest;
import com.tabber.tabby.entity.PortfolioEntity;

public interface PortfolioService {
    PortfolioEntity createPortfolio(PortfolioRequest portfolioRequest,Long userId);
    PortfolioEntity updatePortfolio(PortfolioRequest portfolioRequest,Long userId);
}
