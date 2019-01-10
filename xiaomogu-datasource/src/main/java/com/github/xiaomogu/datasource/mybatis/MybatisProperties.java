/**
 *    Copyright 2015-2018 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.xiaomogu.datasource.mybatis;

import lombok.Data;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;
//@ConfigurationProperties(prefix = MybatisProperties.MYBATIS_PREFIX)
@Data
public class MybatisProperties {

 // public static final String MYBATIS_PREFIX = "mogo-datasource";

  private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();


  private String configLocation;

  private String[] mapperLocations;

  private String mapperScanner;

  private String typeAliasesPackage;

  private Class<?> typeAliasesSuperType;

  private String typeHandlersPackage;

  private boolean checkConfigLocation = false;

  private ExecutorType executorType;

  private Properties configurationProperties;

  @NestedConfigurationProperty
  private Configuration configuration;


  public Resource[] resolveMapperLocations() {
    return Stream.of(Optional.ofNullable(this.mapperLocations).orElse(new String[0]))
        .flatMap(location -> Stream.of(getResources(location)))
        .toArray(Resource[]::new);
  }

  private Resource[] getResources(String location) {
    try {
      return resourceResolver.getResources(location);
    } catch (IOException e) {
      return new Resource[0];
    }
  }

}
