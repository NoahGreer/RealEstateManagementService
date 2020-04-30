package realestatemanagementservice.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LeaseMapperTest {

    private LeaseMapper leaseMapper;

    @BeforeEach
    public void setUp() {
        leaseMapper = new LeaseMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(leaseMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(leaseMapper.fromId(null)).isNull();
    }
}
