
package com.github.xiaomogu.datasource.mybatis;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInterceptor;
import com.github.xiaomogu.commons.utils.EnvironmentUtil;
import com.github.xiaomogu.datasource.ConditionalOnMapProperty;
import com.github.xiaomogu.datasource.jdbc.DynamicDataSourceAutoConfiguration;
import com.github.xiaomogu.datasource.jdbc.MultiMysqlProperties;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

@Slf4j
@org.springframework.context.annotation.Configuration
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
@ConditionalOnMapProperty(value = MultiMysqlProperties.class)//自定义的条件装配注解
@EnableConfigurationProperties(PageHelperProperties.class)
@AutoConfigureAfter(DynamicDataSourceAutoConfiguration.class)
@Order
public class MybatisAutoConfiguration implements BeanFactoryPostProcessor, EnvironmentAware, ResourceLoaderAware {



  private ConfigurableEnvironment environment;

  private ResourceLoader resourceLoader;

  private ConfigurableListableBeanFactory beanFactory;

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
    MultiMybatisProperties multiMybatisProperties = EnvironmentUtil.resolverSetting(MultiMybatisProperties.class,"",
            this.environment);
    multiMybatisProperties.getMoguMysql().forEach(
            (name, properties) -> createBean(name, properties,beanFactory));
  }



  private void createBean(String prefixName, MybatisProperties properties, ConfigurableListableBeanFactory beanFactory ) {
    SqlSessionFactory sqlSessionFactory = createSqlSessionFactory(prefixName, properties,beanFactory);
    if (sqlSessionFactory == null) {
      log.info("mybatis {} sql session factory register failed!", prefixName);
      return;
    }
    log.info("mybatis {} sql session factory register success!", prefixName);
    createSqlSessionTemplate(beanFactory, prefixName, properties,
            sqlSessionFactory);
    log.info("mybatis {} sql session template register success!", prefixName);
  }

  @Nullable
  private SqlSessionFactory createSqlSessionFactory(String prefixName,MybatisProperties properties, ConfigurableListableBeanFactory beanFactory){
    SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
    factory.setDataSource(beanFactory.getBean(prefixName + "Ds", DataSource.class));

    factory.setVfs(SpringBootVFS.class);
    if (StringUtils.hasText(properties.getConfigLocation())) {
      factory.setConfigLocation(this.resourceLoader.getResource(properties.getConfigLocation()));
    }
    applyConfiguration(factory,properties);
    if (properties.getConfigurationProperties() != null) {
      Resource resource = this.resourceLoader.getResource(properties.getConfigLocation());
      Assert.state(resource.exists(), "Cannot find config location: " + resource
              + " (please add config file or check your Mybatis configuration)");
      factory.setConfigurationProperties(properties.getConfigurationProperties());
    }
    //分页插件配置
    PageInterceptor pageInterceptor = beanFactory.getBean(PageInterceptor.class);
    if (!ObjectUtils.isEmpty(beanFactory.getBean(PageInterceptor.class))) {
      factory.setPlugins(new Interceptor[]{pageInterceptor});
    }

    if (StringUtils.hasLength(properties.getTypeAliasesPackage())) {
      factory.setTypeAliasesPackage(properties.getTypeAliasesPackage());
    }
    if (properties.getTypeAliasesSuperType() != null) {
      factory.setTypeAliasesSuperType(properties.getTypeAliasesSuperType());
    }
    if (StringUtils.hasLength(properties.getTypeHandlersPackage())) {
      factory.setTypeHandlersPackage(properties.getTypeHandlersPackage());
    }
    if (!ObjectUtils.isEmpty(properties.resolveMapperLocations())) {
      factory.setMapperLocations(properties.resolveMapperLocations());
    }
    SqlSessionFactory sqlSessionFactory = null;
    try {
       sqlSessionFactory = factory.getObject();
      if(sqlSessionFactory == null){
        log.error("sqlSessionFactoryBean get object is null");
        return null;
      }
      EnvironmentUtil.register(beanFactory, sqlSessionFactory, prefixName + "SessionFactory",
              prefixName + "Sf");

      if (!Strings.isNullOrEmpty(properties.getMapperScanner())) {
        createBasePackageScanner((BeanDefinitionRegistry) beanFactory,
                properties.getMapperScanner(), prefixName);
      } else {
        createClassPathMapperScanner((BeanDefinitionRegistry) beanFactory,
                prefixName,beanFactory);
      }

    } catch (Exception e) {
      log.error("create sqlSessionFactory error {}" ,e);
    }
    return sqlSessionFactory;
  }



  private void createSqlSessionTemplate(
          ConfigurableListableBeanFactory configurableListableBeanFactory, String prefixName,
          MybatisProperties mybatisProperties, SqlSessionFactory sqlSessionFactory) {
    ExecutorType executorType = mybatisProperties.getExecutorType();
    SqlSessionTemplate sqlSessionTemplate;
    if (executorType != null) {
      sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory, executorType);
    } else {
      sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
    }
    EnvironmentUtil.register(configurableListableBeanFactory, sqlSessionTemplate, prefixName + "SessionTemplate",
            prefixName + "St");
  }

  private void createBasePackageScanner(BeanDefinitionRegistry registry, String basePackage,
                                        String prefixName) {
    MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
    scannerConfigurer.setBasePackage(basePackage);
    scannerConfigurer.setSqlSessionFactoryBeanName(prefixName + "SessionFactory");
    scannerConfigurer.postProcessBeanDefinitionRegistry(registry);
  }

  private void createClassPathMapperScanner(BeanDefinitionRegistry registry, String prefixName,ConfigurableListableBeanFactory beanFactory) {
    ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);

    try {
      if (this.resourceLoader != null) {
        scanner.setResourceLoader(this.resourceLoader);
      }
      List<String> packages = AutoConfigurationPackages.get(beanFactory);
      packages.forEach(pkg -> log.info("Using auto-configuration base package '{}'", pkg));
      scanner.setAnnotationClass(Mapper.class);
      scanner.setSqlSessionFactoryBeanName(prefixName + "SessionFactory");
      scanner.registerFilters();
      scanner.doScan(StringUtils.toStringArray(packages));
    } catch (IllegalStateException ex) {
      log.info("Could not determine auto-configuration package", ex);
    }
  }

  private void applyConfiguration(SqlSessionFactoryBean factory,MybatisProperties properties) {
    Configuration configuration = properties.getConfiguration();
    if (configuration == null && !StringUtils.hasText(properties.getConfigLocation())) {
      configuration = new Configuration();
    }
    if (configuration != null) {
      ConfigurationCustomizer customizer = this.beanFactory.getBean(ConfigurationCustomizer.class);
        customizer.customize(configuration);
    }
    factory.setConfiguration(configuration);
  }

  @Override
  public void setEnvironment(Environment environment) {
     this.environment = (ConfigurableEnvironment)environment;
  }

  @Bean
  ConfigurationCustomizer mybatisConfigurationCustomizer() {
    return new ConfigurationCustomizer() {
      @Override
      public void customize(Configuration configuration) {
        // customize ...
        log.info("mybatisConfig自定义配置..........................");
      }
    };
  }

  @Bean
  public PageInterceptor pageHelperConfiguration(PageHelperProperties helperProperties){
    PageInterceptor pageInterceptor = new PageInterceptor();
    Properties props = helperProperties.getProperties();
    pageInterceptor.setProperties(props);
    return pageInterceptor;
  }


  @Override
  public void setResourceLoader(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }
}
