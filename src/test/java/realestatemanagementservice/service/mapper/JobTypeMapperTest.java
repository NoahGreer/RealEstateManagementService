package realestatemanagementservice.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class JobTypeMapperTest {

    private JobTypeMapper jobTypeMapper;

    @BeforeEach
    public void setUp() {
        jobTypeMapper = new JobTypeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(jobTypeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(jobTypeMapper.fromId(null)).isNull();
    }
}
