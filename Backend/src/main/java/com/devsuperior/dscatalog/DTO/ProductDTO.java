package com.devsuperior.dscatalog.DTO;

import com.devsuperior.dscatalog.entity.Category;
import com.devsuperior.dscatalog.entity.Product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProductDTO {

    private Long id;

    @NotBlank(message = "Nome não pode ser vazio.")
    @Size(max = 60, min = 3, message = "Nome inválido.")
    private String name;

    @NotBlank
    private String description;

    @Positive
    private Double price;

    @NotBlank
    private String imgUrl;

    @PastOrPresent
    private Instant date;

    private List<CategoryDTO> categoryDTO = new ArrayList<>();

    public ProductDTO() {

    }

    public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.date = date;
    }

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.imgUrl = product.getImgUrl();
        this.date = product.getDate();
    }

    public ProductDTO(Product product, Set<Category> categories) {
        this(product);
        categories.forEach(category -> categoryDTO.add(new CategoryDTO(category)));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public List<CategoryDTO> getCategoryDTO() {
        return categoryDTO;
    }

    public void setCategoryDTO(List<CategoryDTO> categoryDTO) {
        this.categoryDTO = categoryDTO;
    }
}
