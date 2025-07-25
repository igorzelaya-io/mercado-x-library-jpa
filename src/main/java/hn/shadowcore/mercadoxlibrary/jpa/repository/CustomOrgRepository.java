package hn.shadowcore.mercadoxlibrary.jpa.repository;

import hn.shadowcore.mercadoxlibrary.entity.model.auth.Organization;

public interface CustomOrgRepository {

    Organization findInactiveOrgById(String orgId);
}
