package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@Configuration
// Cada DB debe tener su propia carpeta de repositorios para no chocar
@EnableMongoRepositories(basePackages = "com.example.demo.repository.mongo")
@EnableNeo4jRepositories(basePackages = "com.example.demo.repository.neo4j")
@EnableCassandraRepositories(basePackages = "com.example.demo.repository.cassandra")
public class DatabaseConfig {
}
