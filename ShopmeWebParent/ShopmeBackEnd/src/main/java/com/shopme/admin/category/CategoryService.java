package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepo;

    @Autowired
    public CategoryService(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public List<Category> listAll() {
        List<Category> rootCategories = categoryRepo.findRootCategories();
        return listHierarchicalCategories(rootCategories);
    }

    public List<Category> listHierarchicalCategories(List<Category> rootCategories) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCategory : rootCategories) {
            hierarchicalCategories.add(Category.copyFull(rootCategory));

            Set<Category> children = rootCategory.getChildren();
            for (Category subCategory: children) {
                String name = "--" + subCategory.getName();
                hierarchicalCategories.add(Category.copyFull(subCategory, name));

                listChildHierarchicalCategories(hierarchicalCategories, subCategory, 1);
            }
        }
        return hierarchicalCategories;
    }

    private void listChildHierarchicalCategories(List<Category> hierarchicalCategories,
                                                 Category parent,
                                                 int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> child = parent.getChildren();
        for (Category subCategory : child) {
            String name = "--".repeat(Math.max(0, newSubLevel)) + subCategory.getName();
            hierarchicalCategories.add(Category.copyFull(subCategory, name));

            listChildHierarchicalCategoriesUsedInForm(hierarchicalCategories, subCategory, newSubLevel);
        }
    }


    // Start Form

    /***
     *
     *
     * @return categoriesUsedInForm
     */
    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();

        Iterable<Category> categoriesInDB = categoryRepo.findAll();
        for (Category rootCategory : categoriesInDB) {
            if (rootCategory.getParent() == null) {
                categoriesUsedInForm.add(Category.copyIdAndName(rootCategory));
                // children
                Set<Category> parent = rootCategory.getChildren();
                for (Category subCategory : parent) {
                   String name = "--" + subCategory.getName();
                   categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
                   listChildHierarchicalCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
                }
            }
        }

        return categoriesUsedInForm;
    }

    /***
     *
     *
     * @param categoriesUsedInForm
     * @param parent
     * @param subLevel
     */
    private void listChildHierarchicalCategoriesUsedInForm(List<Category> categoriesUsedInForm,
                                                           Category parent,
                                                           int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> child = parent.getChildren();
        for (Category subCategory : child) {
            String name = "--".repeat(Math.max(0, newSubLevel)) + subCategory.getName();
            categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
            listChildHierarchicalCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
        }
    }
    // End Form

    public Category saveCategory(Category category) {
        return categoryRepo.save(category);
    }

    public Category get(Integer id) throws CategoryNotFoundException {
        try {
            return categoryRepo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }
    }

}
