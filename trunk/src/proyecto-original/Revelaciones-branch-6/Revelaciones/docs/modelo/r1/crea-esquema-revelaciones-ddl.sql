/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     26-03-2012 15:29:25                          */
/*==============================================================*/


alter table REV_AGRUPACION_COLUMNA
   drop constraint FK_REV_AGRU_REF_REV_COLU;

alter table REV_CAMPO_FORMULA
   drop constraint FK_REV_CAMP_REF_REV_CELD;

alter table REV_CATALOGO
   drop constraint FK_REV_CATA_REF_REV_TIPO_CU;

alter table REV_CATALOGO_GRUPO
   drop constraint FK_REV_CATA_REF_REV_CATA;

alter table REV_CATALOGO_GRUPO
   drop constraint FK_REV_CATA_REF_REV_GRUP;

alter table REV_CELDA
   drop constraint FK_REV_CELD_REF_REV_COLU;

alter table REV_CELDA
   drop constraint FK_REV_CELD_REF_REV_TIPO;

alter table REV_CELDA
   drop constraint FK_REV_CELD_REF_REV_TIPO_CELDA;

alter table REV_COLUMNA
   drop constraint FK_REV_COLU_REF_REV_GRIL;

alter table REV_ESTRUCTURA
   drop constraint FK_REV_ESTR_REF_REV_TIPO;

alter table REV_ESTRUCTURA
   drop constraint FK_REV_ESTR_REF_REV_VERS;

alter table REV_FORMULA_GRILLA
   drop constraint FK_REV_FORM_REF_REV_CAMP;

alter table REV_FORMULA_GRILLA
   drop constraint FK_REV_FORM_REF_REV_CELD;

alter table REV_FORMULA_GRILLA
   drop constraint FK_REV_FORM_REF_REV_TIPO;

alter table REV_GRILLA
   drop constraint FK_REV_GRIL_REF_REV_ESTR;

alter table REV_GRUPO
   drop constraint FK_REV_GRUP_REF_REV_AREA;

alter table REV_GRUPO
   drop constraint FK_REV_GRUP_REF_REV_GRUP;

alter table REV_HISTORIAL_VERSION
   drop constraint FK_REV_HIST_REF_REV_ESTA;

alter table REV_HISTORIAL_VERSION
   drop constraint FK_REV_HIST_REF_REV_VERS;

alter table REV_HTML
   drop constraint FK_REV_HTML_REF_REV_ESTR;

alter table REV_MENU_GRUPO
   drop constraint FK_NEW_REV_MENU_REF_REV_GRUP;

alter table REV_MENU_GRUPO
   drop constraint FK_NEW_REV_MENU_REF_REV_MENU;

alter table REV_PERIODO
   drop constraint FK_REV_PERI_REF_REV_ESTA_PER;

alter table REV_TEXTO
   drop constraint FK_REV_TEXT_REFERENCE_REV_ESTR;

alter table REV_USUARIO_GRUPO
   drop constraint FK_REV_USUA_REF_REV_GRUP;

alter table REV_VERSION
   drop constraint FK_REV_VERS_REF_REV_CATA;

alter table REV_VERSION_PERIODO
   drop constraint FK_REV_PERI_REF_REV_ESTA;

alter table REV_VERSION_PERIODO
   drop constraint FK_REV_PERI_REF_REV_PERI;

alter table REV_VERSION_PERIODO
   drop constraint FK_REV_PERI_REF_REV_VERS;

drop table REV_AGRUPACION_COLUMNA cascade constraints;

drop table REV_AREA_NEGOCIO cascade constraints;

drop table REV_CAMPO_FORMULA cascade constraints;

drop table REV_CATALOGO cascade constraints;

drop table REV_CATALOGO_GRUPO cascade constraints;

drop table REV_CELDA cascade constraints;

drop table REV_COLUMNA cascade constraints;

drop table REV_ESTADO_CUADRO cascade constraints;

