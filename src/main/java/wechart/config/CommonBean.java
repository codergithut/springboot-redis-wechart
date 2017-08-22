package wechart.config;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/21
 * @description
 */
@Configuration
public class CommonBean {
    private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;
    private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 20;
    private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = 30 * 1000;
    private static final int DEFAULT_TIME_TO_LIVE_MILLISECONDS = 30 * 1000;

    private int maxTotalConnections = DEFAULT_MAX_TOTAL_CONNECTIONS;
    private int maxConnectionsPerRoute = DEFAULT_MAX_CONNECTIONS_PER_ROUTE;
    private int soTimeout = DEFAULT_READ_TIMEOUT_MILLISECONDS;
    private int connectionTimeout = DEFAULT_READ_TIMEOUT_MILLISECONDS;
    private int timeToLive = DEFAULT_TIME_TO_LIVE_MILLISECONDS;

    @Bean
    public HttpClient getHttpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(timeToLive, TimeUnit.MILLISECONDS);
        connectionManager.setMaxTotal(maxTotalConnections);
        connectionManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);
        RequestConfig config = RequestConfig.custom().setSocketTimeout(soTimeout).setConnectTimeout(connectionTimeout).build();
        HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connectionManager).setDefaultRequestConfig(config).build();
        return httpClient;
    }

}
