package realestatemanagementservice.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BuildingMapperTest {

    private BuildingMapper buildingMapper;

    @BeforeEach
    public void setUp() {
        buildingMapper = new BuildingMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(buildingMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(buildingMapper.fromId(null)).isNull();
    }
}