drop table REV_ESTADO_PERIODO cascade constraints;

drop table REV_ESTRUCTURA cascade constraints;

drop table REV_FORMULA_GRILLA cascade constraints;

drop table REV_GRILLA cascade constraints;

drop table REV_GRUPO cascade constraints;

drop table REV_GRUPO_OID cascade constraints;

drop table REV_HISTORIAL_VERSION cascade constraints;

drop table REV_HTML cascade constraints;

drop table REV_MENU cascade constraints;

drop table REV_MENU_GRUPO cascade constraints;

drop table REV_PERIODO cascade constraints;

drop table REV_TEXTO cascade constraints;

drop table REV_TIPO_CELDA cascade constraints;

drop table REV_TIPO_CUADRO cascade constraints;

drop table REV_TIPO_DATO cascade constraints;

drop table REV_TIPO_ESTRUCTURA cascade constraints;

drop table REV_TIPO_OPERACION cascade constraints;

drop table REV_USUARIO_GRUPO cascade constraints;

drop table REV_VERSION cascade constraints;

drop table REV_VERSION_PERIODO cascade constraints;

/*==============================================================*/
/* Table: REV_AGRUPACION_COLUMNA                                */
/*==============================================================*/
create table REV_AGRUPACION_COLUMNA 
(
   ID_NIVEL             NUMBER(8,0)          not null,
   ID_COLUMNA           NUMBER(8,0)          not null,
   ID_GRILLA            NUMBER(8,0)          not null,
   TITULO               VARCHAR2(256 CHAR),
   ANCHO                NUMBER(4,0),
   GRUPO                NUMBER(4,0),
   constraint PK_REV_AGRUPACION_COLUMNA primary key (ID_COLUMNA, ID_GRILLA, ID_NIVEL)
         ENABLE
);

/*==============================================================*/
/* Table: REV_AREA_NEGOCIO                                      */
/*==============================================================*/
create table REV_AREA_NEGOCIO 
(
   ID_AREA_NEGOCIO      VARCHAR2(3)          not null,
   NOMBRE               VARCHAR2(256),
   constraint PK_NEW_REV_AREA_NEGOCIO primary key (ID_AREA_NEGOCIO)
         ENABLE
);

comment on table REV_AREA_NEGOCIO is
'TABLA QUE ALMACENA LAS AREAS DE NEGOCIO INVOLUCRADAS EN EL SISTEMA';

/*==============================================================*/
/* Table: REV_CAMPO_FORMULA                                     */
/*==============================================================*/
create table REV_CAMPO_FORMULA 
(
   ID_CAMPO_FORMULA     NUMBER               not null,
   ID_COLUMNA           NUMBER,
   ID_FILA              NUMBER(4,0),
   ID_GRILLA            NUMBER(8,0),
   constraint PK_NEW_REV_CAMPO_FORMULA primary key (ID_CAMPO_FORMULA)
         ENABLE
);

comment on table REV_CAMPO_FORMULA is
'TABLA QUE INDICA LAS CELDAS QUE SE DEBEN APLICAR UNA OPERACION';

/*==============================================================*/
/* Table: REV_CATALOGO                                          */
/*==============================================================*/
create table REV_CATALOGO 
(
   ID_CATALOGO          NUMBER(8,0)          not null,
   COD_CUADRO           NUMBER(5,0),
   COD_SUBCUADRO        NUMBER(5,0),
   NOMBRE               VARCHAR2(256 CHAR),
   TITULO               VARCHAR2(256 CHAR),
   ORDEN                NUMBER(5,2),
   VIGENCIA             NUMBER(1,0),
   ID_TIPO_CUADRO       NUMBER(4,0),
   IMPRESION_HORIZONTAL NUMBER(1),
   constraint PK_NEW_REV_CATALOGO_NOTA primary key (ID_CATALOGO)
         ENABLE
);

