package hn.shadowcore.mercadoxlibrary.jpa.repository.impl;

import hn.shadowcore.mercadoxcontext.utils.OrgIdContextHolder;
import hn.shadowcore.mercadoxlibrary.entity.model.core.Inventory;
import hn.shadowcore.mercadoxlibrary.entity.model.core.Item;
import hn.shadowcore.mercadoxlibrary.entity.model.core.QInventory;
import hn.shadowcore.mercadoxlibrary.entity.model.core.QItem;
import hn.shadowcore.mercadoxlibrary.jpa.predicate.ItemPredicateFactory;
import hn.shadowcore.mercadoxlibrary.jpa.querydsl.OrgAwareQueryFactory;
import hn.shadowcore.mercadoxlibrary.jpa.repository.CustomItemRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements CustomItemRepository {

    private static final QItem item = QItem.item;

    private final OrgAwareQueryFactory jpaQueryFactory;

    private final ItemPredicateFactory itemPredicateFactory;


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
