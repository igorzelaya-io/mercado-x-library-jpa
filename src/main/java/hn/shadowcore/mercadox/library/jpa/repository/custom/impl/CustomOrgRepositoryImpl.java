package hn.shadowcore.mercadox.library.jpa.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hn.shadowcore.mercadox.library.jpa.repository.custom.CustomOrgRepository;
import hn.shadowcore.mercadox.library.jpa.predicate.OrgPredicateFactory;
import hn.shadowcore.mercadox.library.jpa.util.DisableHibernateFilters;
import hn.shadowcore.mercadox.library.entity.model.auth.Organization;
import hn.shadowcore.mercadox.library.entity.model.auth.QOrganization;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomOrgRepositoryImpl implements CustomOrgRepository {

    private static final OrgPredicateFactory orgPredicateFactory = new OrgPredicateFactory();

    private final JPAQueryFactory queryFactory;

    private static final QOrganization qOrg = QOrganization.organization;

    @Override
    @DisableHibernateFilters({"enabledEntityFilter"})
    public Organization findInactiveOrgById(String orgId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qOrg).where(orgPredicateFactory.matchId(orgId)
                                .and(orgPredicateFactory.isInactive()))
                        .fetchOne())
                .orElseThrow(() -> new ResourceNotFoundException(String
                        .format("Organization was not found for ID: '%s'", orgId)));
    }
}
