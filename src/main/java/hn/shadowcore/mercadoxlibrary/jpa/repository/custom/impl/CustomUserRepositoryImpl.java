package hn.shadowcore.mercadoxlibrary.jpa.repository.custom.impl;

import hn.shadowcore.mercadoxcontext.utils.OrgIdContextHolder;
import hn.shadowcore.mercadoxlibrary.entity.model.auth.QUser;
import hn.shadowcore.mercadoxlibrary.entity.model.auth.User;
import hn.shadowcore.mercadoxlibrary.jpa.predicate.UserPredicateFactory;
import hn.shadowcore.mercadoxlibrary.jpa.querydsl.OrgAwareQueryFactory;
import hn.shadowcore.mercadoxlibrary.jpa.repository.custom.CustomUserRepository;
import hn.shadowcore.mercadoxlibrary.jpa.util.DisableHibernateFilters;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final OrgAwareQueryFactory queryFactory;
    private final UserPredicateFactory userPredicateFactory;
    private static final QUser qUser = QUser.user;

    @Override
    @DisableHibernateFilters({"enabledEntityFilter"})
    public User findDisabledUserById(String id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qUser, qUser.orgId)
                        .where(userPredicateFactory.isInactive(qUser)
                                .and(userPredicateFactory.equalsId(qUser, id)))
                        .fetchOne())
                .orElseThrow(() -> new ResourceNotFoundException(String
                        .format("User was not found for ID: '%s'", id)));
    }

    @Override
    public List<User> findAllEnabledOrgAdmins() {
        List<User> users = queryFactory.selectFrom(qUser, qUser.orgId)
                .where(userPredicateFactory.isAdmin(qUser)).fetch();
        if(users.isEmpty()) {
            throw new ResourceNotFoundException(String
                    .format("No admins were found for organization with OrgID: '%s'",
                            OrgIdContextHolder.getTenantId()));
        }
        return users;
    }

    @Override
    public List<User> findAvailableDrivers() {
        List<User> users = queryFactory.selectFrom(qUser, qUser.orgId)
                .where(userPredicateFactory.isActive(qUser)
                        .and(userPredicateFactory.isDriver(qUser))
                        .and(userPredicateFactory.isActiveDriver(qUser)))
                        .fetch();
        if(users.isEmpty()) {
            throw new ResourceNotFoundException(String
                    .format("No drivers available at this moment for ORG with ID: '%s'.", OrgIdContextHolder
                            .getTenantId()));
        }
        return users;
    }
}