comment on table REV_CATALOGO is
'TABLA DE MAESTRO QUE CONTIENE EL CATALOGO DE TODAS LAS NOTAS DEL SISTEMA';

/*==============================================================*/
/* Table: REV_CATALOGO_GRUPO                                    */
/*==============================================================*/
create table REV_CATALOGO_GRUPO 
(
   ID_CATALOGO          NUMBER(8,0)          not null,
   ID_GRUPO_ACCESO      VARCHAR2(128 CHAR)   not null,
   constraint PK_NEW_REV_CATALOGO_NOTA_GRUPO primary key (ID_CATALOGO, ID_GRUPO_ACCESO)
         ENABLE
);

comment on table REV_CATALOGO_GRUPO is
'TABLA QUE ALMACENA LA ASOCIACION ENTRE EL CATALOGO DE UNA NOTA Y LOS GRUPOS QUE TIENEN ACCESO A ELLA.';

/*==============================================================*/
/* Table: REV_CELDA                                             */
/*==============================================================*/
create table REV_CELDA 
(
   ID_COLUMNA           NUMBER(8,0)          not null,
   ID_FILA              NUMBER(4,0)          not null,
   ID_TIPO_CELDA        NUMBER(4,0),
   ID_TIPO_DATO         NUMBER(2,0),
   VALOR                VARCHAR2(2048 CHAR),
   GRUPO                NUMBER(4,0),
   ID_GRILLA            NUMBER(8,0)          not null,
   GRUPO_RESULTADO      NUMBER(4,0),
   constraint PK_NEW_REV_CELDA_NOTA primary key (ID_COLUMNA, ID_GRILLA, ID_FILA)
         ENABLE
);

comment on table REV_CELDA is
'TABLA QUE ALMACENA LOS ATRIBUTOS DE LA COLUMNA DE UNA NOTA.';

/*==============================================================*/
/* Table: REV_COLUMNA                                           */
/*==============================================================*/
create table REV_COLUMNA 
(
   ID_COLUMNA           NUMBER(8,0)          not null,
   ID_GRILLA            NUMBER(8,0)          not null,
   TITULO_COLUMNA       VARCHAR2(128 CHAR),
   ORDEN                NUMBER(3,0),
   ANCHO                NUMBER(4,0),
   ROW_HEADER           NUMBER(1,0),
   constraint PK_NEW_REV_COLUMNA_NOTA primary key (ID_COLUMNA, ID_GRILLA)
         ENABLE
);

comment on table REV_COLUMNA is
'TABLA QUE CONTIENE LAS COLUMNAS DE LA GRILLA';

/*==============================================================*/
/* Table: REV_ESTADO_CUADRO                                     */
/*==============================================================*/
create table REV_ESTADO_CUADRO 
(
   ID_ESTADO_CUADRO     NUMBER(2,0)          not null,
   NOMBRE               VARCHAR2(128),
   constraint PK_NEW_REV_ESTADO_NOTA primary key (ID_ESTADO_CUADRO)
         ENABLE
);

comment on table REV_ESTADO_CUADRO is
'TABLA QUE ALMACENA LOS ESTADOS QUE PUEDE TENER UNA NOTA';

/*==============================================================*/
/* Table: REV_ESTADO_PERIODO                                    */
/*==============================================================*/
create table REV_ESTADO_PERIODO 
(
   ID_ESTADO_PERIODO    NUMBER(2,0)          not null,
   NOMBRE               VARCHAR2(128),
   constraint PK_NEW_REV_ESTADO_PERIODO primary key (ID_ESTADO_PERIODO)
         ENABLE
);

comment on table REV_ESTADO_PERIODO is
'TABLA QUE ALMACENA LOS ESTADOS QUE PUEDE TENER UN PERIODO';

