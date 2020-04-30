package realestatemanagementservice.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ApartmentMapperTest {

    private ApartmentMapper apartmentMapper;

    @BeforeEach
    public void setUp() {
        apartmentMapper = new ApartmentMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(apartmentMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(apartmentMapper.fromId(null)).isNull();
    }
}
