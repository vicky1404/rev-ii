--------------------------------------------------------
-- Archivo creado  - viernes-agosto-17-2012   
--------------------------------------------------------
  DROP SEQUENCE "REVELACIONES"."SEQ_CAMPO_FORMULA";
  DROP SEQUENCE "REVELACIONES"."SEQ_CATALOGO";
  DROP SEQUENCE "REVELACIONES"."SEQ_CELDA";
  DROP SEQUENCE "REVELACIONES"."SEQ_COLUMNA";
  DROP SEQUENCE "REVELACIONES"."SEQ_ESTADO_CUADRO";
  DROP SEQUENCE "REVELACIONES"."SEQ_ESTADO_PERIODO";
  DROP SEQUENCE "REVELACIONES"."SEQ_ESTRUCTURA";
  DROP SEQUENCE "REVELACIONES"."SEQ_FORMULA_GRILLA";
  DROP SEQUENCE "REVELACIONES"."SEQ_GRILLA";
  DROP SEQUENCE "REVELACIONES"."SEQ_HISTORIAL_REPORTE";
  DROP SEQUENCE "REVELACIONES"."SEQ_HISTORIAL_VERSION";
  DROP SEQUENCE "REVELACIONES"."SEQ_HTML";
  DROP SEQUENCE "REVELACIONES"."SEQ_LOG_PROCESO";
  DROP SEQUENCE "REVELACIONES"."SEQ_MENU";
  DROP SEQUENCE "REVELACIONES"."SEQ_PERIODO";
  DROP SEQUENCE "REVELACIONES"."SEQ_SUB_COLUMNA";
  DROP SEQUENCE "REVELACIONES"."SEQ_SUB_GRILLA";
  DROP SEQUENCE "REVELACIONES"."SEQ_TEXTO";
  DROP SEQUENCE "REVELACIONES"."SEQ_TIPO_CELDA";
  DROP SEQUENCE "REVELACIONES"."SEQ_TIPO_DATO";
  DROP SEQUENCE "REVELACIONES"."SEQ_TIPO_ESTRUCTURA";
  DROP SEQUENCE "REVELACIONES"."SEQ_TIPO_OPERACION";
  DROP SEQUENCE "REVELACIONES"."SEQ_USUARIO_GRUPO";
  DROP SEQUENCE "REVELACIONES"."SEQ_VERSION";
  DROP SEQUENCE "REVELACIONES"."SEQ_VERSION_EEFF";
  DROP SEQUENCE "REVELACIONES"."SEQ_VERSION_PERIODO";
  DROP SEQUENCE "REVELACIONES"."SEQ_XBRL_TAXONOMIA";
--------------------------------------------------------
--  DDL for Sequence SEQ_CAMPO_FORMULA
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_CAMPO_FORMULA"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 10618 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_CATALOGO
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_CATALOGO"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 135 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_CELDA
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_CELDA"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_COLUMNA
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_COLUMNA"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_ESTADO_CUADRO
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_ESTADO_CUADRO"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_ESTADO_PERIODO
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_ESTADO_PERIODO"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_ESTRUCTURA
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_ESTRUCTURA"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1847 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_FORMULA_GRILLA
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_FORMULA_GRILLA"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 30659 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_GRILLA
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_GRILLA"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_HISTORIAL_REPORTE
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_HISTORIAL_REPORTE"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 55 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_HISTORIAL_VERSION
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_HISTORIAL_VERSION"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 4720 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_HTML
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_HTML"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_LOG_PROCESO
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_LOG_PROCESO"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 65 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_MENU
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_MENU"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_PERIODO
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_PERIODO"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 30 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_SUB_COLUMNA
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_SUB_COLUMNA"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 21 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_SUB_GRILLA
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_SUB_GRILLA"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 241 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_TEXTO
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_TEXTO"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_TIPO_CELDA
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_TIPO_CELDA"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_TIPO_DATO
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_TIPO_DATO"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_TIPO_ESTRUCTURA
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_TIPO_ESTRUCTURA"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_TIPO_OPERACION
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_TIPO_OPERACION"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_USUARIO_GRUPO
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_USUARIO_GRUPO"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_VERSION
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_VERSION"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1286 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_VERSION_EEFF
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_VERSION_EEFF"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 201 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_VERSION_PERIODO
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_VERSION_PERIODO"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_XBRL_TAXONOMIA
--------------------------------------------------------

   CREATE SEQUENCE  "REVELACIONES"."SEQ_XBRL_TAXONOMIA"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 5 NOCACHE  NOORDER  NOCYCLE ;
