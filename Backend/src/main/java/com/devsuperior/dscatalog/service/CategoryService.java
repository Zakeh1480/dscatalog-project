package com.devsuperior.dscatalog.service;

import com.devsuperior.dscatalog.entity.Category;
import com.devsuperior.dscatalog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    //Garante que o método estará incluido na transação do banco ou irá fazer uma transação.
    @Transactional
    public List<Category> findAll(){
        return categoryRepository.findAll();
    }
}
