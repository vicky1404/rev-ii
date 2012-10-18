--------------------------------------------------------
-- Archivo creado  - jueves-octubre-18-2012   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table IFRS_XBRL_CONCEPTO_CELDA
--------------------------------------------------------

  CREATE TABLE "IFRS_XBRL_CONCEPTO_CELDA" 
   (	"ID_CONCEPTO_XBRL" VARCHAR2(1024 CHAR), 
	"ID_COLUMNA" NUMBER(8,0), 
	"ID_GRILLA" NUMBER(8,0), 
	"ID_FILA" NUMBER(4,0), 
	"ID_TAXONOMIA" NUMBER(8,0)
   ) ;
--------------------------------------------------------
--  DDL for Table IFRS_XBRL_CONCEPTO_CODIGO_FECU
--------------------------------------------------------

  CREATE TABLE "IFRS_XBRL_CONCEPTO_CODIGO_FECU" 
   (	"ID_FECU" NUMBER(12,0), 
	"ID_VERSION_EEFF" NUMBER(10,0), 
	"ID_TAXONOMIA" NUMBER(8,0), 
	"ID_CONCEPTO_XBRL" VARCHAR2(1024 CHAR)
   ) ;

   COMMENT ON TABLE "IFRS_XBRL_CONCEPTO_CODIGO_FECU"  IS 'TABLA QUE ALMACENA LA RELACION DE MAPEO ENTRE UN CONCEPTO XBRL Y UN CODIGO FECU DEL ESTADO FINANCIERO';
--------------------------------------------------------
--  DDL for Table IFRS_XBRL_RANGO_CODIGO_FECU
--------------------------------------------------------

  CREATE TABLE "IFRS_XBRL_RANGO_CODIGO_FECU" 
   (	"ID_INFORME_EEFF" VARCHAR2(256 CHAR), 
	"CODIGO_FECU_DESDE" NUMBER(12,0), 
	"CODIGO_FECU_HASTA" NUMBER(12,0)
   ) ;

   COMMENT ON TABLE "IFRS_XBRL_RANGO_CODIGO_FECU"  IS 'TABLA QUE ALMACENA LOS RANGOS DE CODIGOS FECU 
QUE SE DEBEN MOSTRAR PARA CADA INFORME';
--------------------------------------------------------
--  DDL for Table IFRS_XBRL_TAXONOMIA
--------------------------------------------------------

  CREATE TABLE "IFRS_XBRL_TAXONOMIA" 
   (	"ID_TAXONOMIA" NUMBER(8,0), 
	"NOMBRE" VARCHAR2(1024 CHAR), 
	"TOP_TAXONOMY" VARCHAR2(1024 CHAR), 
	"NOMBRE_ARCHIVO" VARCHAR2(1024 CHAR), 
	"URI" VARCHAR2(1024 CHAR), 
	"VIGENTE" NUMBER(1,0), 
	"FECHA_TAXONOMIA" DATE, 
	"USUARIO_CREACION" VARCHAR2(256 CHAR), 
	"USUARIO_EDICION" VARCHAR2(256 CHAR), 
	"FECHA_CREACION" DATE, 
	"FECHA_EDICION" DATE
   ) ;

