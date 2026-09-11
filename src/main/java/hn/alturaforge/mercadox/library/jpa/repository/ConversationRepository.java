package hn.alturaforge.mercadox.library.jpa.repository;

import hn.alturaforge.mercadox.library.entity.model.ai.Conversation;
import hn.alturaforge.mercadox.library.entity.model.enums.ConversationChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    Optional<Conversation> findByOrgIdAndChannelAndExternalContactId(
            UUID orgId,
            ConversationChannel channel,
            String externalContactId
    );

    // Cascades to conversation_message at the DB level (V34's fk_conversation_message_conversation
    // is ON DELETE CASCADE), so purging the conversation row is enough to purge its transcript too.
    long deleteByLastInboundAtBefore(Instant cutoff);

}
