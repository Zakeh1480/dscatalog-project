package com.devsuperior.dscatalog.controller;

import com.devsuperior.dscatalog.DTO.UserDTO;
import com.devsuperior.dscatalog.DTO.UserInsertDTO;
import com.devsuperior.dscatalog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    //Anteriormente era feito com list, porém vamos utilizar paginação.
    //RequestParam é uma busca opcional.
    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAllPaged(Pageable pageable) {
        Page<UserDTO> users = userService.findAllPaged(pageable);
        return ResponseEntity.status(200).body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable Long id) {
        UserDTO UserDTO = userService.findById(id);
        return ResponseEntity.status(200).body(UserDTO);
    }

    //O método continuara a retornar uma UserDTO, porém, como passaremos a senha por outro método/class
    //Implementamos essa classe como argumento
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserInsertDTO userInsertDTO) {
        UserDTO userDTO = userService.createUser(userInsertDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(userDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(userDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO UserDTO) {
        UserDTO = userService.updateUser(id, UserDTO);
        return ResponseEntity.status(200).body(UserDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(204).build();
    }
}
