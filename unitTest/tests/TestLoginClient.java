package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.common.MessageCS;
import client.common.MessageCS.MessageType;
import client.controllers.loginPageController;

class TestLoginClient {

	loginPageController controller;

	@BeforeEach
	void setUp() throws Exception {
		controller = new loginPageController();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	// check input validation - number and size 9 
	@Test
	public void testNumberAndSize9() {
		String id = "315668418";
		boolean check = controller.ValidateNumber(id) && controller.ValidateSizeID(id);
		assertEquals(true, check);
	}
	
	// check input validation - string and size 9
	@Test
	public void testStringAndSize9() {
		String id = "AbcAbcAbc";
		boolean check = controller.ValidateNumber(id) && controller.ValidateSizeID(id);
		assertEquals(false, check);
	}
	
	// check input validation - number and size which is not 9
	@Test
	public void testNumberAndNotSize9() {
		String id = "0";
		boolean check = controller.ValidateNumber(id) && controller.ValidateSizeID(id);
		assertEquals(false, check);
	}
	
	// check input validation - string and size which is not 9
	@Test
	public void testStringAndNotSize9() {
		String id = "A";
		boolean check = controller.ValidateNumber(id) && controller.ValidateSizeID(id);
		assertEquals(false, check);
	}
	
	//test empty inputs
	@Test
	public void testEmpty() {
		String id = "";
		boolean check = controller.ValidateNumber(id) && controller.ValidateSizeID(id);
		assertEquals(false, check);
	}
	
}
