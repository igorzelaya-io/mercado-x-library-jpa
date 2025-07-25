package hn.shadowcore.mercadoxlibrary.jpa.predicate;

import com.querydsl.core.types.dsl.BooleanExpression;
import hn.shadowcore.mercadoxlibrary.entity.model.auth.QUser;
import hn.shadowcore.mercadoxlibrary.entity.model.enums.UserTypeName;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserPredicateFactory {

    public BooleanExpression isActive() {
        return QUser.user.enabled.isTrue();
    }

    public BooleanExpression isDriver() {
        return QUser.user.userType().name.eq(UserTypeName.DRIVER);
    }

    public BooleanExpression isInactive() {
        return QUser.user.enabled.isFalse();
    }

    public BooleanExpression isActiveDriver() {
        return QUser.user.driveAvailable.isTrue();
    }

    public BooleanExpression equalsId(String userId) {
        return QUser.user.id.eq(UUID.fromString(userId));
    }

    public BooleanExpression isAdmin() {
        return QUser.user.isAdmin.isTrue();
    }

}