Insert into IFRS_XBRL_CONCEPTO_CELDA (ID_CONCEPTO_XBRL,ID_COLUMNA,ID_GRILLA,ID_FILA,ID_TAXONOMIA) values ('cl-cs_DeterioroSiniestros',2,2341,2,2);
Insert into IFRS_XBRL_CONCEPTO_CELDA (ID_CONCEPTO_XBRL,ID_COLUMNA,ID_GRILLA,ID_FILA,ID_TAXONOMIA) values ('cl-cs_DeterioroPrimas',2,2341,1,2);
Insert into IFRS_XBRL_CONCEPTO_CELDA (ID_CONCEPTO_XBRL,ID_COLUMNA,ID_GRILLA,ID_FILA,ID_TAXONOMIA) values ('cl-cs_DeterioroActivoReaseguro',2,2341,3,2);
Insert into IFRS_XBRL_CONCEPTO_CELDA (ID_CONCEPTO_XBRL,ID_COLUMNA,ID_GRILLA,ID_FILA,ID_TAXONOMIA) values ('cl-cs_OtrosDeteriorosSeguros',2,2341,4,2);
Insert into IFRS_XBRL_CONCEPTO_CELDA (ID_CONCEPTO_XBRL,ID_COLUMNA,ID_GRILLA,ID_FILA,ID_TAXONOMIA) values ('cl-cs_DeterioroSeguros',2,2341,5,2);
Insert into IFRS_XBRL_TAXONOMIA (ID_TAXONOMIA,NOMBRE,TOP_TAXONOMY,NOMBRE_ARCHIVO,URI,VIGENTE,FECHA_TAXONOMIA,USUARIO_CREACION,USUARIO_EDICION,FECHA_CREACION,FECHA_EDICION) values (2,'Borrador Estados Financieros y Notas','cl-cs_cor_2012-07-17.xsd','cl-cs_shell_2012-07-17.xsd','file://eq13830/taxonomias/2012-07-17/taxonomia-svs/eeff-y-notas/cl-cs_shell_2012-07-17.xsd',1,to_date('17/07/12','DD/MM/RR'),'USUARIO.PRUEBA','USUARIO.PRUEBA',to_date('17/07/12','DD/MM/RR'),to_date('21/08/12','DD/MM/RR'));
Insert into IFRS_XBRL_TAXONOMIA (ID_TAXONOMIA,NOMBRE,TOP_TAXONOMY,NOMBRE_ARCHIVO,URI,VIGENTE,FECHA_TAXONOMIA,USUARIO_CREACION,USUARIO_EDICION,FECHA_CREACION,FECHA_EDICION) values (1,'Estados Financieros','cl-cs_cor_2012-01-02.xsd','cl-cs_shell_2012-01-02.xsd','file://eq13830/taxonomias/2012-01-02/taxonomia-svs/eeff/cl-cs_shell_2012-01-02.xsd',1,to_date('02/01/12','DD/MM/RR'),'USUARIO.PRUEBA','USUARIO.PRUEBA',to_date('02/01/12','DD/MM/RR'),to_date('21/08/12','DD/MM/RR'));
--------------------------------------------------------
--  DDL for Index PK_IFRS_XBRL_CONCEPTO_CELDA
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_IFRS_XBRL_CONCEPTO_CELDA" ON "IFRS_XBRL_CONCEPTO_CELDA" ("ID_CONCEPTO_XBRL", "ID_COLUMNA", "ID_GRILLA", "ID_FILA") 
  ;
--------------------------------------------------------
--  DDL for Index PK_IFRS_XBRL_CONCEPTO_CODIGO_FE
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_IFRS_XBRL_CONCEPTO_CODIGO_FE" ON "IFRS_XBRL_CONCEPTO_CODIGO_FECU" ("ID_FECU", "ID_VERSION_EEFF", "ID_CONCEPTO_XBRL") 
  ;
--------------------------------------------------------
--  DDL for Index PK_IFRS_XBRL_RANGO_CODIGO_FECU
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_IFRS_XBRL_RANGO_CODIGO_FECU" ON "IFRS_XBRL_RANGO_CODIGO_FECU" ("ID_INFORME_EEFF") 
  ;
--------------------------------------------------------
--  DDL for Index PK_IFRS_XBRL_TAXONOMIA
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_IFRS_XBRL_TAXONOMIA" ON "IFRS_XBRL_TAXONOMIA" ("ID_TAXONOMIA") 
  ;
