package com.pearson.sam.bridgeapi.samclient;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pearson.sam.bridgeapi.samservices.IdamServices;

@RunWith(MockitoJUnitRunner.class)
public class IdamClientTest {

	@InjectMocks
	private IdamClient underTest = new IdamClient();

	@Mock
	private IdamServices idamServices;

	@Test
	public void testCallLogout() {
		Map<String, Object> rolesMap = new HashMap<>();
		when(idamServices.callLogout(any(String.class))).thenReturn(rolesMap);
		Map<String, Object> map = underTest.callLogout("token");
		assertNotNull(map);
	}

	@Test
	public void testValidateToken() {
		HttpStatus status = HttpStatus.ACCEPTED;
		ResponseEntity<Map<String, Object>> rolesMap = new ResponseEntity<>(status);

		when(idamServices.validateToken(any(String.class))).thenReturn(rolesMap);
		ResponseEntity<Map<String, Object>> map = underTest.validateToken("token");
		assertNotNull(map);
	}

}
