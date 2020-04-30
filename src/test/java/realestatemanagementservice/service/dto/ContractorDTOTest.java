package realestatemanagementservice.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import realestatemanagementservice.web.rest.TestUtil;

public class ContractorDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContractorDTO.class);
        ContractorDTO contractorDTO1 = new ContractorDTO();
        contractorDTO1.setId(1L);
        ContractorDTO contractorDTO2 = new ContractorDTO();
        assertThat(contractorDTO1).isNotEqualTo(contractorDTO2);
        contractorDTO2.setId(contractorDTO1.getId());
        assertThat(contractorDTO1).isEqualTo(contractorDTO2);
        contractorDTO2.setId(2L);
        assertThat(contractorDTO1).isNotEqualTo(contractorDTO2);
        contractorDTO1.setId(null);
        assertThat(contractorDTO1).isNotEqualTo(contractorDTO2);
    }
}
