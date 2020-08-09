package com.ssafy.videoconference.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("videoconference-api").apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.ssafy.videoconference.controller"))
				.paths(PathSelectors.ant("/api/**")).build()
				.securitySchemes(Lists.newArrayList(apiKey()))
				.securityContexts(Lists.newArrayList(securityContext())).securitySchemes(Lists.newArrayList(apiKey()));

	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Swagger-VideoConference")
				.description("Video Conference API Reference for Developers").version("1.0").build();
	}

	private ApiKey apiKey() {
		return new ApiKey("JWT", "Authorization", "header");
	}

	private springfox.documentation.spi.service.contexts.SecurityContext securityContext() {
		return springfox.documentation.spi.service.contexts.SecurityContext.builder().securityReferences(defaultAuth())
				.forPaths(PathSelectors.any()).build();
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Lists.newArrayList(new SecurityReference("JWT", authorizationScopes));
	}

}
