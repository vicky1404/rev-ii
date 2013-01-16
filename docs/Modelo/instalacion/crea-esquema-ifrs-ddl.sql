/*==============================================================*/
/* DBMS name:      ORACLE Version 10g                           */
/* Created on:     15/01/2013 11:58:18                          */
/*==============================================================*/


alter table IFRS_AGRUPACION_COLUMNA
   drop constraint FK_REV_AGRU_REF_REV_COLU;

alter table IFRS_AREA_NEGOCIO
   drop constraint FK_IFRS_ARE_REFERENCE_IFRS_EMP;

alter table IFRS_CATALOGO
   drop constraint FK_REV_CATA_REF_REV_TIPO_CU;

alter table IFRS_CATALOGO
   drop constraint FK_IFRS_CAT_REFERENCE_IFRS_EMP;

alter table IFRS_CATALOGO_GRUPO
   drop constraint FK_REV_CATA_REF_REV_CATA;

alter table IFRS_CATALOGO_GRUPO
   drop constraint FK_REV_CATA_REF_REV_GRUP;

alter table IFRS_CELDA
   drop constraint FK_REV_CELD_REF_REV_COLU;

alter table IFRS_CELDA
   drop constraint FK_REV_CELD_REF_REV_TIPO;

alter table IFRS_CELDA
   drop constraint FK_REV_CELD_REF_REV_TIPO_CELDA;

alter table IFRS_COLUMNA
   drop constraint FK_REV_COLU_REF_REV_GRIL;

alter table IFRS_DETALLE_EEFF
   drop constraint FK_IFRS_DET_REFERENCE_IFRS_EEF;

alter table IFRS_DETALLE_EEFF
   drop constraint FK_IFRS_DET_REFERENCE_IFRS_CUE;

alter table IFRS_EEFF
   drop constraint FK_IFRS_EEF_REFERENCE_IFRS_VER;

alter table IFRS_EEFF
   drop constraint FK_IFRS_EEF_REFERENCE_IFRS_COD;

alter table IFRS_ESTRUCTURA
   drop constraint FK_REV_ESTR_REF_REV_TIPO;

alter table IFRS_ESTRUCTURA
   drop constraint FK_REV_ESTR_REF_REV_VERS;

alter table IFRS_GRILLA
   drop constraint FK_REV_GRIL_REF_REV_ESTR;

alter table IFRS_GRUPO
   drop constraint FK_REV_GRUP_REF_REV_AREA;

alter table IFRS_GRUPO_EMPRESA
   drop constraint FK_IFRS_GRU_REFERENCE_IFRS_GRU;

alter table IFRS_GRUPO_EMPRESA
   drop constraint FK_IFRS_GRU_REFERENCE_IFRS_EMP;

alter table IFRS_HISTORIAL_REPORTE
   drop constraint FK_IFRS_HIS_REFERENCE_IFRS_PER;

alter table IFRS_HISTORIAL_REPORTE
   drop constraint FK_IFRS_HIS_REFERENCE_IFRS_USU;

alter table IFRS_HISTORIAL_VERSION
   drop constraint FK_REV_HIST_REF_REV_ESTA;

alter table IFRS_HISTORIAL_VERSION
   drop constraint FK_IFRS_HIS_REFERENCE_IFRS_VER;

alter table IFRS_HISTORIAL_VERSION
   drop constraint FK_IFRS_HIS_REFERENCE_IFRS_US2;

alter table IFRS_HTML
   drop constraint FK_REV_HTML_REF_REV_ESTR;

alter table IFRS_MENU_GRUPO
   drop constraint FK_NEW_REV_MENU_REF_REV_GRUP;

alter table IFRS_MENU_GRUPO
   drop constraint FK_NEW_REV_MENU_REF_REV_MENU;

alter table IFRS_PARAMETRO
   drop constraint FK_IFRS_PAR_REFERENCE_IFRS_TIP;

alter table IFRS_PERIODO
   drop constraint FK_IFRS_PER_REF_IFRS_EST_P;

alter table IFRS_PERIODO_EMPRESA
   drop constraint FK_IFRS_PER_REFERENCE_IFRS_EMP;

alter table IFRS_PERIODO_EMPRESA
   drop constraint FK_IFRS_PER_REFERENCE_IFRS_EST;

alter table IFRS_PERIODO_EMPRESA
   drop constraint FK_IFRS_PER_REFERENCE_IFRS_PER;

alter table IFRS_RELACION_DETALLE_EEFF
   drop constraint FK_IFRS_REL_CONST_REL_IFRS_COD;

alter table IFRS_RELACION_DETALLE_EEFF
   drop constraint FK_REV_RELA_DET_REF_REV_CELD;

alter table IFRS_RELACION_DETALLE_EEFF
   drop constraint FK_IFRS_REL_REFERENCE_IFRS_PER;

alter table IFRS_RELACION_DETALLE_EEFF
   drop constraint FK_IFRS_REL_REFERENCE_IFRS_CUE;

alter table IFRS_RELACION_EEFF
   drop constraint FK_IFRS_REL_CONS_REL__IFRS_COD;

alter table IFRS_RELACION_EEFF
   drop constraint FK_REV_RELA_EEFF_REF_REV_CELD;

alter table IFRS_RELACION_EEFF
   drop constraint FK_IFRS_REL_REFERENCE_IFRS_PE2;

alter table IFRS_TEXTO
   drop constraint FK_REV_TEXT_REFERENCE_REV_ESTR;

alter table IFRS_USUARIO
   drop constraint FK_IFRS_USU_REFERENCE_IFRS_ROL;

alter table IFRS_USUARIO_GRUPO
   drop constraint FK_REV_USUA_REF_REV_GRUP;

alter table IFRS_USUARIO_GRUPO
   drop constraint FK_IFRS_USU_REFERENCE_IFRS_USU;

alter table IFRS_VERSION
   drop constraint FK_REV_VERS_REF_REV_CATA;

alter table IFRS_VERSION
   drop constraint FK_IFRS_VER_REFERENCE_IFRS_PER;

alter table IFRS_VERSION
   drop constraint FK_IFRS_VER_REFERENCE_IFRS_EST;

alter table IFRS_VERSION_EEFF
   drop constraint FK_IFRS_VER_REFERENCE_IFRS_PE2;

