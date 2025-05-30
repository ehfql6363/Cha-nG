package com.ssafy.chaing.common.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@ConfigurationProperties(prefix = "app.cors")
@Component
public class CorsProperties {
    private List<String> allowHosts = new ArrayList<>();
}

