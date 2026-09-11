package hn.alturaforge.mercadox.library.jpa.repository.custom.impl;

import hn.alturaforge.mercadox.context.utils.OrgIdContextHolder;
import hn.alturaforge.mercadox.library.jpa.predicate.ItemPredicateFactory;
import hn.alturaforge.mercadox.library.jpa.querydsl.OrgAwareQueryFactory;
import hn.alturaforge.mercadox.library.jpa.repository.custom.CustomItemRepository;
import hn.alturaforge.mercadox.library.entity.model.core.Inventory;
import hn.alturaforge.mercadox.library.entity.model.core.Item;
import hn.alturaforge.mercadox.library.entity.model.core.QInventory;
import hn.alturaforge.mercadox.library.entity.model.core.QItem;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class CustomItemRepositoryImpl implements CustomItemRepository {

    private static final QItem item = QItem.item;

    private final OrgAwareQueryFactory jpaQueryFactory;

    private static final ItemPredicateFactory itemPredicateFactory = new ItemPredicateFactory();

    @Override
    public Item findItemWithInventory(String itemId) {
        return Optional
                .ofNullable(jpaQueryFactory
                        .selectFrom(item, item.orgId)
                        .join(item.inventory(), QInventory.inventory).fetchJoin()
                        .where(item.id.eq(UUID.fromString(itemId))).fetchOne())
                .orElseThrow(() -> new ResourceNotFoundException(String
                        .format("Item was not found for ID: '%s'", itemId)));
    }

    @Override
    public Inventory findItemInventory(String itemId) {
        final UUID orgId = UUID.fromString(OrgIdContextHolder.getTenantId());
        return Optional.ofNullable(
                jpaQueryFactory.raw().select(item.inventory())
                        .from(item)
                        .where(item.orgId.eq(orgId)
                                .and(itemPredicateFactory.equalsId(itemId)))
                        .fetchOne()
        ).orElseThrow(() -> new ResourceNotFoundException(String
                .format("Inventory was not found for Item with ID: '%s'", itemId)));
    }

}
