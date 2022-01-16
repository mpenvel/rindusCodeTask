package com.rindus.codingtask.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rindus.codingtask.dto.AlbumDTO;
import com.rindus.codingtask.dto.UserDTO;
import com.rindus.codingtask.dto.UsersXML;
import com.rindus.codingtask.exceptions.UserException;
import com.rindus.codingtask.utils.Constants;

@Service
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private RestTemplate restTemplate;

	public List<UserDTO> getUsers() {
		ResponseEntity<List<UserDTO>> response = restTemplate.exchange(
				Constants.FAKE_API_URL + Constants.USERS_RESOURCE, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<UserDTO>>() {
				});

		return response.getBody();
	}

	public UserDTO getUser(String userId) throws UserException {
		try {
			ResponseEntity<UserDTO> response = restTemplate
					.getForEntity(Constants.FAKE_API_URL + Constants.USERS_RESOURCE + "/" + userId, UserDTO.class);
			return response.getBody();
		} catch (HttpClientErrorException e) {
			LOGGER.error(e.getMessage());
			throw new UserException(e.getStatusCode(), e.getMessage());
		}
	}

	public void createUser(UserDTO user) throws UserException {
		try {
			restTemplate.postForEntity(Constants.FAKE_API_URL + Constants.USERS_RESOURCE, user, UserDTO.class);
		} catch (HttpClientErrorException e) {
			LOGGER.error(e.getMessage());
			throw new UserException(e.getStatusCode(), e.getMessage());
		}
	}

	public void updateUser(String userId, UserDTO user) throws UserException {
		try {
			HttpEntity<UserDTO> requestUpdate = new HttpEntity<>(user, null);
			restTemplate.exchange(Constants.FAKE_API_URL + Constants.USERS_RESOURCE + "/" + userId, HttpMethod.PUT,
					requestUpdate, UserDTO.class);
		} catch (HttpClientErrorException e) {
			LOGGER.error(e.getMessage());
			throw new UserException(e.getStatusCode(), e.getMessage());
		}
	}

	public void patchUser(String userId, UserDTO user) throws UserException {
		try {
			HttpEntity<UserDTO> requestUpdate = new HttpEntity<>(user, null);
			restTemplate.exchange(Constants.FAKE_API_URL + Constants.USERS_RESOURCE + "/" + userId, HttpMethod.PATCH,
					requestUpdate, UserDTO.class);
		} catch (HttpClientErrorException e) {
			LOGGER.error(e.getMessage());
			throw new UserException(e.getStatusCode(), e.getMessage());
		}
	}

	public void deleteUser(String userId) throws UserException {
		try {
			restTemplate.delete(Constants.FAKE_API_URL + Constants.USERS_RESOURCE + "/" + userId);
		} catch (HttpClientErrorException e) {
			LOGGER.error(e.getMessage());
			throw new UserException(e.getStatusCode(), e.getMessage());
		}
	}

	public List<AlbumDTO> getUserAlbums(String userId) throws UserException {
		try {
			ResponseEntity<List<AlbumDTO>> response = restTemplate.exchange(
					Constants.FAKE_API_URL + Constants.USERS_RESOURCE + "/" + userId + "/" + Constants.ALBUMS_RESOURCE,
					HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumDTO>>() {
					});
			return response.getBody();
		} catch (HttpClientErrorException e) {
			LOGGER.error(e.getMessage());
			throw new UserException(e.getStatusCode(), e.getMessage());
		}
	}

	public byte[] getUsersJSONFormat() throws UserException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsBytes(getUsers());

		} catch (JsonProcessingException e) {
			LOGGER.error(e.getMessage());
			throw new UserException(e.getMessage());
		}
	}

	public byte[] getUsersXMLFormat() throws UserException {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			StreamResult result = new StreamResult(bos);
			JAXBContext jaxbContext = JAXBContext.newInstance(UsersXML.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			UsersXML usersXML = new UsersXML();
			usersXML.setUsers(getUsers());
			jaxbMarshaller.marshal(usersXML, result);
			return bos.toByteArray();
		} catch (JAXBException | IOException e) {
			LOGGER.error(e.getMessage());
			throw new UserException(e.getMessage());
		}
	}
}
