package hn.shadowcore.mercadox.library.jpa.predicate;


import com.querydsl.core.types.dsl.BooleanExpression;
import hn.shadowcore.mercadox.library.entity.model.core.QItem;

import java.util.UUID;

public class ItemPredicateFactory {

    public BooleanExpression inStock() {
        return QItem.item.inStock.isTrue()
                .and(QItem.item.unitQuantity.gt(0));
    }

    public BooleanExpression equalsId(String itemId) {
        return QItem.item.id.eq(UUID.fromString(itemId));
    }

}
