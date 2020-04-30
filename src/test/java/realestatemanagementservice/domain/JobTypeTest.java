package realestatemanagementservice.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import realestatemanagementservice.web.rest.TestUtil;

public class JobTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobType.class);
        JobType jobType1 = new JobType();
        jobType1.setId(1L);
        JobType jobType2 = new JobType();
        jobType2.setId(jobType1.getId());
        assertThat(jobType1).isEqualTo(jobType2);
        jobType2.setId(2L);
        assertThat(jobType1).isNotEqualTo(jobType2);
        jobType1.setId(null);
        assertThat(jobType1).isNotEqualTo(jobType2);
    }
}
