create sequence SEQ_VERSION_EEFF start with 1;


create table REV_CODIGO_FECU 
(
   ID_FECU              number(12)           not null,
   DESCRIPCION          VARCHAR2(256)        not null,
   VIGENCIA             number(1)            not null,
   constraint PK_REV_CODIGO_FECU primary key (ID_FECU)
);

create table REV_TIPO_ESTADO_EEFF 
(
   ID_ESTADO_EEFF       NUMBER(4)            not null,
   NOMBRE               VARCHAR2(256)        not null,
   VIGENTE              NUMBER(1)            not null,
   constraint PK_REV_TIPO_ESTADO_EEFF primary key (ID_ESTADO_EEFF)
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
	  


create table REV_RELACION_EEFF 
(
   ID_FECU              number(12)           not null,
   ID_PERIODO           NUMBER(4,0)          not null,
   ID_COLUMNA           NUMBER(8,0)          not null,
   ID_GRILLA            NUMBER(8,0),
   ID_FILA              NUMBER(4,0),
   MONTO_TOTAL          NUMBER(18,4),
   constraint PK_REV_RELACION_EEFF primary key (ID_FECU, ID_PERIODO)
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


create table REV_DETALLE_EEFF 
(
   ID_CUENTA            NUMBER(10)           not null,
   ID_FECU              number(12)           not null,
   ID_VERSION_EEFF      NUMBER(10)           not null,
   DESCRIPCION_CUENTA   VARCHAR2(256),
   MONTO_EBS            NUMBER(18,4),
   RECLASIFICACION      NUMBER(18,4),
   MONTO_PESOS          NUMBER(18,4),
   MONTO_MILES          NUMBER(18,4),
   constraint PK_REV_DETALLE_EEFF primary key (ID_CUENTA, ID_FECU, ID_VERSION_EEFF)
);

alter table REV_DETALLE_EEFF
   add constraint FK_REV_DETA_REFERENCE_REV_EEFF foreign key (ID_FECU, ID_VERSION_EEFF)
      references REV_EEFF (ID_FECU, ID_VERSION_EEFF);


create table REV_RELACION_DETALLE_EEFF 
(
   ID_CUENTA            NUMBER(10)           not null,
   ID_FECU              NUMBER(10)           not null,
   REV_ID_FECU          number(12)           not null,
   ID_PERIODO           NUMBER(4,0)          not null,
   ID_COLUMNA           NUMBER(8,0),
   ID_GRILLA            NUMBER(8,0),
   ID_FILA              NUMBER(4,0),
   DESCRIPCION_CUENTA   VARCHAR2(256),
   MONTO_EBS            NUMBER(18,4),
   RECLASIFICACION      NUMBER(18,4),
   MONTO_PESOS          NUMBER(18,4),
   MONTO_MILES          NUMBER(18,4),
   constraint PK_REV_RELACION_DETALLE_EEFF primary key (REV_ID_FECU, ID_CUENTA, ID_FECU, ID_PERIODO)
);

alter table REV_RELACION_DETALLE_EEFF
   add constraint FK_REV_RELA_REFERENCE_REV_RELA foreign key (REV_ID_FECU, ID_PERIODO)
      references REV_RELACION_EEFF (ID_FECU, ID_PERIODO);

alter table REV_RELACION_DETALLE_EEFF
   add constraint FK_REV_RELA_DET_REF_REV_CELD foreign key (ID_COLUMNA, ID_GRILLA, ID_FILA)
      references REV_CELDA (ID_COLUMNA, ID_GRILLA, ID_FILA);	  