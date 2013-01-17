/*==============================================================*/
/* DBMS name:      Microsoft SQL Server 2008                    */
/* Created on:     17/01/2013 12:16:02                          */
/*==============================================================*/


if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_AGRUPACION_COLUMNA') and o.name = 'FK_REV_AGRU_REF_REV_COLU')
alter table IFRS_AGRUPACION_COLUMNA
   drop constraint FK_REV_AGRU_REF_REV_COLU
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_AREA_NEGOCIO') and o.name = 'FK_IFRS_ARE_REFERENCE_IFRS_EMP')
alter table IFRS_AREA_NEGOCIO
   drop constraint FK_IFRS_ARE_REFERENCE_IFRS_EMP
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_CATALOGO') and o.name = 'FK_REV_CATA_REF_REV_TIPO_CU')
alter table IFRS_CATALOGO
   drop constraint FK_REV_CATA_REF_REV_TIPO_CU
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_CATALOGO') and o.name = 'FK_IFRS_CAT_REFERENCE_IFRS_EMP')
alter table IFRS_CATALOGO
   drop constraint FK_IFRS_CAT_REFERENCE_IFRS_EMP
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_CATALOGO_GRUPO') and o.name = 'FK_REV_CATA_REF_REV_CATA')
alter table IFRS_CATALOGO_GRUPO
   drop constraint FK_REV_CATA_REF_REV_CATA
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_CATALOGO_GRUPO') and o.name = 'FK_REV_CATA_REF_REV_GRUP')
alter table IFRS_CATALOGO_GRUPO
   drop constraint FK_REV_CATA_REF_REV_GRUP
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_CELDA') and o.name = 'FK_REV_CELD_REF_REV_COLU')
alter table IFRS_CELDA
   drop constraint FK_REV_CELD_REF_REV_COLU
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_CELDA') and o.name = 'FK_REV_CELD_REF_REV_TIPO')
alter table IFRS_CELDA
   drop constraint FK_REV_CELD_REF_REV_TIPO
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_CELDA') and o.name = 'FK_REV_CELD_REF_REV_TIPO_CELDA')
alter table IFRS_CELDA
   drop constraint FK_REV_CELD_REF_REV_TIPO_CELDA
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_COLUMNA') and o.name = 'FK_REV_COLU_REF_REV_GRIL')
alter table IFRS_COLUMNA
   drop constraint FK_REV_COLU_REF_REV_GRIL
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_DETALLE_EEFF') and o.name = 'FK_IFRS_DET_REFERENCE_IFRS_EEF')
alter table IFRS_DETALLE_EEFF
   drop constraint FK_IFRS_DET_REFERENCE_IFRS_EEF
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_DETALLE_EEFF') and o.name = 'FK_IFRS_DET_REFERENCE_IFRS_CUE')
alter table IFRS_DETALLE_EEFF
   drop constraint FK_IFRS_DET_REFERENCE_IFRS_CUE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_EEFF') and o.name = 'FK_IFRS_EEF_REFERENCE_IFRS_VER')
alter table IFRS_EEFF
   drop constraint FK_IFRS_EEF_REFERENCE_IFRS_VER
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_EEFF') and o.name = 'FK_IFRS_EEF_REFERENCE_IFRS_COD')
alter table IFRS_EEFF
   drop constraint FK_IFRS_EEF_REFERENCE_IFRS_COD
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_ESTRUCTURA') and o.name = 'FK_REV_ESTR_REF_REV_TIPO')
alter table IFRS_ESTRUCTURA
   drop constraint FK_REV_ESTR_REF_REV_TIPO
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_ESTRUCTURA') and o.name = 'FK_REV_ESTR_REF_REV_VERS')
alter table IFRS_ESTRUCTURA
   drop constraint FK_REV_ESTR_REF_REV_VERS
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_GRILLA') and o.name = 'FK_REV_GRIL_REF_REV_ESTR')
alter table IFRS_GRILLA
   drop constraint FK_REV_GRIL_REF_REV_ESTR
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_GRUPO') and o.name = 'FK_REV_GRUP_REF_REV_AREA')
alter table IFRS_GRUPO
   drop constraint FK_REV_GRUP_REF_REV_AREA
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_GRUPO_EMPRESA') and o.name = 'FK_IFRS_GRU_REFERENCE_IFRS_GRU')
alter table IFRS_GRUPO_EMPRESA
   drop constraint FK_IFRS_GRU_REFERENCE_IFRS_GRU
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_GRUPO_EMPRESA') and o.name = 'FK_IFRS_GRU_REFERENCE_IFRS_EMP')
alter table IFRS_GRUPO_EMPRESA
   drop constraint FK_IFRS_GRU_REFERENCE_IFRS_EMP
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_HISTORIAL_REPORTE') and o.name = 'FK_IFRS_HIS_REFERENCE_IFRS_PER')
alter table IFRS_HISTORIAL_REPORTE
   drop constraint FK_IFRS_HIS_REFERENCE_IFRS_PER
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_HISTORIAL_REPORTE') and o.name = 'FK_IFRS_HIS_REFERENCE_IFRS_USU')
alter table IFRS_HISTORIAL_REPORTE
   drop constraint FK_IFRS_HIS_REFERENCE_IFRS_USU
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_HISTORIAL_VERSION') and o.name = 'FK_REV_HIST_REF_REV_ESTA')
alter table IFRS_HISTORIAL_VERSION
   drop constraint FK_REV_HIST_REF_REV_ESTA
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_HISTORIAL_VERSION') and o.name = 'FK_IFRS_HIS_REFERENCE_IFRS_VER')
alter table IFRS_HISTORIAL_VERSION
   drop constraint FK_IFRS_HIS_REFERENCE_IFRS_VER
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_HISTORIAL_VERSION') and o.name = 'FK_IFRS_HIS_REFERENCE_IFRS_US2')
alter table IFRS_HISTORIAL_VERSION
   drop constraint FK_IFRS_HIS_REFERENCE_IFRS_US2
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_HTML') and o.name = 'FK_REV_HTML_REF_REV_ESTR')
alter table IFRS_HTML
   drop constraint FK_REV_HTML_REF_REV_ESTR
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_MENU_GRUPO') and o.name = 'FK_NEW_REV_MENU_REF_REV_GRUP')
alter table IFRS_MENU_GRUPO
   drop constraint FK_NEW_REV_MENU_REF_REV_GRUP
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_MENU_GRUPO') and o.name = 'FK_NEW_REV_MENU_REF_REV_MENU')
alter table IFRS_MENU_GRUPO
   drop constraint FK_NEW_REV_MENU_REF_REV_MENU
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_PARAMETRO') and o.name = 'FK_IFRS_PAR_REFERENCE_IFRS_TIP')
alter table IFRS_PARAMETRO
   drop constraint FK_IFRS_PAR_REFERENCE_IFRS_TIP
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_PERIODO') and o.name = 'FK_IFRS_PER_REF_IFRS_EST_P')
alter table IFRS_PERIODO
   drop constraint FK_IFRS_PER_REF_IFRS_EST_P
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_PERIODO_EMPRESA') and o.name = 'FK_IFRS_PER_REFERENCE_IFRS_EMP')
alter table IFRS_PERIODO_EMPRESA
   drop constraint FK_IFRS_PER_REFERENCE_IFRS_EMP
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_PERIODO_EMPRESA') and o.name = 'FK_IFRS_PER_REFERENCE_IFRS_EST')
alter table IFRS_PERIODO_EMPRESA
   drop constraint FK_IFRS_PER_REFERENCE_IFRS_EST
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_PERIODO_EMPRESA') and o.name = 'FK_IFRS_PER_REFERENCE_IFRS_PER')
alter table IFRS_PERIODO_EMPRESA
   drop constraint FK_IFRS_PER_REFERENCE_IFRS_PER
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_RELACION_DETALLE_EEFF') and o.name = 'FK_IFRS_REL_CONST_REL_IFRS_COD')
alter table IFRS_RELACION_DETALLE_EEFF
   drop constraint FK_IFRS_REL_CONST_REL_IFRS_COD
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_RELACION_DETALLE_EEFF') and o.name = 'FK_REV_RELA_DET_REF_REV_CELD')
alter table IFRS_RELACION_DETALLE_EEFF
   drop constraint FK_REV_RELA_DET_REF_REV_CELD
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_RELACION_DETALLE_EEFF') and o.name = 'FK_IFRS_REL_REFERENCE_IFRS_PER')
alter table IFRS_RELACION_DETALLE_EEFF
   drop constraint FK_IFRS_REL_REFERENCE_IFRS_PER
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_RELACION_DETALLE_EEFF') and o.name = 'FK_IFRS_REL_REFERENCE_IFRS_CUE')
alter table IFRS_RELACION_DETALLE_EEFF
   drop constraint FK_IFRS_REL_REFERENCE_IFRS_CUE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_RELACION_EEFF') and o.name = 'FK_IFRS_REL_CONS_REL__IFRS_COD')
