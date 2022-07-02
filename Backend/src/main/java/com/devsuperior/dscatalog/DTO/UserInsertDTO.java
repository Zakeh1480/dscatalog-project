package com.devsuperior.dscatalog.DTO;

import com.devsuperior.dscatalog.entity.User;
import com.devsuperior.dscatalog.service.validation.UserInsertValid;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


//Essa classe servira para implementar a senha com maior segurança.
@UserInsertValid
public class UserInsertDTO extends UserDTO {

    @NotBlank
    @Size(min = 6, max = 18, message = "Senha inválida.")
    private String password;

    public UserInsertDTO() {
        super();
    }

    public UserInsertDTO(String password) {
        this.password = password;
    }

    public UserInsertDTO(Long id, String firstName, String lastName, String email, String password) {
        super(id, firstName, lastName, email);
        this.password = password;
    }

    public UserInsertDTO(User user, String password) {
        super(user);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
