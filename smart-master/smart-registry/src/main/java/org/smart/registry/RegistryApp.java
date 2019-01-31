package org.smart.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Hello world!
 *
 */
@EnableEurekaServer
@SpringBootApplication
public class RegistryApp 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(RegistryApp.class, args);
    }
}
