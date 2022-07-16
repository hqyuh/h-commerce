package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepo;

    @Autowired
    public CategoryService(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public List<Category> listAll(String sortDir) {
        Sort sort = Sort.by("name");

        if (sortDir.equals("asc"))
            sort = sort.ascending();
        else if (sortDir.equals("desc"))
            sort = sort.descending();

        List<Category> rootCategories = categoryRepo.findRootCategories(sort);
        return listHierarchicalCategories(rootCategories, sortDir);
    }

    public List<Category> listHierarchicalCategories(List<Category> rootCategories,
                                                     String sortDir) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCategory : rootCategories) {
            hierarchicalCategories.add(Category.copyFull(rootCategory));

            Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);
            for (Category subCategory: children) {
                String name = "--" + subCategory.getName();
                hierarchicalCategories.add(Category.copyFull(subCategory, name));

                listChildHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
            }
        }
        return hierarchicalCategories;
    }

    private void listChildHierarchicalCategories(List<Category> hierarchicalCategories,
                                                 Category parent,
                                                 int subLevel,
                                                 String sortDir) {
        int newSubLevel = subLevel + 1;
        Set<Category> child = sortSubCategories(parent.getChildren(), sortDir);
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

        Iterable<Category> categoriesInDB = categoryRepo.findRootCategories(Sort.by("name").ascending());
        for (Category rootCategory : categoriesInDB) {
            if (rootCategory.getParent() == null) {
                categoriesUsedInForm.add(Category.copyIdAndName(rootCategory));
                // children
                Set<Category> parent = sortSubCategories(rootCategory.getChildren());
                for (Category subCategory : parent) {
                   String name = "--" + subCategory.getName();
                   categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
                   listChildHierarchicalCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
                }
            }
        }

        return categoriesUsedInForm;
    }



    /**
     * > This function recursively lists all the child categories of a parent category, and adds them to a list of
     * categories
     *
     * @param categoriesUsedInForm The list of categories that will be used in the form.
     * @param parent the parent category
     * @param subLevel the number of dashes to be added to the category name.
     */
    private void listChildHierarchicalCategoriesUsedInForm(List<Category> categoriesUsedInForm,
                                                           Category parent,
                                                           int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> child = sortSubCategories(parent.getChildren());
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


    /**
     * If the category exists, return it, otherwise throw an exception.
     *
     * @param id The ID of the category to be retrieved.
     * @return A category object
     */
    public Category get(Integer id) throws CategoryNotFoundException {
        try {
            return categoryRepo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }
    }



    /**
     * If the category is new, check if the name or alias is already in use. If the category is not new, check if the name
     * or alias is already in use by another category
     *
     * @param id the id of the category being edited. If it's a new category, it will be null.
     * @param name The name of the category
     * @param alias The alias of the category.
     * @return A string.
     */
    public String checkUnique(Integer id,
                              String name,
                              String alias) {
        boolean isCreatingNew = (id == null || id == 0);

        Category categoryByName = categoryRepo.findByName(name);

        if (isCreatingNew) {
            if (categoryByName != null) {
                return "DuplicateName";
            } else {
                Category categoryByAlias = categoryRepo.findByAlias(alias);
                if (categoryByAlias != null) {
                    return "DuplicateAlias";
                }
            }
        } else {
            if (categoryByName != null && !categoryByName.getId().equals(id)) {
                return "DuplicateName";
            }
            Category categoryByAlias = categoryRepo.findByAlias(alias);
            if (categoryByAlias != null && !categoryByAlias.getId().equals(id)) {
                return "DuplicateAlias";
            }
        }
        return "OK";
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children) {
        return sortSubCategories(children, "asc");
    }

    /**
     * Sort the subcategories by name.
     *
     * @param children The set of subcategories to sort.
     * @return A sorted set of categories.
     */
    private SortedSet<Category> sortSubCategories(Set<Category> children,
                                                  String sortDir) {
        SortedSet<Category> sortedChildren = new TreeSet<>((cat1, cat2) -> {
            if (sortDir.equals("asc"))
                return cat1.getName().compareTo(cat2.getName());
            else
                return cat2.getName().compareTo(cat1.getName());

        });
        sortedChildren.addAll(children);

        return sortedChildren;
    }

}
