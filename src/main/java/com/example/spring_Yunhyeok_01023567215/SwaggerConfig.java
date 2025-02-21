package com.example.spring_Yunhyeok_01023567215;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi boardGroupedOpenApi() {
        return GroupedOpenApi
                .builder()
                .group("board")
                .pathsToMatch("/board/**")
                .addOpenApiCustomizer(openApi ->
                        openApi.setInfo(new Info()
                                .title("Prography PingPong Game API")
                                .description("프로그라피 과제 API 명세서")
                                .version("0.0.1")
                        )
                )
                .build();
    }
}
