package com.devsuperior.dscatalog.service;

import com.devsuperior.dscatalog.DTO.RoleDTO;
import com.devsuperior.dscatalog.DTO.UserDTO;
import com.devsuperior.dscatalog.DTO.UserInsertDTO;
import com.devsuperior.dscatalog.entity.Role;
import com.devsuperior.dscatalog.entity.User;
import com.devsuperior.dscatalog.repository.RoleRepository;
import com.devsuperior.dscatalog.repository.UserRepository;
import com.devsuperior.dscatalog.service.exceptions.DatabaseException;
import com.devsuperior.dscatalog.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    //Garante que o método estará incluido na transação do banco ou irá fazer uma transação.
    @Transactional
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        Page<User> listPage = userRepository.findAll(pageable);

//        Convertendo uma lista User em uma lista UserDTO - Jeito resumido.
//        Page já um .stream(), então não precisa chamar e converter para list novamente (collect)
        return listPage.map(x -> new UserDTO(x));

//        Convertendo uma lista User em uma lista UserDTO - Jeito detalhado.
//        List<UserDTO> UserDTOList = new ArrayList<>();
//
//        for (User cat : list){
//            UserDTOList.add(new UserDTO(cat));
//        }
    }

    @Transactional
    public UserDTO findById(Long id) {
        //Optional é usada para não trabalhar com nulos - exemplo UserRepository.findById(id) ficaria sozinho;
        Optional<User> user = userRepository.findById(id);
        //Usando a entidade e lançando uma exception.
        User entity = user.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
        return new UserDTO(entity);
    }

    //Passamos a classe que atribuira a senha como argumenta, assim trabalhamos com a userDTO e ela.
    @Transactional
    public UserDTO createUser(UserInsertDTO userInsertDTO) {
        User user = new User();
        copyDtoToEntity(userInsertDTO, user);
        user.setPassword(bCryptPasswordEncoder.encode(userInsertDTO.getPassword()));
        userRepository.save(user);
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.orElseThrow(() -> new ResourceNotFoundException("ID not found. " + id));
        copyDtoToEntity(userDTO, user);
        userRepository.save(user);
        return new UserDTO(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException erro) {
            throw new ResourceNotFoundException("ID not found " + id);
        } catch (DataIntegrityViolationException erro) {
            throw new DatabaseException("Integrity violation");
        }
    }

    //Método para transferir dados de um DTO para a entidade.
    private void copyDtoToEntity(UserDTO userDTO, User user) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());

        //Limpando as categorias da entidade para usar da classe DTO
        user.getRoleList().clear();

        for (RoleDTO roleDTO : userDTO.getRoles()) {
            Role role = roleRepository.getReferenceById(roleDTO.getId());
            user.getRoleList().add(role);
        }
    }
}