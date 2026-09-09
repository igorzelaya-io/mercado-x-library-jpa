CREATE TABLE core.leads (
                            id UUID PRIMARY KEY,
                            lead_status VARCHAR(50) NOT NULL,
                            user_name VARCHAR(255) NOT NULL,
                            org_name VARCHAR(255) NOT NULL,
                            org_id UUID NOT NULL,
                            email VARCHAR(255) NOT NULL,
                            phone_number VARCHAR(50),
                            message TEXT, CONSTRAINT fk_leads_org
                                FOREIGN KEY(org_id)
                                    REFERENCES auth.organization(id)
                        );
