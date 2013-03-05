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
   add constraint FK_REV_SUB__REFERENCE_REV_TIPO foreign key (ID_TIPO_CELDA)
      references REV_TIPO_CELDA (ID_TIPO_CELDA);

alter table REV_SUB_COLUMNA
   add constraint FK_REV_SUB__REFERENCE_REV_SUB_ foreign key (ID_SUB_GRILLA, ID_GRILLA)
      references REV_SUB_GRILLA (ID_SUB_GRILLA, ID_GRILLA);

alter table REV_SUB_GRILLA
   add constraint FK_REV_SUB__REFERENCE_REV_GRIL foreign key (ID_GRILLA)
      references REV_GRILLA (ID_GRILLA);

alter table REV_SUB_GRILLA
   add constraint FK_REV_SUB__REFERENCE_REV_GRUP foreign key (ID_GRUPO_ACCESO)
      references REV_GRUPO (ID_GRUPO_ACCESO);
	  
alter table REV_VERSION add CANT_GRUPOS NUMBER(2,0);
	  
CREATE SEQUENCE SEQ_SUB_COLUMNA MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER NOCYCLE ;
CREATE SEQUENCE SEQ_SUB_GRILLA MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER NOCYCLE ;


insert into REV_MENU values (24,	'Desagregar',	1,	10,	'#',	1);
insert into REV_MENU values (25,	'Desagregar cuadros',	1,	10,	'/pages/desagregacion/desagregacion.jsf',	0);
insert into REV_MENU values (29,	'Administración',	1,	10,	'/pages/desagregacion/perfilamiento.jsf',	0);

commit;