package realestatemanagementservice.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import realestatemanagementservice.web.rest.TestUtil;

public class VehicleDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleDTO.class);
        VehicleDTO vehicleDTO1 = new VehicleDTO();
        vehicleDTO1.setId(1L);
        VehicleDTO vehicleDTO2 = new VehicleDTO();
        assertThat(vehicleDTO1).isNotEqualTo(vehicleDTO2);
        vehicleDTO2.setId(vehicleDTO1.getId());
        assertThat(vehicleDTO1).isEqualTo(vehicleDTO2);
        vehicleDTO2.setId(2L);
        assertThat(vehicleDTO1).isNotEqualTo(vehicleDTO2);
        vehicleDTO1.setId(null);
        assertThat(vehicleDTO1).isNotEqualTo(vehicleDTO2);
    }
}
