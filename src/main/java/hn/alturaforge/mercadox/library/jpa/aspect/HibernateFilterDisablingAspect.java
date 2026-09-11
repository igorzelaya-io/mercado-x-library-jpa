package hn.alturaforge.mercadox.library.jpa.aspect;

import hn.alturaforge.mercadox.context.utils.OrgIdContextHolder;
import hn.alturaforge.mercadox.library.jpa.util.DisableHibernateFilters;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class HibernateFilterDisablingAspect {

    @PersistenceContext
    private EntityManager em;

    @Around("@annotation(disableFilters)")
    public Object handleMethodLevel(ProceedingJoinPoint jp, DisableHibernateFilters disableFilters) throws Throwable {
        return proceedingWithDisabledFilters(jp, disableFilters.value());
    }

    @Around("@within(disableFilters)")
    public Object handleClassLevel(ProceedingJoinPoint jp, DisableHibernateFilters disableFilters) throws Throwable {
        return proceedingWithDisabledFilters(jp, disableFilters.value());
    }

    private Object proceedingWithDisabledFilters(ProceedingJoinPoint jp, String[] filtersToDisable) throws Throwable {
        Session session = em.unwrap(Session.class);
        Map<String, Boolean> previousState = new HashMap<>();

        for(String filter : filtersToDisable) {
            boolean wasEnabled = session.getEnabledFilter(filter) != null;
            previousState.put(filter, wasEnabled);
            if(wasEnabled) {
                session.disableFilter(filter);
            }
        }
        try {
            return jp.proceed();
        }
        finally {
            for(Map.Entry<String, Boolean> entry : previousState.entrySet()) {
                if(Boolean.TRUE.equals(entry.getValue())) {
                    Filter filter = session.enableFilter(entry.getKey());

                    if(entry.getKey().equals("orgIdFilter") && OrgIdContextHolder.hasTenantId()) {
                        filter.setParameter("orgId", OrgIdContextHolder.getTenantId());
                    }
                    if(entry.getKey().equals("enabledEntityFilter")) {
                        filter.setParameter("enabled", true);
                    }
                }
            }
        }
    }
}