/*==============================================================*/
/* Table: REV_ESTRUCTURA                                        */
/*==============================================================*/
create table REV_ESTRUCTURA 
(
   ID_ESTRUCTURA        NUMBER(8,0)          not null,
   ID_VERSION           NUMBER(8,0),
   ID_TIPO_ESTRUCTURA   NUMBER(4,0),
   ORDEN                NUMBER(5,0),
   constraint PK_NEW_REV_ESTRUCTURA_NOTA primary key (ID_ESTRUCTURA)
         ENABLE
);

comment on table REV_ESTRUCTURA is
'TABLA QUE DEFINE COMO SE ESTRUCTURA LA NOTA';

/*==============================================================*/
/* Table: REV_FORMULA_GRILLA                                    */
/*==============================================================*/
create table REV_FORMULA_GRILLA 
(
   ID_FORMULA           NUMBER               not null,
   ID_TIPO_OPERACION    NUMBER(2,0),
   ID_CAMPO_FORMULA     NUMBER,
   ID_COLUMNA           NUMBER,
   ID_FILA              NUMBER(4,0),
   ID_GRILLA            NUMBER(8,0),
   constraint PK_NEW_REV_FORMULA_GRILLA primary key (ID_FORMULA)
         ENABLE
);

comment on table REV_FORMULA_GRILLA is
'TABLA QUE CONTIENE LAS FORMULAS DE SUMAS PARA LAS GRILLAS DE LAS NOTAS';

/*==============================================================*/
/* Table: REV_GRILLA                                            */
/*==============================================================*/
create table REV_GRILLA 
(
   ID_GRILLA            NUMBER(8,0)          not null,
   TITULO               VARCHAR2(256 CHAR),
   TIPO_FORMULA         NUMBER(4,0)          default 1,
   constraint PK_NEW_REV_GRILLA_NOTA primary key (ID_GRILLA)
         ENABLE
);

comment on table REV_GRILLA is
'TABLA QUE DEFINE UNA GRILLA ';

/*==============================================================*/
/* Table: REV_GRUPO                                             */
/*==============================================================*/
create table REV_GRUPO 
(
   ID_GRUPO_ACCESO      VARCHAR2(128 CHAR)   not null,
   ID_AREA_NEGOCIO      VARCHAR2(3),
   ID_GRUPO_OID         VARCHAR2(128 CHAR),
   NOMBRE               VARCHAR2(512 CHAR),
   constraint PK_NEW_REV_GRUPO_ACCESO primary key (ID_GRUPO_ACCESO)
         ENABLE
);

comment on table REV_GRUPO is
'TABLA QUE ALMACENA EL MAESTRO CON LOS GRUPOS DE ACCESO A LA APLICACION,
CLASIFICADOS POR AREA DE NEGOCIO';

/*==============================================================*/
/* Table: REV_GRUPO_OID                                         */
/*==============================================================*/
create table REV_GRUPO_OID 
(
   ID_GRUPO_OID         VARCHAR2(128 CHAR)   not null,
   NOMBRE               VARCHAR2(512 CHAR),
   constraint PK_NEW_REV_GRUPO_OID primary key (ID_GRUPO_OID)
         ENABLE
);

comment on table REV_GRUPO_OID is
'MAESTRO CON GRUPOS CREADOS EN EL OID,
SE UTILIZA PARA NORMALIZAR LOS NOMBRES DE LOS GRUPOS PADRES DE LA APLICACION';

/*==============================================================*/
/* Table: REV_HISTORIAL_VERSION                                 */
/*==============================================================*/
create table REV_HISTORIAL_VERSION 
(
   ID_HISTORIAL         NUMBER               not null,
   ID_VERSION           NUMBER(8,0),
   ID_PERIODO           NUMBER(4,0),
   ID_ESTADO_CUADRO     NUMBER(2,0),
   FECHA_PROCESO        DATE,
   COMENTARIO           VARCHAR2(2048 CHAR),
   USUARIO              VARCHAR2(256 CHAR),
   constraint PK_REV_HISTORIAL_VERSION primary key (ID_HISTORIAL)
         ENABLE
);

