package hn.alturaforge.mercadox.library.jpa.repository.custom;

import hn.alturaforge.mercadox.library.entity.model.auth.Organization;

public interface  CustomOrgRepository {

    Organization findInactiveOrgById(String orgId);
}
