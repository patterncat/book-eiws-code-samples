package net.lkrnac.book.eiws.chapter05;

import javax.jms.ConnectionFactory;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.converter.MessagingMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class JmsMessageAbstrationsApplication {
  public static void main(String[] args) throws InterruptedException {
    SpringApplication.run(JmsMessageAbstrationsApplication.class, args);
  }

  // @Bean
  // public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory,
  // MessageConverter messageConverter) {
  // JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
  // jmsTemplate.setMessageConverter(messageConverter);
  // return jmsTemplate;
  // }

  @Bean
  public JmsMessagingTemplate jmsMessagingTemplate(
      ConnectionFactory connectionFactory,// JmsTemplate jmsTemplate,
      MessageConverter messageConverter) {
    JmsMessagingTemplate jmsMessagingTemplate =
        new JmsMessagingTemplate(connectionFactory);// jmsTemplate);
    jmsMessagingTemplate.setJmsMessageConverter(messageConverter);
    return jmsMessagingTemplate;
  }

  @Bean
  public MessageConverter messageConverter() {
    MappingJackson2MessageConverter payloadConverter =
        new MappingJackson2MessageConverter();
    payloadConverter.setTargetType(MessageType.TEXT);
    payloadConverter.setTypeIdPropertyName("__type");

    MessagingMessageConverter messageConverter =
        new MessagingMessageConverter();
    messageConverter.setPayloadConverter(payloadConverter);
    return messageConverter;
  }

  @Bean
  public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
      ConnectionFactory connectionFactory, MessageConverter messageConverter) {
    DefaultJmsListenerContainerFactory factory =
        new DefaultJmsListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setMessageConverter(messageConverter);
    return factory;
  }

  @Bean
  public UserHandler userHandler() {
    return new UserHandler() {
      @Override
      public void handleUser(User user) {
        log.info("User object Received: {}", user);
      }
    };
  }
}