comment on table REV_HISTORIAL_VERSION is
'TABLA QUE ALMACENA EL HITORIAL DE LOS CAMBIOS DE ESTADO DE UNA VERSION PARA EL PERIODO DETERMINADO';

/*==============================================================*/
/* Table: REV_HTML                                              */
/*==============================================================*/
create table REV_HTML 
(
   ID_HTML              NUMBER               not null,
   CONTENIDO            BLOB,
   TITULO               VARCHAR2(256 CHAR),
   constraint PK_NEW_REV_HTML_NOTA primary key (ID_HTML)
         ENABLE
);

/*==============================================================*/
/* Table: REV_MENU                                              */
/*==============================================================*/
create table REV_MENU 
(
   ID_MENU              NUMBER(3,0)          not null,
   NOMBRE               VARCHAR2(512),
   ESTADO               NUMBER(1,0),
   GRUPO                NUMBER(2,0),
   URL_MENU             VARCHAR2(512 CHAR),
   ES_PADRE             NUMBER(1,0)          default NULL,
   constraint PK_NEW_REV_MENU primary key (ID_MENU)
         ENABLE
);

comment on table REV_MENU is
'TABLA QUE ALMACENA LAS OPCIONES DE MENU DISPONIBLES EN LA APLICACIÓN';

/*==============================================================*/
/* Table: REV_MENU_GRUPO                                        */
/*==============================================================*/
create table REV_MENU_GRUPO 
(
   ID_MENU              NUMBER(3,0)          not null,
   ID_GRUPO_ACCESO      VARCHAR2(128 CHAR)   not null,
   constraint PK_NEW_REV_MENU_GRUPO primary key (ID_MENU, ID_GRUPO_ACCESO)
         ENABLE
);

comment on table REV_MENU_GRUPO is
'TABLA QUE ALMACENA LA ASOCIACION DE MENU CON EL GRUPO QUE POSEE EL ACCESO';

/*==============================================================*/
/* Table: REV_PERIODO                                           */
/*==============================================================*/
create table REV_PERIODO 
(
   ID_PERIODO           NUMBER(4,0)          not null,
   ID_ESTADO_PERIODO    NUMBER(2,0),
   PERIODO              NUMBER(6,0),
   constraint PK_NEW_REV_PERIODO primary key (ID_PERIODO)
         ENABLE
);

comment on table REV_PERIODO is
'TABLA QUE ALMACENA LOS PERIODOS FINANCIEROS Y RESPECTIVOS ESTADOS';

/*==============================================================*/
/* Table: REV_TEXTO                                             */
/*==============================================================*/
create table REV_TEXTO 
(
   ID_TEXTO             NUMBER(8,0)          not null,
   TEXTO                VARCHAR2(4000),
   NEGRITA              NUMBER(1,0)          default 0,
   constraint PK_NEW_REV_TEXTO_NOTA primary key (ID_TEXTO)
         ENABLE
);

comment on table REV_TEXTO is
'TABLA QUE ALMACENA LOS TEXTOS FIJOS QUE TIENE UNA NOTA';

/*==============================================================*/
/* Table: REV_TIPO_CELDA                                        */
/*==============================================================*/
create table REV_TIPO_CELDA 
(
   ID_TIPO_CELDA        NUMBER(4,0)          not null,
   NOMBRE               VARCHAR2(128 CHAR),
   constraint PK_NEW_REV_TIPO_CELDA primary key (ID_TIPO_CELDA)
         ENABLE
);

comment on table REV_TIPO_CELDA is
'TABLA QUE CONTIENE SI LA CELDA ES TITULO, SUBTOTAL, TOTAL, ETC';

/*==============================================================*/
/* Table: REV_TIPO_CUADRO                                       */
/*==============================================================*/
create table REV_TIPO_CUADRO 
(
   ID_TIPO_CUADRO       NUMBER(4,0)          not null,
   NOMBRE               VARCHAR2(256 CHAR),
   TITULO               VARCHAR2(512 CHAR),
   constraint PK_NEW_REV_TIPO_CUADRO primary key (ID_TIPO_CUADRO)
         ENABLE
);