alter table IFRS_VERSION_EEFF
   drop constraint FK_IFRS_VER_REFERENCE_IFRS_TIP;

drop table IFRS_AGRUPACION_COLUMNA cascade constraints;

drop table IFRS_AREA_NEGOCIO cascade constraints;

drop table IFRS_CATALOGO cascade constraints;

drop table IFRS_CATALOGO_GRUPO cascade constraints;

drop table IFRS_CELDA cascade constraints;

drop table IFRS_CODIGO_FECU cascade constraints;

drop table IFRS_COLUMNA cascade constraints;

drop table IFRS_CUENTA_CONTABLE cascade constraints;

drop table IFRS_DETALLE_EEFF cascade constraints;

drop table IFRS_EEFF cascade constraints;

drop table IFRS_EMPRESA cascade constraints;

drop table IFRS_ESTADO_CUADRO cascade constraints;

drop table IFRS_ESTADO_PERIODO cascade constraints;

drop table IFRS_ESTRUCTURA cascade constraints;

drop table IFRS_GRILLA cascade constraints;

drop table IFRS_GRUPO cascade constraints;

drop table IFRS_GRUPO_EMPRESA cascade constraints;

drop table IFRS_HISTORIAL_REPORTE cascade constraints;

drop table IFRS_HISTORIAL_VERSION cascade constraints;

drop table IFRS_HTML cascade constraints;

drop table IFRS_LOG_PROCESO cascade constraints;

drop table IFRS_MENU cascade constraints;

drop table IFRS_MENU_GRUPO cascade constraints;

drop table IFRS_PARAMETRO cascade constraints;

drop table IFRS_PERIODO cascade constraints;

drop table IFRS_PERIODO_EMPRESA cascade constraints;

drop table IFRS_RELACION_DETALLE_EEFF cascade constraints;

drop table IFRS_RELACION_EEFF cascade constraints;

drop table IFRS_ROL cascade constraints;

drop table IFRS_TEXTO cascade constraints;

drop table IFRS_TIPO_CELDA cascade constraints;

drop table IFRS_TIPO_CUADRO cascade constraints;

drop table IFRS_TIPO_DATO cascade constraints;

drop table IFRS_TIPO_ESTADO_EEFF cascade constraints;

drop table IFRS_TIPO_ESTRUCTURA cascade constraints;

drop table IFRS_TIPO_PARAMETRO cascade constraints;

drop table IFRS_USUARIO cascade constraints;

drop table IFRS_USUARIO_GRUPO cascade constraints;

drop table IFRS_VERSION cascade constraints;

drop table IFRS_VERSION_EEFF cascade constraints;

drop table IFRS_XBRL_TAXONOMIA cascade constraints;

drop sequence SEQ_CAMPO_FORMULA;

drop sequence SEQ_CATALOGO;

drop sequence SEQ_CELDA;

drop sequence SEQ_COLUMNA;

drop sequence SEQ_ESTADO_CUADRO;

drop sequence SEQ_ESTADO_PERIODO;

drop sequence SEQ_ESTRUCTURA;

drop sequence SEQ_FORMULA_GRILLA;

drop sequence SEQ_GRILLA;

drop sequence SEQ_HISTORIAL_REPORTE;

drop sequence SEQ_HISTORIAL_VERSION;

drop sequence SEQ_HTML;

drop sequence SEQ_LOG_PROCESO;

drop sequence SEQ_MENU;

drop sequence SEQ_PERIODO;

drop sequence SEQ_TEXTO;

drop sequence SEQ_TIPO_CELDA;

drop sequence SEQ_TIPO_DATO;

drop sequence SEQ_TIPO_ESTRUCTURA;

drop sequence SEQ_TIPO_OPERACION;

drop sequence SEQ_USUARIO_GRUPO;

drop sequence SEQ_VERSION;

drop sequence SEQ_VERSION_EEFF;

drop sequence SEQ_VERSION_PERIODO;

create sequence SEQ_CAMPO_FORMULA
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

create sequence SEQ_CATALOGO
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

create sequence SEQ_CELDA
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

create sequence SEQ_COLUMNA
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

create sequence SEQ_ESTADO_CUADRO
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

create sequence SEQ_ESTADO_PERIODO
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

create sequence SEQ_ESTRUCTURA
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

create sequence SEQ_FORMULA_GRILLA
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

create sequence SEQ_GRILLA
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

create sequence SEQ_HISTORIAL_REPORTE
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

create sequence SEQ_HISTORIAL_VERSION
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

create sequence SEQ_HTML
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

create sequence SEQ_LOG_PROCESO
increment by 1
start with 1
 nocache;

create sequence SEQ_MENU
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

create sequence SEQ_PERIODO
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

create sequence SEQ_TEXTO
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

create sequence SEQ_TIPO_CELDA
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

create sequence SEQ_TIPO_DATO
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

create sequence SEQ_TIPO_ESTRUCTURA
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

create sequence SEQ_TIPO_OPERACION
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

create sequence SEQ_USUARIO_GRUPO
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

create sequence SEQ_VERSION
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

create sequence SEQ_VERSION_EEFF
start with 1;

create sequence SEQ_VERSION_PERIODO
increment by 1
start with 0
 maxvalue 9999999999999999999999999999
 minvalue 0
nocycle
 nocache
noorder;

/*==============================================================*/
/* Table: IFRS_AGRUPACION_COLUMNA                               */
/*==============================================================*/
create table IFRS_AGRUPACION_COLUMNA  (
   ID_NIVEL             NUMBER(8,0)                     not null,
   ID_COLUMNA           NUMBER(8,0)                     not null,
   ID_GRILLA            NUMBER(10,0)                    not null,
   TITULO               VARCHAR2(256 CHAR),
   ANCHO                NUMBER(4,0),
   GRUPO                NUMBER(4,0),
   constraint PK_REV_AGRUPACION_COLUMNA primary key (ID_COLUMNA, ID_GRILLA, ID_NIVEL)
);

/*==============================================================*/
/* Table: IFRS_AREA_NEGOCIO                                     */
/*==============================================================*/
create table IFRS_AREA_NEGOCIO  (
   ID_AREA_NEGOCIO      VARCHAR2(3)                     not null,
   ID_RUT               NUMBER(10),
   NOMBRE               VARCHAR2(256),
   VIGENTE              NUMBER(1),
   constraint PK_NEW_REV_AREA_NEGOCIO primary key (ID_AREA_NEGOCIO)
);

