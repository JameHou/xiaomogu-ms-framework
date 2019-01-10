package com.github.xiaomogu.web.swagger;

import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@ConditionalOnClass(Docket.class)
@ConditionalOnProperty(value = "mogu-swagger2.web.enabled", matchIfMissing = true)
@Configuration
@EnableSwagger2
@EnableConfigurationProperties(Swagger2Properties.class)
public class Swagger2Configuration {

    @Bean
    public Docket createRestApi(Swagger2Properties swaggerProperties, ApiInfo apiInfo) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage())) //扫描API的包路径
                .paths(PathSelectors.any())
                .build()
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.newArrayList(apiKey()));
    }

    @Bean("moguSwaggerApiInfo")
    public ApiInfo apiInfo(Swagger2Properties swaggerProperties) {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle()) // 标题
                .description(swaggerProperties.getDescription()) // 描述
                .termsOfServiceUrl("") //网址
                .version(swaggerProperties.getVersion()) // 版本号
                .build();
    }


    private ApiKey apiKey() {
        return new ApiKey("AUTHORIZATION", "Authorization", "header");
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId("")
                .clientSecret("")
                .scopeSeparator(" ")
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/api.*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("AUTHORIZATION", authorizationScopes));
    }
}
