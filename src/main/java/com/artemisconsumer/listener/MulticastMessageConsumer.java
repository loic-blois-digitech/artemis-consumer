package com.artemisconsumer.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

/**
 * Classe permettant d'écouter si un message est disponible dans le broker
 */
public class MulticastMessageConsumer implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MulticastMessageConsumer.class);
    private Session session;
    private boolean ack;

    public MulticastMessageConsumer(Session session, boolean ack) {
        this.session = session;
        this.ack = ack;
    }

    /**
     * Lecture d'un message
     * @param message
     */
    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                LOGGER.info("Message reçu: " + textMessage.getText());
                if(ack) {
                    Thread.sleep(10000);
                    if(textMessage.getText().equals("Bonjour le monde2")) {
                        session.rollback();
                    } else {
                        session.commit();
                    }
                    LOGGER.info("Message commit");
                } else {
                    Thread.sleep(20000);
                    session.rollback();
                    LOGGER.info("Message rollback");
                }
            }
        }catch (JMSException | InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
