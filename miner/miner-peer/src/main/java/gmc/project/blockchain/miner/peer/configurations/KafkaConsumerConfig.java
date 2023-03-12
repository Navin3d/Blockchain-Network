package gmc.project.blockchain.miner.peer.configurations;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import gmc.project.blockchain.miner.peer.models.KafkaModel;

@Configuration
public class KafkaConsumerConfig {

	@Autowired
	private KafkaConfig kafkaConfigs;
	
	public Map<String, Object> getConsumerConfiguration() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigs.getServerUrl());
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		return configs;
	}
	
	public ConsumerFactory<String, KafkaModel> getConsumerFactory() {
		return new DefaultKafkaConsumerFactory<>(getConsumerConfiguration());
	}
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, KafkaModel> kafkaListenerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, KafkaModel> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(getConsumerFactory());
		return factory;
	}
	
}