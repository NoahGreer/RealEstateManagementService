package realestatemanagementservice.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import realestatemanagementservice.web.rest.TestUtil;

public class RentDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RentDTO.class);
        RentDTO rentDTO1 = new RentDTO();
        rentDTO1.setId(1L);
        RentDTO rentDTO2 = new RentDTO();
        assertThat(rentDTO1).isNotEqualTo(rentDTO2);
        rentDTO2.setId(rentDTO1.getId());
        assertThat(rentDTO1).isEqualTo(rentDTO2);
        rentDTO2.setId(2L);
        assertThat(rentDTO1).isNotEqualTo(rentDTO2);
        rentDTO1.setId(null);
        assertThat(rentDTO1).isNotEqualTo(rentDTO2);
    }
}
