CREATE TABLE core.notification_template_variables (
                                                      notification_template_id BIGINT NOT NULL,
                                                      variable VARCHAR(255),
                                                      variable_order INT NOT NULL,
                                                      CONSTRAINT pk_template_vars
                                                          PRIMARY KEY (notification_template_id, variable_order),
                                                      CONSTRAINT fk_template_vars_template
                                                          FOREIGN KEY (notification_template_id)
                                                              REFERENCES core.notification_template(id)
);
