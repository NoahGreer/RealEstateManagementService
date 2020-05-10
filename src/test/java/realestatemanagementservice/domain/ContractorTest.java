package realestatemanagementservice.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import realestatemanagementservice.web.rest.TestUtil;

public class ContractorTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contractor.class);
        Contractor contractor1 = new Contractor();
        contractor1.setId(1L);
        Contractor contractor2 = new Contractor();
        contractor2.setId(contractor1.getId());
        assertThat(contractor1).isEqualTo(contractor2);
        contractor2.setId(2L);
        assertThat(contractor1).isNotEqualTo(contractor2);
        contractor1.setId(null);
        assertThat(contractor1).isNotEqualTo(contractor2);
    }
}
