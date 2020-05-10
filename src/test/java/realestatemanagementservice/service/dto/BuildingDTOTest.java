package realestatemanagementservice.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import realestatemanagementservice.web.rest.TestUtil;

public class BuildingDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BuildingDTO.class);
        BuildingDTO buildingDTO1 = new BuildingDTO();
        buildingDTO1.setId(1L);
        BuildingDTO buildingDTO2 = new BuildingDTO();
        assertThat(buildingDTO1).isNotEqualTo(buildingDTO2);
        buildingDTO2.setId(buildingDTO1.getId());
        assertThat(buildingDTO1).isEqualTo(buildingDTO2);
        buildingDTO2.setId(2L);
        assertThat(buildingDTO1).isNotEqualTo(buildingDTO2);
        buildingDTO1.setId(null);
        assertThat(buildingDTO1).isNotEqualTo(buildingDTO2);
    }
}
