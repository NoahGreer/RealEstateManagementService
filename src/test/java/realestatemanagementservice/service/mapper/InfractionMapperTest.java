package realestatemanagementservice.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class InfractionMapperTest {

    private InfractionMapper infractionMapper;

    @BeforeEach
    public void setUp() {
        infractionMapper = new InfractionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(infractionMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(infractionMapper.fromId(null)).isNull();
    }
}
