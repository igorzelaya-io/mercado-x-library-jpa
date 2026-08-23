CREATE SEQUENCE IF NOT EXISTS core.notification_template_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE core.notification_template (
                                            id BIGINT DEFAULT nextval('core.notification_template_id_seq') PRIMARY KEY,
                                            template_key VARCHAR(100) NOT NULL,
                                            channel VARCHAR(50) NOT NULL,
                                            language_code VARCHAR(10) NOT NULL,
                                            org_id UUID NOT NULL,
                                            subject VARCHAR(255),
                                            whatsapp_template_name VARCHAR(100),
                                            body_html TEXT,
                                            active BOOLEAN,
                                            system_template BOOLEAN,
                                            created_at TIMESTAMP,
                                            updated_at TIMESTAMP,
                                            CONSTRAINT fk_notification_template_org
                                                FOREIGN KEY (org_id)
                                                    REFERENCES auth.organization(id),
                                            CONSTRAINT uq_template_unique
                                                UNIQUE(org_id, template_key, channel, language_code)
);
