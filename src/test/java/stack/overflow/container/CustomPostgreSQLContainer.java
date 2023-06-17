package stack.overflow.container;

import org.testcontainers.containers.PostgreSQLContainer;

public class CustomPostgreSQLContainer extends PostgreSQLContainer<CustomPostgreSQLContainer> {

    public CustomPostgreSQLContainer(String dockerImageName) {
        super(dockerImageName);
    }

    @Override
    public void stop() {
    }
}
