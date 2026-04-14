package hn.shadowcore.mercadox.library.jpa.querydsl;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.ComparablePath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hn.shadowcore.mercadox.context.utils.OrgIdContextHolder;

import java.util.UUID;


public class OrgAwareQueryFactory {

    private final JPAQueryFactory delegate;

    public OrgAwareQueryFactory(JPAQueryFactory delegate) {
        this.delegate = delegate;
    }

    public <T> JPAQuery<T> selectFrom(EntityPath<T> entityPath, ComparablePath<UUID> orgIdPath) {
        final UUID tenantId = UUID.fromString(OrgIdContextHolder.getTenantId());
        return delegate.selectFrom(entityPath).where(orgIdPath.eq(tenantId));
    }

    public JPAQueryFactory raw() {
        return this.delegate;
    }
}
