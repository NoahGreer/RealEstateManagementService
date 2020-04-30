package realestatemanagementservice.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import realestatemanagementservice.web.rest.TestUtil;

public class ApartmentDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApartmentDTO.class);
        ApartmentDTO apartmentDTO1 = new ApartmentDTO();
        apartmentDTO1.setId(1L);
        ApartmentDTO apartmentDTO2 = new ApartmentDTO();
        assertThat(apartmentDTO1).isNotEqualTo(apartmentDTO2);
        apartmentDTO2.setId(apartmentDTO1.getId());
        assertThat(apartmentDTO1).isEqualTo(apartmentDTO2);
        apartmentDTO2.setId(2L);
        assertThat(apartmentDTO1).isNotEqualTo(apartmentDTO2);
        apartmentDTO1.setId(null);
        assertThat(apartmentDTO1).isNotEqualTo(apartmentDTO2);
    }
}
