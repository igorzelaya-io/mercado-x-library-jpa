package hn.shadowcore.mercadoxlibrary.jpa.repository;


import hn.shadowcore.mercadoxlibrary.entity.model.core.Lead;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LeadRepository extends BaseRepository<Lead, UUID> { }
