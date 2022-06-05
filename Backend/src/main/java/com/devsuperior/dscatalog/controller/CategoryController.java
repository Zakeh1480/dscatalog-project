package com.devsuperior.dscatalog.controller;

import com.devsuperior.dscatalog.DTO.CategoryDTO;
import com.devsuperior.dscatalog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //Anteriormente era feito com list, porém vamos utilizar paginação.
    //RequestParam é uma busca opcional.
    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> findAllPaged(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                          @RequestParam(value = "linesPerPage", defaultValue = "6") Integer linesPerPage,
                                                          @RequestParam(value = "direction", defaultValue = "DESC") String direction,
                                                          @RequestParam(value = "orderBy", defaultValue = "name") String orderBy) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<CategoryDTO> categories = categoryService.findAllPaged(pageRequest);
        return ResponseEntity.status(200).body(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findCategoryById(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.findById(id);
        return ResponseEntity.status(200).body(categoryDTO);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(categoryDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(categoryService.createCategory(categoryDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        categoryDTO = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.status(200).body(categoryDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(204).build();
    }
}
