package hn.shadowcore.mercadoxlibrary.jpa.predicate;

import com.querydsl.core.types.dsl.BooleanExpression;
import hn.shadowcore.mercadoxlibrary.entity.model.auth.QOrganization;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrgPredicateFactory {
    public BooleanExpression isActive() {
        return QOrganization.organization.enabled.isTrue();
    }

    public BooleanExpression matchId(String id) {
        return QOrganization.organization.id.eq(UUID.fromString(id));
    }

    public BooleanExpression isInactive() {
        return QOrganization.organization.enabled.isFalse();
    }
}
