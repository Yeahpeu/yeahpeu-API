package com.yeahpeu.category.service;

import com.yeahpeu.category.service.dto.CategoryDTO;
import com.yeahpeu.wedding.service.command.WeddingCategorySaveCommand;
import java.util.List;

public interface CategoryService {
    List<CategoryDTO> findAllCategories();

    List<CategoryDTO> findCategory(Long categoryId);

    List<CategoryDTO> findWeddingCategories(Long userId);

    void saveWeddingCategories(WeddingCategorySaveCommand command);
}
