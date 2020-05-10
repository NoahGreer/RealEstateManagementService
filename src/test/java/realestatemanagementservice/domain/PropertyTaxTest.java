package realestatemanagementservice.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import realestatemanagementservice.web.rest.TestUtil;

public class PropertyTaxTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PropertyTax.class);
        PropertyTax propertyTax1 = new PropertyTax();
        propertyTax1.setId(1L);
        PropertyTax propertyTax2 = new PropertyTax();
        propertyTax2.setId(propertyTax1.getId());
        assertThat(propertyTax1).isEqualTo(propertyTax2);
        propertyTax2.setId(2L);
        assertThat(propertyTax1).isNotEqualTo(propertyTax2);
        propertyTax1.setId(null);
        assertThat(propertyTax1).isNotEqualTo(propertyTax2);
    }
}
