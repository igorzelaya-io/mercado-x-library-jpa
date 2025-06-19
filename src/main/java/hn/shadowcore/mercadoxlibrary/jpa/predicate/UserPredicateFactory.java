package hn.shadowcore.mercadoxlibrary.jpa.predicate;

import com.querydsl.core.types.dsl.BooleanExpression;
import hn.shadowcore.mercadoxlibrary.entity.model.auth.QUser;
import org.springframework.stereotype.Component;

@Component
public class UserPredicateFactory {

    public BooleanExpression isActive() {
        return QUser.user.enabled.isTrue();
    }

}
