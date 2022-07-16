package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.parent.id IS NULL")
    List<Category> findRootCategories(Sort sort);

    Category findByName(String name);

    Category findByAlias(String alias);

}