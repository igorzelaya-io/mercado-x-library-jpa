package hn.shadowcore.mercadoxlibrary.jpa.repository;

import org.springframework.stereotype.Repository;
import hn.shadowcore.mercadoxlibrary.entity.model.core.Category;

import java.util.UUID;

@Repository
public interface CategoryRepository extends BaseRepository<Category, UUID>{ }
