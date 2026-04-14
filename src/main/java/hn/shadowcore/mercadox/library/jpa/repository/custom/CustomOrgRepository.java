package hn.shadowcore.mercadox.library.jpa.repository.custom;

import hn.shadowcore.mercadox.library.entity.model.auth.Organization;

public interface  CustomOrgRepository {

    Organization findInactiveOrgById(String orgId);
}
