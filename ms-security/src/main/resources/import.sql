INSERT INTO usuario (accounnoloked,accountnoexpired,credentialnoexpired,enabled,name,password,user_name) VALUES (true,true,true,true,'Administrador sistema','$2a$10$MuIhEtc.2TF9uKY4FSCRXuF743ltABEC7t20s3znvKohmagsC92B.','wilber')
INSERT INTO roles (id, role_name) VALUES (1, 'ADMIN')
INSERT INTO permission (id, name) VALUES (1, 'READ')
INSERT INTO role_permision (role_id, permission_id) VALUES (1, 1)
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);