--------------------------------------------------------
--  Constraints for Table IFRS_XBRL_CONCEPTO_CELDA
--------------------------------------------------------

  ALTER TABLE "IFRS_XBRL_CONCEPTO_CELDA" ADD CONSTRAINT "PK_IFRS_XBRL_CONCEPTO_CELDA" PRIMARY KEY ("ID_CONCEPTO_XBRL", "ID_COLUMNA", "ID_GRILLA", "ID_FILA") ENABLE;
  ALTER TABLE "IFRS_XBRL_CONCEPTO_CELDA" MODIFY ("ID_FILA" NOT NULL ENABLE);
  ALTER TABLE "IFRS_XBRL_CONCEPTO_CELDA" MODIFY ("ID_GRILLA" NOT NULL ENABLE);
  ALTER TABLE "IFRS_XBRL_CONCEPTO_CELDA" MODIFY ("ID_COLUMNA" NOT NULL ENABLE);
  ALTER TABLE "IFRS_XBRL_CONCEPTO_CELDA" MODIFY ("ID_CONCEPTO_XBRL" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table IFRS_XBRL_CONCEPTO_CODIGO_FECU
--------------------------------------------------------

  ALTER TABLE "IFRS_XBRL_CONCEPTO_CODIGO_FECU" ADD CONSTRAINT "PK_IFRS_XBRL_CONCEPT_COD_FE" PRIMARY KEY ("ID_FECU", "ID_VERSION_EEFF", "ID_CONCEPTO_XBRL") ENABLE;
  ALTER TABLE "IFRS_XBRL_CONCEPTO_CODIGO_FECU" MODIFY ("ID_CONCEPTO_XBRL" NOT NULL ENABLE);
  ALTER TABLE "IFRS_XBRL_CONCEPTO_CODIGO_FECU" MODIFY ("ID_TAXONOMIA" NOT NULL ENABLE);
  ALTER TABLE "IFRS_XBRL_CONCEPTO_CODIGO_FECU" MODIFY ("ID_VERSION_EEFF" NOT NULL ENABLE);
  ALTER TABLE "IFRS_XBRL_CONCEPTO_CODIGO_FECU" MODIFY ("ID_FECU" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table IFRS_XBRL_RANGO_CODIGO_FECU
--------------------------------------------------------

  ALTER TABLE "IFRS_XBRL_RANGO_CODIGO_FECU" ADD CONSTRAINT "PK_IFRS_XBRL_RANGO_COD_FECU" PRIMARY KEY ("ID_INFORME_EEFF") ENABLE;
  ALTER TABLE "IFRS_XBRL_RANGO_CODIGO_FECU" MODIFY ("ID_INFORME_EEFF" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table IFRS_XBRL_TAXONOMIA
--------------------------------------------------------

  ALTER TABLE "IFRS_XBRL_TAXONOMIA" ADD CONSTRAINT "PK_IFRS_XBRL_TAXONOMIA" PRIMARY KEY ("ID_TAXONOMIA") ENABLE;
  ALTER TABLE "IFRS_XBRL_TAXONOMIA" MODIFY ("ID_TAXONOMIA" NOT NULL ENABLE);
--------------------------------------------------------
--  Ref Constraints for Table IFRS_XBRL_CONCEPTO_CELDA
--------------------------------------------------------

  ALTER TABLE "IFRS_XBRL_CONCEPTO_CELDA" ADD CONSTRAINT "FK_IFRS_XBRL_REF_REV_CELD" FOREIGN KEY ("ID_COLUMNA", "ID_GRILLA", "ID_FILA")
	  REFERENCES "IFRS_CELDA" ("ID_COLUMNA", "ID_GRILLA", "ID_FILA") ENABLE;
  ALTER TABLE "IFRS_XBRL_CONCEPTO_CELDA" ADD CONSTRAINT "FK_IFRS_XBRL_REF_IFRS_XBRL" FOREIGN KEY ("ID_TAXONOMIA")
	  REFERENCES "IFRS_XBRL_TAXONOMIA" ("ID_TAXONOMIA") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table IFRS_XBRL_CONCEPTO_CODIGO_FECU
--------------------------------------------------------

  ALTER TABLE "IFRS_XBRL_CONCEPTO_CODIGO_FECU" ADD CONSTRAINT "FK_IFRS_XBRL_REV_EEFF" FOREIGN KEY ("ID_FECU", "ID_VERSION_EEFF")
	  REFERENCES "REV_EEFF" ("ID_FECU", "ID_VERSION_EEFF") ENABLE;
  ALTER TABLE "IFRS_XBRL_CONCEPTO_CODIGO_FECU" ADD CONSTRAINT "FK_IFRS_XBRL_IFRS_XBRL" FOREIGN KEY ("ID_TAXONOMIA")
	  references "IFRS_XBRL_TAXONOMIA" ("ID_TAXONOMIA") enable;
