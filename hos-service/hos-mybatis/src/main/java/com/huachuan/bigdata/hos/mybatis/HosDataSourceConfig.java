package com.huachuan.bigdata.hos.mybatis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

@Configuration
@MapperScan(basePackages = HosDataSourceConfig.PACKAGE,
        sqlSessionFactoryRef = "HosSqlSessionFactory")
public class HosDataSourceConfig {

    static final String PACKAGE = "com.huachuan.bigdata.hos.**";

    /**
     * hosDataSource.
     *
     * @return DataSource DataSource
     * @throws IOException IOException
     */
    @Bean(name = "HosDataSource")
    @Primary
    public DataSource hosDataSource() throws IOException {
        //1.获取datasource相关信息
        ResourceLoader loader = new DefaultResourceLoader();
        InputStream inputStream = loader.getResource("classpath:application.properties")
                .getInputStream();
        Properties properties = new Properties();
        properties.load(inputStream);
        Set<Object> keys = properties.keySet();
        Properties dsproperties = new Properties();
        for (Object key : keys) {
            if (key.toString().startsWith("datasource")) {
                dsproperties.put(key.toString().replace("datasource.", ""), properties.get(key));
            }
        }
        //2.通过HikariDataSourceFactory 生成一个Datasource
        HikariDataSourceFactory factory = new HikariDataSourceFactory();
        factory.setProperties(dsproperties);
        inputStream.close();
        return factory.getDataSource();
    }

    /**
     * hosSqlSessionFactory.
     */
    @Bean(name = "HosSqlSessionFactory")
    @Primary
    public SqlSessionFactory hosSqlSessionFactory(
            @Qualifier("HosDataSource") DataSource hosDataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(hosDataSource);
        //1.读取mybatis相关配置
        ResourceLoader loader = new DefaultResourceLoader();
        String resource = "classpath:mybatis-config.xml";
        //2.获取setSqlSessionFactory
        factoryBean.setConfigLocation(loader.getResource(resource));
        factoryBean.setSqlSessionFactoryBuilder(new SqlSessionFactoryBuilder());
        return factoryBean.getObject();
    }
}

