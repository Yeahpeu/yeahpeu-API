package com.yeahpeu.wedding.config;

import com.yeahpeu.wedding.domain.EventWrapper;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "onboarding.maps")
public class OnboardingEventProperties {
    // YML 파일에서 초기 온보딩 이벤트를 불러오기
    private Map<Long, List<EventWrapper>> events;
}

