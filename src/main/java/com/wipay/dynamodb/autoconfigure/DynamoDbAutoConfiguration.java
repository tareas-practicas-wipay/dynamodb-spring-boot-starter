package com.wipay.dynamodb.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@AutoConfiguration
@ConditionalOnClass(DynamoDbClient.class)
@EnableConfigurationProperties(DynamoDbProperties.class)
public class DynamoDbAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DynamoDbClient dynamoDbClient(DynamoDbProperties properties) {

        return DynamoDbClient.builder()
                .endpointOverride(URI.create(properties.endpoint())) // Para que se conecte a localhost:8000 en lugar del servidor real de AWS en eu-west-1
                .region(Region.of(properties.region()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                properties.accessKey(),
                                properties.secretKey()
                        )
                ))
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {

        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "aws.dynamodb", name = "connection-check-enabled",
            havingValue = "true", matchIfMissing = true)
    public DynamoDbConnectionCheck dynamoDbConnectionCheck(DynamoDbClient dynamoDbClient,
                                                           DynamoDbProperties properties) {
        return new DynamoDbConnectionCheck(dynamoDbClient, properties);
    }
}