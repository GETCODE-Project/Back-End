package com.getcode.config.auth;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {
    GOOGLE("google", (attributes) -> {
        OAuthMemberProfile oAuthMemberProfile = new OAuthMemberProfile();
        oAuthMemberProfile.setEmail((String) attributes.get("email"));
        oAuthMemberProfile.setName((String) attributes.get("name"));
        return oAuthMemberProfile;
    });

    private final String registrationId;
    private final Function<Map<String, Object>, OAuthMemberProfile> of;

    OAuthAttributes(String registrationId, Function<Map<String, Object>, OAuthMemberProfile> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static OAuthMemberProfile extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter((provider) -> registrationId.equals(provider.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}