/*==============================================================*/
/* Table: REV_TIPO_DATO                                         */
/*==============================================================*/
create table REV_TIPO_DATO 
(
   ID_TIPO_DATO         NUMBER(2,0)          not null,
   NOMBRE               VARCHAR2(64 CHAR),
   NOMBRE_CLASE         VARCHAR2(256),
   constraint PK_NEW_REV_TIPO_DATO primary key (ID_TIPO_DATO)
         ENABLE
);

comment on table REV_TIPO_DATO is
'TABLA QUE DEFINE EL TIPO DE DATO JAVA  QUE CONTIENE CADA COLUMNA';

/*==============================================================*/
/* Table: REV_TIPO_ESTRUCTURA                                   */
/*==============================================================*/
create table REV_TIPO_ESTRUCTURA 
(
   ID_TIPO_ESTRUCTURA   NUMBER(4,0)          not null,
   NOMBRE               VARCHAR2(64),
   constraint PK_NEW_REV_TIPO_ESTRUCTURA primary key (ID_TIPO_ESTRUCTURA)
         ENABLE
);

comment on table REV_TIPO_ESTRUCTURA is
'TABLA QUE CONTIENE LA DEFINICION DE LOS TIPOS DE  COMPONENTES DE LA NOTA';

/*==============================================================*/
/* Table: REV_TIPO_OPERACION                                    */
/*==============================================================*/
create table REV_TIPO_OPERACION 
(
   ID_TIPO_OPERACION    NUMBER(2,0)          not null,
   NOMBRE               VARCHAR2(128 CHAR),
   constraint PK_NEW_REV_TIPO_OPERACION primary key (ID_TIPO_OPERACION)
         ENABLE
);

/*==============================================================*/
/* Table: REV_USUARIO_GRUPO                                     */
/*==============================================================*/
create table REV_USUARIO_GRUPO 
(
   USUARIO_OID          VARCHAR2(256 CHAR)   not null,
   ID_GRUPO_ACCESO      VARCHAR2(128 CHAR)   not null,
   constraint PK_NEW_REV_USUARIO_GRUPO primary key (ID_GRUPO_ACCESO, USUARIO_OID)
         ENABLE
);

comment on table REV_USUARIO_GRUPO is
'TABLA QUE ALMACENA LA ASOCIACION ENTRE EL USUARIO Y EL GRUPO AL QUE PERTENECE';

/*==============================================================*/
/* Table: REV_VERSION                                           */
/*==============================================================*/
create table REV_VERSION 
(
   ID_VERSION           NUMBER(8,0)          not null,
   ID_CATALOGO          NUMBER(8,0),
   VERSION              NUMBER(2,0),
   VIGENCIA             NUMBER(1,0),
   FECHA_CREACION       DATE                 default SYSDATE,
   COMENTARIO           VARCHAR2(256),
   constraint PK_NEW_REV_VERSION_NOTA primary key (ID_VERSION)
         ENABLE
);

comment on table REV_VERSION is
'TABLA QUE CONTIENE LAS VERSIONES PARA CADA  NOTA';

/*==============================================================*/
/* Table: REV_VERSION_PERIODO                                   */
/*==============================================================*/
create table REV_VERSION_PERIODO 
(
   ID_VERSION           NUMBER(8,0)          not null,
   ID_PERIODO           NUMBER(4,0)          not null,
   ID_ESTADO_CUADRO     NUMBER(2,0),
   FECHA_CREACION       DATE,
   USUARIO              VARCHAR2(256 CHAR),
   FECHA_ULTIMO_PROCESO DATE,
   constraint PK_NEW_REV_PERIODO_NOTA primary key (ID_VERSION, ID_PERIODO)
         ENABLE
);

comment on table REV_VERSION_PERIODO is
'TABLA CONTIENE LOS PERIODOS PARA LAS VERSIONES DE LAS NOTAS';

