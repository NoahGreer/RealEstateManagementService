package realestatemanagementservice.web.rest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import realestatemanagementservice.RealEstateManagementServiceApp;
import realestatemanagementservice.domain.*;
import realestatemanagementservice.repository.*;

/**
 * Integration tests for the {@link RentResource} REST controller.
 */
@SpringBootTest(classes = RealEstateManagementServiceApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ReportResourceIT {

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
	private PropertyTaxRepository propertyTaxRepository;
	
	@Autowired
	private JobTypeRepository jobTypeRepository;
	
	@Autowired
	private EntityManager em;

	@Autowired
	private MockMvc restRentMockMvc;

	@Test
	@Transactional
	public void getRentsPaid() throws Exception {

		// Dates around our criteria date included and excluded by the query criteria
		LocalDate criteriaDate = LocalDate.of(2020, 5, 10);
		LocalDate firstDayOfCriteriaDateMonth = criteriaDate.withDayOfMonth(1);
		LocalDate firstDayOfPriorMonth = firstDayOfCriteriaDateMonth.minusMonths(1);
		LocalDate firstDayOfFollowingMonth = firstDayOfCriteriaDateMonth.plusMonths(1);

		// This Rent paid from last month should not appear in the results
		Rent rentDueAndPaidPriorMonth = new Rent()
				.dueDate(firstDayOfPriorMonth)
				.receivedDate(firstDayOfPriorMonth)
				.amount(new BigDecimal("800.01"));

		// This Rent that has not been paid from last month should not appear in the
		// results
		Rent rentDueAndNotPaidPriorMonth = new Rent()
				.dueDate(firstDayOfPriorMonth)
				.receivedDate(null)
				.amount(new BigDecimal("825.03"));

		// This Rent that has not been paid from the criteria month should not appear in
		// the results
		Rent rentDueCriteriaMonthUnpaid = new Rent()
				.dueDate(firstDayOfCriteriaDateMonth)
				.receivedDate(null)
				.amount(new BigDecimal("1065.05"));

		// This Rent paid on time from the criteria month should appear in the results
		Rent rentDueCriteriaMonthOnTime = new Rent()
				.dueDate(firstDayOfCriteriaDateMonth)
				.receivedDate(firstDayOfCriteriaDateMonth)
				.amount(new BigDecimal("975.00"));

		// This Rent paid within the grace period from the criteria month should appear
		// in the results
		Rent rentDueCriteriaMonthWithinGracePeriod = new Rent()
				.dueDate(firstDayOfCriteriaDateMonth)
				.receivedDate(firstDayOfCriteriaDateMonth.plusDays(5))
				.amount(new BigDecimal("985.00"));

		// This Rent that has not been paid from the criteria month should not appear in
		// the results
		Rent rentDueFollowingMonthUnpaid = new Rent()
				.dueDate(firstDayOfFollowingMonth)
				.receivedDate(null)
				.amount(new BigDecimal("1165.07"));

		Set<Rent> entities = new HashSet<>();
		entities.add(rentDueAndPaidPriorMonth);
		entities.add(rentDueAndNotPaidPriorMonth);
		entities.add(rentDueCriteriaMonthUnpaid);
		entities.add(rentDueCriteriaMonthOnTime);
		entities.add(rentDueCriteriaMonthWithinGracePeriod);
		entities.add(rentDueFollowingMonthUnpaid);

		// Initialize the database
		rentRepository.saveAll(entities);
		rentRepository.flush();

		// Get list of rents paid
		restRentMockMvc.perform(get("/api/reports/rents/paid?date=" + criteriaDate.format(DateTimeFormatter.ISO_LOCAL_DATE)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", equalTo(rentDueCriteriaMonthOnTime.getId().intValue())))
				.andExpect(jsonPath("$[0].dueDate", is("2020-05-01")))
				.andExpect(jsonPath("$[0].receivedDate", is("2020-05-01")))
				.andExpect(jsonPath("$[0].amount", is(975.00))).andExpect(jsonPath("$[0].leaseId", nullValue()))
				.andExpect(jsonPath("$[1].id", equalTo(rentDueCriteriaMonthWithinGracePeriod.getId().intValue())))
				.andExpect(jsonPath("$[1].dueDate", is("2020-05-01")))
				.andExpect(jsonPath("$[1].receivedDate", is("2020-05-06")))
				.andExpect(jsonPath("$[1].amount", is(985.00))).andExpect(jsonPath("$[1].leaseId", nullValue()));
	}

	@Test
	@Transactional
	public void getBuildingAuthorizedVehicles() throws Exception {

		// Create test buildings to associate test apartments with
		Building includedBuilding = new Building();
		includedBuilding.setPropertyNumber("123");
		includedBuilding.setName("Included");

		Building excludedBuilding = new Building();
		excludedBuilding.setPropertyNumber("456");
		excludedBuilding.setName("Excluded");

		Set<Building> buildingEntities = new HashSet<>();
		buildingEntities.add(includedBuilding);
		buildingEntities.add(excludedBuilding);

		// Initialize buildings in the database
		buildingRepository.saveAll(buildingEntities);
		buildingRepository.flush();

		// Create test apartments to associate test leases with
		Apartment includedBuildingFirstApartment = new Apartment();
		includedBuildingFirstApartment.setBuilding(includedBuilding);
		includedBuildingFirstApartment.setUnitNumber("I1");

		Apartment includedBuildingSecondApartment = new Apartment();
		includedBuildingSecondApartment.setBuilding(includedBuilding);
		includedBuildingSecondApartment.setUnitNumber("I2");
		
		Apartment excludedBuildingApartment = new Apartment();
		excludedBuildingApartment.setBuilding(excludedBuilding);
		excludedBuildingApartment.setUnitNumber("E1");

		Set<Apartment> apartmentEntities = new HashSet<>();
		apartmentEntities.add(includedBuildingFirstApartment);
		apartmentEntities.add(includedBuildingSecondApartment);
		apartmentEntities.add(excludedBuildingApartment);

		// Initialize apartments in the database
		apartmentRepository.saveAll(apartmentEntities);
		apartmentRepository.flush();
		
		// Create test leases to associate test vehicles with
		final LocalDate today = LocalDate.now();
		final LocalDate yesterday = today.minusDays(1);
		
		final LocalDate oneYearAfterToday = today.plusYears(1);
		
		final LocalDate oneYearBeforeYesterday = yesterday.minusYears(1);
		final LocalDate oneYearAfterYesterday = yesterday.plusYears(1);

		Lease includedBuildingFirstApartmentExpiredLease = new Lease();
		includedBuildingFirstApartmentExpiredLease.setApartment(includedBuildingFirstApartment);
		includedBuildingFirstApartmentExpiredLease.dateSigned(oneYearBeforeYesterday);
		includedBuildingFirstApartmentExpiredLease.endDate(yesterday);

		Lease includedBuildingFirstApartmentCurrentLease = new Lease();
		includedBuildingFirstApartmentCurrentLease.setApartment(includedBuildingFirstApartment);
		includedBuildingFirstApartmentCurrentLease.dateSigned(today);
		includedBuildingFirstApartmentCurrentLease.endDate(oneYearAfterToday);
		
		Lease includedBuildingSecondApartmentCurrentLease = new Lease();
		includedBuildingSecondApartmentCurrentLease.setApartment(includedBuildingSecondApartment);
		includedBuildingSecondApartmentCurrentLease.dateSigned(yesterday);
		includedBuildingSecondApartmentCurrentLease.endDate(oneYearAfterYesterday);
		
		Lease excludedBuildingApartmentCurrentLease = new Lease();
		excludedBuildingApartmentCurrentLease.setApartment(excludedBuildingApartment);
		excludedBuildingApartmentCurrentLease.dateSigned(yesterday);
		excludedBuildingApartmentCurrentLease.endDate(oneYearAfterYesterday);
		
		Set<Lease> leaseEntities = new HashSet<>();
		leaseEntities.add(includedBuildingFirstApartmentExpiredLease);
		leaseEntities.add(includedBuildingFirstApartmentCurrentLease);
		leaseEntities.add(includedBuildingSecondApartmentCurrentLease);
		leaseEntities.add(excludedBuildingApartmentCurrentLease);

		// Initialize leases in the database
		leaseRepository.saveAll(leaseEntities);
		leaseRepository.flush();

		// Vehicle associated with an expired lease for the included building should be excluded from the report
		Vehicle includedBuildingFirstApartmentExpiredLeaseVehicle = new Vehicle();
		includedBuildingFirstApartmentExpiredLeaseVehicle.addLease(includedBuildingFirstApartmentExpiredLease);
		includedBuildingFirstApartmentExpiredLeaseVehicle.setLicensePlateNumber("includedBuildingFirstApartmentExpiredLeaseVehicle");

		// Vehicle associated with a current lease for the included building should be included in the report
		Vehicle includedBuildingFirstApartmentCurrentLeaseVehicle = new Vehicle();
		includedBuildingFirstApartmentCurrentLeaseVehicle.addLease(includedBuildingFirstApartmentCurrentLease);
		includedBuildingFirstApartmentCurrentLeaseVehicle.setLicensePlateNumber("includedBuildingFirstApartmentCurrentLeaseVehicle");

		// Vehicle associated with a current lease for the included building should be included in the report
		Vehicle includedBuildingSecondApartmentCurrentLeaseVehicle = new Vehicle();
		includedBuildingSecondApartmentCurrentLeaseVehicle.addLease(includedBuildingSecondApartmentCurrentLease);
		includedBuildingSecondApartmentCurrentLeaseVehicle.setLicensePlateNumber("includedBuildingSecondApartmentCurrentLeaseVehicle");
		
		// Vehicle associated with a current lease for an excluded building should be excluded from the report
		Vehicle excludedBuildingApartmentCurrentLeaseVehicle = new Vehicle();
		excludedBuildingApartmentCurrentLeaseVehicle.addLease(excludedBuildingApartmentCurrentLease);
		excludedBuildingApartmentCurrentLeaseVehicle.setLicensePlateNumber("excludedBuildingApartmentCurrentLeaseVehicle");
		
		Set<Vehicle> vehicleEntities = new HashSet<>();
		vehicleEntities.add(includedBuildingFirstApartmentExpiredLeaseVehicle);
		vehicleEntities.add(includedBuildingFirstApartmentCurrentLeaseVehicle);
		vehicleEntities.add(includedBuildingSecondApartmentCurrentLeaseVehicle);
		vehicleEntities.add(excludedBuildingApartmentCurrentLeaseVehicle);

		// Initialize vehicles in the database
		vehicleRepository.saveAll(vehicleEntities);
		vehicleRepository.flush();
		
		// Get list of authorized vehicles
		restRentMockMvc.perform(get("/api/reports/buildings/" + includedBuilding.getId() + "/vehicles/authorized"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", equalTo(includedBuildingFirstApartmentCurrentLeaseVehicle.getId().intValue())))
				.andExpect(jsonPath("$[1].id", equalTo(includedBuildingSecondApartmentCurrentLeaseVehicle.getId().intValue())));
	}
	
	@Test
	@Transactional
	public void getApartmentVehicles() throws Exception {

		// Create test apartments to associate test leases with
		Apartment includedApartment = new Apartment();
		includedApartment.setUnitNumber("I1");

		Apartment excludedApartment = new Apartment();
		excludedApartment.setUnitNumber("E1");

		Set<Apartment> apartmentEntities = new HashSet<>();
		apartmentEntities.add(includedApartment);
		apartmentEntities.add(excludedApartment);

		// Initialize apartments in the database
		apartmentRepository.saveAll(apartmentEntities);
		apartmentRepository.flush();
		
		// Create test leases to associate test vehicles with
		final LocalDate today = LocalDate.now();
		final LocalDate yesterday = today.minusDays(1);
		
		final LocalDate oneYearAfterToday = today.plusYears(1);
		
		final LocalDate oneYearBeforeYesterday = yesterday.minusYears(1);
		final LocalDate oneYearAfterYesterday = yesterday.plusYears(1);

		Lease includedApartmentExpiredLease = new Lease();
		includedApartmentExpiredLease.setApartment(includedApartment);
		includedApartmentExpiredLease.dateSigned(oneYearBeforeYesterday);
		includedApartmentExpiredLease.endDate(yesterday);

		Lease includedApartmentCurrentLease = new Lease();
		includedApartmentCurrentLease.setApartment(includedApartment);
		includedApartmentCurrentLease.dateSigned(today);
		includedApartmentCurrentLease.endDate(oneYearAfterToday);
		
		Lease excludedApartmentCurrentLease = new Lease();
		excludedApartmentCurrentLease.setApartment(excludedApartment);
		excludedApartmentCurrentLease.dateSigned(yesterday);
		excludedApartmentCurrentLease.endDate(oneYearAfterYesterday);
		
		Set<Lease> leaseEntities = new HashSet<>();
		leaseEntities.add(includedApartmentExpiredLease);
		leaseEntities.add(includedApartmentCurrentLease);
		leaseEntities.add(excludedApartmentCurrentLease);

		// Initialize leases in the database
		leaseRepository.saveAll(leaseEntities);
		leaseRepository.flush();

		// Vehicle associated with an expired lease for the included building should be excluded from the report
		Vehicle includedApartmentExpiredLeaseVehicle = new Vehicle();
		includedApartmentExpiredLeaseVehicle.addLease(includedApartmentExpiredLease);
		includedApartmentExpiredLeaseVehicle.setLicensePlateNumber("includedApartmentExpiredLeaseVehicle");

		// Vehicle associated with a current lease for the included building should be included in the report
		Vehicle includedApartmentCurrentLeaseVehicle = new Vehicle();
		includedApartmentCurrentLeaseVehicle.addLease(includedApartmentCurrentLease);
		includedApartmentCurrentLeaseVehicle.setLicensePlateNumber("includedApartmentCurrentLeaseVehicle");
		
		// Vehicle associated with a current lease for an excluded building should be excluded from the report
		Vehicle excludedApartmentCurrentLeaseVehicle = new Vehicle();
		excludedApartmentCurrentLeaseVehicle.addLease(excludedApartmentCurrentLease);
		excludedApartmentCurrentLeaseVehicle.setLicensePlateNumber("excludedApartmentCurrentLeaseVehicle");
		
		Set<Vehicle> vehicleEntities = new HashSet<>();
		vehicleEntities.add(includedApartmentExpiredLeaseVehicle);
		vehicleEntities.add(includedApartmentCurrentLeaseVehicle);
		vehicleEntities.add(excludedApartmentCurrentLeaseVehicle);

		// Initialize vehicles in the database
		vehicleRepository.saveAll(vehicleEntities);
		vehicleRepository.flush();
		
		// Get list of authorized vehicles
		restRentMockMvc.perform(get("/api/reports/apartments/" + includedApartment.getId() + "/vehicles"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id", equalTo(includedApartmentCurrentLeaseVehicle.getId().intValue())));
	}
	
	@Test
	@Transactional
	public void getApartmentMaintenanceHistory() throws Exception {
		
		//Create test Apartments to associate repairs with
		Apartment includedApartment = new Apartment();
		includedApartment.setUnitNumber("V12");
		
		Apartment excludedApartment = new Apartment();
		excludedApartment.setUnitNumber("I45");
		
		Set<Apartment> apartments = new HashSet<Apartment>();
		apartments.add(includedApartment);
		apartments.add(excludedApartment);
		
		apartmentRepository.saveAll(apartments);
		apartmentRepository.flush();
		
		//Repairs associated with a valid unit number
		Maintenance firstValidMaintenance = new Maintenance();
		firstValidMaintenance.setApartment(includedApartment);
		firstValidMaintenance.setDescription("Valid Maintenance #1");
		Maintenance secondValidMaintenance = new Maintenance();
		secondValidMaintenance.setApartment(includedApartment);
		secondValidMaintenance.setDescription("Valid Maintenance #2");
		
		//Repairs associated with an invalid unit number
		Maintenance firstInvalidMaintenance = new Maintenance();
		firstInvalidMaintenance.setDescription("Invalid Maintenance #1");
		firstInvalidMaintenance.setApartment(excludedApartment);
		Maintenance secondInvalidMaintenance = new Maintenance();
		secondInvalidMaintenance.setDescription("Invalid Maintenance #2");
		secondInvalidMaintenance.setApartment(excludedApartment);
		
		Set<Maintenance> validRepairs = new HashSet<Maintenance>();
		validRepairs.add(firstValidMaintenance);
		validRepairs.add(secondValidMaintenance);
		
		Set<Maintenance> invalidRepairs = new HashSet<Maintenance>();
		invalidRepairs.add(firstInvalidMaintenance);
		invalidRepairs.add(secondInvalidMaintenance);
		
		includedApartment.setMaintenances(validRepairs);
		excludedApartment.setMaintenances(invalidRepairs);
		
		maintenanceRepository.saveAll(validRepairs);
		maintenanceRepository.saveAll(invalidRepairs);
		maintenanceRepository.flush();
		
		restRentMockMvc.perform(get("/api/reports/apartments/" + includedApartment.getId() + "/maintenance/history"))
			.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].id", equalTo(firstValidMaintenance.getId().intValue())))
			.andExpect(jsonPath("$[1].id", equalTo(secondValidMaintenance.getId().intValue())));
	}
	
	@Test
	@Transactional
	public void getMaintenaceByContractor() throws Exception {
		
		//Create test Contractors to associate repairs with
		Contractor includedContractor = new Contractor();
		includedContractor.setCompanyName("Include");
		
		Contractor excludedContractor = new Contractor();
		excludedContractor.setCompanyName("Exclude");
		
		Set<Contractor> contractors = new HashSet<Contractor>();
		contractors.add(includedContractor);
		contractors.add(excludedContractor);
		
		contractorRepository.saveAll(contractors);
		contractorRepository.flush();
		
		//Repairs associated with a valid contractor
		Maintenance firstValidMaintenance = new Maintenance();
		firstValidMaintenance.setContractor(includedContractor);
		firstValidMaintenance.setDescription("Valid Maintenance #1");
		Maintenance secondValidMaintenance = new Maintenance();
		secondValidMaintenance.setContractor(includedContractor);
		secondValidMaintenance.setDescription("Valid Maintenance #2");
		
		//Repairs associated with an invalid invalid
		Maintenance firstInvalidMaintenance = new Maintenance();
		firstInvalidMaintenance.setDescription("Invalid Maintenance #1");
		firstInvalidMaintenance.setContractor(excludedContractor);
		Maintenance secondInvalidMaintenance = new Maintenance();
		secondInvalidMaintenance.setDescription("Invalid Maintenance #2");
		secondInvalidMaintenance.setContractor(excludedContractor);
		
		Set<Maintenance> validRepairs = new HashSet<Maintenance>();
		validRepairs.add(firstValidMaintenance);
		validRepairs.add(secondValidMaintenance);
		
		Set<Maintenance> invalidRepairs = new HashSet<Maintenance>();
		invalidRepairs.add(firstInvalidMaintenance);
		invalidRepairs.add(secondInvalidMaintenance);
		
		includedContractor.setMaintenances(validRepairs);
		excludedContractor.setMaintenances(invalidRepairs);
		
		maintenanceRepository.saveAll(validRepairs);
		maintenanceRepository.saveAll(invalidRepairs);
		maintenanceRepository.flush();
		
		restRentMockMvc.perform(get("/api/reports/contractors/" + includedContractor.getId() + "/maintenance/history"))
			.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].id", equalTo(firstValidMaintenance.getId().intValue())))
			.andExpect(jsonPath("$[1].id", equalTo(secondValidMaintenance.getId().intValue())));
	}
	
	@Test
	@Transactional
	public void getOpenMaintenace() throws Exception {
		
		final LocalDate today = LocalDate.now();
		final LocalDate yesterday = today.minusDays(1);
		
		//Repairs associated with a valid ReceiptOfPayment
		Maintenance firstValidMaintenance = new Maintenance();
		firstValidMaintenance.setDescription("Valid Maintenance #1");
		firstValidMaintenance.setReceiptOfPayment("PaidInFull1");
		Maintenance secondValidMaintenance = new Maintenance();
		secondValidMaintenance.setDescription("Valid Maintenance #2");
		secondValidMaintenance.setReceiptOfPayment("PaidInFull2");;
		
		//Repairs not associated with ReceiptOfPayment
		Maintenance firstInvalidMaintenance = new Maintenance();
		firstInvalidMaintenance.setDescription("Invalid Maintenance #1");
		firstInvalidMaintenance.setRepairPaidOn(today);;
		Maintenance secondInvalidMaintenance = new Maintenance();
		secondInvalidMaintenance.setDescription("Invalid Maintenance #2");
		secondInvalidMaintenance.setDateComplete(yesterday);;
		
		Set<Maintenance> validRepairs = new HashSet<Maintenance>();
		validRepairs.add(firstValidMaintenance);
		validRepairs.add(secondValidMaintenance);
		
		Set<Maintenance> invalidRepairs = new HashSet<Maintenance>();
		invalidRepairs.add(firstInvalidMaintenance);
		invalidRepairs.add(secondInvalidMaintenance);
		
		maintenanceRepository.saveAll(validRepairs);
		maintenanceRepository.saveAll(invalidRepairs);
		maintenanceRepository.flush();
		
		restRentMockMvc.perform(get("/api/reports/maintenance/open"))
			.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].id", equalTo(firstValidMaintenance.getId().intValue())))
			.andExpect(jsonPath("$[1].id", equalTo(secondValidMaintenance.getId().intValue())));

	}
	
	@Test
	@Transactional
	public void getInfractionsByYear() throws Exception {
		
		final LocalDate today = LocalDate.now();
		final LocalDate lastMont = today.minusMonths(1);
		final LocalDate lastYear = today.minusYears(1);
		final LocalDate nextYear = today.plusYears(1);
		
		final int thisYear = today.getYear();
		
		//Infractions that occurred this year
		Infraction firstValidInfraction = new Infraction();
		firstValidInfraction.setResolution("ValidResolution1");
		firstValidInfraction.setDateOccurred(lastMont);
		Infraction secondValidInfraction = new Infraction();
		secondValidInfraction.setResolution("ValidResolution1");
		secondValidInfraction.setDateOccurred(today);
		
		//Infractions that did not occurre this year
		Infraction firstInvalidInfraction = new Infraction();
		firstInvalidInfraction.setResolution("InvalidResolutionLastyear");
		firstInvalidInfraction.setDateOccurred(lastYear);
		Infraction secondInvalidInfraction = new Infraction();
		secondInvalidInfraction.setResolution("InvalidResolutionNextYear");
		secondInvalidInfraction.setDateOccurred(nextYear);
		
		
		Set<Infraction> validInfractions = new HashSet<Infraction>();
		validInfractions.add(firstValidInfraction);
		validInfractions.add(secondValidInfraction);
		
		Set<Infraction> invalidInfractions = new HashSet<Infraction>();
		invalidInfractions.add(firstInvalidInfraction);
		invalidInfractions.add(secondInvalidInfraction);
		
		infractionRepository.saveAll(validInfractions);
		infractionRepository.saveAll(invalidInfractions);
		infractionRepository.flush();
		
		restRentMockMvc.perform(get("/api/reports/infractions?year=" + thisYear))
			.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].id", equalTo(firstValidInfraction.getId().intValue())))
			.andExpect(jsonPath("$[1].id", equalTo(secondValidInfraction.getId().intValue())));
	}
	
	@Test
	@Transactional
	public void getAvailableApartments() throws Exception {
		
		//Creating test buildings to attach apartments to
		Building allAvailableBuilding = new Building();
		allAvailableBuilding.setName("All Available");
		
		Building someAvailableBuilding = new Building();
		someAvailableBuilding.setName("Some Available");
		
		Building nonAvailableBuilding = new Building();
		nonAvailableBuilding.setName("No Available");
		
		Set<Building> buildingEntities = new HashSet<>();
		buildingEntities.add(allAvailableBuilding);
		buildingEntities.add(someAvailableBuilding);
		buildingEntities.add(nonAvailableBuilding);

		// Initialize buildings in the database
		buildingRepository.saveAll(buildingEntities);
		buildingRepository.flush();
		
		//Create test Apartments
		Apartment allAvailableBuildingIncludedApartment1 = new Apartment();
		allAvailableBuildingIncludedApartment1.setUnitNumber("V1");
		allAvailableBuildingIncludedApartment1.setMoveInReady(true);
		allAvailableBuildingIncludedApartment1.setBuilding(allAvailableBuilding);
		
		Apartment allAvailableBuildingIncludedApartment2 = new Apartment();
		allAvailableBuildingIncludedApartment2.setUnitNumber("V2");
		allAvailableBuildingIncludedApartment2.setMoveInReady(true);
		allAvailableBuildingIncludedApartment2.setBuilding(allAvailableBuilding);

		Apartment someAvailableBuildingIncludedApartment3 = new Apartment();
		someAvailableBuildingIncludedApartment3.setUnitNumber("V3");
		someAvailableBuildingIncludedApartment3.setMoveInReady(true);
		someAvailableBuildingIncludedApartment3.setBuilding(someAvailableBuilding);;
		
		Apartment someAvailableBuildingExcludedApartment1 = new Apartment();
		someAvailableBuildingExcludedApartment1.setUnitNumber("I1");
		someAvailableBuildingExcludedApartment1.setMoveInReady(false);
		someAvailableBuildingExcludedApartment1.setBuilding(someAvailableBuilding);
		
		//Apartment not attached to any building
		Apartment noBuildingExcludedApartment2 = new Apartment();
		noBuildingExcludedApartment2.setUnitNumber("I2");
		noBuildingExcludedApartment2.setMoveInReady(true);
		
		Set<Apartment> includedApartments = new HashSet<Apartment>();
		includedApartments.add(allAvailableBuildingIncludedApartment1);
		includedApartments.add(allAvailableBuildingIncludedApartment2);
		includedApartments.add(someAvailableBuildingIncludedApartment3);
		
		Set<Apartment> excludedApartments = new HashSet<Apartment>();
		excludedApartments.add(someAvailableBuildingExcludedApartment1);
		excludedApartments.add(noBuildingExcludedApartment2);
		
		apartmentRepository.saveAll(includedApartments);
		apartmentRepository.saveAll(excludedApartments);
		apartmentRepository.flush();
		
		//Get list of available apartments
		restRentMockMvc.perform(get("/api/reports/apartments/available"))
			.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(jsonPath("$[0].id", equalTo(allAvailableBuildingIncludedApartment1.getId().intValue())))
			.andExpect(jsonPath("$[1].id", equalTo(allAvailableBuildingIncludedApartment2.getId().intValue())))
			.andExpect(jsonPath("$[2].id", equalTo(someAvailableBuildingIncludedApartment3.getId().intValue())));
	}
	
	@Test
	@Transactional
	public void getEmails() throws Exception {
		
		// Create test leases to associate test people with
		final LocalDate today = LocalDate.now();
		final LocalDate yesterday = today.minusDays(1);
		
		final LocalDate oneYearAfterToday = today.plusYears(1);
		
		final LocalDate oneYearBeforeYesterday = yesterday.minusYears(1);
		final LocalDate oneYearAfterYesterday = yesterday.plusYears(1);

		Lease excludedExpiredLease = new Lease();
		excludedExpiredLease.dateSigned(oneYearBeforeYesterday);
		excludedExpiredLease.endDate(yesterday);

		Lease includedCurrentLease = new Lease();
		includedCurrentLease.dateSigned(today);
		includedCurrentLease.endDate(oneYearAfterToday);
		
		Lease includedSecondCurrentLease = new Lease();
		includedSecondCurrentLease.dateSigned(yesterday);
		includedSecondCurrentLease.endDate(oneYearAfterYesterday);
		
		Set<Lease> leaseEntities = new HashSet<>();
		leaseEntities.add(excludedExpiredLease);
		leaseEntities.add(includedCurrentLease);
		leaseEntities.add(includedSecondCurrentLease);

		// Initialize leases in the database
		leaseRepository.saveAll(leaseEntities);
		leaseRepository.flush();

		// Person associated with an expired lease should be excluded from the report
		Person excludedExpiredLeasePerson = new Person();
		excludedExpiredLeasePerson.addLease(excludedExpiredLease);
		excludedExpiredLeasePerson.setEmailAddress("ExpiredLease@Bademail");
		excludedExpiredLeasePerson.setIsMinor(false);

		// Person associated with a current lease should be included in the report
		Person includedCurrentLeasePerson1 = new Person();
		includedCurrentLeasePerson1.addLease(includedCurrentLease);
		includedCurrentLeasePerson1.setEmailAddress("GoodLease@Goodemail");
		includedCurrentLeasePerson1.setIsMinor(false);
		
		// Second Person associated with a current lease should be included in the report
		Person includedCurrentLeasePerson2 = new Person();
		includedCurrentLeasePerson2.addLease(includedCurrentLease);
		includedCurrentLeasePerson2.setEmailAddress("GoodLease@SecondPersonOnLease");
		includedCurrentLeasePerson2.setIsMinor(false);


		// Person associated with a current lease but flagged as minor not be included
		Person excludedCurrentLeasePersonMinor = new Person();
		excludedCurrentLeasePersonMinor.addLease(includedCurrentLease);
		excludedCurrentLeasePersonMinor.setEmailAddress("ExcludeGoodEmail@BadMinor");
		excludedCurrentLeasePersonMinor.setIsMinor(true);
		
		// Person associated with the second current lease should be included in the report
		Person includedSecondCurrentLeasePerson = new Person();
		includedSecondCurrentLeasePerson.addLease(includedSecondCurrentLease);
		includedSecondCurrentLeasePerson.setEmailAddress("SecondGoodLease@Goodemail");
		includedSecondCurrentLeasePerson.setIsMinor(false);

		
		// Person associated with the second current lease without an email and to be excluded
		Person excludedSecondCurrentLeasePerson2 = new Person();
		excludedSecondCurrentLeasePerson2.addLease(includedSecondCurrentLease);
		excludedSecondCurrentLeasePerson2.setIsMinor(false);
		
		// Person not associated with a lease to be excluded
		Person excludedNoLeasePerson = new Person();
		excludedNoLeasePerson.setEmailAddress("NoLease@BadEmail");
		excludedNoLeasePerson.setIsMinor(false);

		Set<Person> personEntities = new HashSet<>();
		personEntities.add(excludedExpiredLeasePerson);
		personEntities.add(includedCurrentLeasePerson1);
		personEntities.add(includedCurrentLeasePerson2);
		personEntities.add(excludedCurrentLeasePersonMinor);
		personEntities.add(includedSecondCurrentLeasePerson);
		personEntities.add(excludedSecondCurrentLeasePerson2);
		personEntities.add(excludedNoLeasePerson);

		// Initialize Person in the database
		personRepository.saveAll(personEntities);
		personRepository.flush();
		
		// Get list of Emails
		restRentMockMvc.perform(get("/api/reports/people/email"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].emailAddress", equalTo(includedCurrentLeasePerson1.getEmailAddress())))
				.andExpect(jsonPath("$[1].emailAddress", equalTo(includedCurrentLeasePerson2.getEmailAddress())))
				.andExpect(jsonPath("$[2].emailAddress", equalTo(includedSecondCurrentLeasePerson.getEmailAddress())));
	}
	
	@Test
	@Transactional
	public void getContact() throws Exception {
		
		// Create test leases to associate test people with
		final LocalDate today = LocalDate.now();
		final LocalDate yesterday = today.minusDays(1);
		
		final LocalDate oneYearAfterToday = today.plusYears(1);
		
		final LocalDate oneYearBeforeYesterday = yesterday.minusYears(1);
		final LocalDate oneYearAfterYesterday = yesterday.plusYears(1);

		Lease excludedExpiredLease = new Lease();
		excludedExpiredLease.dateSigned(oneYearBeforeYesterday);
		excludedExpiredLease.endDate(yesterday);

		Lease includedCurrentLease = new Lease();
		includedCurrentLease.dateSigned(today);
		includedCurrentLease.endDate(oneYearAfterToday);
		
		Lease includedSecondCurrentLease = new Lease();
		includedSecondCurrentLease.dateSigned(yesterday);
		includedSecondCurrentLease.endDate(oneYearAfterYesterday);
		
		Set<Lease> leaseEntities = new HashSet<>();
		leaseEntities.add(excludedExpiredLease);
		leaseEntities.add(includedCurrentLease);
		leaseEntities.add(includedSecondCurrentLease);

		// Initialize leases in the database
		leaseRepository.saveAll(leaseEntities);
		leaseRepository.flush();

		// Person associated with an expired lease should be excluded from the report
		Person excludedExpiredLeasePerson = new Person();
		excludedExpiredLeasePerson.addLease(excludedExpiredLease);
		excludedExpiredLeasePerson.setEmailAddress("ExpiredLease@Bademail");

		// Person associated with a current lease for the included building should be included in the report
		Person includedCurrentLeasePerson1 = new Person();
		includedCurrentLeasePerson1.addLease(includedCurrentLease);
		includedCurrentLeasePerson1.setEmailAddress("GoodLease@Goodemail");
		
		// Second Person associated with a current lease for the included building should be included in the report
		Person includedCurrentLeasePerson2 = new Person();
		includedCurrentLeasePerson2.addLease(includedCurrentLease);
		includedCurrentLeasePerson2.setEmailAddress("GoodLease@SecondPersonOnLease");
		
		// Person associated with the second current lease should be included in the report
		Person includedSecondCurrentLeasePerson = new Person();
		includedSecondCurrentLeasePerson.addLease(includedSecondCurrentLease);
		includedSecondCurrentLeasePerson.setEmailAddress("SecondGoodLease@Goodemail");
		
		// Person not associated with a lease to be excluded
		Person excludedNoLeasePerson = new Person();
		excludedNoLeasePerson.setEmailAddress("NoLease@BadEmail");

		Set<Person> personEntities = new HashSet<>();
		personEntities.add(excludedExpiredLeasePerson);
		personEntities.add(includedCurrentLeasePerson1);
		personEntities.add(includedCurrentLeasePerson2);
		personEntities.add(includedSecondCurrentLeasePerson);;
		personEntities.add(excludedNoLeasePerson);

		// Initialize Person in the database
		personRepository.saveAll(personEntities);
		personRepository.flush();
		
		// Get list of Contact information
		restRentMockMvc.perform(get("/api/reports/people/contact"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].id", equalTo(includedCurrentLeasePerson1.getId().intValue())))
				.andExpect(jsonPath("$[1].id", equalTo(includedCurrentLeasePerson2.getId().intValue())))
				.andExpect(jsonPath("$[2].id", equalTo(includedSecondCurrentLeasePerson.getId().intValue())));
	}
	
	@Test
	@Transactional
	public void getPetsAndOwners() throws Exception {
		
		/* test groupings
		 * person and pet on expired lease
		 * person and pet on both an expired and current lease
		 * person with out pet on current lease
		 * multiple people and pet on current lease
		 * person with multiple pets on current lease
		 */
		
		// Create test leases to associate test people with
		final LocalDate today = LocalDate.now();
		final LocalDate yesterday = today.minusDays(1);
		
		final LocalDate oneYearAfterToday = today.plusYears(1);
		
		final LocalDate oneYearBeforeYesterday = yesterday.minusYears(1);
		final LocalDate oneYearAfterYesterday = yesterday.plusYears(1);

		Lease excludedExpiredLease = new Lease();
		excludedExpiredLease.dateSigned(oneYearBeforeYesterday);
		excludedExpiredLease.setEndDate(yesterday);
		
		Lease excludedSecondExpiredLease = new Lease();
		excludedSecondExpiredLease.dateSigned(oneYearBeforeYesterday);
		excludedSecondExpiredLease.setEndDate(yesterday);

		Lease excludedCurrentLease = new Lease();
		excludedCurrentLease.dateSigned(today);
		excludedCurrentLease.setEndDate(oneYearAfterToday);
		
		Lease includedCurrentLease = new Lease();
		includedCurrentLease.dateSigned(yesterday);
		includedCurrentLease.setEndDate(oneYearAfterYesterday);
		
		Lease includedSecondCurrentLease = new Lease();
		includedSecondCurrentLease.dateSigned(yesterday);
		includedSecondCurrentLease.setEndDate(oneYearAfterYesterday);
		
		Lease includedThirdCurrentLease = new Lease();
		includedThirdCurrentLease.dateSigned(yesterday);
		includedThirdCurrentLease.setEndDate(oneYearAfterYesterday);
		
		Set<Lease> leaseEntities = new HashSet<>();
		leaseEntities.add(excludedExpiredLease);
		leaseEntities.add(excludedSecondExpiredLease);
		leaseEntities.add(excludedCurrentLease);
		leaseEntities.add(includedCurrentLease);
		leaseEntities.add(includedSecondCurrentLease);
		leaseEntities.add(includedThirdCurrentLease);
		
		// Initialize leases in the database
		leaseRepository.saveAll(leaseEntities);
		leaseRepository.flush();

		Person excludedExpiredLeasePerson = new Person();
		excludedExpiredLeasePerson.addLease(excludedExpiredLease);
		excludedExpiredLeasePerson.setFirstName("Experied Lease");
		excludedExpiredLeasePerson.setPrimaryContact(true);
		
		Pet excludedExpiredLeasePet = new Pet();
		excludedExpiredLeasePet.addLease(excludedExpiredLease);
		excludedExpiredLeasePet.setName("Left Town");
		
		
		Person excludedNoPetPerson = new Person();
		excludedNoPetPerson.addLease(excludedCurrentLease);
		excludedNoPetPerson.setFirstName("No Pet Person");
		excludedNoPetPerson.setPrimaryContact(true);
		
		
		Person includedCurrentLeasePersonWithPet = new Person();
		includedCurrentLeasePersonWithPet.addLease(excludedSecondExpiredLease);
		includedCurrentLeasePersonWithPet.addLease(includedCurrentLease);
		includedCurrentLeasePersonWithPet.setFirstName("Both good and bad Lease Person");
		includedCurrentLeasePersonWithPet.setPrimaryContact(true);
		
		Pet includedCurrentLeasePetWithPerson = new Pet();
		includedCurrentLeasePetWithPerson.addLease(excludedSecondExpiredLease);
		includedCurrentLeasePetWithPerson.addLease(includedCurrentLease);
		includedCurrentLeasePetWithPerson.setName("Long Time Pet");
		
		
		Person includedCurrentLeaseMultiPeopleWithPet1 = new Person();
		includedCurrentLeaseMultiPeopleWithPet1.addLease(includedSecondCurrentLease);
		includedCurrentLeaseMultiPeopleWithPet1.setFirstName("Primary Contact With Pet");
		includedCurrentLeaseMultiPeopleWithPet1.setPrimaryContact(true);
		
		Person includedCurrentLeaseMultiPeopleWithPet2 = new Person();
		includedCurrentLeaseMultiPeopleWithPet2.addLease(includedSecondCurrentLease);
		includedCurrentLeaseMultiPeopleWithPet2.setFirstName("Not Primary Contact With Pet");
		
		Pet includedCurrentLeasePetWithMultiplePeople = new Pet();
		includedCurrentLeasePetWithMultiplePeople.addLease(includedSecondCurrentLease);
		includedCurrentLeasePetWithMultiplePeople.setName("Family Pet");
		
		
		Person includedCurrentLeasePeopleWithMultiPet = new Person();
		includedCurrentLeasePeopleWithMultiPet.addLease(includedThirdCurrentLease);
		includedCurrentLeasePeopleWithMultiPet.setFirstName("Primary Contact With Multiple Pets");
		includedCurrentLeasePeopleWithMultiPet.setPrimaryContact(true);
		
		Pet includedCurrentLeaseMultiPetWithPerson1 = new Pet();
		includedCurrentLeaseMultiPetWithPerson1.addLease(includedThirdCurrentLease);
		includedCurrentLeaseMultiPetWithPerson1.setName("Best Pet");
		
		Pet includedCurrentLeaseMultiPetWithPerson2 = new Pet();
		includedCurrentLeaseMultiPetWithPerson2.addLease(includedThirdCurrentLease);
		includedCurrentLeaseMultiPetWithPerson2.setName("Second Best Pet");
		
		// Get list of Owners with Pets
		Set<Person> personEntities = new HashSet<>();
		personEntities.add(excludedExpiredLeasePerson);
		personEntities.add(excludedNoPetPerson);
		personEntities.add(includedCurrentLeasePersonWithPet);
		personEntities.add(includedCurrentLeaseMultiPeopleWithPet1);
		personEntities.add(includedCurrentLeaseMultiPeopleWithPet2);
		personEntities.add(includedCurrentLeasePeopleWithMultiPet);
		
		personRepository.saveAll(personEntities);	
		
		Set<Pet> petEntities = new HashSet<>();
		petEntities.add(excludedExpiredLeasePet);
		petEntities.add(includedCurrentLeasePetWithPerson);
		petEntities.add(includedCurrentLeasePetWithMultiplePeople);
		petEntities.add(includedCurrentLeaseMultiPetWithPerson1);
		petEntities.add(includedCurrentLeaseMultiPetWithPerson2);
		
		petRepository.saveAll(petEntities);
		personRepository.flush();
		petRepository.flush();

		restRentMockMvc.perform(get("/api/reports/pet/owner"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[0].id", equalTo(includedCurrentLeasePetWithPerson.getId().intValue())))
				.andExpect(jsonPath("$[1].id", equalTo(includedCurrentLeasePetWithMultiplePeople.getId().intValue())))
				.andExpect(jsonPath("$[2].id", equalTo(includedCurrentLeaseMultiPetWithPerson1.getId().intValue())))
				.andExpect(jsonPath("$[3].id", equalTo(includedCurrentLeaseMultiPetWithPerson2.getId().intValue())));
	}
	
	@Test
	@Transactional
	public void getTaxHistory() throws Exception {
		
		//Creating test buildings to attach tax records to
		Building multiYearBuildingHistory = new Building();
		multiYearBuildingHistory.setPropertyNumber("MultiYear");
		
		Building oneYearBuildingHistory = new Building();
		oneYearBuildingHistory.setPropertyNumber("One Year");
		
		Building noYearBuildingHistory = new Building();
		noYearBuildingHistory.setPropertyNumber("No History");
		
		Set<Building> buildingEntities = new HashSet<>();
		buildingEntities.add(multiYearBuildingHistory);
		buildingEntities.add(oneYearBuildingHistory);
		buildingEntities.add(noYearBuildingHistory);

		// Initialize buildings in the database
		buildingRepository.saveAll(buildingEntities);
		buildingRepository.flush();
		
		//Create test tax histories
		PropertyTax multiYearHistoryBuildingTaxRecortd1 = new PropertyTax();
		multiYearHistoryBuildingTaxRecortd1.setBuilding(multiYearBuildingHistory);
		multiYearHistoryBuildingTaxRecortd1.setTaxYear(2000);
		multiYearHistoryBuildingTaxRecortd1.setConfirmationNumber("Confirmed 1");
		
		PropertyTax multiYearHistoryBuildingTaxRecortd2 = new PropertyTax();
		multiYearHistoryBuildingTaxRecortd2.setBuilding(multiYearBuildingHistory);
		multiYearHistoryBuildingTaxRecortd2.setTaxYear(2001);
		multiYearHistoryBuildingTaxRecortd2.setConfirmationNumber("Confirmed 2");
		
		PropertyTax oneYearBuildingHistoryRecord = new PropertyTax();
		oneYearBuildingHistoryRecord.setBuilding(oneYearBuildingHistory);
		oneYearBuildingHistoryRecord.setTaxYear(2001);
		oneYearBuildingHistoryRecord.setConfirmationNumber("Confirmed");
		
		//Tax History not attached to any building
		PropertyTax unattachedRecord = new PropertyTax();
		unattachedRecord.setTaxYear(2001);
		unattachedRecord.setConfirmationNumber("unConfirmed");
		
		Set<PropertyTax> taxHistory = new HashSet<PropertyTax>();
		taxHistory.add(multiYearHistoryBuildingTaxRecortd1);
		taxHistory.add(multiYearHistoryBuildingTaxRecortd2);
		taxHistory.add(oneYearBuildingHistoryRecord);
		
		propertyTaxRepository.saveAll(taxHistory);;
		apartmentRepository.flush();
		
		//Get list of tax history with tax number
		restRentMockMvc.perform(get("/api/reports/tax/property"))
			.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(jsonPath("$[0].id", equalTo(multiYearHistoryBuildingTaxRecortd1.getId().intValue())))
			.andExpect(jsonPath("$[1].id", equalTo(multiYearHistoryBuildingTaxRecortd2.getId().intValue())))
			.andExpect(jsonPath("$[2].id", equalTo(oneYearBuildingHistoryRecord.getId().intValue())));
	}
	@Test
	@Transactional
	public void getLeasesNextToExpire() throws Exception {
		
		int count = 3;
		
		// Create test leases
		final LocalDate today = LocalDate.now();
		final LocalDate yesterday = today.minusDays(1);
		final LocalDate tomorrow = today.plusDays(1);
		final LocalDate dayAfter = today.plusDays(2);
		final LocalDate tooMany = today.plusDays(3);
		
		/*Generated and inserted out of order should make the IDs 
		be in this order to test the sorting
		*/
		
		Lease excludeFourthCurrentLease = new Lease();
		excludeFourthCurrentLease.setDateSigned(yesterday);
		excludeFourthCurrentLease.setEndDate(tooMany);
		
		Lease includedthirdCurrentLease = new Lease();
		includedthirdCurrentLease.setDateSigned(yesterday);
		includedthirdCurrentLease.setEndDate(dayAfter);

		Lease includedCurrentLease = new Lease();
		includedCurrentLease.setDateSigned(yesterday);
		includedCurrentLease.setEndDate(today);
		
		Lease excludedExpiredLease = new Lease();
		excludedExpiredLease.setDateSigned(yesterday);
		excludedExpiredLease.setEndDate(yesterday);
		
		Lease includedSecondCurrentLease = new Lease();
		includedSecondCurrentLease.setDateSigned(yesterday);
		includedSecondCurrentLease.setEndDate(tomorrow);
		
		Set<Lease> leaseEntities = new HashSet<>();
		leaseEntities.add(excludeFourthCurrentLease);
		leaseEntities.add(includedthirdCurrentLease);
		leaseEntities.add(includedCurrentLease);
		leaseEntities.add(excludedExpiredLease);
		leaseEntities.add(includedSecondCurrentLease);
		
		// Initialize leases in the database
		leaseRepository.saveAll(leaseEntities);
		leaseRepository.flush();
		
		// Get list of Contact information
		restRentMockMvc.perform(get("/api/reports/lease/expire?count=" + count))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].id", equalTo(includedCurrentLease.getId().intValue())))
				.andExpect(jsonPath("$[1].id", equalTo(includedSecondCurrentLease.getId().intValue())))
				.andExpect(jsonPath("$[2].id", equalTo(includedthirdCurrentLease.getId().intValue())));
	}
	
	@Test
	@Transactional
	public void getContractorByJobType() throws Exception {
		
		//searched for
		JobType water = new JobType();
		
		//red hearing
		JobType heating = new JobType();
		
		//not used
		JobType flooring = new JobType();
		
		Set<JobType> jobs = new HashSet<JobType>();
		jobs.add(water);
		jobs.add(heating);
		jobs.add(flooring);
		
		jobTypeRepository.saveAll(jobs);
		jobTypeRepository.flush();
		
		//Create test Contractors to associate
		Contractor includedContractorOneJobType = new Contractor();
		includedContractorOneJobType.setCompanyName("IncludeOneJobType");
		includedContractorOneJobType.addJobType(water);
		
		Contractor includedContractorMultiJobType = new Contractor();
		includedContractorMultiJobType.setCompanyName("IncludeMultiJobType");
		includedContractorMultiJobType.addJobType(water);
		includedContractorMultiJobType.addJobType(heating);
		
		Contractor excludedContractorBadJobType = new Contractor();
		excludedContractorBadJobType.setCompanyName("ExcludeBadJobType");
		excludedContractorBadJobType.addJobType(heating);

		Contractor excludedContractorNoJobType = new Contractor();
		excludedContractorNoJobType.setCompanyName("ExcludeNoJobType");
		
		Set<Contractor> contractors = new HashSet<Contractor>();
		contractors.add(includedContractorOneJobType);
		contractors.add(includedContractorMultiJobType);
		contractors.add(excludedContractorBadJobType);
		contractors.add(excludedContractorNoJobType);
		
		contractorRepository.saveAll(contractors);
		contractorRepository.flush();
		
		restRentMockMvc.perform(get("/api/reports/contractor/jobtype?id=" + water.getId()))
			.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].id", equalTo(includedContractorOneJobType.getId().intValue())))
			.andExpect(jsonPath("$[1].id", equalTo(includedContractorMultiJobType.getId().intValue())));
	}

	public void getApartmentTenants() throws Exception {
		
		//Create test Apartments to link leases to
		Apartment includedApartment = new Apartment();
		includedApartment.setUnitNumber("Included Apartment");
		Apartment excludedApartment = new Apartment();
		excludedApartment.setUnitNumber("Excluded Apartment");
		
		Set<Apartment> apartments = new HashSet<Apartment>();
		apartments.add(includedApartment);
		apartments.add(excludedApartment);
		
		apartmentRepository.saveAll(apartments);
		apartmentRepository.flush();
		
		final LocalDate today = LocalDate.now();
		
		//Create test Leases to link People to
		Lease validLeaseValidApartment = new Lease();
		validLeaseValidApartment.setDateSigned(today.minusYears(1));
		validLeaseValidApartment.setEndDate(today.plusYears(1));
		Lease invalidLeaseValidApartment = new Lease();
		invalidLeaseValidApartment.setDateSigned(today.minusYears(2));
		invalidLeaseValidApartment.setEndDate(today.minusYears(1));
		
		Lease validLeaseInvalidApartment = new Lease();
		validLeaseInvalidApartment.setDateSigned(today.minusYears(1));
		validLeaseInvalidApartment.setEndDate(today.plusYears(1));
		Lease invalidLeaseInvalidApartment = new Lease();
		invalidLeaseInvalidApartment.setDateSigned(today.minusYears(2));
		invalidLeaseInvalidApartment.setEndDate(today.minusYears(1));
		
		includedApartment.addLease(validLeaseValidApartment);
		includedApartment.addLease(invalidLeaseValidApartment);
		excludedApartment.addLease(validLeaseInvalidApartment);
		excludedApartment.addLease(invalidLeaseInvalidApartment);
		
		Set<Lease> leases = new HashSet<Lease>();
		leases.add(validLeaseValidApartment);
		leases.add(invalidLeaseValidApartment);
		leases.add(validLeaseInvalidApartment);
		leases.add(invalidLeaseInvalidApartment);
		
		leaseRepository.saveAll(leases);
		leaseRepository.flush();
		
		//Create test People to link to Leases
		Person validPerson = new Person();
		validPerson.setFirstName("ValidPerson");
		Person invalidPerson = new Person();
		invalidPerson.setFirstName("InvalidPerson");
		
		validPerson.addLease(validLeaseValidApartment);
		invalidPerson.addLease(invalidLeaseInvalidApartment);
		invalidPerson.addLease(validLeaseInvalidApartment);
		invalidPerson.addLease(invalidLeaseValidApartment);
		
		Set<Person> people = new HashSet<Person>();
		people.add(validPerson);
		people.add(invalidPerson);
		
		personRepository.saveAll(people);
		personRepository.flush();
		
		restRentMockMvc.perform(get("/api/reports/apartments/"+ includedApartment.getId() +"/tenants"))
			.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].id", equalTo(validPerson.getId().intValue())));
	}
	
	@Test
	@Transactional
	public void getInfractionsByApartmentId() throws Exception {
		
		Apartment searchForApartment = new Apartment();
		
		Apartment notSearchForApartment = new Apartment();

		Apartment noLeasesApartment = new Apartment();

		Set<Apartment> apartments = new HashSet<Apartment>();
		apartments.add(searchForApartment);
		apartments.add(notSearchForApartment);
		apartments.add(noLeasesApartment);

		apartmentRepository.saveAll(apartments);
		apartmentRepository.flush();
		
		Lease searchApartLeaseNoInfractions = new Lease();
		searchApartLeaseNoInfractions.setApartment(searchForApartment);
		
		Lease searchApartLeaseOneInfraction = new Lease();
		searchApartLeaseOneInfraction.setApartment(searchForApartment);
		
		Lease searchApartLeaseMultiInfraction = new Lease();
		searchApartLeaseMultiInfraction.setApartment(searchForApartment);
		
		Lease notSearchApartLeaseNoInfractions = new Lease();
		notSearchApartLeaseNoInfractions.setApartment(notSearchForApartment);
		
		Lease notSearchApartLeaseOneInfraction = new Lease();
		notSearchApartLeaseOneInfraction.setApartment(notSearchForApartment);
		
		Lease notSearchApartLeaseMultiInfraction = new Lease();
		notSearchApartLeaseMultiInfraction.setApartment(notSearchForApartment);
		
		Lease noApartLeaseWithInfraction = new Lease();
		
		Set<Lease> leases = new HashSet<Lease>();
		leases.add(searchApartLeaseNoInfractions);
		leases.add(searchApartLeaseOneInfraction);
		leases.add(searchApartLeaseMultiInfraction);
		leases.add(notSearchApartLeaseNoInfractions);
		leases.add(notSearchApartLeaseOneInfraction);
		leases.add(notSearchApartLeaseMultiInfraction);
		leases.add(noApartLeaseWithInfraction);

		leaseRepository.saveAll(leases);
		leaseRepository.flush();
		
		//Infractions that are tied to the searched for apartment
		Infraction firstSearchApartLeaseOneInfraction = new Infraction();
		firstSearchApartLeaseOneInfraction.setLease(searchApartLeaseOneInfraction);
		
		Infraction firstSearchApartLeaseMultiInfraction = new Infraction();
		firstSearchApartLeaseMultiInfraction.setLease(searchApartLeaseMultiInfraction);
		
		Infraction secondSearchApartLeaseMultiInfraction = new Infraction();
		secondSearchApartLeaseMultiInfraction.setLease(searchApartLeaseMultiInfraction);
		
		//Infractions on a leases that are not tied to the searched for apartment
		Infraction firstNotSearchApartLeaseOneInfraction = new Infraction();
		firstNotSearchApartLeaseOneInfraction.setLease(notSearchApartLeaseOneInfraction);
		
		Infraction firstNotSearchApartLeaseMultiInfraction = new Infraction();
		firstNotSearchApartLeaseMultiInfraction.setLease(notSearchApartLeaseMultiInfraction);
		
		Infraction secondNotSearchApartLeaseMultiInfraction = new Infraction();
		secondNotSearchApartLeaseMultiInfraction.setLease(notSearchApartLeaseMultiInfraction);
		
		Infraction noApartInfraction = new Infraction();
		noApartInfraction.setLease(noApartLeaseWithInfraction);
		
		Set<Infraction> infractions = new HashSet<Infraction>();
		infractions.add(firstSearchApartLeaseOneInfraction);
		infractions.add(firstSearchApartLeaseMultiInfraction);
		infractions.add(secondSearchApartLeaseMultiInfraction);
		infractions.add(firstNotSearchApartLeaseOneInfraction);
		infractions.add(firstNotSearchApartLeaseMultiInfraction);
		infractions.add(secondNotSearchApartLeaseMultiInfraction);
		infractions.add(noApartInfraction);
		
		infractionRepository.saveAll(infractions);
		infractionRepository.flush();
		
		restRentMockMvc.perform(get("/api/reports/apartment/" + searchForApartment.getId() + "/infractions"))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(jsonPath("$[0].id", equalTo(firstSearchApartLeaseOneInfraction.getId().intValue())))
			.andExpect(jsonPath("$[1].id", equalTo(firstSearchApartLeaseMultiInfraction.getId().intValue())))
			.andExpect(jsonPath("$[2].id", equalTo(secondSearchApartLeaseMultiInfraction.getId().intValue())));
	}
	
	@Test
	@Transactional
	public void getDelinquencies() throws Exception {
		
		LocalDate criteriaDate = LocalDate.of(2020, 5, 10);
		
		//A lease expiring well after
		Lease includedLease = new Lease();
		includedLease.setLeaseType("Included Lease");
		includedLease.setEndDate(criteriaDate.plusYears(1));
		
		//A lease that's only just ran out
		Lease excludedLease = new Lease();
		excludedLease.setLeaseType("Excluded Lease");
		excludedLease.setEndDate(criteriaDate.minusDays(4));
		
		Set<Lease> leases = new HashSet<Lease>();
		leases.add(includedLease);
		leases.add(excludedLease);
		
		leaseRepository.saveAll(leases);
		leaseRepository.flush();
		
		//Make it 5 days overdue
		Rent delinquentRentIncludedLease = new Rent();
		delinquentRentIncludedLease.setReceivedDate(null);
		delinquentRentIncludedLease.setDueDate(criteriaDate.minusDays(5));
		delinquentRentIncludedLease.setLease(includedLease);
		
		//Same lease but a valid paid rent of the previous month
		Rent onTimeRentIncludedLease = new Rent();
		onTimeRentIncludedLease.setReceivedDate(criteriaDate.minusMonths(1));
		onTimeRentIncludedLease.setDueDate(criteriaDate.minusMonths(1));
		onTimeRentIncludedLease.setLease(includedLease);
		
		//Delinquent rent on the excluded rent to cover bases
		Rent delinquentRentExcludedLease = new Rent();
		delinquentRentExcludedLease.setReceivedDate(null);
		delinquentRentExcludedLease.setDueDate(criteriaDate.minusDays(6));
		delinquentRentExcludedLease.setLease(excludedLease);
		
		Set<Rent> rents = new HashSet<Rent>();
		rents.add(delinquentRentIncludedLease);
		rents.add(onTimeRentIncludedLease);
		rents.add(delinquentRentExcludedLease);
		
		System.out.println(rents);
		
		rentRepository.saveAll(rents);
		rentRepository.flush();
		
		restRentMockMvc.perform(get("/api/reports/rents/delinquencies?date=" + criteriaDate.format(DateTimeFormatter.ISO_LOCAL_DATE)))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(jsonPath("$", hasSize(1)))
		.andExpect(jsonPath("$[0].id", equalTo(includedLease.getId().intValue())));
	}
}
