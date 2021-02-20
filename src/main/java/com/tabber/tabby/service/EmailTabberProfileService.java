package com.tabber.tabby.service;

import com.tabber.tabby.dto.EmailHistoryResponse;
import com.tabber.tabby.exceptions.BadRequestException;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface EmailTabberProfileService {
    void sendTabbyProfileInEmail(Long userProfileId,String receiverEmail, MultipartFile multipartFile) throws Exception;
    EmailHistoryResponse getPaginatedEmailHistory(Integer pageNo, Integer itemsPerPage, Long userProfileId) throws Exception;

}