comment on table IFRS_AREA_NEGOCIO is
'TABLA QUE ALMACENA LAS AREAS DE NEGOCIO INVOLUCRADAS EN EL SISTEMA';

/*==============================================================*/
/* Table: IFRS_CATALOGO                                         */
/*==============================================================*/
create table IFRS_CATALOGO  (
   ID_CATALOGO          NUMBER(8,0)                     not null,
   RUT                  NUMBER(10)                      not null,
   ID_TIPO_CUADRO       NUMBER(4,0)                     not null,
   COD_CUADRO           NUMBER(5,0),
   COD_SUBCUADRO        NUMBER(5,0),
   NOMBRE               VARCHAR2(256 CHAR),
   TITULO               VARCHAR2(256 CHAR),
   ORDEN                NUMBER(5,2),
   VIGENCIA             NUMBER(1,0),
   IMPRESION_HORIZONTAL NUMBER(1),
   VALIDAR_EEFF         NUMBER(1),
   constraint PK_NEW_REV_CATALOGO_NOTA primary key (ID_CATALOGO)
);

comment on table IFRS_CATALOGO is
'TABLA DE MAESTRO QUE CONTIENE EL CATALOGO DE TODAS LAS NOTAS DEL SISTEMA';

/*==============================================================*/
/* Table: IFRS_CATALOGO_GRUPO                                   */
/*==============================================================*/
create table IFRS_CATALOGO_GRUPO  (
   ID_CATALOGO          NUMBER(8,0)                     not null,
   ID_GRUPO_ACCESO      VARCHAR2(128 CHAR)              not null,
   constraint PK_NEW_REV_CATALOGO_NOTA_GRUPO primary key (ID_CATALOGO, ID_GRUPO_ACCESO)
);

comment on table IFRS_CATALOGO_GRUPO is
'TABLA QUE ALMACENA LA ASOCIACION ENTRE EL CATALOGO DE UNA NOTA Y LOS GRUPOS QUE TIENEN ACCESO A ELLA.';

/*==============================================================*/
/* Table: IFRS_CELDA                                            */
/*==============================================================*/
create table IFRS_CELDA  (
   ID_COLUMNA           NUMBER(8,0)                     not null,
   ID_FILA              NUMBER(4,0)                     not null,
   ID_GRILLA            NUMBER(10,0)                    not null,
   ID_TIPO_CELDA        NUMBER(4,0),
   ID_TIPO_DATO         NUMBER(2,0),
   VALOR                VARCHAR2(2048 CHAR),
   CHILD_HORIZONTAL     NUMBER(4,0),
   PARENT_HORIZONTAL    NUMBER(4,0),
   CHILD_VERTICAL       NUMBER(4,0),
   PARENT_VERTICAL      NUMBER(4,0),
   FORMULA              VARCHAR2(256),
   constraint PK_NEW_REV_CELDA_NOTA primary key (ID_COLUMNA, ID_GRILLA, ID_FILA)
);

comment on table IFRS_CELDA is
'TABLA QUE ALMACENA LOS ATRIBUTOS DE LA COLUMNA DE UNA NOTA.';

/*==============================================================*/
/* Table: IFRS_CODIGO_FECU                                      */
/*==============================================================*/
create table IFRS_CODIGO_FECU  (
   ID_FECU              number(12)                      not null,
   DESCRIPCION          VARCHAR2(256)                   not null,
   VIGENCIA             number(1)                       not null,
   constraint PK_IFRS_CODIGO_FECU primary key (ID_FECU)
);

/*==============================================================*/
/* Table: IFRS_COLUMNA                                          */
/*==============================================================*/
create table IFRS_COLUMNA  (
   ID_COLUMNA           NUMBER(8,0)                     not null,
   ID_GRILLA            NUMBER(10,0)                    not null,
   TITULO_COLUMNA       VARCHAR2(128 CHAR),
   ORDEN                NUMBER(3,0),
   ANCHO                NUMBER(4,0),
   ROW_HEADER           NUMBER(1,0),
   constraint PK_NEW_REV_COLUMNA_NOTA primary key (ID_COLUMNA, ID_GRILLA)
);

comment on table IFRS_COLUMNA is
'TABLA QUE CONTIENE LAS COLUMNAS DE LA GRILLA';

/*==============================================================*/
/* Table: IFRS_CUENTA_CONTABLE                                  */
/*==============================================================*/
create table IFRS_CUENTA_CONTABLE  (
   ID_CUENTA            NUMBER(10)                      not null,
   DESCRIPCION          VARCHAR2(256)                   not null,
   VIGENCIA             number(1),
   constraint PK_IFRS_CUENTA_CONTABLE primary key (ID_CUENTA)
);

/*==============================================================*/
/* Table: IFRS_DETALLE_EEFF                                     */
/*==============================================================*/
create table IFRS_DETALLE_EEFF  (
   ID_CUENTA            NUMBER(10)                      not null,
   ID_FECU              number(12)                      not null,
   ID_VERSION_EEFF      NUMBER(10)                      not null,
   MONTO_EBS            NUMBER(18,4),
   MONTO_RECLASIFICACION NUMBER(18,4),
   MONTO_PESOS          NUMBER(18,4),
   MONTO_MILES          NUMBER(18,4),
   MONTO_PESOS_MIL      NUMBER(18,4),
   constraint PK_IFRS_DETALLE_EEFF primary key (ID_CUENTA, ID_FECU, ID_VERSION_EEFF)
);

/*==============================================================*/
/* Table: IFRS_EEFF                                             */
/*==============================================================*/
create table IFRS_EEFF  (
   ID_FECU              number(12)                      not null,
   ID_VERSION_EEFF      NUMBER(10)                      not null,
   MONTO_TOTAL          NUMBER(18,4),
   constraint PK_IFRS_EEFF primary key (ID_FECU, ID_VERSION_EEFF)
);

/*==============================================================*/
/* Table: IFRS_EMPRESA                                          */
/*==============================================================*/
create table IFRS_EMPRESA  (
   ID_RUT               NUMBER(10)                      not null,
   DV                   CHAR(1)                         not null,
   NOMBRE               VARCHAR(256)                    not null,
   RAZON_SOCIAL         VARCHAR2(256),
   GIRO                 VARCHAR(256),
   constraint PK_IFRS_EMPRESA primary key (ID_RUT)
);

