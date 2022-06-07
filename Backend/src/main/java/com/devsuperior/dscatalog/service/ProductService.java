package com.devsuperior.dscatalog.service;

import com.devsuperior.dscatalog.DTO.ProductDTO;
import com.devsuperior.dscatalog.entity.Product;
import com.devsuperior.dscatalog.repository.ProductRepository;
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
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    //Garante que o método estará incluido na transação do banco ou irá fazer uma transação.
    @Transactional
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> listPage = productRepository.findAll(pageRequest);

//        Convertendo uma lista Product em uma lista ProductDTO - Jeito resumido.
//        Page já um .stream(), então não precisa chamar e converter para list novamente (collect)
        return listPage.map(x -> new ProductDTO(x));

//        Convertendo uma lista Product em uma lista ProductDTO - Jeito detalhado.
//        List<ProductDTO> ProductDTOList = new ArrayList<>();
//
//        for (Product cat : list){
//            ProductDTOList.add(new ProductDTO(cat));
//        }
    }

    public ProductDTO findById(Long id) {
        //Optional é usada para não trabalhar com nulos - exemplo ProductRepository.findById(id) ficaria sozinho;
        Optional<Product> product = productRepository.findById(id);
        //Usando a entidade e lançando uma exception.
        Product entity = product.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
        return new ProductDTO(entity, entity.getCategories());
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
//        productDTO.setName(productDTO.getName());
        productRepository.save(product);

        return new ProductDTO(product);
    }


    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Optional<Product> productOptional = productRepository.findById(id);
        Product product = productOptional.orElseThrow(() -> new ResourceNotFoundException("ID not found"));
//        product.setName(ProductDTO.getName());
        productRepository.save(product);
        return new ProductDTO(product);
    }

    public void deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException erro) {
            throw new ResourceNotFoundException("ID not found " + id);
        } catch (DataIntegrityViolationException erro) {
            throw new DatabaseException("Integrity violation");
        }
    }
}
