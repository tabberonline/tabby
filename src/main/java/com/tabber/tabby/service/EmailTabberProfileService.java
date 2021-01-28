package com.tabber.tabby.service;

import org.springframework.web.multipart.MultipartFile;

public interface EmailTabberProfileService {
    void sendTabbyProfileInEmail(Long userProfileId,String receiverEmail, MultipartFile multipartFile);
}