alter table REV_AGRUPACION_COLUMNA
   add constraint FK_REV_AGRU_REF_REV_COLU foreign key (ID_COLUMNA, ID_GRILLA)
      references REV_COLUMNA (ID_COLUMNA, ID_GRILLA);

alter table REV_CAMPO_FORMULA
   add constraint FK_REV_CAMP_REF_REV_CELD foreign key (ID_COLUMNA, ID_GRILLA, ID_FILA)
      references REV_CELDA (ID_COLUMNA, ID_GRILLA, ID_FILA);

alter table REV_CATALOGO
   add constraint FK_REV_CATA_REF_REV_TIPO_CU foreign key (ID_TIPO_CUADRO)
      references REV_TIPO_CUADRO (ID_TIPO_CUADRO);

alter table REV_CATALOGO_GRUPO
   add constraint FK_REV_CATA_REF_REV_CATA foreign key (ID_CATALOGO)
      references REV_CATALOGO (ID_CATALOGO);

alter table REV_CATALOGO_GRUPO
   add constraint FK_REV_CATA_REF_REV_GRUP foreign key (ID_GRUPO_ACCESO)
      references REV_GRUPO (ID_GRUPO_ACCESO);

alter table REV_CELDA
   add constraint FK_REV_CELD_REF_REV_COLU foreign key (ID_COLUMNA, ID_GRILLA)
      references REV_COLUMNA (ID_COLUMNA, ID_GRILLA);

alter table REV_CELDA
   add constraint FK_REV_CELD_REF_REV_TIPO foreign key (ID_TIPO_DATO)
      references REV_TIPO_DATO (ID_TIPO_DATO);

alter table REV_CELDA
   add constraint FK_REV_CELD_REF_REV_TIPO_CELDA foreign key (ID_TIPO_CELDA)
      references REV_TIPO_CELDA (ID_TIPO_CELDA);

alter table REV_COLUMNA
   add constraint FK_REV_COLU_REF_REV_GRIL foreign key (ID_GRILLA)
      references REV_GRILLA (ID_GRILLA);

alter table REV_ESTRUCTURA
   add constraint FK_REV_ESTR_REF_REV_TIPO foreign key (ID_TIPO_ESTRUCTURA)
      references REV_TIPO_ESTRUCTURA (ID_TIPO_ESTRUCTURA);

alter table REV_ESTRUCTURA
   add constraint FK_REV_ESTR_REF_REV_VERS foreign key (ID_VERSION)
      references REV_VERSION (ID_VERSION);

alter table REV_FORMULA_GRILLA
   add constraint FK_REV_FORM_REF_REV_CAMP foreign key (ID_CAMPO_FORMULA)
      references REV_CAMPO_FORMULA (ID_CAMPO_FORMULA);

alter table REV_FORMULA_GRILLA
   add constraint FK_REV_FORM_REF_REV_CELD foreign key (ID_COLUMNA, ID_GRILLA, ID_FILA)
      references REV_CELDA (ID_COLUMNA, ID_GRILLA, ID_FILA);

alter table REV_FORMULA_GRILLA
   add constraint FK_REV_FORM_REF_REV_TIPO foreign key (ID_TIPO_OPERACION)
      references REV_TIPO_OPERACION (ID_TIPO_OPERACION);

alter table REV_GRILLA
   add constraint FK_REV_GRIL_REF_REV_ESTR foreign key (ID_GRILLA)
      references REV_ESTRUCTURA (ID_ESTRUCTURA);

alter table REV_GRUPO
   add constraint FK_REV_GRUP_REF_REV_AREA foreign key (ID_AREA_NEGOCIO)
      references REV_AREA_NEGOCIO (ID_AREA_NEGOCIO);

alter table REV_GRUPO
   add constraint FK_REV_GRUP_REF_REV_GRUP foreign key (ID_GRUPO_OID)
      references REV_GRUPO_OID (ID_GRUPO_OID);

