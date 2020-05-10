package realestatemanagementservice.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import realestatemanagementservice.web.rest.TestUtil;

public class InfractionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Infraction.class);
        Infraction infraction1 = new Infraction();
        infraction1.setId(1L);
        Infraction infraction2 = new Infraction();
        infraction2.setId(infraction1.getId());
        assertThat(infraction1).isEqualTo(infraction2);
        infraction2.setId(2L);
        assertThat(infraction1).isNotEqualTo(infraction2);
        infraction1.setId(null);
        assertThat(infraction1).isNotEqualTo(infraction2);
    }
}
