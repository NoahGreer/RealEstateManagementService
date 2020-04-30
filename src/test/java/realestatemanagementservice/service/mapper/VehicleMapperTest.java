package realestatemanagementservice.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class VehicleMapperTest {

    private VehicleMapper vehicleMapper;

    @BeforeEach
    public void setUp() {
        vehicleMapper = new VehicleMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(vehicleMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(vehicleMapper.fromId(null)).isNull();
    }
}
