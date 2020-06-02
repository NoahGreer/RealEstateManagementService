package realestatemanagementservice.web.rest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import realestatemanagementservice.domain.Apartment;
import realestatemanagementservice.domain.Building;
import realestatemanagementservice.domain.Contractor;
import realestatemanagementservice.domain.Infraction;
import realestatemanagementservice.domain.JobType;
import realestatemanagementservice.domain.Lease;
import realestatemanagementservice.domain.Maintenance;
import realestatemanagementservice.domain.Person;
import realestatemanagementservice.domain.Pet;
import realestatemanagementservice.domain.PropertyTax;
import realestatemanagementservice.domain.Rent;
import realestatemanagementservice.domain.Vehicle;
import realestatemanagementservice.repository.ApartmentRepository;
import realestatemanagementservice.repository.BuildingRepository;
import realestatemanagementservice.repository.ContractorRepository;
import realestatemanagementservice.repository.InfractionRepository;
import realestatemanagementservice.repository.JobTypeRepository;
import realestatemanagementservice.repository.LeaseRepository;
import realestatemanagementservice.repository.MaintenanceRepository;
import realestatemanagementservice.repository.PersonRepository;
import realestatemanagementservice.repository.PetRepository;
import realestatemanagementservice.repository.RentRepository;
import realestatemanagementservice.repository.VehicleRepository;


@RestController
@RequestMapping("/api")
public class testdata {
	
	@Autowired
	private ApartmentRepository apartmentRepository;

	@Autowired
	private BuildingRepository buildingRepository;
	
	@Autowired
	private LeaseRepository leaseRepository;

	@Autowired
	private RentRepository rentRepository;
	
	@Autowired
	private VehicleRepository vehicleRepository;
	
	@Autowired
	private MaintenanceRepository maintenanceRepository;
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private ContractorRepository contractorRepository;

	@Autowired
	private InfractionRepository infractionRepository;
	
	@Autowired
	private PetRepository petRepository;
	
	@Autowired
	private JobTypeRepository jobTypeRepository;
	
	@Autowired
	private EntityManager em;

	@Value("${jhipster.clientApp.name}")
    private String applicationName;
	
