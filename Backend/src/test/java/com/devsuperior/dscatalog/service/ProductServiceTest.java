package com.devsuperior.dscatalog.service;

import com.devsuperior.dscatalog.DTO.ProductDTO;
import com.devsuperior.dscatalog.entity.Category;
import com.devsuperior.dscatalog.entity.Product;
import com.devsuperior.dscatalog.repository.CategoryRepository;
import com.devsuperior.dscatalog.repository.ProductRepository;
import com.devsuperior.dscatalog.service.exceptions.DatabaseException;
import com.devsuperior.dscatalog.service.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    //@InjectMocks substitui o Autowired, pois ele nao injeta as dependencias normais do service
    @InjectMocks
    private ProductService productService;

    //Sinalizando a classe a ser mockada.
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    //Quando trabalhamos com page em testes, utilizamos o IMPL.
    private PageImpl<Product> page;

    private Product product;

    private ProductDTO productDTO;

    private Category category;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 2L;
        product = Factory.createProduct();
        productDTO = Factory.createProductDTO();
        category = Factory.createCategory();
        page = new PageImpl<>(List.of(product));

        //Quando trabalhamos com métodos que não retornam void, primeiro colocamos a condição depois a ação.
        Mockito.when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));

        Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        Mockito.when(productRepository.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
        Mockito.when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        //Quando trabalhamos com métodos que retornam void, primeiro vem a ação depois a condição
        Mockito.doNothing().when(productRepository).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
    }

    @Test
    public void findByIdDeveriaRetornarUmProductDtoQuandoIdExistir() {
        productDTO = productService.findById(existingId);
        Assertions.assertNotNull(productDTO);
        Mockito.verify(productRepository).findById(existingId);
    }

    @Test
    public void findByIdDeveriaLancarResourceNotFoundExceptionQuandoIdNaoExistir() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productDTO = productService.findById(nonExistingId);
        });

        Mockito.verify(productRepository).findById(nonExistingId);
    }

    //Como utilizamos o método copyToEntity, onde ele passa uma Category para o Product
    //Precisamos instaciar uma Category e mockar uma CategoryRepository
    //Analisar o código e verificar os métodos antes.
    @Test
    public void updateDeveriaRetornarUmProductDtoQuandoIdExistir() {
        productDTO = productService.updateProduct(existingId, productDTO);
        Assertions.assertNotNull(productDTO);
        Mockito.verify(productRepository).findById(existingId);
    }

    @Test
    public void updateDeveriaLancarResourceNotFoundExceptionQuandoIdNaoExistir() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productDTO = productService.updateProduct(nonExistingId, productDTO);
        });

        Mockito.verify(productRepository).findById(nonExistingId);
    }

    @Test
    public void findAllPagedDeveriaRetornarUmaPage() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<ProductDTO> productDTOS = productService.findAllPaged(pageable);
        Assertions.assertNotNull(productDTOS);
        Mockito.verify(productRepository).findAll(pageable);
    }

    @Test
    public void deletarDeveriaNaoLancarNenhumaExceptionQuandoIdExistir() {
        //Verificando se não foi lançado uma exception ao chamar o método
        Assertions.assertDoesNotThrow(() -> {
            productService.deleteProduct(existingId);
        });

        //Verificando se o método foi chamado corretamente.
        Mockito.verify(productRepository).deleteById(existingId);
    }

    @Test
    public void deleteDeveriaLancarResourceNotFoundExceptionQuandoIdNaoExistir() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct(nonExistingId);
        });

        Mockito.verify(productRepository).deleteById(nonExistingId);
    }

    @Test
    public void deleteDeveriaLancarDatabaseExceptionQuandoObjetoEstaAssociadoComOutro() {
        Assertions.assertThrows(DatabaseException.class, () -> {
            productService.deleteProduct(dependentId);
        });

        Mockito.verify(productRepository).deleteById(dependentId);
    }
}
