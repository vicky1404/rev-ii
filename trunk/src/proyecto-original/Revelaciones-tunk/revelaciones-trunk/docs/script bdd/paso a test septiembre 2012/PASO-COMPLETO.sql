ALTER TABLE REV_CATALOGO add VALIDAR_EEFF NUMBER(1) default 0;

ALTER TABLE REV_VERSION add VALIDADO_EEFF NUMBER(1) default 0;

alter table REV_VERSION add CANT_GRUPOS NUMBER(2);

create table REV_TIPO_ESTADO_EEFF 
(
   ID_ESTADO_EEFF       NUMBER(4)            not null,
   NOMBRE               VARCHAR2(256)        not null,
   VIGENTE              NUMBER(1)            not null,
   constraint PK_REV_TIPO_ESTADO_EEFF primary key (ID_ESTADO_EEFF)
);

create table REV_CODIGO_FECU 
(
   ID_FECU              number(12)           not null,
   DESCRIPCION          VARCHAR2(256)        not null,
   VIGENCIA             number(1)            not null,
   constraint PK_REV_CODIGO_FECU primary key (ID_FECU)
);

create table REV_CUENTA_CONTABLE 
(
   ID_CUENTA            NUMBER(10)           not null,
   DESCRIPCION          VARCHAR2(256)        not null,
   VIGENCIA             number(1),
   constraint PK_REV_CUENTA_CONTABLE primary key (ID_CUENTA)
);

	  

create table REV_VERSION_EEFF 
(
   ID_VERSION_EEFF      NUMBER(10)           not null,
   ID_PERIODO           NUMBER(4,0)          not null,
   ID_ESTADO_EEFF       NUMBER(4),
   VERSION              NUMBER(4)            not null,
   VIGENCIA             NUMBER(1)            not null,
   USUARIO              VARCHAR2(256),
   FECHA                DATE,
   constraint PK_REV_VERSION_EEFF primary key (ID_VERSION_EEFF)
);

alter table REV_VERSION_EEFF
   add constraint FK_REV_VERS_REFERENCE_REV_PERI foreign key (ID_PERIODO)
      references REV_PERIODO (ID_PERIODO);

alter table REV_VERSION_EEFF
   add constraint FK_REV_VERS_REFERENCE_REV_TIPO foreign key (ID_ESTADO_EEFF)
      references REV_TIPO_ESTADO_EEFF (ID_ESTADO_EEFF);

create table REV_EEFF 
(
   ID_FECU              number(12)           not null,
   ID_VERSION_EEFF      NUMBER(10)           not null,
   MONTO_TOTAL          NUMBER(18,4),
   constraint PK_REV_EEFF primary key (ID_FECU, ID_VERSION_EEFF)
);

alter table REV_EEFF
   add constraint FK_REV_EEFF_REFERENCE_REV_VERS foreign key (ID_VERSION_EEFF)
      references REV_VERSION_EEFF (ID_VERSION_EEFF);

alter table REV_EEFF
   add constraint FK_REV_EEFF_REFERENCE_REV_CODI foreign key (ID_FECU)
      references REV_CODIGO_FECU (ID_FECU);
	  
create table REV_DETALLE_EEFF 
(
   ID_CUENTA            NUMBER(10)           not null,
   ID_FECU              number(12)           not null,
   ID_VERSION_EEFF      NUMBER(10)           not null,
   MONTO_EBS            NUMBER(18,4),
   MONTO_RECLASIFICACION      NUMBER(18,4),
   MONTO_PESOS          NUMBER(18,4),
   MONTO_MILES          NUMBER(18,4),
   MONTO_PESOS_MIL      NUMBER(18,4),
   constraint PK_REV_DETALLE_EEFF primary key (ID_CUENTA, ID_FECU, ID_VERSION_EEFF)
);

alter table REV_DETALLE_EEFF
   add constraint FK_REV_DETA_REFERENCE_REV_EEFF foreign key (ID_FECU, ID_VERSION_EEFF)
      references REV_EEFF (ID_FECU, ID_VERSION_EEFF);

alter table REV_DETALLE_EEFF
   add constraint FK_REV_DETA_REFERENCE_REV_CUEN foreign key (ID_CUENTA)
      references REV_CUENTA_CONTABLE (ID_CUENTA);


create table REV_RELACION_EEFF 
(
   ID_FECU              number(12)           not null,
   ID_PERIODO           NUMBER(4,0)          not null,
   ID_COLUMNA           NUMBER(8,0)          not null,
   ID_GRILLA            NUMBER(8,0)          not null,
   ID_FILA              NUMBER(4,0)          not null,
   MONTO_TOTAL          NUMBER(18,4),
   constraint PK_REV_RELACION_EEFF primary key (ID_FECU, ID_PERIODO, ID_COLUMNA, ID_GRILLA, ID_FILA)
);

