delimiter $$

CREATE TABLE `re_remito` (
  `re_remito_k` int(11) NOT NULL AUTO_INCREMENT,
  `re_fecha` datetime DEFAULT NULL,
  `re_distribuidora_ed` int(11) DEFAULT NULL,  
  PRIMARY KEY (`re_remito_k`),  
  CONSTRAINT `re_distribuidora_ed` FOREIGN KEY (`re_distribuidora_ed`) REFERENCES `ed_editorial` (`ed_editorial_k`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=latin1$$

CREATE TABLE `ri_remito_item` (
  `ri_remito_item_k` int(10) NOT NULL AUTO_INCREMENT,
  `ri_remito_re` int(10) NOT NULL,
  `ri_catalogo_cg` int(10) DEFAULT NULL,
  `ri_cantidad` int(11) DEFAULT '1',
  `ri_nombre_libro` varchar(200) DEFAULT NULL,
  `ri_autor` varchar(45) DEFAULT NULL,
  `ri_editorial` varchar(45) DEFAULT NULL,  
  `ri_isbn` varchar(200) DEFAULT NULL,  
  `ri_motivo` varchar(200) DEFAULT NULL,  
  `ri_factura` varchar(200) DEFAULT NULL,    
  PRIMARY KEY (`ri_remito_item_k`,`ri_remito_re`),
  KEY `ri_remito_re` (`ri_remito_re`),  
  CONSTRAINT `ri_remito_re` FOREIGN KEY (`ri_remito_re`) REFERENCES `re_remito` (`re_remito_k`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1466 DEFAULT CHARSET=latin1$$