	@PostMapping("/testData")
    public void posttestdata() throws Exception {
    	
		LocalDate date = LocalDate.now();
	
		Long[] longsList = new Long[20];
		for(int i = 0 ; i<20; i++){
			longsList[i] = (long) i;
		}
		BigDecimal[] rents = {new BigDecimal("500"), new BigDecimal("750"), new BigDecimal("1000"), new BigDecimal("1250"),new BigDecimal("1500")};	
		
		//Adding the jobtypes and contractor branch
		Contractor contractor1 = new Contractor();
		contractor1.setId(longsList[1]);
		contractor1.setCompanyName("bob's water and heating");
		
			JobType water = new JobType();
			water.setId(longsList[1]);
			water.setName("water");
			water.addContractor(contractor1);
		
			JobType heating = new JobType();
			heating.setId(longsList[2]);
			heating.setName("heating");
			
		contractor1.addJobType(heating);
		
		Contractor contractor2 = new Contractor();
		contractor2.setId(longsList[2]);
		contractor2.setCompanyName("jim's water");
		contractor2.addJobType(water);
		
		Contractor contractor3 = new Contractor();
		contractor3.setId(longsList[3]);
		contractor3.setCompanyName("harry's heating");
		contractor3.addJobType(heating);
		
		Contractor contractor4 = new Contractor();
		contractor4.setId(longsList[4]);
		contractor4.setCompanyName("no work steve's");
		
			JobType notdone = new JobType();
			notdone.setId(longsList[3]);
			notdone.setName("notdonework");
		
			Set<Contractor> contractorEntities = new HashSet<>();
			contractorEntities.add(contractor1);
			contractorEntities.add(contractor2);
			contractorEntities.add(contractor3);
			contractorEntities.add(contractor4);

			// Initialize the database
			contractorRepository.saveAll(contractorEntities);
			contractorRepository.flush();
			
			Set<JobType> jobTypeEntities = new HashSet<>();
			jobTypeEntities.add(water);
			jobTypeEntities.add(heating);
			jobTypeEntities.add(notdone);

			// Initialize the database
			jobTypeRepository.saveAll(jobTypeEntities);
			jobTypeRepository.flush();

		//buildings
		//built out as building - apartment - lease - (person, rent, vehicle, pet, infractions)
		Building red = new Building();
		red.setId(longsList[1]);
		red.setPropertyNumber("redpropertynumber");
		red.setName("Red");
		
			//multiple years paid on one building
			PropertyTax redone = new PropertyTax();
			redone.setId(longsList[1]);
			redone.setBuilding(red);
			redone.setDatePaid(date.minusYears(longsList[1]));
			redone.setConfirmationNumber("yearagoredtax");
			PropertyTax redtwo = new PropertyTax();
			redtwo.setId(longsList[2]);
			redtwo.setBuilding(red);
			redtwo.setDatePaid(date);
			redtwo.setConfirmationNumber("thisyearredtax");
			
			//first of multiple apartments in one building
			Apartment redA = new Apartment();
			redA.setBuilding(red);
			redA.setId(longsList[1]);
			redA.setUnitNumber("A");
			redA.setMoveInReady(false);
			
				//multiple leases in one apartment - one done, one current, one in the future
				//also adding apartment to lease
				Lease redALeaseOne = new Lease();
				redALeaseOne.setApartment(redA);
				redALeaseOne.dateSigned(date.minusDays(5));
				redALeaseOne.endDate(date.minusDays(1));
				
				Lease redALeaseTwo = new Lease();
				redALeaseTwo.setApartment(redA);
				redALeaseTwo.dateSigned(date.plusDays(1));
				redALeaseTwo.endDate(date.plusDays(5));
				redALeaseTwo.setAmount(rents[1]);
				
				Lease redALeaseThree = new Lease();
				redALeaseThree.setApartment(redA);
				redALeaseThree.dateSigned(date.minusDays(5));
				redALeaseThree.endDate(date.plusDays(5));
				
					//multiple people on multiple leases - one primary, one bad email, one minor
					//also adding lease to person
					Person smithOne = new Person();
					smithOne.addLease(redALeaseOne);
					smithOne.addLease(redALeaseTwo);
					smithOne.addLease(redALeaseThree);
					smithOne.emailAddress("mrsmith@gmail.com");
					smithOne.firstName("John");
					smithOne.setLastName("Smith");
					smithOne.setPhoneNumber("555-666-1234");
					smithOne.setIsMinor(false);
					smithOne.setId(longsList[1]);
					smithOne.setPrimaryContact(true);
		
					Person smithTwo = new Person();
					smithTwo.addLease(redALeaseOne);
					smithTwo.addLease(redALeaseTwo);
					smithTwo.addLease(redALeaseThree);
					smithTwo.emailAddress("mssmithgmail.com(bademail - no at)");
					smithTwo.firstName("Jane");
					smithTwo.setLastName("Smith");
					smithTwo.setPhoneNumber("555-777-1234");
					smithTwo.setIsMinor(false);
					smithTwo.setId(longsList[2]);
					smithTwo.setPrimaryContact(false);
					
					Person smithThree = new Person();
					smithThree.addLease(redALeaseOne);
					smithThree.addLease(redALeaseTwo);
					smithThree.addLease(redALeaseThree);
					smithThree.emailAddress("jrsmith@gmail.com(bademail - minor)");
					smithThree.firstName("Jane");
					smithThree.setLastName("Smith");
					smithThree.setPhoneNumber("555-888-1234");
					smithThree.setIsMinor(true);
					smithThree.setId(longsList[3]);
					smithThree.setPrimaryContact(false);
		
					//multiple rent payments on one lease, one not yet due, one paid on time, one paid on time in the past
					//adding lease to rent
					Rent redATwoRent1 = new Rent();
					redATwoRent1.setId(longsList[1]);
					redATwoRent1.setLease(redALeaseTwo);
					redATwoRent1.setAmount(rents[1]);
					redATwoRent1.setDueDate(date.minusMonths(longsList[1]));
					redATwoRent1.setReceivedDate(date.minusMonths(longsList[1]));
					
					Rent redATwoRent2 = new Rent();
					redATwoRent2.setId(longsList[2]);
					redATwoRent2.setLease(redALeaseTwo);
					redATwoRent2.setAmount(rents[1]);
					redATwoRent2.setDueDate(date);
					redATwoRent2.setReceivedDate(date);
					
					//rent that is not yet due
					Rent redATwoRent3 = new Rent();
					redATwoRent3.setId(longsList[3]);
					redATwoRent3.setLease(redALeaseTwo);
					redATwoRent3.setAmount(rents[1]);
					redATwoRent3.setDueDate(date.plusMonths(longsList[1]));
					
					//rent payment on inactive lease
					Rent redAOneRent1 = new Rent();
					redAOneRent1.setId(longsList[4]);
					redAOneRent1.setLease(redALeaseOne);
					redAOneRent1.setAmount(rents[1]);
					redAOneRent1.setDueDate(date.minusMonths(longsList[1]));
					redAOneRent1.setReceivedDate(date.minusMonths(longsList[1]));
					
					//multiple vehicles on multiple leases
					//adding lease to rent
					Vehicle redACar1 = new Vehicle();
					redACar1.setId(longsList[1]);
					redACar1.addLease(redALeaseTwo);
					redACar1.setLicensePlateNumber("goodRedATwoPlate1");
					
					Vehicle redACar2 = new Vehicle();
					redACar2.setId(longsList[2]);
					redACar2.addLease(redALeaseTwo);
					redACar2.setLicensePlateNumber("goodRedATwoPlate2");
					
					//expired lease
					Vehicle redACar3 = new Vehicle();
					redACar3.setId(longsList[3]);
					redACar3.addLease(redALeaseOne);
					redACar3.setLicensePlateNumber("badRedATwoPlate - inactive lease");
					
					//multiple pets on multiple leases
					//adding lease to pet
					Pet redAPet1 = new Pet();
					redAPet1.setId(longsList[1]);
					redAPet1.setName("Fluffy");
					redAPet1.addLease(redALeaseTwo);
					
					Pet redAPet2 = new Pet();
					redAPet2.setId(longsList[2]);
					redAPet2.setName("Mr Biggles Worth");
					redAPet2.addLease(redALeaseTwo);
					
					//expired lease
					Pet redAPet3 = new Pet();
					redAPet3.setId(longsList[3]);
					redAPet3.setName("bad pet -inactive lease redALeaseOne");
					redAPet3.addLease(redALeaseOne);
					
					//multiple infractions on multiple leases
					//adding lease to infractions
					Infraction redAInfra1 = new Infraction();
					redAInfra1.setId(longsList[1]);
					redAInfra1.setLease(redALeaseTwo);
					redAInfra1.setCause("something bad happened");
					
					Infraction redAInfra2 = new Infraction();
					redAInfra2.setId(longsList[2]);
					redAInfra2.setLease(redALeaseTwo);
					redAInfra2.setCause("something worse happened");
					
					Infraction redAInfra3 = new Infraction();
					redAInfra3.setId(longsList[3]);
					redAInfra3.setLease(redALeaseOne);
					redAInfra3.setCause("generally shouldnt show up - inactive lease");
				
				//multiple contractors on one apartment
				Maintenance redAMain1 = new Maintenance();
				redAMain1.setId(longsList[1]);
				redAMain1.setApartment(redA);
				redAMain1.setContractor(contractor1);
				redAMain1.setNotificationDate(date.minusWeeks(longsList[1]));
				redAMain1.setDateComplete(date);
				
				Maintenance redAMain2 = new Maintenance();
				redAMain2.setId(longsList[2]);
				redAMain2.setApartment(redA);
				redAMain2.setContractor(contractor2);
				redAMain2.setNotificationDate(date.minusWeeks(longsList[1]));
				redAMain2.setDateComplete(date);
				
				//work not yet completed
				Maintenance redAMain3 = new Maintenance();
				redAMain3.setId(longsList[3]);
				redAMain3.setApartment(redA);
				redAMain3.setContractor(contractor3);
				redAMain3.setNotificationDate(date.minusWeeks(longsList[1]));
			
			//second apartment in first building
			Apartment redB = new Apartment();
			redB.setMoveInReady(true);;
			redB.setId(longsList[2]);
			redB.setUnitNumber("B");	
			
				//same contractor, same apartment, multiple maintenace
				Maintenance redBMain1 = new Maintenance();
				redBMain1.setId(longsList[4]);
				redBMain1.setApartment(redB);
				redBMain1.setContractor(contractor1);
				redBMain1.setNotificationDate(date.minusWeeks(longsList[1]));
				redBMain1.setDateComplete(date);
				
				Maintenance redBMain2 = new Maintenance();
				redBMain2.setId(longsList[5]);
				redBMain2.setContractor(contractor1);
				redBMain2.setNotificationDate(date.minusWeeks(longsList[1]));
				redBMain2.setDateComplete(date);
				
			redB.addMaintenance(redBMain2);
			
			//built out as bottom up (person etc) - lease - apartment - building
			Person doeOne = new Person();
			doeOne.emailAddress("mrdoe@gmail.com");
			doeOne.firstName("John");
			doeOne.setLastName("Doe");
			doeOne.setPhoneNumber("666-666-1234");
			doeOne.setIsMinor(false);
			doeOne.setId(longsList[4]);
			doeOne.setPrimaryContact(true);

			Person doeTwo = new Person();
			doeTwo.emailAddress("msdoe@gmail.com");
			doeTwo.firstName("Jane");
			doeTwo.setLastName("Doe");
			doeTwo.setPhoneNumber("666-777-1234");
			doeTwo.setIsMinor(false);
			doeTwo.setId(longsList[5]);
			doeTwo.setPrimaryContact(true);
			
			
			Rent blueATwoRent1 = new Rent();
			blueATwoRent1.setId(longsList[4]);
			blueATwoRent1.setLease(redALeaseTwo);
			blueATwoRent1.setAmount(rents[1]);
			blueATwoRent1.setDueDate(date.minusMonths(longsList[1]));
			blueATwoRent1.setReceivedDate(date.minusMonths(longsList[1]));
			
			Rent blueATwoRent2 = new Rent();
			blueATwoRent2.setId(longsList[5]);
			blueATwoRent2.setLease(redALeaseTwo);
			blueATwoRent2.setAmount(rents[1]);
			blueATwoRent2.setDueDate(date);
			blueATwoRent2.setReceivedDate(date);
			
			Vehicle blueACar1 = new Vehicle();
			blueACar1.setId(longsList[1]);
			blueACar1.setLicensePlateNumber("goodBlueATwoPlate1");
			
			Pet blueAPet1 = new Pet();
			blueAPet1.setId(longsList[4]);
			blueAPet1.setName("Dog");
			
			Infraction blueAInfra1 = new Infraction();
			blueAInfra1.setId(longsList[4]);
			blueAInfra1.setCause("something bad happened in blue");

				Lease blueALeaseOne = new Lease();
				blueALeaseOne.dateSigned(date.minusDays(5));
				blueALeaseOne.endDate(date.minusDays(1));
				blueALeaseOne.addPerson(doeOne);
				blueALeaseOne.addPerson(doeTwo);
				blueALeaseOne.addVehicle(blueACar1);
				blueALeaseOne.addPet(blueAPet1);
				
				Lease blueALeaseTwo = new Lease();
				blueALeaseTwo.dateSigned(date.plusDays(1));
				blueALeaseTwo.endDate(date.plusDays(5));
				blueALeaseTwo.setAmount(rents[1]);
				blueALeaseTwo.addPerson(doeOne);
				blueALeaseTwo.addPerson(doeTwo);
				blueALeaseTwo.addVehicle(blueACar1);
				blueALeaseTwo.addPet(blueAPet1);
				blueALeaseTwo.addRent(blueATwoRent1);
				blueALeaseTwo.addRent(blueATwoRent2);
				blueALeaseTwo.addInfraction(blueAInfra1);
				
					Apartment blueA = new Apartment();
					blueA.addLease(blueALeaseOne);
					blueA.addLease(blueALeaseTwo);
					blueA.setId(longsList[3]);
					blueA.setUnitNumber("A");
			
						Building blue = new Building();
						blue.setId(longsList[2]);
						blue.setPropertyNumber("bluepropertynumber");
						blue.setName("Blue");	
						blue.addApartment(blueA);
	}
}
