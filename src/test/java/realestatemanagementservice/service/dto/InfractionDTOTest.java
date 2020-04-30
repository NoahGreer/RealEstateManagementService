package realestatemanagementservice.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import realestatemanagementservice.web.rest.TestUtil;

public class InfractionDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InfractionDTO.class);
        InfractionDTO infractionDTO1 = new InfractionDTO();
        infractionDTO1.setId(1L);
        InfractionDTO infractionDTO2 = new InfractionDTO();
        assertThat(infractionDTO1).isNotEqualTo(infractionDTO2);
        infractionDTO2.setId(infractionDTO1.getId());
        assertThat(infractionDTO1).isEqualTo(infractionDTO2);
        infractionDTO2.setId(2L);
        assertThat(infractionDTO1).isNotEqualTo(infractionDTO2);
        infractionDTO1.setId(null);
        assertThat(infractionDTO1).isNotEqualTo(infractionDTO2);
    }
}
