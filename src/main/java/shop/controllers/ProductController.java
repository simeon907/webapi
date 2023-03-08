package shop.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dto.category.CategoryItemDTO;
import shop.dto.product.ProductCreateDTO;
import shop.dto.product.ProductItemDTO;
import shop.interfaces.ProductService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductItemDTO>> index() {
        var result = productService.get();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductItemDTO> create(@Valid @ModelAttribute ProductCreateDTO model) {
        var result = productService.create(model);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