/*==============================================================*/
/* Table: IFRS_ESTADO_CUADRO                                    */
/*==============================================================*/
create table IFRS_ESTADO_CUADRO  (
   ID_ESTADO_CUADRO     NUMBER(2,0)                     not null,
   NOMBRE               VARCHAR2(128),
   constraint PK_NEW_REV_ESTADO_NOTA primary key (ID_ESTADO_CUADRO)
);

comment on table IFRS_ESTADO_CUADRO is
'TABLA QUE ALMACENA LOS ESTADOS QUE PUEDE TENER UNA NOTA';

/*==============================================================*/
/* Table: IFRS_ESTADO_PERIODO                                   */
/*==============================================================*/
create table IFRS_ESTADO_PERIODO  (
   ID_ESTADO_PERIODO    NUMBER(2,0)                     not null,
   NOMBRE               VARCHAR2(128),
   constraint PK_NEW_REV_ESTADO_PERIODO primary key (ID_ESTADO_PERIODO)
);

comment on table IFRS_ESTADO_PERIODO is
'TABLA QUE ALMACENA LOS ESTADOS QUE PUEDE TENER UN PERIODO';

/*==============================================================*/
/* Table: IFRS_ESTRUCTURA                                       */
/*==============================================================*/
create table IFRS_ESTRUCTURA  (
   ID_ESTRUCTURA        NUMBER(10,0)                    not null,
   ID_VERSION           NUMBER(8,0),
   ID_TIPO_ESTRUCTURA   NUMBER(4,0),
   ORDEN                NUMBER(5,0),
   constraint PK_NEW_REV_ESTRUCTURA_NOTA primary key (ID_ESTRUCTURA)
);

comment on table IFRS_ESTRUCTURA is
'TABLA QUE DEFINE COMO SE ESTRUCTURA LA NOTA';

/*==============================================================*/
/* Table: IFRS_GRILLA                                           */
/*==============================================================*/
create table IFRS_GRILLA  (
   ID_GRILLA            NUMBER(10,0)                    not null,
   TITULO               VARCHAR2(256 CHAR),
   TIPO_FORMULA         NUMBER(4,0)                    default 1,
   constraint PK_NEW_REV_GRILLA_NOTA primary key (ID_GRILLA)
);

comment on table IFRS_GRILLA is
'TABLA QUE DEFINE UNA GRILLA ';

/*==============================================================*/
/* Table: IFRS_GRUPO                                            */
/*==============================================================*/
create table IFRS_GRUPO  (
   ID_GRUPO_ACCESO      VARCHAR2(128 CHAR)              not null,
   ID_AREA_NEGOCIO      VARCHAR2(3),
   NOMBRE               VARCHAR2(512 CHAR),
   ACCESO_BLOQUEADO     NUMBER(1),
   VIGENTE              NUMBER(1),
   constraint PK_NEW_REV_GRUPO_ACCESO primary key (ID_GRUPO_ACCESO)
);

comment on table IFRS_GRUPO is
'TABLA QUE ALMACENA EL MAESTRO CON LOS GRUPOS DE ACCESO A LA APLICACION,
CLASIFICADOS POR AREA DE NEGOCIO';

/*==============================================================*/
/* Table: IFRS_GRUPO_EMPRESA                                    */
/*==============================================================*/
create table IFRS_GRUPO_EMPRESA  (
   ID_GRUPO_ACCESO      VARCHAR2(128 CHAR)              not null,
   ID_RUT               NUMBER(10)                      not null,
   constraint PK_IFRS_GRUPO_EMPRESA primary key (ID_GRUPO_ACCESO, ID_RUT)
);

/*==============================================================*/
/* Table: IFRS_HISTORIAL_REPORTE                                */
/*==============================================================*/
create table IFRS_HISTORIAL_REPORTE  (
   ID_HISTORIAL_REPORTE NUMBER(10,0)                    not null,
   COMENTARIO           VARCHAR2(1024 CHAR),
   IP_USUARIO           VARCHAR2(256 CHAR),
   CHECK_SUM_EXPORTACION VARCHAR2(512 CHAR),
   FECHA_EXPORTACION    DATE,
   DOCUMENTO            BLOB,
   NOMBRE_ARCHIVO       VARCHAR2(512 CHAR),
   ID_PERIODO           NUMBER(6),
   ID_RUT               NUMBER(10),
   NOMBRE_USUARIO       VARCHAR2(256 CHAR),
   constraint PK_IFRS_HISTORIAL_REPORTE primary key (ID_HISTORIAL_REPORTE)
);

comment on table IFRS_HISTORIAL_REPORTE is
'TABLA QUE ALMACENA UN HIISTORIAL CON LA GENERACION DEL REPORTE FINAL FECU EN FORMATO MS WORD,
ALMACENANDO EL CHECKSUM MD5 DEL ARCHIVO GENERADO PARA ASI COMPARAR SI EXISTIERON MIDIFICACIONES EN EL DOCUMENTO.';

/*==============================================================*/
/* Table: IFRS_HISTORIAL_VERSION                                */
/*==============================================================*/
create table IFRS_HISTORIAL_VERSION  (
   ID_HISTORIAL         NUMBER                          not null,
   ID_ESTADO_CUADRO     NUMBER(2,0),
   ID_VERSION           NUMBER(8,0),
   NOMBRE_USUARIO       VARCHAR2(256 CHAR),
   FECHA_PROCESO        DATE,
   COMENTARIO           VARCHAR2(2048 CHAR),
   USUARIO              VARCHAR2(256 CHAR),
   constraint PK_REV_HISTORIAL_VERSION primary key (ID_HISTORIAL)
);

comment on table IFRS_HISTORIAL_VERSION is
'TABLA QUE ALMACENA EL HITORIAL DE LOS CAMBIOS DE ESTADO DE UNA VERSION PARA EL PERIODO DETERMINADO';

/*==============================================================*/
/* Table: IFRS_HTML                                             */
/*==============================================================*/
create table IFRS_HTML  (
   ID_HTML              NUMBER(10,0)                    not null,
   CONTENIDO            BLOB,
   TITULO               VARCHAR2(1024 CHAR),
   constraint PK_NEW_REV_HTML_NOTA primary key (ID_HTML)
);

