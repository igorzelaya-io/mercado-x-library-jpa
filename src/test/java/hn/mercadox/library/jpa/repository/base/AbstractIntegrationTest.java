package hn.mercadox.library.jpa.repository.base;

import hn.alturaforge.mercadox.context.utils.OrgIdContextHolder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class AbstractIntegrationTest extends BaseTestEntities {

    @PersistenceContext
    protected EntityManager em;

    @BeforeEach
    void baseInit() {
        if(this.em != null) {
            em.clear();
        }
    }

    @AfterEach
    void baseClear() {
        OrgIdContextHolder.clear();
        if(em != null) {
            em.clear();
        }
    }

    @SafeVarargs
    protected final <T> void persistAll(T... entities) {
        for(T entity: entities) {
            em.persist(entity);
        }
        em.flush();
    }

}