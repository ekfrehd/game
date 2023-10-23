package com.no3.game.oauth;

import lombok.extern.log4j.Log4j2;

import java.util.Map;
@Log4j2
public class GoogleUserInfo implements OAuth2UserInfo{
    private Map<String, Object> attributes; // getAttributes()

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        log.info("Kakao User Info Attributes: {}", attributes);
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
