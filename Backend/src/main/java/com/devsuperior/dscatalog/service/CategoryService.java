package com.devsuperior.dscatalog.service;

import com.devsuperior.dscatalog.DTO.CategoryDTO;
import com.devsuperior.dscatalog.entity.Category;
import com.devsuperior.dscatalog.repository.CategoryRepository;
import com.devsuperior.dscatalog.service.exceptions.DatabaseException;
import com.devsuperior.dscatalog.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    //Garante que o método estará incluido na transação do banco ou irá fazer uma transação.
    @Transactional
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
        Page<Category> listPage = categoryRepository.findAll(pageRequest);

//        Convertendo uma lista Category em uma lista CategoryDTO - Jeito resumido.
//        Page já um .stream(), então não precisa chamar e converter para list novamente (collect)
        return listPage.map(x -> new CategoryDTO(x));

//        Convertendo uma lista Category em uma lista CategoryDTO - Jeito detalhado.
//        List<CategoryDTO> categoryDTOList = new ArrayList<>();
//
//        for (Category cat : list){
//            categoryDTOList.add(new CategoryDTO(cat));
//        }
    }

    public CategoryDTO findById(Long id) {
        //Optional é usada para não trabalhar com nulos - exemplo categoryRepository.findById(id) ficaria sozinho;
        Optional<Category> category = categoryRepository.findById(id);
        //Usando a entidade e lançando uma exception.
        Category entity = category.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
        return new CategoryDTO(entity);
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        categoryRepository.save(category);

        return new CategoryDTO(category);
    }


    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        Category category = categoryOptional.orElseThrow(() -> new ResourceNotFoundException("ID not found"));
        category.setName(categoryDTO.getName());
        categoryRepository.save(category);
        return new CategoryDTO(category);
    }

    public void deleteCategory(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException erro) {
            throw new ResourceNotFoundException("ID not found " + id);
        } catch (DataIntegrityViolationException erro) {
            throw new DatabaseException("Integrity violation");
        }
    }
}