/*==============================================================*/
/* Table: IFRS_LOG_PROCESO                                      */
/*==============================================================*/
create table IFRS_LOG_PROCESO  (
   ID_LOG               NUMBER(10)                      not null,
   USUARIO              VARCHAR2(256),
   FECHA                DATE,
   LOG                  VARCHAR2(4000),
   constraint PK_IFRS_LOG_PROCESO primary key (ID_LOG)
);

/*==============================================================*/
/* Table: IFRS_MENU                                             */
/*==============================================================*/
create table IFRS_MENU  (
   ID_MENU              NUMBER(3,0)                     not null,
   NOMBRE               VARCHAR2(512),
   ESTADO               NUMBER(1,0),
   GRUPO                NUMBER(2,0),
   URL_MENU             VARCHAR2(512 CHAR),
   ES_PADRE             NUMBER(1,0)                    default NULL,
   ORDEN                NUMBER(4,0),
   constraint PK_NEW_REV_MENU primary key (ID_MENU)
);

comment on table IFRS_MENU is
'TABLA QUE ALMACENA LAS OPCIONES DE MENU DISPONIBLES EN LA APLICACIÓN';

/*==============================================================*/
/* Table: IFRS_MENU_GRUPO                                       */
/*==============================================================*/
create table IFRS_MENU_GRUPO  (
   ID_MENU              NUMBER(3,0)                     not null,
   ID_GRUPO_ACCESO      VARCHAR2(128 CHAR)              not null,
   constraint PK_NEW_REV_MENU_GRUPO primary key (ID_MENU, ID_GRUPO_ACCESO)
);

comment on table IFRS_MENU_GRUPO is
'TABLA QUE ALMACENA LA ASOCIACION DE MENU CON EL GRUPO QUE POSEE EL ACCESO';

/*==============================================================*/
/* Table: IFRS_PARAMETRO                                        */
/*==============================================================*/
create table IFRS_PARAMETRO  (
   NOMBRE               VARCHAR2(512)                   not null,
   ID_TIPO_PARAMETRO    NUMBER(4)                       not null,
   VALOR                VARCHAR2(1024)                  not null,
   constraint PK_IFRS_PARAMETRO primary key (NOMBRE)
);

/*==============================================================*/
/* Table: IFRS_PERIODO                                          */
/*==============================================================*/
create table IFRS_PERIODO  (
   ID_PERIODO           NUMBER(6)                       not null,
   ID_ESTADO_PERIODO    NUMBER(2,0),
   constraint PK_IFRS_PERIODO primary key (ID_PERIODO)
);

/*==============================================================*/
/* Table: IFRS_PERIODO_EMPRESA                                  */
/*==============================================================*/
create table IFRS_PERIODO_EMPRESA  (
   ID_PERIODO           NUMBER(6)                       not null,
   ID_RUT               NUMBER(10)                      not null,
   ID_ESTADO_PERIODO    NUMBER(2,0),
   constraint PK_NEW_REV_PERIODO primary key (ID_PERIODO, ID_RUT)
);

comment on table IFRS_PERIODO_EMPRESA is
'TABLA QUE ALMACENA LOS PERIODOS FINANCIEROS Y RESPECTIVOS ESTADOS';

/*==============================================================*/
/* Table: IFRS_RELACION_DETALLE_EEFF                            */
/*==============================================================*/
create table IFRS_RELACION_DETALLE_EEFF  (
   ID_CUENTA            NUMBER(10)                      not null,
   ID_FECU              number(12)                      not null,
   ID_PERIODO           NUMBER(6)                       not null,
   ID_RUT               NUMBER(10)                      not null,
   ID_GRILLA            NUMBER(10,0)                    not null,
   ID_COLUMNA           NUMBER(8,0)                     not null,
   ID_FILA              NUMBER(4,0)                     not null,
   MONTO_EBS            NUMBER(18,4),
   MONTO_RECLASIFICACION NUMBER(18,4),
   MONTO_PESOS          NUMBER(18,4),
   MONTO_MILES          NUMBER(18,4),
   MONTO_PESOS_MIL      NUMBER(18,4),
   constraint PK_IFRS_RELACION_DETALLE_EEFF primary key (ID_PERIODO, ID_FECU, ID_CUENTA, ID_RUT, ID_GRILLA, ID_COLUMNA, ID_FILA)
);

/*==============================================================*/
/* Table: IFRS_RELACION_EEFF                                    */
/*==============================================================*/
create table IFRS_RELACION_EEFF  (
   ID_FECU              number(12)                      not null,
   ID_PERIODO           NUMBER(6)                       not null,
   ID_RUT               NUMBER(10)                      not null,
   ID_GRILLA            NUMBER(10,0),
   ID_COLUMNA           NUMBER(8,0)                     not null,
   ID_FILA              NUMBER(4,0),
   MONTO_TOTAL          NUMBER(18,4),
   constraint PK_IFRS_RELACION_EEFF primary key (ID_PERIODO, ID_FECU, ID_RUT)
);

/*==============================================================*/
/* Table: IFRS_ROL                                              */
/*==============================================================*/
create table IFRS_ROL  (
   ID_ROL               VARCHAR(256)                    not null,
   NOMBRE_ROL           VARCHAR(512),
   constraint PK_IFRS_ROL primary key (ID_ROL)
);

comment on table IFRS_ROL is
'TABLA QUE DEFINE LO ROLES DISPONIBLES EN EL SISTEMA.
';

/*==============================================================*/
/* Table: IFRS_TEXTO                                            */
/*==============================================================*/
create table IFRS_TEXTO  (
   ID_TEXTO             NUMBER(10,0)                    not null,
   TEXTO                VARCHAR2(4000),
   NEGRITA              NUMBER(1,0)                    default 0,
   constraint PK_NEW_REV_TEXTO_NOTA primary key (ID_TEXTO)
);

comment on table IFRS_TEXTO is
'TABLA QUE ALMACENA LOS TEXTOS FIJOS QUE TIENE UNA NOTA';

/*==============================================================*/
/* Table: IFRS_TIPO_CELDA                                       */
/*==============================================================*/
create table IFRS_TIPO_CELDA  (
   ID_TIPO_CELDA        NUMBER(4,0)                     not null,
   NOMBRE               VARCHAR2(128 CHAR),
   constraint PK_NEW_REV_TIPO_CELDA primary key (ID_TIPO_CELDA)
);

