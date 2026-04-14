package hn.shadowcore.mercadox.library.jpa.repository;

import org.springframework.stereotype.Repository;
import hn.shadowcore.mercadox.library.entity.model.core.Category;

import java.util.UUID;

@Repository
public interface CategoryRepository extends BaseRepository<Category, UUID>{ }
