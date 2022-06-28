package com.devsuperior.dscatalog.controller;

import com.devsuperior.dscatalog.DTO.ProductDTO;
import com.devsuperior.dscatalog.service.ProductService;
import com.devsuperior.dscatalog.service.exceptions.DatabaseException;
import com.devsuperior.dscatalog.service.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //Utilizamos MockBean iremos carregar o contexto/lógica da aplicação.
    @MockBean
    private ProductService productService;

    private Long existingId;

    private Long nonExistingId;

    private Long dependentId;

    @Autowired
    private ObjectMapper objectMapper;

    private PageImpl<ProductDTO> productDTOPage;

    private ProductDTO productDTO;

    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        nonExistingId = 55L;
        dependentId = 5L;
        productDTO = Factory.createProductDTO();
        productDTOPage = new PageImpl<>(List.of(productDTO));

        Mockito.when(productService.findAllPaged(any())).thenReturn(productDTOPage);

        Mockito.when(productService.findById(existingId)).thenReturn(productDTO);
        Mockito.when(productService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(productService.updateProduct(eq(existingId), any())).thenReturn(productDTO);
        Mockito.when(productService.updateProduct(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        Mockito.when(productService.createProduct(productDTO)).thenReturn(productDTO);

        Mockito.doNothing().when(productService).deleteProduct(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(productService).deleteProduct(nonExistingId);
        Mockito.doThrow(DatabaseException.class).when(productService).deleteProduct(dependentId);
    }

    @Test
    public void createdProductDeveriaRetornarStatus200ComUmProductDto() throws Exception {

        String body = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void findAllPagedDeveriaRetornarUmaPageProductDto() throws Exception {
        mockMvc.perform(get("/products")).andExpect(status().isOk());
    }

    @Test
    public void findProductByIdDeveriaRetornarUmProductDtoCasoIdExista() throws Exception {
        //Poderiamos colocar o código em uma variavel ResultActions.
        //Colocariamos o código até o final do MediaType.
        mockMvc.perform(get("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void findProductByIdDeveriaRetornarResourceNotFoundExceptionoCasoIdNaoExista() throws Exception {
        mockMvc.perform(get("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void updateProductDeveriaRetornarUmProductDtoCasoIdExista() throws Exception {

        String body = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(put("/products/{id}", existingId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

    }

    @Test
    public void updateProductDeveriaRetornarResourceNotFoundExceptionoCasoIdNaoExista() throws Exception {

        String body = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(put("/products/{id}", nonExistingId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteDeveriaDeletarUmProductDtoQuandoIdExistir() throws Exception {
        mockMvc.perform(delete("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteDeveriaRetornarResourceNotFoundExceptionQuandoIdNaoExistir() throws Exception {
        mockMvc.perform(delete("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteDeveriaRetornarDatabaseExceptionQuandoIdEstiverRelacionadoComOutro() throws Exception {
        mockMvc.perform(delete("/products/{id}", dependentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
