/*
	FAVOR AL FINALIZAR LAS SENTENCIAS INSERT PONER CUIDADO EN PONER UN ';' (PUNTO Y COMA) AL FINAL DE LA SENTENCIA, PARA QUE NO SE CAIGA AL CORRER EL SCRIPT.
	ataitaitaipiugettttttttt!!!!!!!!!!!!!!!
*/



/*Menu Estados fianncieros*/
insert into ifrs_menu values (22,    'Estados financieros',    1,    9,    '#',    1);
insert into ifrs_menu values (23,    'Mapeador',    1,    9,    '/pages/eeff/validador-eeff.jsf',    0);
insert into ifrs_menu values (24,    'Cargador',    1,    9,    '/pages/eeff/cargador-eeff.jsf',    0);
insert into ifrs_menu values (25,    'Visualizador',    1,    9,    '/pages/eeff/visualizador-eeff.jsf',    0);

/*Menu Periodo*/
Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE) values ('2','Abrir Período','1','1','/pages/periodo/abrir-periodo.jsf','0');
Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE) values ('3','Cerrar Período','1','1','/pages/periodo/cerrar-periodo.jsf','0');
Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE) values ('1','Período','1','1','#','1');

--menu de xbrl
INSERT INTO "IFRS_MENU" (ID_MENU, NOMBRE, ESTADO, GRUPO, URL_MENU, ES_PADRE) VALUES ('24', 'XBRL', '1', '10', '#', '1');
INSERT INTO "IFRS_MENU" (ID_MENU, NOMBRE, ESTADO, GRUPO, URL_MENU, ES_PADRE) VALUES ('25', 'Navegador de Taxonomía', '1', '10', '/pages/xbrl/navegador-taxonomia.jsf', '0');


--menu de mantenedores
INSERT INTO "IFRS_MENU" (ID_MENU, NOMBRE, ESTADO, GRUPO, URL_MENU, ES_PADRE) VALUES ('27', 'Mantenedor de Areas de Negocio', '1', '8', '/pages/mantenedores/area-negocio.jsf', '0');
INSERT INTO "IFRS_MENU" (ID_MENU, NOMBRE, ESTADO, GRUPO, URL_MENU, ES_PADRE) VALUES ('28', 'Mantenedor de Grupos', '1', '8', '/pages/mantenedores/grupo.jsf', 0);

INSERT INTO "IFRS_MENU" (ID_MENU, NOMBRE, ESTADO, GRUPO, URL_MENU, ES_PADRE) VALUES ('15',	'Ingreso de Cuadro',	1,	8,	'/pages/mantenedores/ingreso-nota.jsf',	0);
INSERT INTO "IFRS_MENU" (ID_MENU, NOMBRE, ESTADO, GRUPO, URL_MENU, ES_PADRE) VALUES ('18',	'Cargador Excel',	1,	8,	'/pages/mantenedores/cargador-grilla.jsf',	0);
INSERT INTO "IFRS_MENU" (ID_MENU, NOMBRE, ESTADO, GRUPO, URL_MENU, ES_PADRE) VALUES ('16',	'Diseño de Cuadro',	1,	8,	'/pages/mantenedores/generador-estructura.jsf',	0);
INSERT INTO "IFRS_MENU" (ID_MENU, NOMBRE, ESTADO, GRUPO, URL_MENU, ES_PADRE) VALUES ('21',	'Tipo Cuadro',	1,	8,	'/pages/mantenedores/tipo-cuadro.jsf',	0);
INSERT INTO "IFRS_MENU" (ID_MENU, NOMBRE, ESTADO, GRUPO, URL_MENU, ES_PADRE) VALUES ('17',	'Mantenedor Formulas',	1,	8,	'/pages/mantenedores/mantenedor-formula.jsf',	0);
INSERT INTO "IFRS_MENU" (ID_MENU, NOMBRE, ESTADO, GRUPO, URL_MENU, ES_PADRE) VALUES ('14',	'Mantenedores',	1,	8,	'#'	1);

COMMIT;