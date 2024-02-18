package com.ep.api.service.distributed

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import reactor.netty.Connection
import reactor.netty.http.client.HttpClient
import java.time.Duration
import java.util.concurrent.TimeUnit

@Configuration
class ApiConfig {

    @Bean
    fun depositApi(): DepositApi {
        val client: WebClient = WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .defaultHeader("accept", "application/json")
            .baseUrl("http://localhost:8081")
            .build()

        val factory: HttpServiceProxyFactory = HttpServiceProxyFactory.builder()
            .clientAdapter(WebClientAdapter.forClient(client))
            .build()

        return factory.createClient(DepositApi::class.java)
    }

    @Bean
    fun withdrawApi(): WithdrawApi {
        val client: WebClient = WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .defaultHeader("accept", "application/json")
            .baseUrl("http://localhost:8082")
            .build()

        val factory: HttpServiceProxyFactory = HttpServiceProxyFactory.builder()
            .clientAdapter(WebClientAdapter.forClient(client))
            .build()

        return factory.createClient(WithdrawApi::class.java)
    }

    companion object {
        private val DEFAULT_TIME_OUT: Int = 5000
        private val httpClient: HttpClient
            /**
             * WebClient 설정시 기본 TimeOut 설정
             * @return HttpClient
             */
            get() = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, DEFAULT_TIME_OUT)
                .responseTimeout(Duration.ofMillis(DEFAULT_TIME_OUT.toLong()))
                .doOnConnected { conn: Connection ->
                    conn.addHandlerLast(
                        ReadTimeoutHandler(
                            DEFAULT_TIME_OUT.toLong(),
                            TimeUnit.MILLISECONDS
                        )
                    )
                        .addHandlerLast(
                            WriteTimeoutHandler(
                                DEFAULT_TIME_OUT.toLong(),
                                TimeUnit.MILLISECONDS
                            )
                        )
                }
    }
}