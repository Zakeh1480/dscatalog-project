package com.devsuperior.dscatalog.controller;

import com.devsuperior.dscatalog.DTO.ProductDTO;
import com.devsuperior.dscatalog.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    //Anteriormente era feito com list, porém vamos utilizar paginação.
    //RequestParam é uma busca opcional.
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAllPaged(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                          @RequestParam(value = "linesPerPage", defaultValue = "6") Integer linesPerPage,
                                                          @RequestParam(value = "direction", defaultValue = "DESC") String direction,
                                                          @RequestParam(value = "orderBy", defaultValue = "name") String orderBy) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<ProductDTO> products = productService.findAllPaged(pageRequest);
        return ResponseEntity.status(200).body(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findProductById(@PathVariable Long id) {
        ProductDTO ProductDTO = productService.findById(id);
        return ResponseEntity.status(200).body(ProductDTO);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO ProductDTO) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(ProductDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(productService.createProduct(ProductDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO ProductDTO) {
        ProductDTO = productService.updateProduct(id, ProductDTO);
        return ResponseEntity.status(200).body(ProductDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(204).build();
    }
}
