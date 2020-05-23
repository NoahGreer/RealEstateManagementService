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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import realestatemanagementservice.RealEstateManagementServiceApp;
import realestatemanagementservice.domain.Rent;
import realestatemanagementservice.repository.RentRepository;

/**
 * Integration tests for the {@link RentResource} REST controller.
 */
@SpringBootTest(classes = RealEstateManagementServiceApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ReportResourceIT {
	
	@Autowired
    private RentRepository rentRepository;

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
                .recievedDate(firstDayOfPriorMonth)
                .amount(new BigDecimal("800.01"));
        
    	// This Rent that has not been paid from last month should not appear in the results
        Rent rentDueAndNotPaidPriorMonth = new Rent()
                .dueDate(firstDayOfPriorMonth)
                .recievedDate(null)
                .amount(new BigDecimal("825.03"));

        // This Rent that has not been paid from the criteria month should not appear in the results
        Rent rentDueCriteriaMonthUnpaid = new Rent()
                .dueDate(firstDayOfCriteriaDateMonth)
                .recievedDate(null)
                .amount(new BigDecimal("1065.05"));
        
        // This Rent paid on time from the criteria month should appear in the results
        Rent rentDueCriteriaMonthOnTime = new Rent()
                .dueDate(firstDayOfCriteriaDateMonth)
                .recievedDate(firstDayOfCriteriaDateMonth)
                .amount(new BigDecimal("975.00"));
        
        // This Rent paid within the grace period from the criteria month should appear in the results
        Rent rentDueCriteriaMonthWithinGracePeriod = new Rent()
                .dueDate(firstDayOfCriteriaDateMonth)
                .recievedDate(firstDayOfCriteriaDateMonth.plusDays(5))
                .amount(new BigDecimal("985.00"));
        
        // This Rent that has not been paid from the criteria month should not appear in the results
        Rent rentDueFollowingMonthUnpaid = new Rent()
                .dueDate(firstDayOfFollowingMonth)
                .recievedDate(null)
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
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", equalTo(rentDueCriteriaMonthOnTime.getId().intValue())))
            .andExpect(jsonPath("$[0].dueDate", is("2020-05-01")))
            .andExpect(jsonPath("$[0].recievedDate", is("2020-05-01")))
            .andExpect(jsonPath("$[0].amount", is(975.00)))
            .andExpect(jsonPath("$[0].leaseId", nullValue()))
            .andExpect(jsonPath("$[1].id", equalTo(rentDueCriteriaMonthWithinGracePeriod.getId().intValue())))
            .andExpect(jsonPath("$[1].dueDate", is("2020-05-01")))
            .andExpect(jsonPath("$[1].recievedDate", is("2020-05-06")))
            .andExpect(jsonPath("$[1].amount", is(985.00)))
            .andExpect(jsonPath("$[1].leaseId", nullValue()));
     }
}
