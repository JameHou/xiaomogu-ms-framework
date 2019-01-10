package com.github.xiaomogu.datasource.jdbc;

import lombok.Data;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * @auther JameHou
 * @date 2019/1/8 14:59
 */
@Data
//@ConfigurationProperties(prefix = "mogo-datasource")
public class DataSourceProperties implements BeanClassLoaderAware {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    //private String type = "com.zaxxer.hikari.HikariDataSource";//默认数据库连接池
    private HikariPoolConfig hikariPool;
    private DruidPoolConfig druidPool;

    private ClassLoader classLoader;

    private String name;

    private Class<? extends DataSource> type;


    public DataSourceBuilder<?> initializeDataSourceBuilder() {
        return DataSourceBuilder.create(getClassLoader()).type(getType())
                .driverClassName(determineDriverClassName()).url(determineUrl())
                .username(determineUsername()).password(determinePassword());
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String determineDriverClassName() {
        if (StringUtils.hasText(this.driverClassName)) {
            Assert.state(driverClassIsLoadable(),
                    () -> "Cannot load driver class: " + this.driverClassName);
            return this.driverClassName;
        }
        String driverClassName = null;
        if (StringUtils.hasText(this.url)) {
            driverClassName = DatabaseDriver.fromJdbcUrl(this.url).getDriverClassName();
        }

        if (!StringUtils.hasText(driverClassName)) {
            throw new  DataSourceBeanCreationException(
                    "Failed to determine a suitable driver class");
        }
        return driverClassName;
    }

    public String determineUrl() {
        if (StringUtils.hasText(this.url)) {
            return this.url;
        }
        if (!StringUtils.hasText(url)) {
            throw new DataSourceBeanCreationException(
                    "Failed to determine suitable jdbc url");
        }
        return url;
    }


    public String determineUsername() {
        if (StringUtils.hasText(this.username)) {
            return this.username;
        }
        if (EmbeddedDatabaseConnection.isEmbedded(determineDriverClassName())) {
            return "sa";
        }
        return null;
    }

    public String determinePassword() {
        if (StringUtils.hasText(this.password)) {
            return this.password;
        }
        if (EmbeddedDatabaseConnection.isEmbedded(determineDriverClassName())) {
            return "";
        }
        return null;
    }


    private boolean driverClassIsLoadable() {
        try {
            ClassUtils.forName(this.driverClassName, null);
            return true;
        }
        catch (UnsupportedClassVersionError ex) {
            // Driver library has been compiled with a later JDK, propagate error
            throw ex;
        }
        catch (Throwable ex) {
            return false;
        }
    }

    static class DataSourceBeanCreationException extends BeanCreationException {
        DataSourceBeanCreationException(String msg) {
            super(msg);
        }
    }

}
