﻿CREATE TABLE IF NOT EXISTS usuario_permissao (
  id_usuario bigint(20) NOT NULL,
  id_permissao bigint(20) NOT NULL,
  PRIMARY KEY (id_usuario,id_permissao),
  KEY fk_usuario_permission_permission (id_permissao),
  CONSTRAINT fk_usuario_permission FOREIGN KEY (id_usuario) REFERENCES usuarios (id),
  CONSTRAINT fk_usuario_permission_permission FOREIGN KEY (id_permissao) REFERENCES permissao (id)
) ENGINE=InnoDB;