package com.tabber.tabby.service;

public interface EmailTabberProfileService {
    void sendTabbyProfileInEmail(Long userProfileId,String receiverEmail);
}
