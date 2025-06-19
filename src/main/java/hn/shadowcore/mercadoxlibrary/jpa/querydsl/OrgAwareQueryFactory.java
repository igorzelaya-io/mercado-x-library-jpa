package hn.shadowcore.mercadoxlibrary.jpa.querydsl;

import com.example.mercadoxcontext.utils.OrgIdContextHolder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrgAwareQueryFactory {

    private final JPAQueryFactory delegate;

    public OrgAwareQueryFactory(JPAQueryFactory delegate) {
        this.delegate = delegate;
    }

    public <T> JPAQuery<T> selectFrom(EntityPath<T> entityPath, StringPath orgIdPath) {
        final UUID tenantId = OrgIdContextHolder.getTenantId();
        return delegate.selectFrom(entityPath).where(orgIdPath.eq(tenantId.toString()));
    }

    public JPAQueryFactory raw() {
        return this.delegate;
    }
}
