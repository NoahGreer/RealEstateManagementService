package realestatemanagementservice.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MaintenanceMapperTest {

    private MaintenanceMapper maintenanceMapper;

    @BeforeEach
    public void setUp() {
        maintenanceMapper = new MaintenanceMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(maintenanceMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(maintenanceMapper.fromId(null)).isNull();
    }
}
