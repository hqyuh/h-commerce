package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = NONE)
@Rollback(value = false)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repo;

    @Test
    public void testCreateRootCategory() {
        Category category = new Category("Electronics");
        Category savedCategory = repo.save(category);

        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateSubCategory() {
        Category parent = new Category(7);
        Category subCategory = new Category("iPhone", parent);
        Category savedCategory = repo.save(subCategory);

        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testGetCategory() {
        Category category = repo.findById(2).get();
        System.out.println(category.getName());

        Set<Category> children = category.getChildren();
        for (Category subCategory : children) {
            System.out.println(subCategory.getName());
        }
        assertThat(children.size()).isGreaterThan(0);
    }

    @Test
    public void testPrintHierarchicalCategories() {
        // Hierarchical -> phân cấp
        Iterable<Category> categories = repo.findAll();

        for (Category rootCategory : categories) {
            // root
            if (rootCategory.getParent() == null) {
                System.out.println(rootCategory.getName());
                // children
                Set<Category> parent = rootCategory.getChildren();
                for (Category subCategory : parent) {
                    System.out.println("--" + subCategory.getName());
                    printChildren(subCategory, 1);
                }
            }
        }
    }

    // get child of parent
    private void printChildren(Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        // get child of parent
        // ex: --Computer Components
        //     ----Memory
        Set<Category> child = parent.getChildren();
        for (Category subCategory : child) {
            for (int i = 0; i < newSubLevel; i++) {
                System.out.print("--");
            }
            System.out.println(subCategory.getName());
            printChildren(subCategory, newSubLevel);
        }
    }

    @Test
    public void testListRootCategories() {
        List<Category> rootCategories = repo.findRootCategories();
        rootCategories.forEach(x -> System.out.println(x.getName()));
    }

}