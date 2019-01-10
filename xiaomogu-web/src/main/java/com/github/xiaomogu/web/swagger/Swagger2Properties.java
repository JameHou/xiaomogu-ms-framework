package com.github.xiaomogu.web.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties(prefix = "mogu-swagger2-web")
public class Swagger2Properties {

  private String title = "My Application Rrestful Apis";
  private String description = "test my swagger api ui";
  private String version = "V2";
  @NotNull
  private String basePackage;
 // private boolean enabled = true;

}