alter table REV_RELACION_EEFF
   add constraint FK_REV_RELA_EEFF_REF_REV_CELD foreign key (ID_COLUMNA, ID_GRILLA, ID_FILA)
      references REV_CELDA (ID_COLUMNA, ID_GRILLA, ID_FILA);

alter table REV_RELACION_EEFF
   add constraint FK_REV_RELA_REFERENCE_REV_PERI foreign key (ID_PERIODO)
      references REV_PERIODO (ID_PERIODO);

alter table REV_RELACION_EEFF
   add constraint FK_REV_RELA_REFERENCE_REV_CODI foreign key (ID_FECU)
      references REV_CODIGO_FECU (ID_FECU);


create table REV_RELACION_DETALLE_EEFF 
(
   ID_CUENTA            NUMBER(10)           not null,
   ID_FECU              number(12)           not null,
   ID_PERIODO           NUMBER(4,0)          not null,
   ID_COLUMNA           NUMBER(8,0)          not null,
   ID_GRILLA            NUMBER(8,0)          not null,
   ID_FILA              NUMBER(4,0)          not null,
   MONTO_EBS            NUMBER(18,4),
   MONTO_RECLASIFICACION      NUMBER(18,4),
   MONTO_PESOS          NUMBER(18,4),
   MONTO_MILES          NUMBER(18,4),
   MONTO_PESOS_MIL      NUMBER(18,4),
   constraint PK_REV_RELACION_DETALLE_EEFF primary key (ID_FECU, ID_CUENTA, ID_PERIODO, ID_COLUMNA, ID_GRILLA, ID_FILA)
);

alter table REV_RELACION_DETALLE_EEFF
   add constraint FK_REV_RELA_DET_REF_REV_CELD foreign key (ID_COLUMNA, ID_GRILLA, ID_FILA)
      references REV_CELDA (ID_COLUMNA, ID_GRILLA, ID_FILA);

alter table REV_RELACION_DETALLE_EEFF
   add constraint FK_REV_RELA_REFERENCE_REV_PERI foreign key (ID_PERIODO)
      references REV_PERIODO (ID_PERIODO);

alter table REV_RELACION_DETALLE_EEFF
   add constraint FK_REV_REL_REF_REV_CUEN foreign key (ID_CUENTA)
      references REV_CUENTA_CONTABLE (ID_CUENTA);

	  

/*==============================================================*/
/* Table: REV_SUB_AGRUPACION_COLUMNA                            */
/*==============================================================*/
create table REV_SUB_AGRUPACION_COLUMNA 
(
   ID_NIVEL             NUMBER(8,0)          not null,
   ID_SUB_GRILLA        NUMBER(8,0)          not null,
   ID_GRILLA            NUMBER(8,0)          not null,
   ID_SUB_COLUMNA       NUMBER(8,0)          not null,
   TITULO               VARCHAR2(256 CHAR),
   ANCHO                NUMBER(4,0),
   GRUPO                NUMBER(4,0),
   constraint PK_REV_SUB_AGRUPACION_COLUMNA primary key (ID_NIVEL, ID_SUB_COLUMNA, ID_SUB_GRILLA, ID_GRILLA)
         ENABLE
);

/*==============================================================*/
/* Table: REV_SUB_CELDA                                         */
/*==============================================================*/
create table REV_SUB_CELDA 
(
   ID_SUB_GRILLA        NUMBER(8,0)          not null,
   ID_GRILLA            NUMBER(8,0)          not null,
   ID_SUB_COLUMNA       NUMBER(8,0)          not null,
   ID_SUB_FILA          NUMBER(4,0)          not null,
   ID_TIPO_DATO         NUMBER(2,0),
   ID_TIPO_CELDA        NUMBER(4,0),
   VALOR                VARCHAR2(2048 CHAR),
   CHILD_HORIZONTAL     NUMBER(4,0),
   PARENT_HORIZONTAL    NUMBER(4,0),
   CHILD_VERTICAL       NUMBER(4,0),
   PARENT_VERTICAL      NUMBER(4,0),
   FORMULA              VARCHAR2(256),
   constraint PK_REV_SUB_CELDA primary key (ID_SUB_GRILLA, ID_GRILLA, ID_SUB_COLUMNA, ID_SUB_FILA)
         ENABLE
);

comment on table REV_SUB_CELDA is
'TABLA QUE ALMACENA LOS ATRIBUTOS DE LA COLUMNA DE UNA NOTA.';