alter table IFRS_RELACION_EEFF
   drop constraint FK_IFRS_REL_CONS_REL__IFRS_COD
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_RELACION_EEFF') and o.name = 'FK_REV_RELA_EEFF_REF_REV_CELD')
alter table IFRS_RELACION_EEFF
   drop constraint FK_REV_RELA_EEFF_REF_REV_CELD
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_RELACION_EEFF') and o.name = 'FK_IFRS_REL_REFERENCE_IFRS_PE2')
alter table IFRS_RELACION_EEFF
   drop constraint FK_IFRS_REL_REFERENCE_IFRS_PE2
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_TEXTO') and o.name = 'FK_REV_TEXT_REFERENCE_REV_ESTR')
alter table IFRS_TEXTO
   drop constraint FK_REV_TEXT_REFERENCE_REV_ESTR
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_USUARIO') and o.name = 'FK_IFRS_USU_REFERENCE_IFRS_ROL')
alter table IFRS_USUARIO
   drop constraint FK_IFRS_USU_REFERENCE_IFRS_ROL
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_USUARIO_GRUPO') and o.name = 'FK_REV_USUA_REF_REV_GRUP')
alter table IFRS_USUARIO_GRUPO
   drop constraint FK_REV_USUA_REF_REV_GRUP
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_USUARIO_GRUPO') and o.name = 'FK_IFRS_USU_REFERENCE_IFRS_USU')
alter table IFRS_USUARIO_GRUPO
   drop constraint FK_IFRS_USU_REFERENCE_IFRS_USU
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_VERSION') and o.name = 'FK_REV_VERS_REF_REV_CATA')
alter table IFRS_VERSION
   drop constraint FK_REV_VERS_REF_REV_CATA
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_VERSION') and o.name = 'FK_IFRS_VER_REFERENCE_IFRS_PER')
alter table IFRS_VERSION
   drop constraint FK_IFRS_VER_REFERENCE_IFRS_PER
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_VERSION') and o.name = 'FK_IFRS_VER_REFERENCE_IFRS_EST')
alter table IFRS_VERSION
   drop constraint FK_IFRS_VER_REFERENCE_IFRS_EST
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_VERSION_EEFF') and o.name = 'FK_IFRS_VER_REFERENCE_IFRS_PE2')
alter table IFRS_VERSION_EEFF
   drop constraint FK_IFRS_VER_REFERENCE_IFRS_PE2
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('IFRS_VERSION_EEFF') and o.name = 'FK_IFRS_VER_REFERENCE_IFRS_TIP')
alter table IFRS_VERSION_EEFF
   drop constraint FK_IFRS_VER_REFERENCE_IFRS_TIP
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_AGRUPACION_COLUMNA')
            and   type = 'U')
   drop table IFRS_AGRUPACION_COLUMNA
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_AREA_NEGOCIO')
            and   type = 'U')
   drop table IFRS_AREA_NEGOCIO
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_CATALOGO')
            and   type = 'U')
   drop table IFRS_CATALOGO
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_CATALOGO_GRUPO')
            and   type = 'U')
   drop table IFRS_CATALOGO_GRUPO
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_CELDA')
            and   type = 'U')
   drop table IFRS_CELDA
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_CODIGO_FECU')
            and   type = 'U')
   drop table IFRS_CODIGO_FECU
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_COLUMNA')
            and   type = 'U')
   drop table IFRS_COLUMNA
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_CUENTA_CONTABLE')
            and   type = 'U')
   drop table IFRS_CUENTA_CONTABLE
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_DETALLE_EEFF')
            and   type = 'U')
   drop table IFRS_DETALLE_EEFF
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_EEFF')
            and   type = 'U')
   drop table IFRS_EEFF
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_EMPRESA')
            and   type = 'U')
   drop table IFRS_EMPRESA
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_ESTADO_CUADRO')
            and   type = 'U')
   drop table IFRS_ESTADO_CUADRO
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_ESTADO_PERIODO')
            and   type = 'U')
   drop table IFRS_ESTADO_PERIODO
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_ESTRUCTURA')
            and   type = 'U')
   drop table IFRS_ESTRUCTURA
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_GRILLA')
            and   type = 'U')
   drop table IFRS_GRILLA
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_GRUPO')
            and   type = 'U')
   drop table IFRS_GRUPO
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_GRUPO_EMPRESA')
            and   type = 'U')
   drop table IFRS_GRUPO_EMPRESA
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_HISTORIAL_REPORTE')
            and   type = 'U')
   drop table IFRS_HISTORIAL_REPORTE
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_HISTORIAL_VERSION')
            and   type = 'U')
   drop table IFRS_HISTORIAL_VERSION
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_HTML')
            and   type = 'U')
   drop table IFRS_HTML
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_LOG_PROCESO')
            and   type = 'U')
   drop table IFRS_LOG_PROCESO
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_MENU')
            and   type = 'U')
   drop table IFRS_MENU
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_MENU_GRUPO')
            and   type = 'U')
   drop table IFRS_MENU_GRUPO
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_PARAMETRO')
            and   type = 'U')
   drop table IFRS_PARAMETRO
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_PERIODO')
            and   type = 'U')
   drop table IFRS_PERIODO
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_PERIODO_EMPRESA')
            and   type = 'U')
   drop table IFRS_PERIODO_EMPRESA
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_RELACION_DETALLE_EEFF')
            and   type = 'U')
   drop table IFRS_RELACION_DETALLE_EEFF
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_RELACION_EEFF')
            and   type = 'U')
   drop table IFRS_RELACION_EEFF
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_ROL')
            and   type = 'U')
   drop table IFRS_ROL
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_TEXTO')
            and   type = 'U')
   drop table IFRS_TEXTO
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_TIPO_CELDA')
            and   type = 'U')
   drop table IFRS_TIPO_CELDA
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_TIPO_CUADRO')
            and   type = 'U')
   drop table IFRS_TIPO_CUADRO
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_TIPO_DATO')
            and   type = 'U')
   drop table IFRS_TIPO_DATO
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_TIPO_ESTADO_EEFF')
            and   type = 'U')
   drop table IFRS_TIPO_ESTADO_EEFF
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_TIPO_ESTRUCTURA')
            and   type = 'U')
   drop table IFRS_TIPO_ESTRUCTURA
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_TIPO_PARAMETRO')
            and   type = 'U')
   drop table IFRS_TIPO_PARAMETRO
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_USUARIO')
            and   type = 'U')
   drop table IFRS_USUARIO
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_USUARIO_GRUPO')
            and   type = 'U')
   drop table IFRS_USUARIO_GRUPO
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_VERSION')
            and   type = 'U')
   drop table IFRS_VERSION
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_VERSION_EEFF')
            and   type = 'U')
   drop table IFRS_VERSION_EEFF
