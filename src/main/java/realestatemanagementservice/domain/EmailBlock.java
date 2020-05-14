package realestatemanagementservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.time.LocalDate;


public class EmailBlock {
	
	private int count;
	/*Could turn the string into a set to remove duplicates but letting the email software prevent the double send
	*Could become a problem if number of email addresses goes over the allowable number of email addresses ~500 for a message 
	*but unlikely at this point
	*/
	private String emails;
	LocalDate today = LocalDate.now();
	
	public void generateBlock(){
		 foreach(Person){
			Person nextperson; /**get next person **/
			if(nextperson.isIsMinor() || nextperson.getEmailAddress() == null || nextperson.getLeases().isEmpty()) {
				Set<Lease> nextpersonleaseset = nextperson.getLeases();
				Lease nextpersonlease = Collections.max(nextpersonleaseset, Comparator.comparing(s -> s.getEndDate()));
				if(today.isBefore(nextpersonlease.getEndDate()) && 
						today.isAfter(nextpersonlease.getDateSigned())) {
					emails += nextperson.getEmailAddress()+"; ";
					count++;
				}
			}
		}
	}
	
	public String getemails(){
		return emails;
	}
	
	public int getcount(){
		return count;
	}
}