package realestatemanagementservice.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import realestatemanagementservice.web.rest.TestUtil;

public class ApartmentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Apartment.class);
        Apartment apartment1 = new Apartment();
        apartment1.setId(1L);
        Apartment apartment2 = new Apartment();
        apartment2.setId(apartment1.getId());
        assertThat(apartment1).isEqualTo(apartment2);
        apartment2.setId(2L);
        assertThat(apartment1).isNotEqualTo(apartment2);
        apartment1.setId(null);
        assertThat(apartment1).isNotEqualTo(apartment2);
    }
}
