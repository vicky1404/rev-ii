/*==============================================================*/
/* Table: IFRS_TIPO_PARAMETRO                                   */
/*==============================================================*/
create table IFRS_TIPO_PARAMETRO  (
   ID_TIPO_PARAMETRO    NUMBER(4)                       not null,
   NOMBRE               VARCHAR(1024),
   constraint PK_IFRS_TIPO_PARAMETRO primary key (ID_TIPO_PARAMETRO)
);


/*==============================================================*/
/* Table: IFRS_PARAMETRO                                        */
/*==============================================================*/
create table IFRS_PARAMETRO  (
   NOMBRE               VARCHAR(512)                    not null,
   VALOR                VARCHAR(1024)                   not null,
   ID_TIPO_PARAMETRO    NUMBER(4)                       not null,
   constraint PK_IFRS_PARAMETRO primary key (ID_TIPO_PARAMETRO, NOMBRE)
);

alter table IFRS_PARAMETRO
   add constraint FK_IFRS_PAR_REFERENCE_IFRS_TIP foreign key (ID_TIPO_PARAMETRO)
      references IFRS_TIPO_PARAMETRO (ID_TIPO_PARAMETRO);