comment on table IFRS_TIPO_CELDA is
'TABLA QUE CONTIENE SI LA CELDA ES TITULO, SUBTOTAL, TOTAL, ETC';

/*==============================================================*/
/* Table: IFRS_TIPO_CUADRO                                      */
/*==============================================================*/
create table IFRS_TIPO_CUADRO  (
   ID_TIPO_CUADRO       NUMBER(4,0)                     not null,
   NOMBRE               VARCHAR2(256 CHAR),
   TITULO               VARCHAR2(512 CHAR),
   constraint PK_NEW_REV_TIPO_CUADRO primary key (ID_TIPO_CUADRO)
);

/*==============================================================*/
/* Table: IFRS_TIPO_DATO                                        */
/*==============================================================*/
create table IFRS_TIPO_DATO  (
   ID_TIPO_DATO         NUMBER(2,0)                     not null,
   NOMBRE               VARCHAR2(64 CHAR),
   NOMBRE_CLASE         VARCHAR2(256),
   constraint PK_NEW_REV_TIPO_DATO primary key (ID_TIPO_DATO)
);

comment on table IFRS_TIPO_DATO is
'TABLA QUE DEFINE EL TIPO DE DATO JAVA  QUE CONTIENE CADA COLUMNA';

/*==============================================================*/
/* Table: IFRS_TIPO_ESTADO_EEFF                                 */
/*==============================================================*/
create table IFRS_TIPO_ESTADO_EEFF  (
   ID_ESTADO_EEFF       NUMBER(4)                       not null,
   NOMBRE               VARCHAR2(256)                   not null,
   VIGENTE              NUMBER(1)                       not null,
   constraint PK_IFRS_TIPO_ESTADO_EEFF primary key (ID_ESTADO_EEFF)
);

/*==============================================================*/
/* Table: IFRS_TIPO_ESTRUCTURA                                  */
/*==============================================================*/
create table IFRS_TIPO_ESTRUCTURA  (
   ID_TIPO_ESTRUCTURA   NUMBER(4,0)                     not null,
   NOMBRE               VARCHAR2(64),
   constraint PK_NEW_REV_TIPO_ESTRUCTURA primary key (ID_TIPO_ESTRUCTURA)
);

comment on table IFRS_TIPO_ESTRUCTURA is
'TABLA QUE CONTIENE LA DEFINICION DE LOS TIPOS DE  COMPONENTES DE LA NOTA';

/*==============================================================*/
/* Table: IFRS_TIPO_PARAMETRO                                   */
/*==============================================================*/
create table IFRS_TIPO_PARAMETRO  (
   ID_TIPO_PARAMETRO    NUMBER(4)                       not null,
   NOMBRE               VARCHAR2(1024 BYTE)             not null,
   constraint PK_IFRS_TIPO_PARAMETRO primary key (ID_TIPO_PARAMETRO)
);

/*==============================================================*/
/* Table: IFRS_USUARIO                                          */
/*==============================================================*/
create table IFRS_USUARIO  (
   NOMBRE_USUARIO       VARCHAR2(256 CHAR)              not null,
   ID_ROL               VARCHAR(256),
   PASSWORD             VARCHAR2(256 CHAR),
   EMAIL                VARCHAR2(256 CHAR),
   VIGENTE              NUMBER(1),
   FECHA_CREACION       DATE,
   FECHA_ACTUALIZACION  DATE,
   FECHA_ULTIMO_ACCESO  DATE,
   NOMBRE               VARCHAR2(256 CHAR),
   APELLIDO_PATERNO     VARCHAR2(256 CHAR),
   APELLIDO_MATERNO     VARCHAR2(256 CHAR),
   CAMBIAR_PASSWORD     NUMBER(1),
   constraint PK_IFRS_USUARIO primary key (NOMBRE_USUARIO)
);

/*==============================================================*/
/* Table: IFRS_USUARIO_GRUPO                                    */
/*==============================================================*/
create table IFRS_USUARIO_GRUPO  (
   NOMBRE_USUARIO       VARCHAR2(256 CHAR)              not null,
   ID_GRUPO_ACCESO      VARCHAR2(128 CHAR)              not null,
   constraint PK_NEW_REV_USUARIO_GRUPO primary key (ID_GRUPO_ACCESO, NOMBRE_USUARIO)
);

comment on table IFRS_USUARIO_GRUPO is
'TABLA QUE ALMACENA LA ASOCIACION ENTRE EL USUARIO Y EL GRUPO AL QUE PERTENECE';

/*==============================================================*/
/* Table: IFRS_VERSION                                          */
/*==============================================================*/
create table IFRS_VERSION  (
   ID_VERSION           NUMBER(8,0)                     not null,
   ID_CATALOGO          NUMBER(8,0),
   ID_PERIODO           NUMBER(6),
   ID_RUT               NUMBER(10),
   ID_ESTADO_CUADRO     NUMBER(2,0),
   VERSION              NUMBER(2,0),
   VIGENCIA             NUMBER(1,0),
   FECHA_CREACION       DATE                           default SYSDATE,
   COMENTARIO           VARCHAR2(256),
   FECHA_ULTIMO_PROCESO DATE,
   USUARIO              VARCHAR2(256),
   VALIDADO_EEFF        NUMBER(1,0),
   DATOS_MODIFICADOS    NUMBER(1,0),
   constraint PK_NEW_REV_VERSION_NOTA primary key (ID_VERSION)
);

comment on table IFRS_VERSION is
'TABLA QUE CONTIENE LAS VERSIONES PARA CADA  NOTA';

/*==============================================================*/
/* Table: IFRS_VERSION_EEFF                                     */
/*==============================================================*/
create table IFRS_VERSION_EEFF  (
   ID_VERSION_EEFF      NUMBER(10)                      not null,
   ID_PERIODO           NUMBER(6)                       not null,
   ID_RUT               NUMBER(10)                      not null,
   ID_ESTADO_EEFF       NUMBER(4),
   VERSION              NUMBER(4)                       not null,
   VIGENCIA             NUMBER(1)                       not null,
   USUARIO              VARCHAR2(256),
   FECHA                DATE,
   constraint PK_IFRS_VERSION_EEFF primary key (ID_VERSION_EEFF)
);

