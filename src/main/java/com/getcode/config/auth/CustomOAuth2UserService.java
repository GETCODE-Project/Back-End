package com.getcode.config.auth;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final AuthMemberRepository authMemberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("loadUser method is called.");
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        // OAUTH 정보 가져오기
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // OAuth 서비스 이름(ex. kakao, naver, google)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();

        // OAUTH 유저의 attribute
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthMemberProfile memberProfile = OAuthAttributes.extract(registrationId, attributes);
        memberProfile.setProvider(registrationId);
        AuthMember authMember = saveOrUpdate(memberProfile);
        System.out.println(authMember);
        Map<String, Object> customAttribute = customAttribute(attributes, userNameAttributeName, memberProfile, registrationId);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                customAttribute,
                userNameAttributeName);
    }

    private Map customAttribute(Map attributes, String userNameAttributeName, OAuthMemberProfile memberProfile, String registrationId) {
        Map<String, Object> customAttribute = new LinkedHashMap<>();
        customAttribute.put(userNameAttributeName, attributes.get(userNameAttributeName));
        customAttribute.put("provider", registrationId);
        customAttribute.put("name", memberProfile.getName());
        customAttribute.put("email", memberProfile.getEmail());
        return customAttribute;

    }

    private AuthMember saveOrUpdate(OAuthMemberProfile memberProfile) {

        AuthMember member = authMemberRepository.findByEmailAndProvider(memberProfile.getEmail(), memberProfile.getProvider())
                .map(m -> m.update(memberProfile.getName(), memberProfile.getEmail())) // OAuth 서비스 사이트에서 유저 정보 변경이 있을 수 있기 때문에 우리 DB에도 update
                .orElse(memberProfile.toMember());
        log.info(member.getEmail());
        return authMemberRepository.save(member);
    }
}
