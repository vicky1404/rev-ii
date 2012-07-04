alter table IFRS_USUARIO
add ID_ROL  varchar2(256) NULL; 
      
create table IFRS_ROL  (
   ID_ROL               VARCHAR(256)                    not null,
   NOMBRE_ROL           VARCHAR(512),
   constraint PK_IFRS_ROL primary key (ID_ROL)
);

comment on table IFRS_ROL is
'TABLA QUE DEFINE LO ROLES DISPONIBLES EN EL SISTEMA.';

alter table IFRS_USUARIO
   add constraint FK_IFRS_USU_REFERENCE_IFRS_ROL foreign key (ID_ROL)
      references IFRS_ROL (ID_ROL);
