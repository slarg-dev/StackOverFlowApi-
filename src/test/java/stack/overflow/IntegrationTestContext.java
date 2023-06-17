package stack.overflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import stack.overflow.container.CustomPostgreSQLContainer;
import stack.overflow.util.TestUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class IntegrationTestContext {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected TestUtil testUtil;
    @PersistenceContext
    protected EntityManager entityManager;

    @Container
    private static final PostgreSQLContainer<CustomPostgreSQLContainer> POSTGRES = new CustomPostgreSQLContainer("postgres:15")
            .withDatabaseName("stack_overflow_postgres")
            .withUsername("postgres")
            .withPassword("postgres123")
            .withExposedPorts(5432)
            .withReuse(true)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(5577), new ExposedPort(5432)))
            ));
    ;

    @DynamicPropertySource
    private static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }
}
