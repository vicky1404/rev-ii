create table EXFIDA_TIPO_PARAMETRO  (
   ID_TIPO_PARAMETRO    NUMBER(4)                       not null,
   NOMBRE               VARCHAR(1024),
   constraint PK_EXFIDA_TIPO_PARAMETRO primary key (ID_TIPO_PARAMETRO)
);

create table EXFIDA_PARAMETRO  (
   NOMBRE               VARCHAR(512)                    not null,
   VALOR                VARCHAR(1024)                   not null,
   ID_TIPO_PARAMETRO    NUMBER(4)                       not null,
   constraint PK_EXFIDA_PARAMETRO primary key (ID_TIPO_PARAMETRO, NOMBRE)
);

alter table EXFIDA_PARAMETRO
   add constraint FK_EXFIDA_P_REFERENCE_EXFIDA_T foreign key (ID_TIPO_PARAMETRO)
      references EXFIDA_TIPO_PARAMETRO (ID_TIPO_PARAMETRO);