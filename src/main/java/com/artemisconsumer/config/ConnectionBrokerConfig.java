package com.artemisconsumer.config;

import com.artemisconsumer.jms.ArtemisConsumer;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.stereotype.Component;

import javax.jms.Connection;
import javax.jms.JMSException;

@Component
@EnableJms
public class ConnectionBrokerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArtemisConsumer.class);

    @Value("${broker.protocol}")
    private String protocol;

    @Value("${broker.host}")
    private String host;

    @Value("${broker.port}")
    private String port;

    @Value("${broker.user}")
    private String user;

    @Value("${broker.password}")
    private String password;

    /**
     * Connexion au broker d'Artémis avec en params:
     * - URL
     * - USER
     * - PASSWORD
     *
     * @return connection
     */
    public Connection connectionBroker() {

        Connection connection = null;

        LOGGER.info("Création de l'url pour se connecter au broker.");
        String uri = protocol + host + port;

        try {
            LOGGER.info("Connexion au broker en cours...");
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            connectionFactory.setBrokerURL(uri);
            connectionFactory.setUser(this.user);
            connectionFactory.setPassword(this.password);
            connection = connectionFactory.createConnection();

        } catch (JMSException e) {
            LOGGER.error(e.getErrorCode() + ": " + e.getMessage());
            return null;

        }
        LOGGER.info("Connexion au broker réussie.");
        return connection;
    }
}
