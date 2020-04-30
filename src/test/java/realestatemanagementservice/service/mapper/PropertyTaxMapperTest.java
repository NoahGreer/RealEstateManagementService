package realestatemanagementservice.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PropertyTaxMapperTest {

    private PropertyTaxMapper propertyTaxMapper;

    @BeforeEach
    public void setUp() {
        propertyTaxMapper = new PropertyTaxMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(propertyTaxMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(propertyTaxMapper.fromId(null)).isNull();
    }
}
