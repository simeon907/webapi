package shop.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dto.category.CategoryItemDTO;
import shop.dto.category.CreateCategoryDTO;
import shop.entities.CategoryEntity;
import shop.mapper.CategoryMapper;
import shop.repositories.CategoryRepository;
import shop.storage.StorageService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final StorageService storageService;
    @GetMapping
    public ResponseEntity<List<CategoryItemDTO>> index() {
        var list = categoryRepository.findAll();
        var model = categoryMapper.CategoryItemDTOsByCategories(list);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CategoryItemDTO> create(@RequestBody CreateCategoryDTO model) {
        var filename = storageService.save(model.getImage());
        CategoryEntity category = categoryMapper.categoryByCategoryCreateDTO(model);

        category.setImage(filename);
        categoryRepository.save(category);
        var result = categoryMapper.CategoryItemDTOByCategory(category);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
    @GetMapping("{id}")
    public ResponseEntity<CategoryEntity> get(@PathVariable("id") int categoryId){
        var catOptional = categoryRepository.findById(categoryId);
        if(catOptional.isPresent()){
            return new ResponseEntity<>(catOptional.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryEntity> update(@PathVariable("id") int categoryId,
                                                 @RequestBody CreateCategoryDTO model) {
        var catOptional = categoryRepository.findById(categoryId);
        if(catOptional.isPresent())
        {
            var cat = catOptional.get();
            cat.setName(model.getName());
            cat.setImage((model.getImage()));
            cat.setDescription((model.getDescription()));
            categoryRepository.save(cat);
            return new ResponseEntity<>(cat, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int categoryId) {
        categoryRepository.deleteById(categoryId);
        return new ResponseEntity<>("Категорію видалено", HttpStatus.OK);
    }
}
