package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.common.MessageCS;
import client.common.MessageCS.MessageType;
import client.controllers.DepReportVisitingController;
import client.logic.Subscriber;
import server.common.EchoServer;
import server.database.mysqlConnection;

class TestVisitingReportsServer {
	EchoServer echoServer;
	MessageCS msg;
	String ans;
	
	@BeforeEach
	void setUp() throws Exception {
		echoServer = new EchoServer(0);
		mysqlConnection mysqlConnection = new mysqlConnection();
		echoServer.set_mysqlConnection(mysqlConnection);
		echoServer.setConn(mysqlConnection.connectToDB());
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	
	// test if the answer from the server for given input is equals to expected result
	@Test
	public void testServerAnsResultTrue() {
		String from = "2020-12-30";
		String to = "2020-12-30";
		String park = "C";
		ans = "Guide,11,11:30:00=Single,8,12:30:00=Single,12,12:30:00=Family,2,17:30:00=Family,13,20:30:00=Family,3,20:30:00=Single,5,10:30:00=Single,12,13:30:00=Single,11,18:30:00=Family,5,13:30:00=Single,9,18:30:00=Single,1,09:30:00=Guide,1,19:30:00=Guide,5,18:30:00=Guide,10,09:30:00=Single,14,11:30:00=Family,6,12:30:00=Guide,11,16:30:00=Single,10,10:30:00=Guide,8,12:30:00=Single,6,18:30:00=Single,13,17:30:00=Single,6,16:30:00=Family,6,12:30:00=";
		msg = new MessageCS(MessageType.REPORT_VISITING, from,to,park);
		echoServer.handleMessageFromClient(msg, null);
		msg = echoServer.getServer().getMessage();
		assertEquals(ans, msg.getString());
	}
	
	// test if the answer from the server for given input is not equals to expected result
	@Test
	public void testServerWrongValues() {
		String from = "2020-12-28";
		String to = "2020-12-30";
		String park = "C";
		ans = "Guide,11,11:30:00=Single,8,12:30:00=Single,12,12:30:00=Family,2,17:30:00=Family,13,20:30:00=Family,3,20:30:00=Single,5,10:30:00=Single,12,13:30:00=Single,11,18:30:00=Family,5,13:30:00=Single,9,18:30:00=Single,1,09:30:00=Guide,1,19:30:00=Guide,5,18:30:00=Guide,10,09:30:00=Single,14,11:30:00=Family,6,12:30:00=Guide,11,16:30:00=Single,10,10:30:00=Guide,8,12:30:00=Single,6,18:30:00=Single,13,17:30:00=Single,6,16:30:00=Family,6,12:30:00=";
		msg = new MessageCS(MessageType.REPORT_VISITING, from,to,park);
		echoServer.handleMessageFromClient(msg, null);
		msg = echoServer.getServer().getMessage();
		assertNotEquals(ans, msg.getString());
	}
	
}
