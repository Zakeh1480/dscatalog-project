package com.devsuperior.dscatalog.service;

import com.devsuperior.dscatalog.DTO.CategoryDTO;
import com.devsuperior.dscatalog.entity.Category;
import com.devsuperior.dscatalog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    //Garante que o método estará incluido na transação do banco ou irá fazer uma transação.
    @Transactional
    public List<CategoryDTO> findAll() {
        List<Category> list = categoryRepository.findAll();

//        Convertendo uma lista Category em uma lista CategoryDTO - Jeito resumido.
        return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());

//        Convertendo uma lista Category em uma lista CategoryDTO - Jeito detalhado.
//        List<CategoryDTO> categoryDTOList = new ArrayList<>();
//
//        for (Category cat : list){
//            categoryDTOList.add(new CategoryDTO(cat));
//        }
    }
}
