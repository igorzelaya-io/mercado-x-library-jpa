package hn.shadowcore.mercadoxlibrary.jpa.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hn.shadowcore.mercadoxlibrary.entity.model.auth.Organization;
import hn.shadowcore.mercadoxlibrary.entity.model.auth.QOrganization;
import hn.shadowcore.mercadoxlibrary.jpa.predicate.OrgPredicateFactory;
import hn.shadowcore.mercadoxlibrary.jpa.repository.custom.CustomOrgRepository;
import hn.shadowcore.mercadoxlibrary.jpa.util.DisableHibernateFilters;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomOrgRepositoryImpl implements CustomOrgRepository {

    private final OrgPredicateFactory orgPredicateFactory;

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
