package realestatemanagementservice.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import realestatemanagementservice.web.rest.TestUtil;

public class LeaseDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaseDTO.class);
        LeaseDTO leaseDTO1 = new LeaseDTO();
        leaseDTO1.setId(1L);
        LeaseDTO leaseDTO2 = new LeaseDTO();
        assertThat(leaseDTO1).isNotEqualTo(leaseDTO2);
        leaseDTO2.setId(leaseDTO1.getId());
        assertThat(leaseDTO1).isEqualTo(leaseDTO2);
        leaseDTO2.setId(2L);
        assertThat(leaseDTO1).isNotEqualTo(leaseDTO2);
        leaseDTO1.setId(null);
        assertThat(leaseDTO1).isNotEqualTo(leaseDTO2);
    }
}
