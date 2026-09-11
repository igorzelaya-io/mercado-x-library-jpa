package hn.alturaforge.mercadox.library.jpa.repository;


import hn.alturaforge.mercadox.library.entity.model.core.Lead;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LeadRepository extends BaseRepository<Lead, UUID> { }
