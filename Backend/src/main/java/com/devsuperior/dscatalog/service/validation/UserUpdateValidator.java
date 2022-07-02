package com.devsuperior.dscatalog.service.validation;

import com.devsuperior.dscatalog.DTO.UserUpdateDTO;
import com.devsuperior.dscatalog.controller.exceptions.FieldMessage;
import com.devsuperior.dscatalog.entity.User;
import com.devsuperior.dscatalog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UserUpdateValid ann) {
    }

    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

        //Não existe o método get em Object.
        //Tudo em http é string
        //Pegamos a requisição http (uriVars) para trabalhar com ela (userId);
        var uriVars = (Map<String, String>) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        long userId = Long.parseLong(uriVars.get("id"));

        List<FieldMessage> list = new ArrayList<>();

        // Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista

        User user = userRepository.findByEmail(dto.getEmail());

        if (user != null && userId != user.getId()) {
            list.add(new FieldMessage("email", "Email em uso"));
        }


        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}
