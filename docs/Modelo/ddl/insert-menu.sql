/*
	FAVOR AL FINALIZAR LAS SENTENCIAS INSERT PONER CUIDADO EN PONER UN ';' (PUNTO Y COMA) AL FINAL DE LA SENTENCIA, PARA QUE NO SE CAIGA AL CORRER EL SCRIPT.
	ataitaitaipiugettttttttt!!!!!!!!!!!!!!!
*/

/*
para agregar una opcion de menu, utilizen el id principal del grupo ejemplo ID: 6 Estados Financieros y a la opcion pongante el id 63
luego para asignar el ordenamiento utilizen el campo orden.
*/
DELETE FROM IFRS_MENU_GRUPO;
DELETE FROM IFRS_MENU;

--menu de configuracion	
Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('3','Configuración','1','3','#','1',200);
	Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('31','Configurador y Diseñador de Cuadros','1','3','/pages/disenador/principal.jsf','0',201);
	Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('32','Configurador de Fórmulas','1','3','/pages/mantenedores/mantenedor-formula.jsf','0',202);

--menu de EE.FF
Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('6','Estados Financieros','1','6','#','1',300);	
	insert into IFRS_MENU values (61,'Mapeador', 1, 6, '/pages/eeff/validador-eeff.jsf',0,301);
	insert into IFRS_MENU values (62,'Cargador', 1, 6, '/pages/eeff/cargador-eeff.jsf',0,302);
	insert into IFRS_MENU values (63,'Visualizador', 1, 6, '/pages/eeff/visualizador-eeff.jsf',0,303);

--menu de xbrl
Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('7','XBRL','1','7','#','1',350);
	Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('71','Navegador de Taxonomías','1','7','/pages/xbrl/navegador-taxonomia.jsf','0',351);
	Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('72','Mantenedor de Taxonomías','1','7','/pages/xbrl/mantenedor-taxonomia.jsf','0',352);	
	
--menu de Período
Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('1','Período','1','1','#','1',400);
	Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('11','Abrir Período','1','1','/pages/periodo/abrir-periodo.jsf','0',401);
	Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('12','Cerrar Período','1','1','/pages/periodo/cerrar-periodo.jsf','0',402);	

--flujo de aprobacion
Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('8','Workflow de Aprobación','1','8','#','1',500);
	Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('81','Flujo','1','8','/pages/aprobacion/flujo-aprobacion.jsf','0',501);
	
--perfilamiento	
Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('4','Perfilamiento','1','4','#','1',600);
	Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('41','Usuario por Grupo','1','4','/pages/perfilamiento/usuario-por-grupo.jsf','0',601);
	Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('42','Revelaciones por Grupo','1','4','/pages/perfilamiento/estructura-por-grupo.jsf','0',602);
	Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('43','Menú de acceso por Grupo','1','4','/pages/perfilamiento/menu-por-grupo.jsf','0',603);
	Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('44','Bloqueo de ingreso de Información','1','4','/pages/perfilamiento/bloqueo-por-grupo.jsf','0',604);
	Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('45','Grupo por Empresa','1','4','/pages/perfilamiento/grupo-por-empresa.jsf','0',605);
	Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('46','Administración de Usuarios','1','4','/pages/perfilamiento/administracion-de-usuario.jsf','0',606);

--menu de Reportes
Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('5','Reportes','1','5','#','1',700);
	Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('51','Exportar a MS Word y Ms Excel','1','5','/pages/reportes/exportar-cuadro.jsf','0',701);
	Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('52','Historial de Generación de Reportes MS Word','1','5','/pages/reportes/historial-reporte.jsf','0',702);	
	
--menu de mantenedores
Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE, ORDEN) values ('2','Mantenedores','1','2','#','1',800);
	INSERT INTO IFRS_MENU (ID_MENU, NOMBRE, ESTADO, GRUPO, URL_MENU, ES_PADRE, ORDEN) VALUES ('22', 'Áreas de Negocio', '1', '2', '/pages/mantenedores/area-negocio.jsf', '0',803);
	INSERT INTO IFRS_MENU (ID_MENU, NOMBRE, ESTADO, GRUPO, URL_MENU, ES_PADRE, ORDEN) VALUES ('23', 'Grupos de Usuario', '1', '2', '/pages/mantenedores/grupo.jsf', '0', 804);
	INSERT INTO IFRS_MENU (ID_MENU, NOMBRE, ESTADO, GRUPO, URL_MENU, ES_PADRE, ORDEN) VALUES ('24', 'Catálogo de Revelaciones',1, 2, '/pages/mantenedores/ingreso-nota.jsf', '0', 802);
	INSERT INTO IFRS_MENU (ID_MENU, NOMBRE, ESTADO, GRUPO, URL_MENU, ES_PADRE, ORDEN) VALUES ('25', 'Tipo Revelación',	1,	2,	'/pages/mantenedores/tipo-cuadro.jsf', '0',	801);

insert into ifrs_menu_grupo values (4,	'GRP_ADMIN');
insert into ifrs_menu_grupo values (43, 'GRP_ADMIN');

COMMIT;

