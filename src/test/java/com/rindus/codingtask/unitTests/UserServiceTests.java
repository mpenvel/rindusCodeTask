package com.rindus.codingtask.unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rindus.codingtask.dto.AddressDTO;
import com.rindus.codingtask.dto.AlbumDTO;
import com.rindus.codingtask.dto.CompanyDTO;
import com.rindus.codingtask.dto.GeoDTO;
import com.rindus.codingtask.dto.UserDTO;
import com.rindus.codingtask.exceptions.UserException;
import com.rindus.codingtask.services.UserService;

@SpringBootTest(classes = { UserService.class })
class UserServiceTests {

	@InjectMocks
	private UserService userService;

	@MockBean
	private RestTemplate restTemplate;

	private AutoCloseable closeable;

	private List<UserDTO> usersDTO;

	@BeforeEach
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		usersDTO = new ArrayList<>();
		UserDTO userExpected = new UserDTO();
		userExpected.setId(Long.valueOf(1));
		userExpected.setName("Leanne Graham");
		userExpected.setUsername("Bret");
		userExpected.setEmail("Sincere@april.biz");
		userExpected.setPhone("1-770-736-8031 x56442");
		userExpected.setWebsite("hildegard.org");

		AddressDTO address = new AddressDTO();
		address.setCity("Gwenborough");
		address.setStreet("Kulas Light");
		address.setSuite("Apt. 556");
		address.setZipcode("92998-3874");
		GeoDTO geo = new GeoDTO();
		geo.setLat(-37.3159);
		geo.setLng(81.1496);
		address.setGeo(geo);
		userExpected.setAddress(address);

		CompanyDTO company = new CompanyDTO();
		company.setName("Romaguera-Crona");
		company.setCatchPhrase("Multi-layered client-server neural-net");
		company.setBs("harness real-time e-markets");
		userExpected.setCompany(company);

		UserDTO userExpected2 = new UserDTO();
		userExpected2.setId(Long.valueOf(2));
		userExpected2.setName("Leanne Bonham");
		userExpected2.setUsername("Bret");
		userExpected2.setEmail("Sincere@april.biz");
		userExpected2.setPhone("1-770-736-8031 x56442");
		userExpected2.setWebsite("hildegard.org");
		userExpected2.setAddress(address);
		userExpected2.setCompany(company);

		usersDTO.add(userExpected);
		usersDTO.add(userExpected2);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void whenGetUsersIsSuccessful() {
		ResponseEntity<List<UserDTO>> responseOk = new ResponseEntity<>(usersDTO, HttpStatus.OK);
		when(restTemplate.exchange(Mockito.anyString(), Mockito.any(), Mockito.any(),
				Mockito.any(ParameterizedTypeReference.class))).thenReturn(responseOk);

		assertEquals(usersDTO, userService.getUsers());
	}

	@Test
	public void whenGetUserIsSuccessful() throws UserException {
		ResponseEntity<UserDTO> responseOk = new ResponseEntity<>(usersDTO.get(0), HttpStatus.OK);
		when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(UserDTO.class))).thenReturn(responseOk);

		assertEquals(usersDTO.get(0), userService.getUser("1"));
	}

	@Test
	public void whenGetUserNotFound() throws UserException {
		when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(UserDTO.class)))
				.thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
		assertThrows(UserException.class, () -> {
			userService.getUser("12313");
		});
	}

	@Test
	public void whenCreateUserIsSuccessful() throws UserException {
		ResponseEntity<UserDTO> responseCreated = new ResponseEntity<>(HttpStatus.CREATED);
		when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.eq(UserDTO.class)))
				.thenReturn(responseCreated);
		userService.createUser(usersDTO.get(0));
		Mockito.verify(restTemplate).postForEntity(Mockito.anyString(), Mockito.any(), Mockito.eq(UserDTO.class));
	}

	@Test
	public void whenUpdateUserIsSuccesful() throws UserException {
		ResponseEntity<UserDTO> responseOk = new ResponseEntity<>(HttpStatus.OK);
		when(restTemplate.exchange(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.eq(UserDTO.class)))
				.thenReturn(responseOk);
		userService.updateUser("1", usersDTO.get(0));
		Mockito.verify(restTemplate).exchange(Mockito.anyString(), Mockito.any(), Mockito.any(),
				Mockito.eq(UserDTO.class));
	}

	@Test
	public void whenUpdateUserNotFound() throws UserException {
		when(restTemplate.exchange(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.eq(UserDTO.class)))
				.thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
		assertThrows(UserException.class, () -> {
			userService.updateUser("1", usersDTO.get(0));
		});
	}

	@Test
	public void whenPatchUserIsSuccessful() throws UserException {
		ResponseEntity<UserDTO> responseOk = new ResponseEntity<>(HttpStatus.OK);
		when(restTemplate.exchange(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.eq(UserDTO.class)))
				.thenReturn(responseOk);
		userService.patchUser("1", usersDTO.get(0));
		Mockito.verify(restTemplate).exchange(Mockito.anyString(), Mockito.any(), Mockito.any(),
				Mockito.eq(UserDTO.class));
	}

	@Test
	public void whenPatchUserIsNotFound() throws UserException {
		when(restTemplate.exchange(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.eq(UserDTO.class)))
				.thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
		assertThrows(UserException.class, () -> {
			userService.patchUser("1", usersDTO.get(0));
		});
	}
	
	@Test
	public void whenDeleteUserIsSuccesful() throws UserException {
		userService.deleteUser("1");
		Mockito.verify(restTemplate).delete(Mockito.anyString());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void whenGetUserAlbumsIsSuccessful() throws Exception {
		AlbumDTO album = new AlbumDTO();
		album.setId(new Long(1));
		album.setTitle("Album");
		List<AlbumDTO> albums = new ArrayList<>();
		albums.add(album);
		
		ResponseEntity<List<AlbumDTO>> responseOk = new ResponseEntity<>(albums, HttpStatus.OK);
		when(restTemplate.exchange(Mockito.anyString(), Mockito.any(), Mockito.any(),
				Mockito.any(ParameterizedTypeReference.class))).thenReturn(responseOk);
		assertEquals(albums, userService.getUserAlbums("1"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void whenGetUserAlbumsUserNotFound() throws Exception {
		AlbumDTO album = new AlbumDTO();
		album.setId(new Long(1));
		album.setTitle("Album");
		List<AlbumDTO> albums = new ArrayList<>();
		albums.add(album);
		when(restTemplate.exchange(Mockito.anyString(), Mockito.any(), Mockito.any(),
				Mockito.any(ParameterizedTypeReference.class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
		
		assertThrows(UserException.class, () -> {
			userService.getUserAlbums("12313");
		});
	}
	
	@AfterEach
	public void finish() throws Exception {
		closeable.close();
	}
}
