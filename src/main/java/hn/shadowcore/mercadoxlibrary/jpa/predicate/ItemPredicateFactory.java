package hn.shadowcore.mercadoxlibrary.jpa.predicate;


import com.querydsl.core.types.dsl.BooleanExpression;
import hn.shadowcore.mercadoxlibrary.entity.model.core.QItem;

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
