package hn.alturaforge.mercadox.library.jpa.predicate;

import com.querydsl.core.types.dsl.BooleanExpression;
import hn.alturaforge.mercadox.library.entity.model.auth.QOrganization;

import java.util.UUID;

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
