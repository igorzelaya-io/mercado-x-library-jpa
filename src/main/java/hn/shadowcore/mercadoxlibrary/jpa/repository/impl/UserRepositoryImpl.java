package hn.shadowcore.mercadoxlibrary.jpa.repository.impl;

import hn.shadowcore.mercadoxcontext.utils.OrgIdContextHolder;
import hn.shadowcore.mercadoxlibrary.entity.model.auth.QUser;
import hn.shadowcore.mercadoxlibrary.entity.model.auth.User;
import hn.shadowcore.mercadoxlibrary.jpa.predicate.UserPredicateFactory;
import hn.shadowcore.mercadoxlibrary.jpa.querydsl.OrgAwareQueryFactory;
import hn.shadowcore.mercadoxlibrary.jpa.repository.CustomUserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserRepositoryImpl implements CustomUserRepository {

    private final OrgAwareQueryFactory queryFactory;

    private final UserPredicateFactory userPredicateFactory;
    private static final QUser qUser = QUser.user;

    @Override
    public User findDisabledUserById(String id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qUser, qUser.orgId)
                        .where(userPredicateFactory.isInactive()
                                .and(userPredicateFactory.equalsId(id)))
                        .fetchOne())
                .orElseThrow(() -> new ResourceNotFoundException(String
                        .format("User was not found for ID: '%s'", id)));
    }

    @Override
    public List<User> findAllEnabledOrgAdmins() {
        return Optional.of(queryFactory.selectFrom(qUser, qUser.orgId)
                .where(userPredicateFactory.isAdmin()).fetchAll().stream().toList())
                .orElseThrow(() -> new ResourceNotFoundException(String
                        .format("No admins found for ORG with ID: '%s'", OrgIdContextHolder.getTenantId())));
    }

    @Override
    public List<User> findAvailableDrivers() {
        return Optional.of(queryFactory.selectFrom(qUser, qUser.id)
                .where(userPredicateFactory.isActive()
                        .and(userPredicateFactory.isDriver())
                        .and(userPredicateFactory.isActiveDriver()))
                        .fetchAll().stream().toList())
                .orElseThrow(() -> new ResourceNotFoundException(String
                        .format("No drivers available at this moment for ORG with ID: '%s'.", OrgIdContextHolder
                                .getTenantId())));
    }
}
