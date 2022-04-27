package com.huachuan.bigdata.hos.mybatis.test;

import com.huachuan.bigdata.hos.mybatis.HosDataSourceConfig;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@Import(HosDataSourceConfig.class)
@PropertySource("classpath:application.properties")
@ComponentScan("com.huachuan.bigdata.*")
@MapperScan("com.huachuan.bigdata.*")
public class BaseTest {

}
