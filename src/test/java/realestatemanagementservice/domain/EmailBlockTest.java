package realestatemanagementservice.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import realestatemanagementservice.web.rest.TestUtil;
import java.time.LocalDate;



class EmailBlockTest {

	@Test
    public void equalsVerifier() throws Exception {
		
		LocalDate date = LocalDate.now();
		
        TestUtil.equalsVerifier(Lease.class);
        
        //used to test:
        //that no one with an ended lease appears on the email list
        Lease lease1 = new Lease();
		lease1.dateSigned(date.minusDays(5));
		lease1.endDate(date.minusDays(1));
		Person person1 = new Person();
		person1.setEmailAddress("bademail@pastperson");
		lease1.addPerson(person1);
        
		//that no one who does not yet have an active lease appears 
		Lease lease2 = new Lease();
        lease2.dateSigned(date.plusDays(1));
		lease2.endDate(date.plusDays(5));
		Person person2 = new Person();
		person1.setEmailAddress("bademail@futureperson");
		lease2.addPerson(person2);
        
		//a person with only one lease does appear
		Lease lease3 = new Lease();
        lease3.dateSigned(date.minusDays(5));
		lease3.endDate(date.plusDays(5));
		Person person3 = new Person();
		person3.setEmailAddress("goodemail@singleresident");
		lease3.addPerson(person3);
        
		//two people on the same lease both appear on the list
		Lease lease4 = new Lease();
        lease4.dateSigned(date.minusDays(10));
		lease4.endDate(date.plusDays(10));
		Person person4 = new Person();
		person4.setEmailAddress("goodemail@multiresidentfirst");
		lease4.addPerson(person4);
		Person person5 = new Person();
		person5.setEmailAddress("goodemail@multiresidentsecond");
		lease4.addPerson(person5);
		
		//a null email does not error the method
		Lease lease5 = new Lease();
        lease5.dateSigned(date.minusDays(10));
		lease5.endDate(date.plusDays(10));
		Person person6 = new Person();
		person6.setEmailAddress(null);
		lease5.addPerson(person6);
		
		//check the isMinor should not be on the list
		Person person7 = new Person();
		person7.setIsMinor(true);
		person7.setEmailAddress("bademail@isminor");
		lease5.addPerson(person7);
		
		//with a good and bad leases still appears and only once
		Lease lease6 = new Lease();
        lease6.dateSigned(date.minusDays(15));
		lease6.endDate(date.minusDays(5));
		lease6.addPerson(person3);
		person3.addLease(lease6);
		
	}

}
