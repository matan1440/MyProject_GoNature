package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.common.MessageCS;
import client.common.MessageCS.MessageType;
import client.controllers.DepReportVisitingController;
import client.controllers.loginPageController;

class TestVisitingReportsClient {
	DepReportVisitingController controller;
	@BeforeEach
	void setUp() throws Exception {
		controller = new DepReportVisitingController();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	//check Validate Dates
	@Test
	public void testValidateDates() {
		LocalDate from = LocalDate.parse("2020-12-02");
		LocalDate to = LocalDate.parse("2020-12-22");
		boolean check = controller.Check(from) && controller.Check(to);
		assertEquals(true, check);
	}
	
	// check one empty date , and one not empty date
	@Test
	public void OneEmptyDate() {
		LocalDate from = LocalDate.parse("2020-12-02");
		boolean check = controller.Check(from) && controller.Check(null);
		assertEquals(false, check);
	}
	
	// check output when both inputs are null
	@Test
	public void EmptyDates() {
		boolean check = controller.Check(null) && controller.Check(null);
		assertEquals(false, check);
	}
	
	//check output when the date from is before the date to
	@Test
	public void FromBeforeToTrue() {
		String from = "2020-12-02";
		String to = "2020-12-22";
		boolean check = controller.CheckFromBeforeTo(from, to);
		assertEquals(true, check);
	}
	
	//check output when the date from is after the date to
	@Test
	public void FromBeforeToFalse() {
		String from = "2020-12-22";
		String to = "2020-12-02";
		boolean check = controller.CheckFromBeforeTo(from, to);
		assertEquals(false, check);
	}
	
	// test if arrays that we get from the server are equal
	@Test
	public void testArraysEquals() {
	
		
		String ans = "Guide,11,11:30:00=Single,8,12:30:00=Single,12,12:30:00=Family,2,17:30:00=Family,13,20:30:00=Family,3,20:30:00=Single,5,10:30:00=Single,12,13:30:00=Single,11,18:30:00=Family,5,13:30:00=Single,9,18:30:00=Single,1,09:30:00=Guide,1,19:30:00=Guide,5,18:30:00=Guide,10,09:30:00=Single,14,11:30:00=Family,6,12:30:00=Guide,11,16:30:00=Single,10,10:30:00=Guide,8,12:30:00=Single,6,18:30:00=Single,13,17:30:00=Single,6,16:30:00=Family,6,12:30:00=\r\n" + 
				"";

		
		ArrayList<Object> result = controller.getVisitingArray(ans);
		int[] hoursSingle = (int[])result.get(0);
		int[] hoursGuide = (int[])result.get(1);
		int[] hoursFamily = (int[])result.get(2);

		
		
		int[] hoursSingle_check = new int [] {1,15,14,20,12,0,0,6,13,26,0,0,0}; 
        int[] hoursGuide_check = new int [] {10,0,11,8,0,0,0,11,0,5,1,0,0}; 
        int[] hoursFamily_check = new int [] {0,0,0,12,5,0,0,0,2,0,0,16,0}; 		
        boolean checkEquals = Arrays.equals(hoursSingle, hoursSingle_check) &&
        		Arrays.equals(hoursGuide, hoursGuide_check) &&
        		Arrays.equals(hoursFamily, hoursFamily_check); 
		assertEquals(true, checkEquals);
	}
	
	// test if arrays that we get from the server are not equal
	@Test
	public void WrongValuesArraysEquals() {

		String ans = "Guide,11,11:30:00=Single,8,12:30:00=Single,12,12:30:00=Family,2,17:30:00=Family,13,20:30:00=Family,3,20:30:00=";
		
		ArrayList<Object> result = controller.getVisitingArray(ans);
		int[] hoursSingle = (int[])result.get(0);
		int[] hoursGuide = (int[])result.get(1);
		int[] hoursFamily = (int[])result.get(2);

		
		
		int[] hoursSingle_check = new int [] {5,15,14,20,12,0,0,6,13,26,0,0,0}; 
        int[] hoursGuide_check = new int [] {10,6,11,8,0,0,0,11,0,5,1,0,0}; 
        int[] hoursFamily_check = new int [] {0,0,7,12,5,0,0,0,2,0,0,16,0}; 		
        boolean checkEquals = Arrays.equals(hoursSingle, hoursSingle_check) &&
        		Arrays.equals(hoursGuide, hoursGuide_check) &&
        		Arrays.equals(hoursFamily, hoursFamily_check); 
		assertEquals(false, checkEquals);
	}
	
}
