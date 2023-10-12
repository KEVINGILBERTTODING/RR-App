package com.example.rumahraga.viewmodel.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.rumahraga.data.repository.category.CategoryRepository;
import com.example.rumahraga.model.CategoryModel;
import com.example.rumahraga.model.ResponseModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CategoryViewModel extends ViewModel {
    private CategoryRepository categoryRepository;
    @Inject
    public CategoryViewModel(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public LiveData<ResponseModel<List<CategoryModel>>> getAllCategory() {
        return categoryRepository.getAllCategory();
    }
}
