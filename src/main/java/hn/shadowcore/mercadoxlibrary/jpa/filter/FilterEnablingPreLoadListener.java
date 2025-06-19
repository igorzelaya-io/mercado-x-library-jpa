package hn.shadowcore.mercadoxlibrary.jpa.filter;

import com.example.mercadoxcontext.utils.OrgIdContextHolder;
import org.hibernate.Session;
import org.hibernate.event.spi.PreLoadEvent;
import org.hibernate.event.spi.PreLoadEventListener;

import java.util.Optional;

public class FilterEnablingPreLoadListener implements PreLoadEventListener {

    @Override
    public void onPreLoad(PreLoadEvent preLoadEvent) {
        Session session = preLoadEvent.getSession();

        if(Optional.ofNullable(session.getEnabledFilter("enabledEntityFilter")).isEmpty()) {
            session.enableFilter("softDeleteFilter")
                    .setParameter("enabled", true);
        }
        if(Optional.ofNullable(session.getEnabledFilter("orgIdFilter")).isEmpty()
                && OrgIdContextHolder.hasTenantId()) {
                session.enableFilter("orgIdFilter")
                        .setParameter("orgId", OrgIdContextHolder.getTenantId());
            }

    }

}
