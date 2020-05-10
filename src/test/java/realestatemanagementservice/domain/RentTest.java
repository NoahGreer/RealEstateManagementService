package realestatemanagementservice.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import realestatemanagementservice.web.rest.TestUtil;

public class RentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rent.class);
        Rent rent1 = new Rent();
        rent1.setId(1L);
        Rent rent2 = new Rent();
        rent2.setId(rent1.getId());
        assertThat(rent1).isEqualTo(rent2);
        rent2.setId(2L);
        assertThat(rent1).isNotEqualTo(rent2);
        rent1.setId(null);
        assertThat(rent1).isNotEqualTo(rent2);
    }
}
