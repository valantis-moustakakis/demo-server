package com.example.demoserver.mqtt;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
@AllArgsConstructor
public class MqttConfiguration {

    final String host = "f0cdbc9159594b919d68036f1fc85241.s2.eu.hivemq.cloud";
    final String username = "UPMFinalThesis2023";
    final String password = "UPMFinalThesis2023";
    final String topic = "measurements";

    MqttHandler handler;

    @Bean
    public void initializeMqttConnection() {
        // create an MQTT client
        final Mqtt5BlockingClient client = MqttClient.builder()
                .useMqttVersion5()
                .serverHost(host)
                .serverPort(8883)
                .sslWithDefaultConfig()
                .buildBlocking();

        // connect to HiveMQ Cloud with TLS and username/pw
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

