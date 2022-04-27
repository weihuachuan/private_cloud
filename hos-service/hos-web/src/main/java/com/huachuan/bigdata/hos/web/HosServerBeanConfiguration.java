package com.huachuan.bigdata.hos.web;

import com.huachuan.bigdata.hos.core.HosConfiguration;
import com.huachuan.bigdata.hos.server.HdfsServiceImpl;
import com.huachuan.bigdata.hos.server.HosStoreServiceImpl;
import com.huachuan.bigdata.hos.server.IHosStoreService;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class HosServerBeanConfiguration {
    /**
     * create hbase client connection.
     *
     * @return conn
     * @throws IOException ioe.
     */
    @Bean
    public Connection getConnection() throws IOException {
        org.apache.hadoop.conf.Configuration config = HBaseConfiguration.create();
        HosConfiguration confUtil = HosConfiguration.getConfiguration();

        config.set("hbase.zookeeper.quorum", confUtil.getString("hbase.zookeeper.quorum"));
        config.set("hbase.zookeeper.property.clientPort",
                confUtil.getString("hbase.zookeeper.port"));
        config.set(HConstants.HBASE_RPC_TIMEOUT_KEY, "3600000");

        return ConnectionFactory.createConnection(config);
    }

    @Bean(name = "hosStoreService")
    public IHosStoreService getHosStore(@Autowired Connection connection) throws Exception {
        HosConfiguration confUtil = HosConfiguration.getConfiguration();
        String zkHosts = confUtil.getString("hbase.zookeeper.quorum");
        HosStoreServiceImpl store = new HosStoreServiceImpl(connection, new HdfsServiceImpl(),
                zkHosts);
        return store;
    }

}
