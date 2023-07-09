package com.example.demoserver.mqtt;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
@RequiredArgsConstructor
public class MqttConfiguration {

    @Value("${mqtt.broker.url}")
    private String host;

    @Value("${mqtt.broker.username}")
    private String username;

    @Value("${mqtt.broker.password}")
    private String password;

    @Value("${mqtt.broker.topic}")
    private String topic;

    private final MqttHandler handler;

    @Bean
    public void initializeMqttConnection() {
        // create an MQTT client
        final Mqtt5BlockingClient client = MqttClient.builder()
                .useMqttVersion5()
                .serverHost(host)
                .serverPort(8883)
                .sslWithDefaultConfig()
                .buildBlocking();

        // connect to HiveMQ Cloud with TLS and username/password
        client.connectWith()
                .simpleAuth()
                .username(username)
                .password(UTF_8.encode(password))
                .applySimpleAuth()
                .send();

        System.out.println("Connected successfully");

        // subscribe to the topic
        client.subscribeWith()
                .topicFilter(topic)
                .send();

        // set a callback that is called when a message is received (using the async API style)
        client.toAsync().publishes(ALL, publish -> {
            String message = String.valueOf(UTF_8.decode(publish.getPayload().get()));
            System.out.println("Received message: " + publish.getTopic() + " -> " + message);
            handler.handleMqttMessage(message);
        });
    }
}

