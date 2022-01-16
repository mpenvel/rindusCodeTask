package com.rindus.codingtask.controllers;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rindus.codingtask.dto.AlbumDTO;
import com.rindus.codingtask.dto.UserDTO;
import com.rindus.codingtask.exceptions.UserException;
import com.rindus.codingtask.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping
	public ResponseEntity<List<UserDTO>> getUsers() {
		return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<UserDTO> getUser(@PathVariable("userId") String userId) throws UserException {
		return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Void> createUser(@RequestBody UserDTO user) throws UserException {
		userService.createUser(user);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping("/{userId}")
	public ResponseEntity<Void> updateUser(@PathVariable("userId") String userId, @RequestBody UserDTO user) throws UserException {
		userService.updateUser(userId, user);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PatchMapping("/{userId}")
	public ResponseEntity<Void> patchUser(@PathVariable("userId") String userId, @RequestBody UserDTO user) throws UserException {
		userService.patchUser(userId, user);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> patchUser(@PathVariable("userId") String userId) throws UserException {
		userService.deleteUser(userId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/{userId}/albums")
	public ResponseEntity<List<AlbumDTO>> getUserAlbums(@PathVariable("userId") String userId) throws UserException {
		return new ResponseEntity<>(userService.getUserAlbums(userId), HttpStatus.OK);
	}

	@GetMapping("json")
	public ResponseEntity<InputStreamResource> getJSONFile() throws UserException {
		byte[] jsonFile = userService.getUsersJSONFormat();
		return ResponseEntity.ok().contentLength(jsonFile.length)
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.header("Content-Disposition", "attachment; filename=\"users.json\"")
				.body(new InputStreamResource(new ByteArrayInputStream(jsonFile)));
	}

	@GetMapping("xml")
	public ResponseEntity<InputStreamResource> getXMLFile() throws UserException {
		byte[] xmlFile = userService.getUsersXMLFormat();
		return ResponseEntity.ok().contentLength(xmlFile.length)
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.header("Content-Disposition", "attachment; filename=\"users.xml\"")
				.body(new InputStreamResource(new ByteArrayInputStream(xmlFile)));
	}
}
