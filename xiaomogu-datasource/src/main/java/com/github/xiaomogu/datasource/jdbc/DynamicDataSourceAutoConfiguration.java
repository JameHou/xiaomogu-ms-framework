package com.github.xiaomogu.datasource.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.xiaomogu.commons.utils.EnvironmentUtil;
import com.github.xiaomogu.datasource.ConditionalOnMapProperty;
import com.zaxxer.hikari.HikariDataSource;
import net.sf.log4jdbc.log.slf4j.Slf4jSpyLogDelegator;
import net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.aspectj.AnnotationTransactionAspect;

import javax.sql.DataSource;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @auther JameHou
 * @date 2019/1/8 17:22
 */
@Configuration
@ConditionalOnMapProperty(value = MultiMysqlProperties.class)//自定义的条件装配注解
@ConditionalOnClass(value = {DataSource.class, HikariDataSource.class, DataSourceSpy.class, DruidDataSource.class})
//@EnableConfigurationProperties({DataSourceProperties.class})
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class DynamicDataSourceAutoConfiguration implements BeanFactoryPostProcessor, EnvironmentAware {

    private  ConfigurableEnvironment environment;

    //log4jdbc 官网常用配置文件属性
    private static final String[] PROPERTIES_TO_COPY = {
            "log4jdbc.log4j2.properties.file",
            "log4jdbc.debug.stack.prefix",
            "log4jdbc.sqltiming.warn.threshold",
            "log4jdbc.sqltiming.error.threshold",
            "log4jdbc.dump.booleanastruefalse",
            "log4jdbc.dump.fulldebugstacktrace",
            "log4jdbc.dump.sql.maxlinelength",
            "log4jdbc.statement.warn",
            "log4jdbc.dump.sql.select",
            "log4jdbc.dump.sql.insert",
            "log4jdbc.dump.sql.update",
            "log4jdbc.dump.sql.delete",
            "log4jdbc.dump.sql.create",
            "log4jdbc.dump.sql.addsemicolon",
            "log4jdbc.auto.load.popular.drivers",
            "log4jdbc.drivers",
            "log4jdbc.trim.sql",
            "log4jdbc.trim.sql.extrablanklines",
            "log4jdbc.suppress.generated.keys.exception",
            "log4jdbc.log4j2.properties.file",
    };

    private void initLog4jdbc() {
        //定制化属性配置 可以在application.yml 或 appliation.properties 中修改默认值
        for (final String  property : PROPERTIES_TO_COPY){
            if(this.environment.containsProperty(property)){
                System.setProperty(property,this.environment.getProperty(property));
            }
            System.setProperty("log4jdbc.spylogdelegator.name", Slf4jSpyLogDelegator.class.getName());
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        MultiMysqlProperties multiMysqlProperties = EnvironmentUtil.resolverSetting(MultiMysqlProperties.class,"",environment);
        initLog4jdbc();
        multiMysqlProperties.getMoguMysql().forEach((name,properties) ->{
            createBean(name,properties,beanFactory);
        });
    }

    private void createBean(String prefixName,DataSourceProperties properties,ConfigurableListableBeanFactory beanFactory ){
        String url = properties.getUrl();
        checkArgument(StringUtils.isNoneBlank(url),prefixName+ " mysql url is null or empty");
        //通过数据源配置创建HikariDataSource
      //  HikariDataSource hikariDataSource = createHikariDataSource(properties);
        DataSource dataSource = createDataSource(properties,
                properties.getType());
        //装饰增强数据源，使其拥有打印sql语句的能力
        DataSourceSpy dataSourceSpy = new DataSourceSpy(dataSource);
        //通过 DataSource创建数据库事物管理器
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSourceSpy);
        AnnotationTransactionAspect.aspectOf().setTransactionManager(dataSourceTransactionManager);
        //创建 ORM jdbcTemplate 对象 操作数据库
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceSpy);

        //注入bean的定义信息 并指定alias别名
        EnvironmentUtil.register(beanFactory, dataSourceSpy, prefixName + "DataSource",
                prefixName + "Ds");
        EnvironmentUtil.register(beanFactory, dataSourceTransactionManager, prefixName + "TransactionManager",
                prefixName + "Tx");
        EnvironmentUtil.register(beanFactory, jdbcTemplate, prefixName + "JdbcTemplate",
                prefixName + "Jt");

    }

    private static <T> T  createDataSource(DataSourceProperties properties,Class<? extends DataSource> type){
        T dataSource = (T) properties.initializeDataSourceBuilder().type(type).build();
        if(dataSource != null && dataSource instanceof HikariDataSource){
            return (T)createHikariDataSource(properties,(HikariDataSource) dataSource);
        }
        if(dataSource != null && dataSource instanceof DruidDataSource){
            return (T)createDruidDataSource(properties,(DruidDataSource) dataSource);
        }

        return null;
    }


    private static DruidDataSource createDruidDataSource(DataSourceProperties dataSourceProperties,DruidDataSource druidDataSource) {
       //TODO 数据源配置
        //DruidDataSource druidDataSource = new DruidDataSource();
       // DruidPoolConfig druidPoolConfig = dataSourceProperties.getDruidPool();
        return druidDataSource;
    }



    private static HikariDataSource createHikariDataSource(DataSourceProperties dataSourceProperties,HikariDataSource hikariDataSource) {
    /*    HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(dataSourceProperties.getUrl());
        hikariDataSource.setUsername(dataSourceProperties.getUsername());
        hikariDataSource.setPassword(dataSourceProperties.getPassword());
        Optional.ofNullable(hikariPool.getDriverClassName())
                .ifPresent(hikariDataSource::setDriverClassName);*/

        HikariPoolConfig hikariPool = dataSourceProperties.getHikariPool();
        hikariDataSource.setAutoCommit(hikariPool.isAutoCommit());
        hikariDataSource.setConnectionTimeout(hikariPool.getConnectionTimeout());
        hikariDataSource.setIdleTimeout(hikariPool.getIdleTimeout());
        hikariDataSource.setMaxLifetime(hikariPool.getMaxLifetime());
        hikariDataSource.setMaximumPoolSize(hikariPool.getMaximumPoolSize());
        hikariDataSource.setMinimumIdle(hikariPool.getMinimumIdle());
        hikariDataSource
                .setInitializationFailTimeout(hikariPool.getInitializationFailTimeout());
        hikariDataSource.setIsolateInternalQueries(hikariPool.isIsolateInternalQueries());
        hikariDataSource.setReadOnly(hikariPool.isReadOnly());
        hikariDataSource.setRegisterMbeans(hikariPool.isRegisterMbeans());
        hikariDataSource.setValidationTimeout(hikariPool.getValidationTimeout());
        hikariDataSource.setLeakDetectionThreshold(hikariPool.getLeakDetectionThreshold());
        Optional.ofNullable(hikariPool.getDriverClassName())
                .ifPresent(hikariDataSource::setPoolName);
        return hikariDataSource;
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment)environment;
    }
}
