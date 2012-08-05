BEGIN
  FOR c IN
  (SELECT c.owner, c.table_name, c.constraint_name
   FROM user_constraints c, user_tables t
   WHERE c.table_name = t.table_name
   AND c.status = 'ENABLED'
   ORDER BY c.constraint_type DESC)
  LOOP
    dbms_utility.exec_ddl_statement('alter table "' || c.owner || '"."' || c.table_name || '" disable constraint ' || c.constraint_name);
  END LOOP;
END;
/

ALTER TABLE IFRS.IFRS_PERIODO
MODIFY(ID_PERIODO NUMBER(6));


ALTER TABLE IFRS.IFRS_PERIODO DROP COLUMN PERIODO;


create table IFRS_PERIODO_EMPRESA  (
   ID_PERIODO           NUMBER(6)                       not null,
   ID_RUT               NUMBER(10)                       not null,
   ID_ESTADO_PERIODO    NUMBER(2,0),
   constraint PK_IFRS_PERIODO_EMP primary key (ID_PERIODO, ID_RUT)
         enable
);

comment on table IFRS_PERIODO_EMPRESA is
'TABLA QUE ALMACENA LOS PERIODOS FINANCIEROS Y RESPECTIVOS ESTADOS';

alter table IFRS_PERIODO_EMPRESA
   add constraint FK_IFRS_PER_REFERENCE_IFRS_EMP foreign key (ID_RUT)
      references IFRS_EMPRESA (RUT);

alter table IFRS_PERIODO_EMPRESA
   add constraint FK_IFRS_PER_REFERENCE_IFRS_EST foreign key (ID_ESTADO_PERIODO)
      references IFRS_ESTADO_PERIODO (ID_ESTADO_PERIODO);

