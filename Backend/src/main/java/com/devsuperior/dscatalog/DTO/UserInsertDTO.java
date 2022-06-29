package com.devsuperior.dscatalog.DTO;

import com.devsuperior.dscatalog.entity.User;

//Essa classe servira para implementar a senha com maior segurança.
public class UserInsertDTO extends UserDTO {

    private String password;

    public UserInsertDTO(){
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
