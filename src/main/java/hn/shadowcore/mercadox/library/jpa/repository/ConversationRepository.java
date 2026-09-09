package hn.shadowcore.mercadox.library.jpa.repository;

import hn.shadowcore.mercadox.library.entity.model.ai.Conversation;
import hn.shadowcore.mercadox.library.entity.model.enums.ConversationChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    Optional<Conversation> findByOrgIdAndChannelAndExternalContactId(
            UUID orgId,
            ConversationChannel channel,
            String externalContactId
    );

}
