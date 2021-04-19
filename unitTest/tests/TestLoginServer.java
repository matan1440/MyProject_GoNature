package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.common.MessageCS;
import client.common.MessageCS.MessageType;
import client.logic.Subscriber;
import client.logic.Worker;
import server.common.EchoServer;
import server.database.mysqlConnection;

class TestLoginServer {
	EchoServer echoServer;
	MessageCS msg;

	@BeforeEach
	void setUp() throws Exception {
		echoServer = new EchoServer(0);
		mysqlConnection mysqlConnection = new mysqlConnection();
		echoServer.set_mysqlConnection(mysqlConnection);
		echoServer.setConn(mysqlConnection.connectToDB());
	}

	// test not Exist Worker
	@Test
	public void testNotExistWorker() {
		Worker worker = new Worker("169", "250");
		msg = new MessageCS(MessageType.LOGIN_WORKER, worker);
		echoServer.handleMessageFromClient(msg, null);
		msg = echoServer.getServer().getMessage();
		assertEquals("The id / username not exists", msg.getString());
	}

	// test Exist Worker With Password Wrong
	@Test
	public void testExistWorkerPasswordWrong() {
		Worker worker = new Worker("7", "150");
		msg = new MessageCS(MessageType.LOGIN_WORKER, worker);
		echoServer.handleMessageFromClient(msg, null);
		msg = echoServer.getServer().getMessage();
		assertEquals("Wrong Password - Try Again", msg.getString());
	}

	// test Exist Worker With Password Good
	@Test
	public void testWorkerSuccees() {
		Worker worker = new Worker("7", "127");
		msg = new MessageCS(MessageType.LOGIN_WORKER, worker);
		echoServer.handleMessageFromClient(msg, null);
		msg = echoServer.getServer().getMessage();
		
		Worker worker_check = new Worker();
		worker_check.setEmail("bolrer@gmail.com");
		worker_check.setFirstName("Tomer");
		worker_check.setLastName("B");
		worker_check.setJob("Service");
		worker_check.setParkName("A,B,C");
		worker_check.setPhone(527603210);
		worker_check.setID("7");
		worker_check.setPassword("127");
		assertTrue(worker_check.equals(msg.Worker));
		

	}

	// test New Visitor + Not Subscriber
	@Test
	public void testNewVisitorNotSubscriberSuccees() {
		Subscriber subscriber = new Subscriber(888888888);
		msg = new MessageCS(MessageType.LOGIN_VISITOR, subscriber);
		echoServer.handleMessageFromClient(msg, null);
		msg = echoServer.getServer().getMessage();
		assertEquals(MessageType.LOGIN_NEW_VISITOR, msg.messageType);
	}

	// test Not New Visitor + Not Subscriber
	@Test
	public void testNotNewVisitorNotSubscriberSuccees() {
		Subscriber subscriber = new Subscriber(315668418);
		msg = new MessageCS(MessageType.LOGIN_VISITOR, subscriber);
		echoServer.handleMessageFromClient(msg, null);
		msg = echoServer.getServer().getMessage();
		assertEquals(MessageType.LOGIN_OWNER_ORDER, msg.messageType);
	}

	// test New Visitor + Subscriber
	@Test
	public void testNewVisitorSubscriberSuccees() {
		Subscriber subscriber = new Subscriber(21238);
		msg = new MessageCS(MessageType.LOGIN_VISITOR, subscriber);
		echoServer.handleMessageFromClient(msg, null);
		msg = echoServer.getServer().getMessage();
		
		Subscriber subscriber_check = new Subscriber();
		subscriber_check.setSubscriber_id(21238);
		subscriber_check.setFirstname("Avi");
		subscriber_check.setLastname("Moshe");
		subscriber_check.setEmail("omerby12@gmail.com");
		subscriber_check.setSubscriber_amount(15);
		subscriber_check.setType("Guide");
		
		assertTrue(subscriber_check.equals(msg.subscriber));
	}

	// test Not New Visitor + Subscriber
	@Test
	public void testNotNewVisitorSubscriberSuccees() {
		Subscriber subscriber = new Subscriber(21230);
		msg = new MessageCS(MessageType.LOGIN_VISITOR, subscriber);
		echoServer.handleMessageFromClient(msg, null);
		msg = echoServer.getServer().getMessage();
		assertEquals(MessageType.LOGIN_SUBSCRIBER_WITH_ORDER, msg.messageType);
	}

	// test User Is Log On
	@Test
	public void testUserLogOn() {

		msg = new MessageCS(MessageType.LOGIN_CHECK, "999999999", false);
		echoServer.handleMessageFromClient(msg, null);

		msg = new MessageCS(MessageType.LOGIN_CHECK, "999999999", false);
		echoServer.handleMessageFromClient(msg, null);
		msg = echoServer.getServer().getMessage();
		assertEquals(true, msg.conn_check);
	}

	// test User Which Is Not Log On
	@Test
	public void testUserNotLogOn() {
		msg = new MessageCS(MessageType.LOGIN_CHECK, "315668418", false);
		echoServer.handleMessageFromClient(msg, null);
		msg = echoServer.getServer().getMessage();
		assertEquals(false, msg.conn_check);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

}
