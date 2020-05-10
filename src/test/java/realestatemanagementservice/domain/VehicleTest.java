package realestatemanagementservice.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import realestatemanagementservice.web.rest.TestUtil;

public class VehicleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vehicle.class);
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setId(1L);
        Vehicle vehicle2 = new Vehicle();
        vehicle2.setId(vehicle1.getId());
        assertThat(vehicle1).isEqualTo(vehicle2);
        vehicle2.setId(2L);
        assertThat(vehicle1).isNotEqualTo(vehicle2);
        vehicle1.setId(null);
        assertThat(vehicle1).isNotEqualTo(vehicle2);
    }
}
