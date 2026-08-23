CREATE TABLE auth.user_notification_preference (
                                                    id UUID PRIMARY KEY,
                                                    user_id UUID NOT NULL,
                                                    channel VARCHAR(50) NOT NULL,
                                                    enabled BOOLEAN NOT NULL,
                                                    CONSTRAINT fk_user_notification_preference_user
                                                        FOREIGN KEY (user_id)
                                                            REFERENCES auth.users(id),
                                                    CONSTRAINT uq_user_notification_preference
                                                        UNIQUE(user_id, channel)
);