/*==============================================================*/
/* Table: IFRS_XBRL_TAXONOMIA                                   */
/*==============================================================*/
create table IFRS_XBRL_TAXONOMIA  (
   ID_TAXONOMIA__NOMBRE__TOP_TAXO NUMBER(8)                       not null,
   NOMBRE               VARCHAR2(1024 CHAR),
   TOP_TAXONOMY         VARCHAR2(1024 CHAR),
   NOMBRE_ARCHIVO       VARCHAR2(1024 CHAR),
   URI                  VARCHAR2(1024 CHAR),
   VIGENTE              NUMBER(1),
   FECHA_TAXONOMIA      DATE,
   USUARIO_CREACION     VARCHAR2(256 CHAR),
   USUARIO_EDICION      VARCHAR2(256 CHAR),
   FECHA_CREACION       DATE,
   FECHA_EDICION        DATE
);

comment on table IFRS_XBRL_TAXONOMIA is
'TABLA PARA GUARDAR LA RUTA DONDE SE ENCUENTRAN LAS TAXONOMIAS.';

alter table IFRS_AGRUPACION_COLUMNA
   add constraint FK_REV_AGRU_REF_REV_COLU foreign key (ID_COLUMNA, ID_GRILLA)
      references IFRS_COLUMNA (ID_COLUMNA, ID_GRILLA);

alter table IFRS_AREA_NEGOCIO
   add constraint FK_IFRS_ARE_REFERENCE_IFRS_EMP foreign key (ID_RUT)
      references IFRS_EMPRESA (ID_RUT);

alter table IFRS_CATALOGO
   add constraint FK_REV_CATA_REF_REV_TIPO_CU foreign key (ID_TIPO_CUADRO)
      references IFRS_TIPO_CUADRO (ID_TIPO_CUADRO);

alter table IFRS_CATALOGO
   add constraint FK_IFRS_CAT_REFERENCE_IFRS_EMP foreign key (RUT)
      references IFRS_EMPRESA (ID_RUT);

alter table IFRS_CATALOGO_GRUPO
   add constraint FK_REV_CATA_REF_REV_CATA foreign key (ID_CATALOGO)
      references IFRS_CATALOGO (ID_CATALOGO);

alter table IFRS_CATALOGO_GRUPO
   add constraint FK_REV_CATA_REF_REV_GRUP foreign key (ID_GRUPO_ACCESO)
      references IFRS_GRUPO (ID_GRUPO_ACCESO);

alter table IFRS_CELDA
   add constraint FK_REV_CELD_REF_REV_COLU foreign key (ID_COLUMNA, ID_GRILLA)
      references IFRS_COLUMNA (ID_COLUMNA, ID_GRILLA);

alter table IFRS_CELDA
   add constraint FK_REV_CELD_REF_REV_TIPO foreign key (ID_TIPO_DATO)
      references IFRS_TIPO_DATO (ID_TIPO_DATO);

alter table IFRS_CELDA
   add constraint FK_REV_CELD_REF_REV_TIPO_CELDA foreign key (ID_TIPO_CELDA)
      references IFRS_TIPO_CELDA (ID_TIPO_CELDA);

alter table IFRS_COLUMNA
   add constraint FK_REV_COLU_REF_REV_GRIL foreign key (ID_GRILLA)
      references IFRS_GRILLA (ID_GRILLA);

alter table IFRS_DETALLE_EEFF
   add constraint FK_IFRS_DET_REFERENCE_IFRS_EEF foreign key (ID_FECU, ID_VERSION_EEFF)
      references IFRS_EEFF (ID_FECU, ID_VERSION_EEFF);

alter table IFRS_DETALLE_EEFF
   add constraint FK_IFRS_DET_REFERENCE_IFRS_CUE foreign key (ID_CUENTA)
      references IFRS_CUENTA_CONTABLE (ID_CUENTA);

alter table IFRS_EEFF
   add constraint FK_IFRS_EEF_REFERENCE_IFRS_VER foreign key (ID_VERSION_EEFF)
      references IFRS_VERSION_EEFF (ID_VERSION_EEFF);

alter table IFRS_EEFF
   add constraint FK_IFRS_EEF_REFERENCE_IFRS_COD foreign key (ID_FECU)
      references IFRS_CODIGO_FECU (ID_FECU);

alter table IFRS_ESTRUCTURA
   add constraint FK_REV_ESTR_REF_REV_TIPO foreign key (ID_TIPO_ESTRUCTURA)
      references IFRS_TIPO_ESTRUCTURA (ID_TIPO_ESTRUCTURA);

alter table IFRS_ESTRUCTURA
   add constraint FK_REV_ESTR_REF_REV_VERS foreign key (ID_VERSION)
      references IFRS_VERSION (ID_VERSION);

alter table IFRS_GRILLA
   add constraint FK_REV_GRIL_REF_REV_ESTR foreign key (ID_GRILLA)
      references IFRS_ESTRUCTURA (ID_ESTRUCTURA);

alter table IFRS_GRUPO
   add constraint FK_REV_GRUP_REF_REV_AREA foreign key (ID_AREA_NEGOCIO)
      references IFRS_AREA_NEGOCIO (ID_AREA_NEGOCIO);

alter table IFRS_GRUPO_EMPRESA
   add constraint FK_IFRS_GRU_REFERENCE_IFRS_GRU foreign key (ID_GRUPO_ACCESO)
      references IFRS_GRUPO (ID_GRUPO_ACCESO);

alter table IFRS_GRUPO_EMPRESA
   add constraint FK_IFRS_GRU_REFERENCE_IFRS_EMP foreign key (ID_RUT)
      references IFRS_EMPRESA (ID_RUT);

alter table IFRS_HISTORIAL_REPORTE
   add constraint FK_IFRS_HIS_REFERENCE_IFRS_PER foreign key (ID_PERIODO, ID_RUT)
      references IFRS_PERIODO_EMPRESA (ID_PERIODO, ID_RUT);

alter table IFRS_HISTORIAL_REPORTE
   add constraint FK_IFRS_HIS_REFERENCE_IFRS_USU foreign key (NOMBRE_USUARIO)
      references IFRS_USUARIO (NOMBRE_USUARIO);

alter table IFRS_HISTORIAL_VERSION
   add constraint FK_REV_HIST_REF_REV_ESTA foreign key (ID_ESTADO_CUADRO)
      references IFRS_ESTADO_CUADRO (ID_ESTADO_CUADRO);

