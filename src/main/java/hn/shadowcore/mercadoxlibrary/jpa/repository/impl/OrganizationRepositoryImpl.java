package hn.shadowcore.mercadoxlibrary.jpa.repository.impl;


import com.querydsl.jpa.impl.JPAQueryFactory;
import hn.shadowcore.mercadoxlibrary.entity.model.auth.Organization;
import hn.shadowcore.mercadoxlibrary.entity.model.auth.QOrganization;
import hn.shadowcore.mercadoxlibrary.jpa.predicate.OrgPredicateFactory;
import hn.shadowcore.mercadoxlibrary.jpa.repository.CustomOrgRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;

import java.util.Optional;

@RequiredArgsConstructor
public class OrganizationRepositoryImpl implements CustomOrgRepository {

    private final OrgPredicateFactory orgPredicateFactory;

    private final JPAQueryFactory queryFactory;

    private static final QOrganization qOrg = QOrganization.organization;

    @Override
    public Organization findInactiveOrgById(String orgId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qOrg).where(orgPredicateFactory.isActive()
                                .and(orgPredicateFactory.isInactive()))
                        .fetchOne())
                .orElseThrow(() -> new ResourceNotFoundException(String
                        .format("Organization was not found for ID: '%s'", orgId)));
    }
}
