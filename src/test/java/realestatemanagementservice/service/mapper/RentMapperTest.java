package realestatemanagementservice.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RentMapperTest {

    private RentMapper rentMapper;

    @BeforeEach
    public void setUp() {
        rentMapper = new RentMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(rentMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(rentMapper.fromId(null)).isNull();
    }
}