alter table REV_HISTORIAL_VERSION
   add constraint FK_REV_HIST_REF_REV_ESTA foreign key (ID_ESTADO_CUADRO)
      references REV_ESTADO_CUADRO (ID_ESTADO_CUADRO);

alter table REV_HISTORIAL_VERSION
   add constraint FK_REV_HIST_REF_REV_VERS foreign key (ID_VERSION, ID_PERIODO)
      references REV_VERSION_PERIODO (ID_VERSION, ID_PERIODO);

alter table REV_HTML
   add constraint FK_REV_HTML_REF_REV_ESTR foreign key (ID_HTML)
      references REV_ESTRUCTURA (ID_ESTRUCTURA);

alter table REV_MENU_GRUPO
   add constraint FK_NEW_REV_MENU_REF_REV_GRUP foreign key (ID_GRUPO_ACCESO)
      references REV_GRUPO (ID_GRUPO_ACCESO);

alter table REV_MENU_GRUPO
   add constraint FK_NEW_REV_MENU_REF_REV_MENU foreign key (ID_MENU)
      references REV_MENU (ID_MENU);

alter table REV_PERIODO
   add constraint FK_REV_PERI_REF_REV_ESTA_PER foreign key (ID_ESTADO_PERIODO)
      references REV_ESTADO_PERIODO (ID_ESTADO_PERIODO);

alter table REV_TEXTO
   add constraint FK_REV_TEXT_REFERENCE_REV_ESTR foreign key (ID_TEXTO)
      references REV_ESTRUCTURA (ID_ESTRUCTURA);

alter table REV_USUARIO_GRUPO
   add constraint FK_REV_USUA_REF_REV_GRUP foreign key (ID_GRUPO_ACCESO)
      references REV_GRUPO (ID_GRUPO_ACCESO);

alter table REV_VERSION
   add constraint FK_REV_VERS_REF_REV_CATA foreign key (ID_CATALOGO)
      references REV_CATALOGO (ID_CATALOGO);

alter table REV_VERSION_PERIODO
   add constraint FK_REV_PERI_REF_REV_ESTA foreign key (ID_ESTADO_CUADRO)
      references REV_ESTADO_CUADRO (ID_ESTADO_CUADRO);

alter table REV_VERSION_PERIODO
   add constraint FK_REV_PERI_REF_REV_PERI foreign key (ID_PERIODO)
      references REV_PERIODO (ID_PERIODO);

alter table REV_VERSION_PERIODO
   add constraint FK_REV_PERI_REF_REV_VERS foreign key (ID_VERSION)
      references REV_VERSION (ID_VERSION);
	  
	  
--------------------------------------------------------
--  DDL for Sequence SEQ_CAMPO_FORMULA
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_CAMPO_FORMULA"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_CATALOGO
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_CATALOGO"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_CELDA
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_CELDA"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_COLUMNA
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_COLUMNA"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_ESTADO_CUADRO
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_ESTADO_CUADRO"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_ESTADO_PERIODO
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_ESTADO_PERIODO"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_ESTRUCTURA
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_ESTRUCTURA"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_FORMULA_GRILLA
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_FORMULA_GRILLA"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_GRILLA
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_GRILLA"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_HISTORIAL_VERSION
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_HISTORIAL_VERSION"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_HTML
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_HTML"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_MENU
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_MENU"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_PERIODO
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_PERIODO"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_TEXTO
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_TEXTO"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_TIPO_CELDA
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_TIPO_CELDA"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_TIPO_DATO
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_TIPO_DATO"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_TIPO_ESTRUCTURA
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_TIPO_ESTRUCTURA"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_TIPO_OPERACION
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_TIPO_OPERACION"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_USUARIO_GRUPO
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_USUARIO_GRUPO"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_VERSION
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_VERSION"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_VERSION_PERIODO
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_VERSION_PERIODO"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;

