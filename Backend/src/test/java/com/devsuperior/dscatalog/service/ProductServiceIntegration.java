package com.devsuperior.dscatalog.service;

import com.devsuperior.dscatalog.DTO.ProductDTO;
import com.devsuperior.dscatalog.repository.ProductRepository;
import com.devsuperior.dscatalog.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

//Carregando o contexto da aplicação
@SpringBootTest
//Da um rollback no banco após fazer algum teste
@Transactional
public class ProductServiceIntegration {

    //Como estamos carregando o contexto da atualização, podemos trabalhar com o Autowired
    //Para trabalhar diretamente com as entidades e realizar os testes diretamente do banco.
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private Long existId;

    private Long nonExistId;

    private Long dependentId;

    private Long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        existId = 1L;
        nonExistId = 55L;
        dependentId = 2L;
        countTotalProducts = 25L;
    }

    @Test
    public void deleteDeveriaDeletarUmProductQuandoIdExistir() {
        productService.deleteProduct(existId);
        Assertions.assertEquals(countTotalProducts - 1, productRepository.count());
    }

    @Test
    public void deleteDeveriaLancarResourceNotFoundExceptionQuandoIdNaoExistir() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct(nonExistId);
        });
    }

//    @Test
//    public void deleteDeveriaLancarDatabaseExceptionQuandoIdEstiverRelacionadoComOutro(){
//        Assertions.assertThrows(DatabaseException.class, () -> {
//            productService.deleteProduct(dependentId);
//        });
//    }


    @Test
    public void findAllPagedDeveriaRetornarPagina0Com10Elementos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> dtoPage = productService.findAllPaged(pageable);

        Assertions.assertFalse(dtoPage.isEmpty());
    }

    @Test
    public void findAllPagedDeveriaRetornarUmaPaginaVaziaQuandoPaginaForInvalida() {
        Pageable pageable = PageRequest.of(50, 10);
        Page<ProductDTO> dtoPage = productService.findAllPaged(pageable);

        Assertions.assertTrue(dtoPage.isEmpty());
    }

    @Test
    public void findAllPagedDeveriaRetornarUmaPaginaOrdenada() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        Page<ProductDTO> dtoPage = productService.findAllPaged(pageable);

        Assertions.assertFalse(dtoPage.isEmpty());
    }
}
