alter table IFRS_USUARIO
add NOMBRE  varchar2(256 CHAR) NULL; 

alter table IFRS_USUARIO
add APELLIDO_PATERNO  varchar2(256 CHAR) NULL; 

alter table IFRS_USUARIO
add APELLIDO_MATERNO  varchar2(256 CHAR) NULL;     

--correr este alter para funcionalidad de cambio de clave inicial
ALTER TABLE IFRS_USUARIO 
ADD (CAMBIAR_PASSWORD NUMBER(1) );

UPDATE IFRS_USUARIO SET CAMBIAR_PASSWORD = 0;  
commit;
--fin funcionalidad de cambio de clave inicial
	  