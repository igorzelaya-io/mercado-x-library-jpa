# Graph Report - .  (2026-08-21)

## Corpus Check
- cluster-only mode — file stats not available

## Summary
- 341 nodes · 646 edges · 23 communities (20 shown, 3 thin omitted)
- Extraction: 94% EXTRACTED · 6% INFERRED · 0% AMBIGUOUS · INFERRED: 39 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `e226c989`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- OrgAwareQueryFactory
- CustomOrgRepositoryImpl.java
- H2BaseJpaIntegrationTest
- .persistAll
- BaseRepository
- H2JpaTestConfig.java
- CustomItemRepositoryImpl.java
- JpaRepository
- ItemRepositoryIntTest
- CustomNotificationTemplateRepositoryImpl.java
- OrderRepository
- JpaConfig.java
- DisableHibernateFilters
- UserRepository
- RoleRepository
- QueryDSLInfrastructureConfig.java
- HibernateFilterAspect.java
- MercadoXJpaScanningConfig.java
- AppTest
- UserNotificationPreferenceRepository
- App
- mercado-x-library-jpa
- $org.example:$mercado-x-library-jpa

## God Nodes (most connected - your core abstractions)
1. `H2BaseJpaIntegrationTest` - 24 edges
2. `OrgAwareQueryFactory` - 17 edges
3. `BaseRepository` - 17 edges
4. `H2JpaTestConfig` - 11 edges
5. `UserPredicateFactory` - 11 edges
6. `JpaConfig` - 10 edges
7. `DisableHibernateFilters` - 10 edges
8. `ItemRepositoryIntTest` - 10 edges
9. `UserRepository` - 9 edges
10. `CustomUserRepositoryImpl` - 9 edges

## Surprising Connections (you probably didn't know these)
- `CustomItemRepositoryImpl` --references--> `OrgAwareQueryFactory`  [EXTRACTED]
  src/main/java/hn/shadowcore/mercadox/library/jpa/repository/custom/impl/CustomItemRepositoryImpl.java → src/main/java/hn/shadowcore/mercadox/library/jpa/querydsl/OrgAwareQueryFactory.java
- `CustomNotificationTemplateRepositoryImpl` --references--> `OrgAwareQueryFactory`  [EXTRACTED]
  src/main/java/hn/shadowcore/mercadox/library/jpa/repository/custom/impl/CustomNotificationTemplateRepositoryImpl.java → src/main/java/hn/shadowcore/mercadox/library/jpa/querydsl/OrgAwareQueryFactory.java
- `CategoryRepository` --inherits--> `BaseRepository`  [EXTRACTED]
  src/main/java/hn/shadowcore/mercadox/library/jpa/repository/CategoryRepository.java → src/main/java/hn/shadowcore/mercadox/library/jpa/repository/BaseRepository.java
- `ItemRepository` --inherits--> `BaseRepository`  [EXTRACTED]
  src/main/java/hn/shadowcore/mercadox/library/jpa/repository/ItemRepository.java → src/main/java/hn/shadowcore/mercadox/library/jpa/repository/BaseRepository.java
- `NotificationTemplateRepository` --inherits--> `BaseRepository`  [EXTRACTED]
  src/main/java/hn/shadowcore/mercadox/library/jpa/repository/NotificationTemplateRepository.java → src/main/java/hn/shadowcore/mercadox/library/jpa/repository/BaseRepository.java

## Import Cycles
- None detected.

## Communities (23 total, 3 thin omitted)

### Community 0 - "OrgAwareQueryFactory"
Cohesion: 0.13
Nodes (19): ComparablePath, EntityPath, JPAQuery, QOrder, BooleanExpression, QUser, UserPredicateFactory, JPAQueryFactory (+11 more)

### Community 1 - "CustomOrgRepositoryImpl.java"
Cohesion: 0.12
Nodes (15): QOrganization, BooleanExpression, OrgPredicateFactory, CustomOrgRepository, Organization, CustomOrgRepositoryImpl, JPAQueryFactory, Organization (+7 more)

### Community 2 - "H2BaseJpaIntegrationTest"
Cohesion: 0.13
Nodes (18): ActiveProfiles, ContextConfiguration, ExtendWith, Shipment, CategoryRepository, Category, Repository, Repository (+10 more)

### Community 3 - ".persistAll"
Cohesion: 0.15
Nodes (11): AfterEach, SafeVarargs, AbstractIntegrationTest, BeforeEach, EntityManager, BaseTestEntities, Order, Organization (+3 more)

### Community 4 - "BaseRepository"
Cohesion: 0.15
Nodes (16): Lead, Location, NoRepositoryBean, OrderItem, OrderItemsKey, QuerydslPredicateExecutor, BaseRepository, InventoryRepository (+8 more)

### Community 5 - "H2JpaTestConfig.java"
Cohesion: 0.21
Nodes (14): ConditionalOnClass, Primary, H2JpaTestConfig, Bean, Configuration, DataSource, EnableAspectJAutoProxy, EntityManager (+6 more)

