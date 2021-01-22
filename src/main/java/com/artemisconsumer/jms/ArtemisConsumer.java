package com.artemisconsumer.jms;

import com.artemisconsumer.config.ConnectionBrokerConfig;
import com.artemisconsumer.listener.MulticastMessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.*;

@Component
public class ArtemisConsumer {

    // logs pour cette classe
    private static final Logger LOGGER = LoggerFactory.getLogger(ArtemisConsumer.class);

    // déclaration des variables qu'on a besoin
    @Value("${jms.topic.1}")
    private String semlex;
    @Value("${jms.topic.ack}")
    private boolean ack;

    private ConnectionBrokerConfig connectionBrokerConfig;

    public ArtemisConsumer(ConnectionBrokerConfig connectionBrokerConfig) {
        this.connectionBrokerConfig = connectionBrokerConfig;
    }

    /**
     * Réception d'un message par le consumer
     */
    @PostConstruct
    public void receive() {
        try {
            LOGGER.info("Création d'une connexion au broker.");
            Connection connection = this.connectionBrokerConfig.connectionBroker();

            LOGGER.info("Création d'une session");
            Session session = connection.createSession(Session.SESSION_TRANSACTED);

            LOGGER.info("Création d'une destination (Topic ou Queue)");
            Topic destination = session.createTopic(this.semlex);

            LOGGER.info("Création d'un consumer");
            MessageConsumer consumer = session.createConsumer(destination);

            LOGGER.info("Démarrage de la connexion");
            connection.start();

            LOGGER.info("Création du listener");
            consumer.setMessageListener(new MulticastMessageConsumer(session, this.ack));

        } catch (JMSException e) {
            LOGGER.error(e.getErrorCode() + ": " + e.getMessage());
        }
    }
}