alter table IFRS_PERIODO_EMPRESA
   add constraint FK_IFRS_PER_REFERENCE_IFRS_PER foreign key (ID_PERIODO)
      references IFRS_PERIODO (ID_PERIODO
	  
UPDATE IFRS_PERIODO SET ID_PERIODO = 201112 WHERE ID_PERIODO = 1;

UPDATE IFRS_PERIODO SET ID_PERIODO = 201203 WHERE ID_PERIODO = 3;

COMMIT;

ALTER TABLE IFRS.IFRS_VERSION
MODIFY(ID_PERIODO NUMBER(6));


ALTER TABLE IFRS.IFRS_VERSION
 ADD (ID_RUT  NUMBER(10));


UPDATE IFRS_VERSION SET ID_PERIODO = 201112 WHERE ID_PERIODO = 1;

UPDATE IFRS_VERSION  SET ID_PERIODO = 201203 WHERE ID_PERIODO = 3;


COMMIT;


 ALTER TABLE IFRS_VERSION  DROP CONSTRAINT FK_IFRS_VER_REFERENCE_IFRS_PER;
 
 
 alter table IFRS_VERSION
   add constraint FK_IFRS_VER_REFERENCE_IFRS_PER foreign key (ID_PERIODO, ID_RUT)
      references IFRS_PERIODO_EMPRESA (ID_PERIODO, ID_RUT);
      
      
      
ALTER TABLE IFRS_HISTORIAL_REPORTE  DROP CONSTRAINT FK_IFRS_HIS_REFERENCE_IFRS_PER;

ALTER TABLE IFRS.IFRS_HISTORIAL_REPORTE
 ADD (ID_RUT  NUMBER(10));
 
 ALTER TABLE IFRS.IFRS_HISTORIAL_REPORTE
MODIFY(ID_PERIODO NUMBER(10));
      
      
 alter table IFRS_HISTORIAL_REPORTE
   add constraint FK_IFRS_HIS_REFERENCE_IFRS_PER foreign key (ID_PERIODO, ID_RUT)
      references IFRS_PERIODO_EMPRESA (ID_PERIODO, ID_RUT);
      
 
UPDATE IFRS_HISTORIAL_REPORTE SET ID_PERIODO = 201112 WHERE ID_PERIODO = 1;

UPDATE IFRS_HISTORIAL_REPORTE SET ID_PERIODO = 201203 WHERE ID_PERIODO = 3;

COMMIT



ALTER TABLE IFRS_VERSION_EEFF  DROP CONSTRAINT FK_IFRS_VER_REFERENCE_IFRS_PE2;

ALTER TABLE IFRS.IFRS_VERSION_EEFF
 ADD (ID_RUT  NUMBER(10));
 
 ALTER TABLE IFRS.IFRS_VERSION_EEFF
MODIFY(ID_PERIODO NUMBER(10));
      
      
alter table IFRS_VERSION_EEFF
   add constraint FK_IFRS_VER_REFERENCE_IFRS_PE2 foreign key (ID_PERIODO, ID_RUT)
      references IFRS_PERIODO_EMPRESA (ID_PERIODO, ID_RUT);

 
UPDATE IFRS_VERSION_EEFF SET ID_PERIODO = 201112 WHERE ID_PERIODO = 1;

UPDATE IFRS_VERSION_EEFF SET ID_PERIODO = 201203 WHERE ID_PERIODO = 3;

COMMIT;


ALTER TABLE IFRS_RELACION_EEFF  DROP CONSTRAINT FK_IFRS_REL_REFERENCE_IFRS_PE2;

ALTER TABLE IFRS.IFRS_RELACION_EEFF
 ADD (ID_RUT  NUMBER(10));
 
 ALTER TABLE IFRS.IFRS_RELACION_EEFF
MODIFY(ID_PERIODO NUMBER(10));
      
      
alter table IFRS_RELACION_EEFF
   add constraint FK_IFRS_REL_REFERENCE_IFRS_PE2 foreign key (ID_PERIODO, ID_RUT)
      references IFRS_PERIODO_EMPRESA (ID_PERIODO, ID_RUT);
      
 
UPDATE IFRS_RELACION_EEFF SET ID_PERIODO = 201112 WHERE ID_PERIODO = 1;

UPDATE IFRS_RELACION_EEFF SET ID_PERIODO = 201203 WHERE ID_PERIODO = 3;

COMMIT;



ALTER TABLE IFRS_RELACION_DETALLE_EEFF  DROP CONSTRAINT FK_IFRS_REL_REFERENCE_IFRS_PER;

ALTER TABLE IFRS.IFRS_RELACION_DETALLE_EEFF
 ADD (ID_RUT  NUMBER(10));
 
 ALTER TABLE IFRS.IFRS_RELACION_DETALLE_EEFF
MODIFY(ID_PERIODO NUMBER(10));
      
      
alter table IFRS_RELACION_DETALLE_EEFF
   add constraint FK_IFRS_REL_REFERENCE_IFRS_PER foreign key (ID_PERIODO, ID_RUT)
      references IFRS_PERIODO_EMPRESA (ID_PERIODO, ID_RUT);
      
 
UPDATE IFRS_RELACION_DETALLE_EEFF SET ID_PERIODO = 201112 WHERE ID_PERIODO = 1;

UPDATE IFRS_RELACION_DETALLE_EEFF SET ID_PERIODO = 201203 WHERE ID_PERIODO = 3;

COMMIT;


--EN ESTA PARTE DEBEN REMPLAZAR POR LOS RUT QUE TIENEN 


UPDATE IFRS_PERIODO_EMPRESA SET ID_RUT = 15505123;

UPDATE IFRS_VERSION SET ID_RUT = 15505123;

UPDATE IFRS_HISTORIAL_REPORTE SET ID_RUT = 15505123;

UPDATE IFRS_VERSION_EEFF SET ID_RUT = 15505123;

UPDATE IFRS_RELACION_EEFF SET ID_RUT = 15505123;

UPDATE IFRS_RELACION_DETALLE_EEFF SET ID_RUT = 15505123;

INSERT INTO IFRS_PERIODO_EMPRESA VALUES(201203, 15505123, 0);

INSERT INTO IFRS_PERIODO_EMPRESA VALUES(201112, 15505123, 1);

UPDATE IFRS_VERSION SET ID_RUT = 15505123;

UPDATE IFRS_HISTORIAL_REPORTE SET ID_RUT = 15505123;

UPDATE IFRS_VERSION_EEFF SET ID_RUT = 15505123;

UPDATE IFRS_RELACION_EEFF SET ID_RUT = 15505123;

UPDATE IFRS_RELACION_DETALLE_EEFF SET ID_RUT = 15505123;

COMMIT;

BEGIN
  FOR c IN
  (SELECT c.owner, c.table_name, c.constraint_name
   FROM user_constraints c, user_tables t
   WHERE c.table_name = t.table_name
   AND c.status = 'DISABLED'
   ORDER BY c.constraint_type)
  LOOP
    dbms_utility.exec_ddl_statement('alter table "' || c.owner || '"."' || c.table_name || '" enable constraint ' || c.constraint_name);
  END LOOP;
END;
/