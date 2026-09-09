CREATE TABLE core.appointment (
                                  id UUID PRIMARY KEY,
                                  service_id UUID,
                                  user_id UUID,
                                  branch_id UUID,
                                  CONSTRAINT fk_appointment_service
                                      FOREIGN KEY (service_id)
                                          REFERENCES core.service(id),
                                  CONSTRAINT fk_appointment_user
                                      FOREIGN KEY (user_id)
                                          REFERENCES auth.users(id),
                                  CONSTRAINT fk_appointment_branch
                                      FOREIGN KEY (branch_id)
                                          REFERENCES auth.branch(id)
);
