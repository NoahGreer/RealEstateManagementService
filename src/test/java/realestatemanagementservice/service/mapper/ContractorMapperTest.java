package realestatemanagementservice.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ContractorMapperTest {

    private ContractorMapper contractorMapper;

    @BeforeEach
    public void setUp() {
        contractorMapper = new ContractorMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(contractorMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(contractorMapper.fromId(null)).isNull();
    }
}