/*==============================================================*/
/* Table: REV_SUB_COLUMNA                                       */
/*==============================================================*/
create table REV_SUB_COLUMNA 
(
   ID_SUB_COLUMNA       NUMBER(8,0)          not null,
   ID_SUB_GRILLA        NUMBER(8,0)          not null,
   ID_GRILLA            NUMBER(8,0)          not null,
   TITULO_COLUMNA       VARCHAR2(128 CHAR),
   ORDEN                NUMBER(3,0),
   ANCHO                NUMBER(4,0),
   ROW_HEADER           NUMBER(1,0),
   constraint PK_REV_SUB_COLUMNA primary key (ID_SUB_COLUMNA, ID_SUB_GRILLA, ID_GRILLA)
         ENABLE
);

comment on table REV_SUB_COLUMNA is
'TABLA QUE CONTIENE LAS COLUMNAS DE LA GRILLA';

/*==============================================================*/
/* Table: REV_SUB_GRILLA                                        */
/*==============================================================*/
create table REV_SUB_GRILLA 
(
   ID_SUB_GRILLA        NUMBER(8,0)          not null,
   ID_GRILLA            NUMBER(8,0)          not null,
   ID_GRUPO_ACCESO      VARCHAR2(128 CHAR),
   TITULO               VARCHAR2(256 CHAR),
   TIPO_FORMULA         NUMBER(4,0)          default 1,
   AGRUPADOR            NUMBER(8,0),
   constraint PK_REV_SUB_GRILLA primary key (ID_SUB_GRILLA, ID_GRILLA)
         ENABLE
);

comment on table REV_SUB_GRILLA is
'TABLA QUE DEFINE UNA SUB GRILLA ';


alter table REV_SUB_AGRUPACION_COLUMNA
   add constraint FK_REV_SUB__REFERENCE_REV_SUB_ foreign key (ID_SUB_COLUMNA, ID_SUB_GRILLA, ID_GRILLA)
      references REV_SUB_COLUMNA (ID_SUB_COLUMNA, ID_SUB_GRILLA, ID_GRILLA);

alter table REV_SUB_CELDA
   add constraint FK_REV_SUB__REFERENCE_REV_SUB_ foreign key (ID_SUB_COLUMNA, ID_SUB_GRILLA, ID_GRILLA)
      references REV_SUB_COLUMNA (ID_SUB_COLUMNA, ID_SUB_GRILLA, ID_GRILLA);

alter table REV_SUB_CELDA
   add constraint FK_REV_SUB__REFERENCE_REV_TIPO foreign key (ID_TIPO_DATO)
      references REV_TIPO_DATO (ID_TIPO_DATO);

alter table REV_SUB_CELDA
   add constraint FK_REV_SUB_CEL_REF_REV_TIPO foreign key (ID_TIPO_CELDA)
      references REV_TIPO_CELDA (ID_TIPO_CELDA);

alter table REV_SUB_COLUMNA
   add constraint FK_REV_SUB_CEL_REF_REV_SUB_ foreign key (ID_SUB_GRILLA, ID_GRILLA)
      references REV_SUB_GRILLA (ID_SUB_GRILLA, ID_GRILLA);

alter table REV_SUB_GRILLA
   add constraint FK_REV_SUB__REFERENCE_REV_GRIL foreign key (ID_GRILLA)
      references REV_GRILLA (ID_GRILLA);

alter table REV_SUB_GRILLA
   add constraint FK_REV_SUB__REFERENCE_REV_GRUP foreign key (ID_GRUPO_ACCESO)
      references REV_GRUPO (ID_GRUPO_ACCESO);
	  
	  
CREATE SEQUENCE SEQ_SUB_COLUMNA MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER NOCYCLE ;
CREATE SEQUENCE SEQ_SUB_GRILLA MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER NOCYCLE ;

Insert into REV_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE) values ('21','Estado Financieros','1','9','#','1');
Insert into REV_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE) values ('22','Cargar EEFF','1','9','/pages/eeff/cargador-eeff.jsf','0');
Insert into REV_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE) values ('23','Mapeo Cuadros -  EEFF','1','9','/pages/eeff/validador-eeff.jsf','0');
Insert into REV_MENU (ID_MENU,NOMBRE,ESTADO,GRUPO,URL_MENU,ES_PADRE) values ('24','Visualizar - EEFF','1','9','/pages/eeff/visualizador-eeff.jsf','0');

insert into REV_MENU values (25,	'Desagregar',	1,	10,	'#',	1);
insert into REV_MENU values (26,	'Desagregar cuadros',	1,	10,	'/pages/desagregacion/desagregacion.jsf',	0);
insert into REV_MENU values (27,	'Administración',	1,	10,	'/pages/desagregacion/perfilamiento.jsf',	0);

insert into REV_TIPO_ESTADO_EEFF values (1,'INGRESADA',1);

commit;
