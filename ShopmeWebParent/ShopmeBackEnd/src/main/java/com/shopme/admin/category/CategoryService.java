package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepo;

    @Autowired
    public CategoryService(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public List<Category> listAll() {
        return (List<Category>) categoryRepo.findAll();
    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();

        Iterable<Category> categoriesInDB = categoryRepo.findAll();
        for (Category rootCategory : categoriesInDB) {
            if (rootCategory.getParent() == null) {
                // children
                Set<Category> parent = rootCategory.getChildren();
                for (Category subCategory : parent) {
                   String name = "--" + subCategory.getName();
                   categoriesUsedInForm.add(new Category(name));
                   listChildHierarchicalCategories(categoriesUsedInForm, subCategory, 1);
                }
            }
        }

        return categoriesUsedInForm;
    }

    private void listChildHierarchicalCategories(List<Category> categoriesUsedInForm,
                                                 Category parent,
                                                 int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> child = parent.getChildren();
        for (Category subCategory : child) {
            String name = "--".repeat(Math.max(0, newSubLevel)) + subCategory.getName();
            categoriesUsedInForm.add(new Category(name));
            listChildHierarchicalCategories(categoriesUsedInForm, subCategory, newSubLevel);
        }
    }



}
