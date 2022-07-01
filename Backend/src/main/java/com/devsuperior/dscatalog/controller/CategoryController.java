package com.devsuperior.dscatalog.controller;

import com.devsuperior.dscatalog.DTO.CategoryDTO;
import com.devsuperior.dscatalog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //Anteriormente era feito com list, porém vamos utilizar paginação.
    //RequestParam é uma busca opcional.
    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> findAllPaged(Pageable pageable) {
        Page<CategoryDTO> categories = categoryService.findAllPaged(pageable);
        return ResponseEntity.status(200).body(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findCategoryById(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.findById(id);
        return ResponseEntity.status(200).body(categoryDTO);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(categoryDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(categoryService.createCategory(categoryDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        categoryDTO = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.status(200).body(categoryDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(204).build();
    }
}