go

if exists (select 1
            from  sysobjects
           where  id = object_id('IFRS_XBRL_TAXONOMIA')
            and   type = 'U')
   drop table IFRS_XBRL_TAXONOMIA
go

/*==============================================================*/
/* Table: IFRS_AGRUPACION_COLUMNA                               */
/*==============================================================*/
create table IFRS_AGRUPACION_COLUMNA (
   ID_NIVEL             numeric(8,0)         not null,
   ID_COLUMNA           numeric(8,0)         not null,
   ID_GRILLA            numeric(10,0)        not null,
   TITULO               varchar(256)         null,
   ANCHO                numeric(4,0)         null,
   GRUPO                numeric(4,0)         null,
   constraint PK_REV_AGRUPACION_COLUMNA primary key nonclustered (ID_COLUMNA, ID_GRILLA, ID_NIVEL)
)
go

/*==============================================================*/
/* Table: IFRS_AREA_NEGOCIO                                     */
/*==============================================================*/
create table IFRS_AREA_NEGOCIO (
   ID_AREA_NEGOCIO      varchar(3)           not null,
   ID_RUT               numeric(10)          null,
   NOMBRE               varchar(256)         null,
   VIGENTE              numeric(1)           null,
   constraint PK_NEW_REV_AREA_NEGOCIO primary key nonclustered (ID_AREA_NEGOCIO)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA QUE ALMACENA LAS AREAS DE NEGOCIO INVOLUCRADAS EN EL SISTEMA',
   'user', @CurrentUser, 'table', 'IFRS_AREA_NEGOCIO'
go

/*==============================================================*/
/* Table: IFRS_CATALOGO                                         */
/*==============================================================*/
create table IFRS_CATALOGO (
   ID_CATALOGO          numeric(8,0)         identity,
   RUT                  numeric(10)          not null,
   ID_TIPO_CUADRO       numeric(4,0)         not null,
   COD_CUADRO           numeric(5,0)         null,
   COD_SUBCUADRO        numeric(5,0)         null,
   NOMBRE               varchar(256)         null,
   TITULO               varchar(256)         null,
   ORDEN                numeric(5,2)         null,
   VIGENCIA             numeric(1,0)         null,
   IMPRESION_HORIZONTAL numeric(1)           null,
   VALIDAR_EEFF         numeric(1)           null,
   constraint PK_NEW_REV_CATALOGO_NOTA primary key nonclustered (ID_CATALOGO)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA DE MAESTRO QUE CONTIENE EL CATALOGO DE TODAS LAS NOTAS DEL SISTEMA',
   'user', @CurrentUser, 'table', 'IFRS_CATALOGO'
go

/*==============================================================*/
/* Table: IFRS_CATALOGO_GRUPO                                   */
/*==============================================================*/
create table IFRS_CATALOGO_GRUPO (
   ID_CATALOGO          numeric(8,0)         not null,
   ID_GRUPO_ACCESO      varchar(128)         not null,
   constraint PK_NEW_REV_CATALOGO_NOTA_GRUPO primary key nonclustered (ID_CATALOGO, ID_GRUPO_ACCESO)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA QUE ALMACENA LA ASOCIACION ENTRE EL CATALOGO DE UNA NOTA Y LOS GRUPOS QUE TIENEN ACCESO A ELLA.',
   'user', @CurrentUser, 'table', 'IFRS_CATALOGO_GRUPO'
go

/*==============================================================*/
/* Table: IFRS_CELDA                                            */
/*==============================================================*/
create table IFRS_CELDA (
   ID_COLUMNA           numeric(8,0)         not null,
   ID_FILA              numeric(4,0)         not null,
   ID_GRILLA            numeric(10,0)        not null,
   ID_TIPO_CELDA        numeric(4,0)         null,
   ID_TIPO_DATO         numeric(2,0)         null,
   VALOR                varchar(2048)        null,
   CHILD_HORIZONTAL     numeric(4,0)         null,
   PARENT_HORIZONTAL    numeric(4,0)         null,
   CHILD_VERTICAL       numeric(4,0)         null,
   PARENT_VERTICAL      numeric(4,0)         null,
   FORMULA              varchar(256)         null,
   constraint PK_NEW_REV_CELDA_NOTA primary key nonclustered (ID_COLUMNA, ID_GRILLA, ID_FILA)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA QUE ALMACENA LOS ATRIBUTOS DE LA COLUMNA DE UNA NOTA.',
   'user', @CurrentUser, 'table', 'IFRS_CELDA'
go

/*==============================================================*/
/* Table: IFRS_CODIGO_FECU                                      */
/*==============================================================*/
create table IFRS_CODIGO_FECU (
   ID_FECU              numeric(12)          not null,
   DESCRIPCION          varchar(256)         not null,
   VIGENCIA             numeric(1)           not null,
   constraint PK_IFRS_CODIGO_FECU primary key nonclustered (ID_FECU)
)
go

/*==============================================================*/
/* Table: IFRS_COLUMNA                                          */
/*==============================================================*/
create table IFRS_COLUMNA (
   ID_COLUMNA           numeric(8,0)         not null,
   ID_GRILLA            numeric(10,0)        not null,
   TITULO_COLUMNA       varchar(128)         null,
   ORDEN                numeric(3,0)         null,
   ANCHO                numeric(4,0)         null,
   ROW_HEADER           numeric(1,0)         null,
   constraint PK_NEW_REV_COLUMNA_NOTA primary key nonclustered (ID_COLUMNA, ID_GRILLA)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA QUE CONTIENE LAS COLUMNAS DE LA GRILLA',
   'user', @CurrentUser, 'table', 'IFRS_COLUMNA'
go

/*==============================================================*/
/* Table: IFRS_CUENTA_CONTABLE                                  */
/*==============================================================*/
create table IFRS_CUENTA_CONTABLE (
   ID_CUENTA            numeric(10)          not null,
   DESCRIPCION          varchar(256)         not null,
   VIGENCIA             numeric(1)           null,
   constraint PK_IFRS_CUENTA_CONTABLE primary key nonclustered (ID_CUENTA)
)
go

/*==============================================================*/
/* Table: IFRS_DETALLE_EEFF                                     */
/*==============================================================*/
create table IFRS_DETALLE_EEFF (
   ID_CUENTA            numeric(10)          not null,
   ID_FECU              numeric(12)          not null,
   ID_VERSION_EEFF      numeric(10)          not null,
   MONTO_EBS            numeric(18,4)        null,
   MONTO_RECLASIFICACION numeric(18,4)        null,
   MONTO_PESOS          numeric(18,4)        null,
   MONTO_MILES          numeric(18,4)        null,
   MONTO_PESOS_MIL      numeric(18,4)        null,
   constraint PK_IFRS_DETALLE_EEFF primary key nonclustered (ID_CUENTA, ID_FECU, ID_VERSION_EEFF)
)
go

/*==============================================================*/
/* Table: IFRS_EEFF                                             */
/*==============================================================*/
create table IFRS_EEFF (
   ID_FECU              numeric(12)          not null,
   ID_VERSION_EEFF      numeric(10)          not null,
   MONTO_TOTAL          numeric(18,4)        null,
   constraint PK_IFRS_EEFF primary key nonclustered (ID_FECU, ID_VERSION_EEFF)
)
go

/*==============================================================*/
/* Table: IFRS_EMPRESA                                          */
/*==============================================================*/
create table IFRS_EMPRESA (
   ID_RUT               numeric(10)          not null,
   DV                   char(1)              not null,
   NOMBRE               varchar(256)         not null,
   RAZON_SOCIAL         varchar(256)         null,
   GIRO                 varchar(256)         null,
   constraint PK_IFRS_EMPRESA primary key nonclustered (ID_RUT)
)
go

/*==============================================================*/
/* Table: IFRS_ESTADO_CUADRO                                    */
/*==============================================================*/
create table IFRS_ESTADO_CUADRO (
   ID_ESTADO_CUADRO     numeric(2,0)         identity,
   NOMBRE               varchar(128)         null,
   constraint PK_NEW_REV_ESTADO_NOTA primary key nonclustered (ID_ESTADO_CUADRO)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA QUE ALMACENA LOS ESTADOS QUE PUEDE TENER UNA NOTA',
   'user', @CurrentUser, 'table', 'IFRS_ESTADO_CUADRO'
go

/*==============================================================*/
/* Table: IFRS_ESTADO_PERIODO                                   */
/*==============================================================*/
create table IFRS_ESTADO_PERIODO (
   ID_ESTADO_PERIODO    numeric(2,0)         identity,
   NOMBRE               varchar(128)         null,
   constraint PK_NEW_REV_ESTADO_PERIODO primary key nonclustered (ID_ESTADO_PERIODO)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA QUE ALMACENA LOS ESTADOS QUE PUEDE TENER UN PERIODO',
   'user', @CurrentUser, 'table', 'IFRS_ESTADO_PERIODO'
go

/*==============================================================*/
/* Table: IFRS_ESTRUCTURA                                       */
/*==============================================================*/
create table IFRS_ESTRUCTURA (
   ID_ESTRUCTURA        numeric(10,0)        identity,
   ID_VERSION           numeric(8,0)         null,
   ID_TIPO_ESTRUCTURA   numeric(4,0)         null,
   ORDEN                numeric(5,0)         null,
   constraint PK_NEW_REV_ESTRUCTURA_NOTA primary key nonclustered (ID_ESTRUCTURA)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA QUE DEFINE COMO SE ESTRUCTURA LA NOTA',
   'user', @CurrentUser, 'table', 'IFRS_ESTRUCTURA'
go

/*==============================================================*/
/* Table: IFRS_GRILLA                                           */
/*==============================================================*/
create table IFRS_GRILLA (
   ID_GRILLA            numeric(10,0)        not null,
   TITULO               varchar(256)         null,
   TIPO_FORMULA         numeric(4,0)         null default 1,
   constraint PK_NEW_REV_GRILLA_NOTA primary key nonclustered (ID_GRILLA)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA QUE DEFINE UNA GRILLA ',
   'user', @CurrentUser, 'table', 'IFRS_GRILLA'
go

/*==============================================================*/
/* Table: IFRS_GRUPO                                            */
/*==============================================================*/
create table IFRS_GRUPO (
   ID_GRUPO_ACCESO      varchar(128)         not null,
   ID_AREA_NEGOCIO      varchar(3)           null,
   NOMBRE               varchar(512)         null,
   ACCESO_BLOQUEADO     numeric(1)           null,
   VIGENTE              numeric(1)           null,
   constraint PK_NEW_REV_GRUPO_ACCESO primary key nonclustered (ID_GRUPO_ACCESO)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA QUE ALMACENA EL MAESTRO CON LOS GRUPOS DE ACCESO A LA APLICACION,
   CLASIFICADOS POR AREA DE NEGOCIO',
   'user', @CurrentUser, 'table', 'IFRS_GRUPO'
go

/*==============================================================*/
/* Table: IFRS_GRUPO_EMPRESA                                    */
/*==============================================================*/
create table IFRS_GRUPO_EMPRESA (
   ID_GRUPO_ACCESO      varchar(128)         not null,
   ID_RUT               numeric(10)          not null,
   constraint PK_IFRS_GRUPO_EMPRESA primary key nonclustered (ID_GRUPO_ACCESO, ID_RUT)
)
go

/*==============================================================*/
/* Table: IFRS_HISTORIAL_REPORTE                                */
/*==============================================================*/
create table IFRS_HISTORIAL_REPORTE (
   ID_HISTORIAL_REPORTE numeric(10,0)        identity,
   COMENTARIO           varchar(1024)        null,
   IP_USUARIO           varchar(256)         null,
   CHECK_SUM_EXPORTACION varchar(512)         null,
   FECHA_EXPORTACION    datetime             null,
   DOCUMENTO            varbinary(max)       null,
   NOMBRE_ARCHIVO       varchar(512)         null,
   ID_PERIODO           numeric(6)           null,
   ID_RUT               numeric(10)          null,
   NOMBRE_USUARIO       varchar(256)         null,
   constraint PK_IFRS_HISTORIAL_REPORTE primary key nonclustered (ID_HISTORIAL_REPORTE)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA QUE ALMACENA UN HIISTORIAL CON LA GENERACION DEL REPORTE FINAL FECU EN FORMATO MS WORD,
   ALMACENANDO EL CHECKSUM MD5 DEL ARCHIVO GENERADO PARA ASI COMPARAR SI EXISTIERON MIDIFICACIONES EN EL DOCUMENTO.',
   'user', @CurrentUser, 'table', 'IFRS_HISTORIAL_REPORTE'
go

/*==============================================================*/
/* Table: IFRS_HISTORIAL_VERSION                                */
/*==============================================================*/
create table IFRS_HISTORIAL_VERSION (
   ID_HISTORIAL         numeric              identity,
   ID_ESTADO_CUADRO     numeric(2,0)         null,
   ID_VERSION           numeric(8,0)         null,
   NOMBRE_USUARIO       varchar(256)         null,
   FECHA_PROCESO        datetime             null,
   COMENTARIO           varchar(2048)        null,
   USUARIO              varchar(256)         null,
   constraint PK_REV_HISTORIAL_VERSION primary key nonclustered (ID_HISTORIAL)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA QUE ALMACENA EL HITORIAL DE LOS CAMBIOS DE ESTADO DE UNA VERSION PARA EL PERIODO DETERMINADO',
   'user', @CurrentUser, 'table', 'IFRS_HISTORIAL_VERSION'
go

/*==============================================================*/
/* Table: IFRS_HTML                                             */
/*==============================================================*/
create table IFRS_HTML (
   ID_HTML              numeric(10,0)        not null,
   CONTENIDO            varbinary(Max)       null,
   TITULO               varchar(1024)        null,
   constraint PK_NEW_REV_HTML_NOTA primary key nonclustered (ID_HTML)
)
go

/*==============================================================*/
/* Table: IFRS_LOG_PROCESO                                      */
/*==============================================================*/
create table IFRS_LOG_PROCESO (
   ID_LOG               numeric(10)          identity,
   USUARIO              varchar(256)         null,
   FECHA                datetime             null,
   LOG                  varchar(4000)        null,
   constraint PK_IFRS_LOG_PROCESO primary key nonclustered (ID_LOG)
)
go

/*==============================================================*/
/* Table: IFRS_MENU                                             */
/*==============================================================*/
create table IFRS_MENU (
   ID_MENU              numeric(3,0)         not null,
   NOMBRE               varchar(512)         null,
   ESTADO               numeric(1,0)         null,
   GRUPO                numeric(2,0)         null,
   URL_MENU             varchar(512)         null,
   ES_PADRE             numeric(1,0)         null default NULL,
   ORDEN                numeric(4,0)         null,
   constraint PK_NEW_REV_MENU primary key nonclustered (ID_MENU)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA QUE ALMACENA LAS OPCIONES DE MENU DISPONIBLES EN LA APLICACIÓN',
   'user', @CurrentUser, 'table', 'IFRS_MENU'
go

/*==============================================================*/
/* Table: IFRS_MENU_GRUPO                                       */
/*==============================================================*/
create table IFRS_MENU_GRUPO (
   ID_MENU              numeric(3,0)         not null,
   ID_GRUPO_ACCESO      varchar(128)         not null,
   constraint PK_NEW_REV_MENU_GRUPO primary key nonclustered (ID_MENU, ID_GRUPO_ACCESO)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA QUE ALMACENA LA ASOCIACION DE MENU CON EL GRUPO QUE POSEE EL ACCESO',
   'user', @CurrentUser, 'table', 'IFRS_MENU_GRUPO'
go

/*==============================================================*/
/* Table: IFRS_PARAMETRO                                        */
/*==============================================================*/
create table IFRS_PARAMETRO (
   NOMBRE               varchar(512)         not null,
   ID_TIPO_PARAMETRO    numeric(4)           not null,
   VALOR                varchar(1024)        not null,
   constraint PK_IFRS_PARAMETRO primary key nonclustered (NOMBRE)
)
go

/*==============================================================*/
/* Table: IFRS_PERIODO                                          */
/*==============================================================*/
create table IFRS_PERIODO (
   ID_PERIODO           numeric(6)           not null,
   ID_ESTADO_PERIODO    numeric(2,0)         null,
   constraint PK_IFRS_PERIODO primary key nonclustered (ID_PERIODO)
)
go

/*==============================================================*/
/* Table: IFRS_PERIODO_EMPRESA                                  */
/*==============================================================*/
create table IFRS_PERIODO_EMPRESA (
   ID_PERIODO           numeric(6)           not null,
   ID_RUT               numeric(10)          not null,
   ID_ESTADO_PERIODO    numeric(2,0)         null,
   constraint PK_NEW_REV_PERIODO primary key nonclustered (ID_PERIODO, ID_RUT)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA QUE ALMACENA LOS PERIODOS FINANCIEROS Y RESPECTIVOS ESTADOS',
   'user', @CurrentUser, 'table', 'IFRS_PERIODO_EMPRESA'
go

/*==============================================================*/
/* Table: IFRS_RELACION_DETALLE_EEFF                            */
/*==============================================================*/
create table IFRS_RELACION_DETALLE_EEFF (
   ID_CUENTA            numeric(10)          not null,
   ID_FECU              numeric(12)          not null,
   ID_PERIODO           numeric(6)           not null,
   ID_RUT               numeric(10)          not null,
   ID_GRILLA            numeric(10,0)        not null,
   ID_COLUMNA           numeric(8,0)         not null,
   ID_FILA              numeric(4,0)         not null,
   MONTO_EBS            numeric(18,4)        null,
   MONTO_RECLASIFICACION numeric(18,4)        null,
   MONTO_PESOS          numeric(18,4)        null,
   MONTO_MILES          numeric(18,4)        null,
   MONTO_PESOS_MIL      numeric(18,4)        null,
   constraint PK_IFRS_RELACION_DETALLE_EEFF primary key nonclustered (ID_PERIODO, ID_FECU, ID_CUENTA, ID_RUT, ID_GRILLA, ID_COLUMNA, ID_FILA)
)
go

/*==============================================================*/
/* Table: IFRS_RELACION_EEFF                                    */
/*==============================================================*/
create table IFRS_RELACION_EEFF (
   ID_FECU              numeric(12)          not null,
   ID_PERIODO           numeric(6)           not null,
   ID_RUT               numeric(10)          not null,
   ID_GRILLA            numeric(10,0)        null,
   ID_COLUMNA           numeric(8,0)         not null,
   ID_FILA              numeric(4,0)         null,
   MONTO_TOTAL          numeric(18,4)        null,
   constraint PK_IFRS_RELACION_EEFF primary key nonclustered (ID_PERIODO, ID_FECU, ID_RUT)
)
go

/*==============================================================*/
/* Table: IFRS_ROL                                              */
/*==============================================================*/
create table IFRS_ROL (
   ID_ROL               varchar(256)         not null,
   NOMBRE_ROL           varchar(512)         null,
   constraint PK_IFRS_ROL primary key nonclustered (ID_ROL)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA QUE DEFINE LO ROLES DISPONIBLES EN EL SISTEMA.
   ',
   'user', @CurrentUser, 'table', 'IFRS_ROL'
go

/*==============================================================*/
/* Table: IFRS_TEXTO                                            */
/*==============================================================*/
create table IFRS_TEXTO (
   ID_TEXTO             numeric(10,0)        not null,
   TEXTO                varchar(4000)        null,
   NEGRITA              numeric(1,0)         null default 0,
   constraint PK_NEW_REV_TEXTO_NOTA primary key nonclustered (ID_TEXTO)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA QUE ALMACENA LOS TEXTOS FIJOS QUE TIENE UNA NOTA',
   'user', @CurrentUser, 'table', 'IFRS_TEXTO'
go

/*==============================================================*/
/* Table: IFRS_TIPO_CELDA                                       */
/*==============================================================*/
create table IFRS_TIPO_CELDA (
   ID_TIPO_CELDA        numeric(4,0)         identity,
   NOMBRE               varchar(128)         null,
   constraint PK_NEW_REV_TIPO_CELDA primary key nonclustered (ID_TIPO_CELDA)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA QUE CONTIENE SI LA CELDA ES TITULO, SUBTOTAL, TOTAL, ETC',
   'user', @CurrentUser, 'table', 'IFRS_TIPO_CELDA'
go

/*==============================================================*/
/* Table: IFRS_TIPO_CUADRO                                      */
/*==============================================================*/
create table IFRS_TIPO_CUADRO (
   ID_TIPO_CUADRO       numeric(4,0)         not null,
   NOMBRE               varchar(256)         null,
   TITULO               varchar(512)         null,
   constraint PK_NEW_REV_TIPO_CUADRO primary key nonclustered (ID_TIPO_CUADRO)
)
go

/*==============================================================*/
/* Table: IFRS_TIPO_DATO                                        */
/*==============================================================*/
create table IFRS_TIPO_DATO (
   ID_TIPO_DATO         numeric(2,0)         identity,
   NOMBRE               varchar(64)          null,
   NOMBRE_CLASE         varchar(256)         null,
   constraint PK_NEW_REV_TIPO_DATO primary key nonclustered (ID_TIPO_DATO)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA QUE DEFINE EL TIPO DE DATO JAVA  QUE CONTIENE CADA COLUMNA',
   'user', @CurrentUser, 'table', 'IFRS_TIPO_DATO'
go

/*==============================================================*/
/* Table: IFRS_TIPO_ESTADO_EEFF                                 */
/*==============================================================*/
create table IFRS_TIPO_ESTADO_EEFF (
   ID_ESTADO_EEFF       numeric(4)           not null,
   NOMBRE               varchar(256)         not null,
   VIGENTE              numeric(1)           not null,
   constraint PK_IFRS_TIPO_ESTADO_EEFF primary key nonclustered (ID_ESTADO_EEFF)
)
go

/*==============================================================*/
/* Table: IFRS_TIPO_ESTRUCTURA                                  */
/*==============================================================*/
create table IFRS_TIPO_ESTRUCTURA (
   ID_TIPO_ESTRUCTURA   numeric(4,0)         identity,
   NOMBRE               varchar(64)          null,
   constraint PK_NEW_REV_TIPO_ESTRUCTURA primary key nonclustered (ID_TIPO_ESTRUCTURA)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA QUE CONTIENE LA DEFINICION DE LOS TIPOS DE  COMPONENTES DE LA NOTA',
   'user', @CurrentUser, 'table', 'IFRS_TIPO_ESTRUCTURA'
go

/*==============================================================*/
/* Table: IFRS_TIPO_PARAMETRO                                   */
/*==============================================================*/
create table IFRS_TIPO_PARAMETRO (
   ID_TIPO_PARAMETRO    numeric(4)           not null,
   NOMBRE               varchar(1024)        not null,
   constraint PK_IFRS_TIPO_PARAMETRO primary key nonclustered (ID_TIPO_PARAMETRO)
)
go

/*==============================================================*/
/* Table: IFRS_USUARIO                                          */
/*==============================================================*/
create table IFRS_USUARIO (
   NOMBRE_USUARIO       varchar(256)         not null,
   ID_ROL               varchar(256)         null,
   PASSWORD             varchar(256)         null,
   EMAIL                varchar(256)         null,
   VIGENTE              numeric(1)           null,
   FECHA_CREACION       datetime             null,
   FECHA_ACTUALIZACION  datetime             null,
   FECHA_ULTIMO_ACCESO  datetime             null,
   NOMBRE               varchar(256)         null,
   APELLIDO_PATERNO     varchar(256)         null,
   APELLIDO_MATERNO     varchar(256)         null,
   CAMBIAR_PASSWORD     numeric(1)           null,
   constraint PK_IFRS_USUARIO primary key nonclustered (NOMBRE_USUARIO)
)
go

/*==============================================================*/
/* Table: IFRS_USUARIO_GRUPO                                    */
/*==============================================================*/
create table IFRS_USUARIO_GRUPO (
   NOMBRE_USUARIO       varchar(256)         not null,
   ID_GRUPO_ACCESO      varchar(128)         not null,
   constraint PK_NEW_REV_USUARIO_GRUPO primary key nonclustered (ID_GRUPO_ACCESO, NOMBRE_USUARIO)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA QUE ALMACENA LA ASOCIACION ENTRE EL USUARIO Y EL GRUPO AL QUE PERTENECE',
   'user', @CurrentUser, 'table', 'IFRS_USUARIO_GRUPO'
go

/*==============================================================*/
/* Table: IFRS_VERSION                                          */
/*==============================================================*/
create table IFRS_VERSION (
   ID_VERSION           numeric(8,0)         identity,
   ID_CATALOGO          numeric(8,0)         null,
   ID_PERIODO           numeric(6)           null,
   ID_RUT               numeric(10)          null,
   ID_ESTADO_CUADRO     numeric(2,0)         null,
   VERSION              numeric(2,0)         null,
   VIGENCIA             numeric(1,0)         null,
   FECHA_CREACION       datetime             null default 'SYSDATE',
   COMENTARIO           varchar(256)         null,
   FECHA_ULTIMO_PROCESO datetime             null,
   USUARIO              varchar(256)         null,
   VALIDADO_EEFF        numeric(1,0)         null,
   DATOS_MODIFICADOS    numeric(1,0)         null,
   constraint PK_NEW_REV_VERSION_NOTA primary key nonclustered (ID_VERSION)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA QUE CONTIENE LAS VERSIONES PARA CADA  NOTA',
   'user', @CurrentUser, 'table', 'IFRS_VERSION'
go

/*==============================================================*/
/* Table: IFRS_VERSION_EEFF                                     */
/*==============================================================*/
create table IFRS_VERSION_EEFF (
   ID_VERSION_EEFF      numeric(10)          identity,
   ID_PERIODO           numeric(6)           not null,
   ID_RUT               numeric(10)          not null,
   ID_ESTADO_EEFF       numeric(4)           null,
   VERSION              numeric(4)           not null,
   VIGENCIA             numeric(1)           not null,
   USUARIO              varchar(256)         null,
   FECHA                datetime             null,
   constraint PK_IFRS_VERSION_EEFF primary key nonclustered (ID_VERSION_EEFF)
)
go

/*==============================================================*/
/* Table: IFRS_XBRL_TAXONOMIA                                   */
/*==============================================================*/
create table IFRS_XBRL_TAXONOMIA (
   ID_TAXONOMIA__NOMBRE__TOP_TAXO numeric(8)           not null,
   NOMBRE               varchar(1024)        null,
   TOP_TAXONOMY         varchar(1024)        null,
   NOMBRE_ARCHIVO       varchar(1024)        null,
   URI                  varchar(1024)        null,
   VIGENTE              numeric(1)           null,
   FECHA_TAXONOMIA      datetime             null,
   USUARIO_CREACION     varchar(256)         null,
   USUARIO_EDICION      varchar(256)         null,
   FECHA_CREACION       datetime             null,
   FECHA_EDICION        datetime             null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'TABLA PARA GUARDAR LA RUTA DONDE SE ENCUENTRAN LAS TAXONOMIAS.',
   'user', @CurrentUser, 'table', 'IFRS_XBRL_TAXONOMIA'
go

alter table IFRS_AGRUPACION_COLUMNA
   add constraint FK_REV_AGRU_REF_REV_COLU foreign key (ID_COLUMNA, ID_GRILLA)
      references IFRS_COLUMNA (ID_COLUMNA, ID_GRILLA)
go

alter table IFRS_AREA_NEGOCIO
   add constraint FK_IFRS_ARE_REFERENCE_IFRS_EMP foreign key (ID_RUT)
      references IFRS_EMPRESA (ID_RUT)
go

alter table IFRS_CATALOGO
   add constraint FK_REV_CATA_REF_REV_TIPO_CU foreign key (ID_TIPO_CUADRO)
      references IFRS_TIPO_CUADRO (ID_TIPO_CUADRO)
go

alter table IFRS_CATALOGO
   add constraint FK_IFRS_CAT_REFERENCE_IFRS_EMP foreign key (RUT)
      references IFRS_EMPRESA (ID_RUT)
go

alter table IFRS_CATALOGO_GRUPO
   add constraint FK_REV_CATA_REF_REV_CATA foreign key (ID_CATALOGO)
      references IFRS_CATALOGO (ID_CATALOGO)
go

alter table IFRS_CATALOGO_GRUPO
   add constraint FK_REV_CATA_REF_REV_GRUP foreign key (ID_GRUPO_ACCESO)
      references IFRS_GRUPO (ID_GRUPO_ACCESO)
go

alter table IFRS_CELDA
   add constraint FK_REV_CELD_REF_REV_COLU foreign key (ID_COLUMNA, ID_GRILLA)
      references IFRS_COLUMNA (ID_COLUMNA, ID_GRILLA)
go

alter table IFRS_CELDA
   add constraint FK_REV_CELD_REF_REV_TIPO foreign key (ID_TIPO_DATO)
      references IFRS_TIPO_DATO (ID_TIPO_DATO)
go

alter table IFRS_CELDA
   add constraint FK_REV_CELD_REF_REV_TIPO_CELDA foreign key (ID_TIPO_CELDA)
      references IFRS_TIPO_CELDA (ID_TIPO_CELDA)
go

alter table IFRS_COLUMNA
   add constraint FK_REV_COLU_REF_REV_GRIL foreign key (ID_GRILLA)
      references IFRS_GRILLA (ID_GRILLA)
go

alter table IFRS_DETALLE_EEFF
   add constraint FK_IFRS_DET_REFERENCE_IFRS_EEF foreign key (ID_FECU, ID_VERSION_EEFF)
      references IFRS_EEFF (ID_FECU, ID_VERSION_EEFF)
go

alter table IFRS_DETALLE_EEFF
   add constraint FK_IFRS_DET_REFERENCE_IFRS_CUE foreign key (ID_CUENTA)
      references IFRS_CUENTA_CONTABLE (ID_CUENTA)
go

alter table IFRS_EEFF
   add constraint FK_IFRS_EEF_REFERENCE_IFRS_VER foreign key (ID_VERSION_EEFF)
      references IFRS_VERSION_EEFF (ID_VERSION_EEFF)
go

alter table IFRS_EEFF
   add constraint FK_IFRS_EEF_REFERENCE_IFRS_COD foreign key (ID_FECU)
      references IFRS_CODIGO_FECU (ID_FECU)
go

alter table IFRS_ESTRUCTURA
   add constraint FK_REV_ESTR_REF_REV_TIPO foreign key (ID_TIPO_ESTRUCTURA)
      references IFRS_TIPO_ESTRUCTURA (ID_TIPO_ESTRUCTURA)
go

alter table IFRS_ESTRUCTURA
   add constraint FK_REV_ESTR_REF_REV_VERS foreign key (ID_VERSION)
      references IFRS_VERSION (ID_VERSION)
go

alter table IFRS_GRILLA
   add constraint FK_REV_GRIL_REF_REV_ESTR foreign key (ID_GRILLA)
      references IFRS_ESTRUCTURA (ID_ESTRUCTURA)
go

alter table IFRS_GRUPO
   add constraint FK_REV_GRUP_REF_REV_AREA foreign key (ID_AREA_NEGOCIO)
      references IFRS_AREA_NEGOCIO (ID_AREA_NEGOCIO)
go

alter table IFRS_GRUPO_EMPRESA
   add constraint FK_IFRS_GRU_REFERENCE_IFRS_GRU foreign key (ID_GRUPO_ACCESO)
      references IFRS_GRUPO (ID_GRUPO_ACCESO)
go

alter table IFRS_GRUPO_EMPRESA
   add constraint FK_IFRS_GRU_REFERENCE_IFRS_EMP foreign key (ID_RUT)
      references IFRS_EMPRESA (ID_RUT)
go

alter table IFRS_HISTORIAL_REPORTE
   add constraint FK_IFRS_HIS_REFERENCE_IFRS_PER foreign key (ID_PERIODO, ID_RUT)
      references IFRS_PERIODO_EMPRESA (ID_PERIODO, ID_RUT)
go

alter table IFRS_HISTORIAL_REPORTE
   add constraint FK_IFRS_HIS_REFERENCE_IFRS_USU foreign key (NOMBRE_USUARIO)
      references IFRS_USUARIO (NOMBRE_USUARIO)
go

alter table IFRS_HISTORIAL_VERSION
   add constraint FK_REV_HIST_REF_REV_ESTA foreign key (ID_ESTADO_CUADRO)
      references IFRS_ESTADO_CUADRO (ID_ESTADO_CUADRO)
go

alter table IFRS_HISTORIAL_VERSION
   add constraint FK_IFRS_HIS_REFERENCE_IFRS_VER foreign key (ID_VERSION)
      references IFRS_VERSION (ID_VERSION)
go

alter table IFRS_HISTORIAL_VERSION
   add constraint FK_IFRS_HIS_REFERENCE_IFRS_US2 foreign key (NOMBRE_USUARIO)
      references IFRS_USUARIO (NOMBRE_USUARIO)
go

alter table IFRS_HTML
   add constraint FK_REV_HTML_REF_REV_ESTR foreign key (ID_HTML)
      references IFRS_ESTRUCTURA (ID_ESTRUCTURA)
go

alter table IFRS_MENU_GRUPO
   add constraint FK_NEW_REV_MENU_REF_REV_GRUP foreign key (ID_GRUPO_ACCESO)
      references IFRS_GRUPO (ID_GRUPO_ACCESO)
go

alter table IFRS_MENU_GRUPO
   add constraint FK_NEW_REV_MENU_REF_REV_MENU foreign key (ID_MENU)
      references IFRS_MENU (ID_MENU)
go

alter table IFRS_PARAMETRO
   add constraint FK_IFRS_PAR_REFERENCE_IFRS_TIP foreign key (ID_TIPO_PARAMETRO)
      references IFRS_TIPO_PARAMETRO (ID_TIPO_PARAMETRO)
go

alter table IFRS_PERIODO
   add constraint FK_IFRS_PER_REF_IFRS_EST_P foreign key (ID_ESTADO_PERIODO)
      references IFRS_ESTADO_PERIODO (ID_ESTADO_PERIODO)
go

alter table IFRS_PERIODO_EMPRESA
   add constraint FK_IFRS_PER_REFERENCE_IFRS_EMP foreign key (ID_RUT)
      references IFRS_EMPRESA (ID_RUT)
go

alter table IFRS_PERIODO_EMPRESA
   add constraint FK_IFRS_PER_REFERENCE_IFRS_EST foreign key (ID_ESTADO_PERIODO)
      references IFRS_ESTADO_PERIODO (ID_ESTADO_PERIODO)
go

alter table IFRS_PERIODO_EMPRESA
   add constraint FK_IFRS_PER_REFERENCE_IFRS_PER foreign key (ID_PERIODO)
      references IFRS_PERIODO (ID_PERIODO)
go

alter table IFRS_RELACION_DETALLE_EEFF
   add constraint FK_IFRS_REL_CONST_REL_IFRS_COD foreign key (ID_FECU)
      references IFRS_CODIGO_FECU (ID_FECU)
go

alter table IFRS_RELACION_DETALLE_EEFF
   add constraint FK_REV_RELA_DET_REF_REV_CELD foreign key (ID_COLUMNA, ID_GRILLA, ID_FILA)
      references IFRS_CELDA (ID_COLUMNA, ID_GRILLA, ID_FILA)
go

alter table IFRS_RELACION_DETALLE_EEFF
   add constraint FK_IFRS_REL_REFERENCE_IFRS_PER foreign key (ID_PERIODO, ID_RUT)
      references IFRS_PERIODO_EMPRESA (ID_PERIODO, ID_RUT)
go

alter table IFRS_RELACION_DETALLE_EEFF
   add constraint FK_IFRS_REL_REFERENCE_IFRS_CUE foreign key (ID_CUENTA)
      references IFRS_CUENTA_CONTABLE (ID_CUENTA)
go

alter table IFRS_RELACION_EEFF
   add constraint FK_IFRS_REL_CONS_REL__IFRS_COD foreign key (ID_FECU)
      references IFRS_CODIGO_FECU (ID_FECU)
go

alter table IFRS_RELACION_EEFF
   add constraint FK_REV_RELA_EEFF_REF_REV_CELD foreign key (ID_COLUMNA, ID_GRILLA, ID_FILA)
      references IFRS_CELDA (ID_COLUMNA, ID_GRILLA, ID_FILA)
go

alter table IFRS_RELACION_EEFF
   add constraint FK_IFRS_REL_REFERENCE_IFRS_PE2 foreign key (ID_PERIODO, ID_RUT)
      references IFRS_PERIODO_EMPRESA (ID_PERIODO, ID_RUT)
go

alter table IFRS_TEXTO
   add constraint FK_REV_TEXT_REFERENCE_REV_ESTR foreign key (ID_TEXTO)
      references IFRS_ESTRUCTURA (ID_ESTRUCTURA)
go

alter table IFRS_USUARIO
   add constraint FK_IFRS_USU_REFERENCE_IFRS_ROL foreign key (ID_ROL)
      references IFRS_ROL (ID_ROL)
go

alter table IFRS_USUARIO_GRUPO
   add constraint FK_REV_USUA_REF_REV_GRUP foreign key (ID_GRUPO_ACCESO)
      references IFRS_GRUPO (ID_GRUPO_ACCESO)
go

alter table IFRS_USUARIO_GRUPO
   add constraint FK_IFRS_USU_REFERENCE_IFRS_USU foreign key (NOMBRE_USUARIO)
      references IFRS_USUARIO (NOMBRE_USUARIO)
go

alter table IFRS_VERSION
   add constraint FK_REV_VERS_REF_REV_CATA foreign key (ID_CATALOGO)
      references IFRS_CATALOGO (ID_CATALOGO)
go

alter table IFRS_VERSION
   add constraint FK_IFRS_VER_REFERENCE_IFRS_PER foreign key (ID_PERIODO, ID_RUT)
      references IFRS_PERIODO_EMPRESA (ID_PERIODO, ID_RUT)
go

alter table IFRS_VERSION
   add constraint FK_IFRS_VER_REFERENCE_IFRS_EST foreign key (ID_ESTADO_CUADRO)
      references IFRS_ESTADO_CUADRO (ID_ESTADO_CUADRO)
go

alter table IFRS_VERSION_EEFF
   add constraint FK_IFRS_VER_REFERENCE_IFRS_PE2 foreign key (ID_PERIODO, ID_RUT)
      references IFRS_PERIODO_EMPRESA (ID_PERIODO, ID_RUT)
go

alter table IFRS_VERSION_EEFF
   add constraint FK_IFRS_VER_REFERENCE_IFRS_TIP foreign key (ID_ESTADO_EEFF)
      references IFRS_TIPO_ESTADO_EEFF (ID_ESTADO_EEFF)
go

