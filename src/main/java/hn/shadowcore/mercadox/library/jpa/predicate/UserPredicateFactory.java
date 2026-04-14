package hn.shadowcore.mercadox.library.jpa.predicate;

import com.querydsl.core.types.dsl.BooleanExpression;
import hn.shadowcore.mercadox.library.entity.model.auth.QUser;
import hn.shadowcore.mercadox.library.entity.model.enums.UserTypeName;

import java.util.UUID;

public class UserPredicateFactory {

    public BooleanExpression isActive(QUser qUser) {
        return qUser.enabled.isTrue();
    }

    public BooleanExpression isDriver(QUser qUser) {
        return qUser.user.userType().name.eq(UserTypeName.DRIVER);
    }

    public BooleanExpression isInactive(QUser qUser) {
        return qUser.user.enabled.isFalse();
    }

    public BooleanExpression isActiveDriver(QUser qUser) {
        return QUser.user.driveAvailable.isTrue();
    }

    public BooleanExpression equalsId(QUser qUser, String userId) {
        return QUser.user.id.eq(UUID.fromString(userId));
    }

    public BooleanExpression isAdmin(QUser qUser) {
        return QUser.user.isAdmin.isTrue();
    }

}
