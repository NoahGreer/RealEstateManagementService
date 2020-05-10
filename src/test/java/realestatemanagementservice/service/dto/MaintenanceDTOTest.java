package realestatemanagementservice.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import realestatemanagementservice.web.rest.TestUtil;

public class MaintenanceDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaintenanceDTO.class);
        MaintenanceDTO maintenanceDTO1 = new MaintenanceDTO();
        maintenanceDTO1.setId(1L);
        MaintenanceDTO maintenanceDTO2 = new MaintenanceDTO();
        assertThat(maintenanceDTO1).isNotEqualTo(maintenanceDTO2);
        maintenanceDTO2.setId(maintenanceDTO1.getId());
        assertThat(maintenanceDTO1).isEqualTo(maintenanceDTO2);
        maintenanceDTO2.setId(2L);
        assertThat(maintenanceDTO1).isNotEqualTo(maintenanceDTO2);
        maintenanceDTO1.setId(null);
        assertThat(maintenanceDTO1).isNotEqualTo(maintenanceDTO2);
    }
}