alter table IFRS_HISTORIAL_VERSION
   add constraint FK_IFRS_HIS_REFERENCE_IFRS_VER foreign key (ID_VERSION)
      references IFRS_VERSION (ID_VERSION);

alter table IFRS_HISTORIAL_VERSION
   add constraint FK_IFRS_HIS_REFERENCE_IFRS_US2 foreign key (NOMBRE_USUARIO)
      references IFRS_USUARIO (NOMBRE_USUARIO);

alter table IFRS_HTML
   add constraint FK_REV_HTML_REF_REV_ESTR foreign key (ID_HTML)
      references IFRS_ESTRUCTURA (ID_ESTRUCTURA);

alter table IFRS_MENU_GRUPO
   add constraint FK_NEW_REV_MENU_REF_REV_GRUP foreign key (ID_GRUPO_ACCESO)
      references IFRS_GRUPO (ID_GRUPO_ACCESO);

alter table IFRS_MENU_GRUPO
   add constraint FK_NEW_REV_MENU_REF_REV_MENU foreign key (ID_MENU)
      references IFRS_MENU (ID_MENU);

alter table IFRS_PARAMETRO
   add constraint FK_IFRS_PAR_REFERENCE_IFRS_TIP foreign key (ID_TIPO_PARAMETRO)
      references IFRS_TIPO_PARAMETRO (ID_TIPO_PARAMETRO);

alter table IFRS_PERIODO
   add constraint FK_IFRS_PER_REF_IFRS_EST_P foreign key (ID_ESTADO_PERIODO)
      references IFRS_ESTADO_PERIODO (ID_ESTADO_PERIODO);

alter table IFRS_PERIODO_EMPRESA
   add constraint FK_IFRS_PER_REFERENCE_IFRS_EMP foreign key (ID_RUT)
      references IFRS_EMPRESA (ID_RUT);

alter table IFRS_PERIODO_EMPRESA
   add constraint FK_IFRS_PER_REFERENCE_IFRS_EST foreign key (ID_ESTADO_PERIODO)
      references IFRS_ESTADO_PERIODO (ID_ESTADO_PERIODO);

alter table IFRS_PERIODO_EMPRESA
   add constraint FK_IFRS_PER_REFERENCE_IFRS_PER foreign key (ID_PERIODO)
      references IFRS_PERIODO (ID_PERIODO);

alter table IFRS_RELACION_DETALLE_EEFF
   add constraint FK_IFRS_REL_CONST_REL_IFRS_COD foreign key (ID_FECU)
      references IFRS_CODIGO_FECU (ID_FECU);

alter table IFRS_RELACION_DETALLE_EEFF
   add constraint FK_REV_RELA_DET_REF_REV_CELD foreign key (ID_COLUMNA, ID_GRILLA, ID_FILA)
      references IFRS_CELDA (ID_COLUMNA, ID_GRILLA, ID_FILA);

alter table IFRS_RELACION_DETALLE_EEFF
   add constraint FK_IFRS_REL_REFERENCE_IFRS_PER foreign key (ID_PERIODO, ID_RUT)
      references IFRS_PERIODO_EMPRESA (ID_PERIODO, ID_RUT);

alter table IFRS_RELACION_DETALLE_EEFF
   add constraint FK_IFRS_REL_REFERENCE_IFRS_CUE foreign key (ID_CUENTA)
      references IFRS_CUENTA_CONTABLE (ID_CUENTA);

alter table IFRS_RELACION_EEFF
   add constraint FK_IFRS_REL_CONS_REL__IFRS_COD foreign key (ID_FECU)
      references IFRS_CODIGO_FECU (ID_FECU);

alter table IFRS_RELACION_EEFF
   add constraint FK_REV_RELA_EEFF_REF_REV_CELD foreign key (ID_COLUMNA, ID_GRILLA, ID_FILA)
      references IFRS_CELDA (ID_COLUMNA, ID_GRILLA, ID_FILA);

alter table IFRS_RELACION_EEFF
   add constraint FK_IFRS_REL_REFERENCE_IFRS_PE2 foreign key (ID_PERIODO, ID_RUT)
      references IFRS_PERIODO_EMPRESA (ID_PERIODO, ID_RUT);

alter table IFRS_TEXTO
   add constraint FK_REV_TEXT_REFERENCE_REV_ESTR foreign key (ID_TEXTO)
      references IFRS_ESTRUCTURA (ID_ESTRUCTURA);

alter table IFRS_USUARIO
   add constraint FK_IFRS_USU_REFERENCE_IFRS_ROL foreign key (ID_ROL)
      references IFRS_ROL (ID_ROL);

alter table IFRS_USUARIO_GRUPO
   add constraint FK_REV_USUA_REF_REV_GRUP foreign key (ID_GRUPO_ACCESO)
      references IFRS_GRUPO (ID_GRUPO_ACCESO);

alter table IFRS_USUARIO_GRUPO
   add constraint FK_IFRS_USU_REFERENCE_IFRS_USU foreign key (NOMBRE_USUARIO)
      references IFRS_USUARIO (NOMBRE_USUARIO);

alter table IFRS_VERSION
   add constraint FK_REV_VERS_REF_REV_CATA foreign key (ID_CATALOGO)
      references IFRS_CATALOGO (ID_CATALOGO);

alter table IFRS_VERSION
   add constraint FK_IFRS_VER_REFERENCE_IFRS_PER foreign key (ID_PERIODO, ID_RUT)
      references IFRS_PERIODO_EMPRESA (ID_PERIODO, ID_RUT);

alter table IFRS_VERSION
   add constraint FK_IFRS_VER_REFERENCE_IFRS_EST foreign key (ID_ESTADO_CUADRO)
      references IFRS_ESTADO_CUADRO (ID_ESTADO_CUADRO);

alter table IFRS_VERSION_EEFF
   add constraint FK_IFRS_VER_REFERENCE_IFRS_PE2 foreign key (ID_PERIODO, ID_RUT)
      references IFRS_PERIODO_EMPRESA (ID_PERIODO, ID_RUT);

alter table IFRS_VERSION_EEFF
   add constraint FK_IFRS_VER_REFERENCE_IFRS_TIP foreign key (ID_ESTADO_EEFF)
      references IFRS_TIPO_ESTADO_EEFF (ID_ESTADO_EEFF);

