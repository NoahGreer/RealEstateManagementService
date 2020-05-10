package realestatemanagementservice.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import realestatemanagementservice.web.rest.TestUtil;

public class PropertyTaxDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PropertyTaxDTO.class);
        PropertyTaxDTO propertyTaxDTO1 = new PropertyTaxDTO();
        propertyTaxDTO1.setId(1L);
        PropertyTaxDTO propertyTaxDTO2 = new PropertyTaxDTO();
        assertThat(propertyTaxDTO1).isNotEqualTo(propertyTaxDTO2);
        propertyTaxDTO2.setId(propertyTaxDTO1.getId());
        assertThat(propertyTaxDTO1).isEqualTo(propertyTaxDTO2);
        propertyTaxDTO2.setId(2L);
        assertThat(propertyTaxDTO1).isNotEqualTo(propertyTaxDTO2);
        propertyTaxDTO1.setId(null);
        assertThat(propertyTaxDTO1).isNotEqualTo(propertyTaxDTO2);
    }
}
