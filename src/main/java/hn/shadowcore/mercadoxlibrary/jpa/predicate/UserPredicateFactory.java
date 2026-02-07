package hn.shadowcore.mercadoxlibrary.jpa.predicate;

import com.querydsl.core.types.dsl.BooleanExpression;
import hn.shadowcore.mercadoxlibrary.entity.model.auth.QUser;
import hn.shadowcore.mercadoxlibrary.entity.model.enums.UserTypeName;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
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
