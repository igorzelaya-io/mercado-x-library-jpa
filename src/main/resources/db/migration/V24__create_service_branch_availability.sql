CREATE TABLE core.service_branch_availability (
                                                  service_id UUID NOT NULL,
                                                  branch_id UUID NOT NULL,
                                                  PRIMARY KEY (service_id, branch_id),
                                                  CONSTRAINT fk_service_branch_service
                                                      FOREIGN KEY (service_id)
                                                          REFERENCES core.service(id),
                                                  CONSTRAINT fk_service_branch_branch
                                                      FOREIGN KEY (branch_id)
                                                          REFERENCES auth.branch(id)
);
