package com.example.demo;

import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.Mongo;

@Configuration
public class MongoConfig {
	public @Bean Mongo mongo() throws UnknownHostException {
		return new Mongo("localhost");
	}
	
	public @Bean MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate( mongo(), "FitnessTracker");
	}
	

}