### Community 6 - "CustomItemRepositoryImpl.java"
Cohesion: 0.17
Nodes (11): QItem, ItemPredicateFactory, BooleanExpression, CustomItemRepository, Inventory, Item, CustomItemRepositoryImpl, Inventory (+3 more)

### Community 7 - "JpaRepository"
Cohesion: 0.20
Nodes (11): Conversation, ConversationChannel, ConversationMessage, JpaRepository, OrganizationWhatsAppConfig, ConversationMessageRepository, Repository, ConversationRepository (+3 more)

### Community 8 - "ItemRepositoryIntTest"
Cohesion: 0.20
Nodes (11): Lock, Query, ItemRepository, Item, Repository, ItemRepositoryIntTest, BeforeEach, Category (+3 more)

### Community 9 - "CustomNotificationTemplateRepositoryImpl.java"
Cohesion: 0.19
Nodes (12): QNotificationTemplate, CustomNotificationTemplateRepository, NotificationTemplate, TemplateChannel, CustomNotificationTemplateRepositoryImpl, NotificationTemplate, Override, RequiredArgsConstructor (+4 more)

### Community 10 - "OrderRepository"
Cohesion: 0.20
Nodes (8): CustomOrderRepository, Order, OrderStatus, Order, Repository, OrderRepository, Test, OrderRepositoryIntTest

### Community 11 - "JpaConfig.java"
Cohesion: 0.24
Nodes (12): ConditionalOnProperty, Bean, Configuration, DataSource, EnableAspectJAutoProxy, EnableTransactionManagement, EntityManagerFactory, Import (+4 more)

### Community 12 - "DisableHibernateFilters"
Cohesion: 0.30
Nodes (9): Around, ProceedingJoinPoint, Retention, HibernateFilterDisablingAspect, Aspect, Component, EntityManager, DisableHibernateFilters (+1 more)

### Community 13 - "UserRepository"
Cohesion: 0.29
Nodes (5): CustomUserRepository, User, Repository, User, UserRepository

### Community 14 - "RoleRepository"
Cohesion: 0.39
Nodes (5): Role, Repository, RoleRepository, Test, RoleRepositoryIntTest

### Community 15 - "QueryDSLInfrastructureConfig.java"
Cohesion: 0.42
Nodes (6): Bean, Configuration, EntityManager, JPAQueryFactory, Profile, QueryDSLInfrastructureConfig

### Community 16 - "HibernateFilterAspect.java"
Cohesion: 0.43
Nodes (6): Before, Slf4j, HibernateFilterAspect, Aspect, Component, EntityManager

### Community 17 - "MercadoXJpaScanningConfig.java"
Cohesion: 0.52
Nodes (6): ComponentScan, EnableJpaRepositories, EntityScan, Configuration, EnableTransactionManagement, MercadoXJpaScanningConfig

### Community 18 - "AppTest"
Cohesion: 0.38
Nodes (3): AppTest, Test, TestCase

### Community 19 - "UserNotificationPreferenceRepository"
Cohesion: 0.60
Nodes (4): Repository, TemplateChannel, UserNotificationPreferenceRepository, UserNotificationPreference

## Knowledge Gaps
- **2 isolated node(s):** `mercado-x-library-jpa`, `$org.example:$mercado-x-library-jpa`
  These have ≤1 connection - possible missing edges or undocumented components.
- **3 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `BaseRepository` connect `BaseRepository` to `CustomOrgRepositoryImpl.java`, `H2BaseJpaIntegrationTest`, `JpaRepository`, `ItemRepositoryIntTest`, `CustomNotificationTemplateRepositoryImpl.java`, `OrderRepository`, `UserRepository`, `RoleRepository`, `UserNotificationPreferenceRepository`?**
  _High betweenness centrality (0.297) - this node is a cross-community bridge._
- **Why does `H2BaseJpaIntegrationTest` connect `H2BaseJpaIntegrationTest` to `CustomOrgRepositoryImpl.java`, `.persistAll`, `ItemRepositoryIntTest`, `OrderRepository`, `RoleRepository`?**
  _High betweenness centrality (0.251) - this node is a cross-community bridge._
- **Why does `OrgAwareQueryFactory` connect `OrgAwareQueryFactory` to `CustomNotificationTemplateRepositoryImpl.java`, `H2JpaTestConfig.java`, `CustomItemRepositoryImpl.java`, `QueryDSLInfrastructureConfig.java`?**
  _High betweenness centrality (0.208) - this node is a cross-community bridge._
- **What connects `mercado-x-library-jpa`, `$org.example:$mercado-x-library-jpa` to the rest of the system?**
  _2 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `OrgAwareQueryFactory` be split into smaller, more focused modules?**
  _Cohesion score 0.12762762762762764 - nodes in this community are weakly interconnected._
- **Should `CustomOrgRepositoryImpl.java` be split into smaller, more focused modules?**
  _Cohesion score 0.12433862433862433 - nodes in this community are weakly interconnected._
- **Should `H2BaseJpaIntegrationTest` be split into smaller, more focused modules?**
  _Cohesion score 0.12615384615384614 - nodes in this community are weakly interconnected._