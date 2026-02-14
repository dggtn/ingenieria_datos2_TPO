package com.example.demo.config;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.DriverConfigLoaderBuilderConfigurer;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableCassandraRepositories("com.example.demo.repository.cassandra")
public class CassandraConfig extends AbstractCassandraConfiguration {
        @Value("${cassandra.keyspace}")
        private String keyspace;
        @Override
        protected String getKeyspaceName() {
            return keyspace;
        }

        public String[] getEntityBasePackages() {
            return new String[] { "com.example.demo.model" };
        }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        CreateKeyspaceSpecification specification = CreateKeyspaceSpecification
                .createKeyspace(keyspace).ifNotExists()
                .with(KeyspaceOption.DURABLE_WRITES, true).withSimpleReplication();
        return Arrays.asList(specification);
    }
    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    protected @Nullable DriverConfigLoaderBuilderConfigurer getDriverConfigLoaderBuilderConfigurer() {
        return config ->
                config.withString(DefaultDriverOption.METADATA_SCHEMA_REQUEST_TIMEOUT, "30s")
                        .withString(DefaultDriverOption.REQUEST_TIMEOUT, "30s");
    }

    @Override
    protected @Nullable String getLocalDataCenter() {
        return "datacenter1";
    }
}

