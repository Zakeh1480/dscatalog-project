package com.devsuperior.dscatalog.repository;

import com.devsuperior.dscatalog.entity.Product;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    private long existId;
    private long nonExistId;

    private Long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        existId = 1L;
        nonExistId = 55L;
        countTotalProducts = 25L;
    }

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void deletaObjetoCasoExistaId() {

        productRepository.deleteById(existId);
        Optional<Product> productOptional = productRepository.findById(existId);

        //isPresent verifica se existe um objeto, caso não, retorna um boolean (false).
        //Somente quando estiver trabalhando com Optional.
        Assertions.assertFalse(productOptional.isPresent());
    }

    @Test
    public void deveLancarExceptionCasoOIDNaoExista() {

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            productRepository.deleteById(nonExistId);
        });
    }

    @Test
    public void saveDeveIncrementarCasoIdSejaNull() {
        Product product = Factory.createProduct();
        product.setId(null);
        productRepository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void deveRetornarUmOptionalNaoVazioSeExistirId() {
        Optional<Product> productOptional = productRepository.findById(existId);

        Assertions.assertTrue(productOptional.isPresent());
    }

    @Test
    public void deveRetornarUmOptionalVazioSeNaoExistirId() {
        Optional<Product> productOptional = productRepository.findById(nonExistId);

        //Poderia usar o isEmpty()
        Assertions.assertFalse(productOptional.isPresent());
    }
}
