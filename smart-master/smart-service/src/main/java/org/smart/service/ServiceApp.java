package org.smart.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
//@EnableCircuitBreaker
public class ServiceApp 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(ServiceApp.class, args);
    }
}
