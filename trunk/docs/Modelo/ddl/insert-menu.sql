/*Menu Estados fianncieros*/
insert into ifrs_menu values (22,    'Estados financieros',    1,    9,    '#',    1);
insert into ifrs_menu values (23,    'Mapeador',    1,    9,    '/pages/eeff/validador-eeff.jsf',    0);
insert into ifrs_menu values (24,    'Cargador',    1,    9,    '/pages/eeff/cargador-eeff.jsf',    0);
insert into ifrs_menu values (25,    'Visualizador',    1,    9,    '/pages/eeff/visualizador-eeff.jsf',    0);

/*Menu Periodo*/
Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE) values ('2','Abrir Período','1','1','/pages/periodo/abrir-periodo.jsf','0');
Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE) values ('3','Cerrar Período','1','1','/pages/periodo/cerrar-periodo.jsf','0');
Insert into IFRS_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE) values ('1','Período','1','1','#','1');
