CREATE OR REPLACE PACKAGE "IFRS"."PKG_IFRS_PERIODO" AS 

  PROCEDURE PRC_CERRAR_PERIODO (P_ID_PERIODO IN NUMBER, P_ID_RUT IN NUMBER, P_USUARIO IN VARCHAR2, P_ERRNO OUT NUMBER);
  
  PROCEDURE PRC_NUEVO_PERIODO (P_USUARIO IN VARCHAR2, P_ERRNO OUT NUMBER);

END PKG_IFRS_PERIODO;

/


CREATE OR REPLACE PACKAGE BODY "IFRS"."PKG_IFRS_PERIODO" AS


PROCEDURE PRC_CERRAR_PERIODO (P_ID_PERIODO IN NUMBER, P_ID_RUT IN NUMBER, P_USUARIO IN VARCHAR2, P_ERRNO OUT NUMBER) IS 

    BEGIN

    DECLARE 

    V_ERRNO NUMBER;
    V_ERRMSG VARCHAR2(2048);   
    V_ID_LOG NUMBER(12);
		V_EMPRESA VARCHAR2(512);
		V_TYPE_PERIODO_EMPRESA IFRS_PERIODO_EMPRESA % ROWTYPE;
		V_PERIODO_CERRADO CONSTANT  NUMBER(1) := 1;
		V_CONTADOR_PERIODO_ABIERTOS NUMBER(5);
		
	--ERRORES
	--1 SE CIERRA PERIODO SOLO PARA LA EMPRESA
	--2 SE CIERRA PERIODO PARA TODAS LAS EMPRESAS (TODAS LAS EMPRESAS TIENE EL PERIODO CERRADO) 
	--3 EMPERSA NO EXISTE
	--4 ERROR INSERTAR LOG
	--5 PERIODO NO EXISTE
	--6 PERIODO EN ESTADO CERRADO
	--7 OTRO ERROR
		
    
    BEGIN -- PRINCIPAL
		
		--INSERTANDO LOG PROCESO
		BEGIN
			
			SELECT SEQ_LOG_PROCESO.NEXTVAL INTO V_ID_LOG FROM DUAL;
			
			INSERT INTO IFRS_LOG_PROCESO(ID_LOG,USUARIO,FECHA,LOG) VALUES(V_ID_LOG,P_USUARIO,SYSDATE,'INICIA PROCESO DE CERRAR PERIODO');      
      COMMIT;
		EXCEPTION  WHEN OTHERS THEN
			
			P_ERRNO := 4;
			RETURN;
		
		END;
    
    -- VALIDANDO EMPRESA
		BEGIN

			SELECT NOMBRE INTO V_EMPRESA FROM IFRS_EMPRESA WHERE ID_RUT = P_ID_RUT;
			
			UPDATE IFRS_LOG_PROCESO SET LOG = ' CERRANDO PERIODO PARA RUT : ' || P_ID_RUT || ' EMPRESA : ' || V_EMPRESA WHERE ID_LOG = V_ID_LOG;

		EXCEPTION 
		
			WHEN NO_DATA_FOUND THEN
        
				UPDATE IFRS_LOG_PROCESO SET LOG = 'ERROR : NO EXISTE EMPRESA CON EL RUT ' || P_ID_RUT WHERE ID_LOG = V_ID_LOG;
				P_ERRNO := 3;
        COMMIT;
				RETURN;
			
			WHEN OTHERS THEN
			
				V_ERRNO  := SQLCODE;
				V_ERRMSG := SQLERRM;
				UPDATE IFRS_LOG_PROCESO SET LOG = 'ERROR : ' || V_ERRNO || ' - ' || V_ERRMSG WHERE ID_LOG = V_ID_LOG;
				P_ERRNO := 7;
        COMMIT;
				RETURN;
			
		END;
			
		-- VALIDANDO PERIODO
		BEGIN

			SELECT * INTO V_TYPE_PERIODO_EMPRESA FROM IFRS_PERIODO_EMPRESA WHERE ID_RUT = P_ID_RUT AND ID_PERIODO = P_ID_PERIODO;
			
			IF V_TYPE_PERIODO_EMPRESA.ID_ESTADO_PERIODO = V_PERIODO_CERRADO THEN
				
				UPDATE IFRS_LOG_PROCESO SET LOG = 'ERROR : PERIODO EN ESADO CERRADO ' || P_ID_PERIODO || ' EMPRESA ' || P_ID_RUT WHERE ID_LOG = V_ID_LOG;
				P_ERRNO := 6;
        COMMIT;
				RETURN;
				
			END IF;
			
		EXCEPTION WHEN NO_DATA_FOUND THEN
			
			UPDATE IFRS_LOG_PROCESO SET LOG = 'ERROR : NO EXISTE PERIODO ' || P_ID_PERIODO || ' EMPRESA ' || P_ID_RUT WHERE ID_LOG = V_ID_LOG;
			P_ERRNO := 5;
      COMMIT;
			RETURN;
		END;
		
		-- CERRANDO PERIODO EMPRESA
		BEGIN
			
			UPDATE IFRS_PERIODO_EMPRESA SET ID_ESTADO_PERIODO = V_PERIODO_CERRADO WHERE ID_PERIODO = P_ID_PERIODO AND ID_RUT = P_ID_RUT;
			P_ERRNO := 1;
      
			SELECT 
				COUNT(*) 
			INTO 
				V_CONTADOR_PERIODO_ABIERTOS 
			FROM 
				IFRS_PERIODO_EMPRESA 
			WHERE 
				ID_PERIODO = P_ID_PERIODO AND 
				ID_ESTADO_PERIODO <> V_PERIODO_CERRADO;
			
			IF V_CONTADOR_PERIODO_ABIERTOS = 0 THEN
			
				UPDATE IFRS_PERIODO SET ID_ESTADO_PERIODO = V_PERIODO_CERRADO WHERE ID_PERIODO = P_ID_PERIODO;
				P_ERRNO := 2;
			END IF;
		
		EXCEPTION WHEN OTHERS THEN
      ROLLBACK;
			V_ERRNO  := SQLCODE;
		  V_ERRMSG := SQLERRM;
			UPDATE IFRS_LOG_PROCESO SET LOG = 'ERROR : ' || V_ERRNO || ' - ' || V_ERRMSG WHERE ID_LOG = V_ID_LOG;
			P_ERRNO := 7;
		END;
		
    COMMIT;
    
    END;

END PRC_CERRAR_PERIODO;


PROCEDURE PRC_NUEVO_PERIODO (P_USUARIO IN VARCHAR2, P_ERRNO OUT NUMBER) IS
BEGIN

DECLARE

--SECUENCIAS
    V_ID_LOG NUMBER;
    V_ID_VERSION_NUEVA NUMBER;
    V_ID_ESTRUCTURA_NUEVA NUMBER;

--REPRESENTACION TABLA EN VARIABLE
    V_TYPE_PERIODO_ACTUAL IFRS_PERIODO%ROWTYPE;
    V_TYPE_TEXTO IFRS_TEXTO%ROWTYPE;       --SE ASIGNA A TABLA
    V_TYPE_GRILLA IFRS_GRILLA%ROWTYPE;     --SE ASIGNA A TABLA
    V_TYPE_HTML IFRS_HTML%ROWTYPE;


--CONSTANTES
    CONST_PERIODO_ABIERTO CONSTANT  NUMBER(1) := 0;
    CONST_CUADRO_INICIADO CONSTANT  NUMBER(1) := 0;
    CONST_PERIODO_CERRADO CONSTANT  NUMBER(1) := 1;
    CONST_MESES_PERIODO CONSTANT  NUMBER(1) := 3;
    CONST_VIGENTE CONSTANT  NUMBER(1) := 1;
    CONST_NO_VIGENTE CONSTANT  NUMBER(1) := 1;
    CONST_COMENTARIO_VERSION VARCHAR2(64) := 'VERSION INICIAL NUEVO PERIODO';
    CONST_TIPO_GRILLA NUMBER := 0;
    CONST_TIPO_HTML NUMBER := 1;
    CONST_TIPO_TEXTO NUMBER := 2;


--VARIABLES VALIDACION
    V_COUNT_PERIODO_ABIERTO NUMBER(5);
    V_COUNT_PERIODO_NUEVO NUMBER(5);
    
--VARIABLES GENERAL
    V_PERIODO_DATE DATE;
    V_ID_PRIODO_NUEVO NUMBER(6);
    V_MES_PERIODO NUMBER(2);
    V_ANIO_PERIODO NUMBER(4);
  
--VARIABLES DE ERROR
    V_ERRNO NUMBER;
    V_MSG VARCHAR2(2048);
    
--ERRORES
--1 ABRE PERIODO CORRECTO
--2 PERIODO NO CERRADO    
--3 PERIODO NO CERRADO POR EMPRESA
--4 INSERTAR LOG
--5 PERIODO NUEVO EXISTE
--6 ERROR INSERTAR PERIODO
--7 ERROR AL GENERAR VERSIONES
--8 GRILLA INCONSITENTE
--9 TEXTO INCONSISTENTE
--10 HTML INCONSISTENTE
  
  
 BEGIN --PRINCIPAL

    SELECT SEQ_LOG_PROCESO.NEXTVAL INTO V_ID_LOG FROM DUAL;
    P_ERRNO := 1;
    
    BEGIN
        
        INSERT INTO IFRS_LOG_PROCESO(ID_LOG,USUARIO,FECHA,LOG) VALUES(V_ID_LOG,P_USUARIO,SYSDATE,'INICIANDO ABRIR PERIODO');
        COMMIT;
    EXCEPTION WHEN OTHERS THEN
        P_ERRNO := 4;
        RETURN;
        
    END;
    
    --SE OBTIENE PERIODO ACTUAL
    SELECT 
        * 
    INTO 
        V_TYPE_PERIODO_ACTUAL 
    FROM 
        IFRS_PERIODO P
    WHERE 
        P.ID_PERIODO = (SELECT MAX(ID_PERIODO) FROM IFRS_PERIODO);
    
    
    -- SE VALIDA EL ESTADO DEL PERIODO POR EMPRESA Y GENERAL
    
    SELECT 
        COUNT(*) 
    INTO 
        V_COUNT_PERIODO_ABIERTO 
    FROM 
        IFRS_PERIODO_EMPRESA 
    WHERE 
        ID_PERIODO = V_TYPE_PERIODO_ACTUAL.ID_PERIODO AND 
        ID_ESTADO_PERIODO <> CONST_PERIODO_CERRADO;
    
    IF V_COUNT_PERIODO_ABIERTO > 0 THEN
        ROLLBACK;
        UPDATE IFRS_LOG_PROCESO SET LOG = '2 - ERROR : NO TODAS LAS EMPRESAS HAN CERRADO PERIODO' WHERE ID_LOG = V_ID_LOG;
        P_ERRNO := 3;
        COMMIT;
        RETURN;
    
    END IF;
    
    IF V_TYPE_PERIODO_ACTUAL.ID_ESTADO_PERIODO <> CONST_PERIODO_CERRADO THEN
        ROLLBACK;
        UPDATE IFRS_LOG_PROCESO SET LOG = '3 - ERROR : EL PERIODO NO ESTA EN ESTADO CERRADO' WHERE ID_LOG = V_ID_LOG;
        P_ERRNO := 2;
        COMMIT;
        RETURN;
    
    END IF;
    
    -- GENERANDO ID NUEVO PERIODO
    
    V_PERIODO_DATE := TO_DATE( (TO_CHAR(V_TYPE_PERIODO_ACTUAL.ID_PERIODO) || '01'), 'YYYYMMDD' );
    
    V_MES_PERIODO := EXTRACT( MONTH FROM ADD_MONTHS( V_PERIODO_DATE, CONST_MESES_PERIODO ) ); 
    V_ANIO_PERIODO := EXTRACT( YEAR FROM ADD_MONTHS( V_PERIODO_DATE, CONST_MESES_PERIODO ) );
    
    -- SE CALCULA EL NUEVO ID_PERIODO
    V_ID_PRIODO_NUEVO :=  (V_ANIO_PERIODO * 100) + V_MES_PERIODO;
    
    
    -- SE VALIDA QUE NO EXISTE NUEVO PERIODO GENERADO
    SELECT 
        COUNT(ID_PERIODO) 
    INTO 
        V_COUNT_PERIODO_NUEVO 
    FROM 
        IFRS_PERIODO 
    WHERE 
        ID_PERIODO = V_ID_PRIODO_NUEVO;
    
    IF V_COUNT_PERIODO_NUEVO > 0 THEN
        ROLLBACK;
        UPDATE IFRS_LOG_PROCESO SET LOG = '4 - EL PERIODO NUEVO' || V_ID_PRIODO_NUEVO ||'ESTA REGISTRADO EN BASE DE DATOS' WHERE ID_LOG = V_ID_LOG;
        P_ERRNO := 5;
        COMMIT;
        RETURN;
    
    END IF;
    
    --INSERTANDO PERIODO Y PERIODO EMPRESA
    
    BEGIN
    
        INSERT INTO IFRS_PERIODO (ID_PERIODO, ID_ESTADO_PERIODO)  VALUES ( V_ID_PRIODO_NUEVO, CONST_PERIODO_ABIERTO);
        
        FOR C_PERIODO_EMPRESA_ROW IN( SELECT
                                        ID_PERIODO,
                                        ID_RUT
                                      FROM 
                                        IFRS_PERIODO_EMPRESA 
                                      WHERE 
                                        ID_PERIODO = V_TYPE_PERIODO_ACTUAL.ID_PERIODO) LOOP
            
            DBMS_OUTPUT.PUT_LINE('INSERTANTO RUT - ' || C_PERIODO_EMPRESA_ROW.ID_RUT || ' PERIODO ' || V_ID_PRIODO_NUEVO || ' ESTADO ' || CONST_PERIODO_ABIERTO);
            INSERT INTO IFRS_PERIODO_EMPRESA (ID_PERIODO, ID_RUT, ID_ESTADO_PERIODO) VALUES (V_ID_PRIODO_NUEVO, C_PERIODO_EMPRESA_ROW.ID_RUT, CONST_PERIODO_ABIERTO);
            DBMS_OUTPUT.PUT_LINE('TERMINO DE INSERTAR');
        END LOOP;
        
    EXCEPTION WHEN OTHERS THEN
        ROLLBACK;
        V_ERRNO := SQLCODE;
        V_MSG := SQLERRM;
        UPDATE IFRS_LOG_PROCESO SET LOG = '5 - ERROR : ' || V_ERRNO || ' - ' || V_MSG WHERE ID_LOG = V_ID_LOG;
        P_ERRNO := 6;
        RETURN;
    
    END;
    
    BEGIN

        --ITERANDO VERSIONES PARA COPIAR
    
        FOR C_VERSION_ROW IN (SELECT 
                                    ID_VERSION, 
                                    ID_CATALOGO, 
                                    ID_PERIODO, 
                                    ID_RUT, 
                                    VERSION, 
                                    VIGENCIA, 
                                    FECHA_CREACION, 
                                    COMENTARIO 
                                FROM 
                                    IFRS_VERSION 
                                WHERE 
                                    VIGENCIA = 1 AND 
                                    ID_PERIODO = V_TYPE_PERIODO_ACTUAL.ID_PERIODO) LOOP
        
        
        SELECT SEQ_VERSION.NEXTVAL INTO V_ID_VERSION_NUEVA FROM DUAL;
        
        INSERT INTO IFRS_VERSION 
/*1*/            (     ID_VERSION, 
/*2*/                ID_CATALOGO, 
/*3*/                ID_PERIODO, 
/*4*/                ID_RUT, 
/*5*/                ID_ESTADO_CUADRO, 
/*6*/                VERSION, 
/*7*/                VIGENCIA, 
/*8*/                FECHA_CREACION, 
/*9*/                COMENTARIO)
        VALUES 
/*1*/            (    V_ID_VERSION_NUEVA, 
/*2*/                C_VERSION_ROW.ID_CATALOGO, 
/*3*/                V_ID_PRIODO_NUEVO, 
/*4*/                C_VERSION_ROW.ID_RUT,
/*5*/                CONST_CUADRO_INICIADO, 
/*6*/                (C_VERSION_ROW.VERSION + 1), 
/*7*/                CONST_VIGENTE, 
/*8*/                SYSDATE, 
/*9*/                CONST_COMENTARIO_VERSION);

        INSERT INTO IFRS_HISTORIAL_VERSION 
/*1*/                (   ID_HISTORIAL,
/*2*/                    ID_ESTADO_CUADRO, 
/*3*/                    ID_VERSION, 
/*4*/                    NOMBRE_USUARIO, 
/*5*/                    FECHA_PROCESO, 
/*6*/                    COMENTARIO    ) 
        VALUES 
/*1*/            (        SEQ_HISTORIAL_VERSION.NEXTVAL, 
/*2*/                    CONST_VIGENTE, 
/*3*/                    V_ID_VERSION_NUEVA, 
/*4*/                    P_USUARIO, 
/*5*/                    SYSDATE, 
/*6*/                    CONST_COMENTARIO_VERSION);


        --ITERANDO ESTRUCTURAS PARA COPIAR
        FOR C_ESTRUCTURA_ROW IN (SELECT * FROM IFRS_ESTRUCTURA WHERE ID_VERSION = C_VERSION_ROW.ID_VERSION) LOOP
        
            SELECT SEQ_ESTRUCTURA.NEXTVAL INTO V_ID_ESTRUCTURA_NUEVA FROM DUAL;
            
            INSERT INTO IFRS_ESTRUCTURA 
                        (ID_ESTRUCTURA, 
                        ID_VERSION, 
                        ID_TIPO_ESTRUCTURA, 
                        ORDEN) 
            VALUES 
                        (V_ID_ESTRUCTURA_NUEVA, 
                        V_ID_VERSION_NUEVA, 
                        C_ESTRUCTURA_ROW.ID_TIPO_ESTRUCTURA, 
                        C_ESTRUCTURA_ROW.ORDEN);
                        
            IF C_ESTRUCTURA_ROW.ID_TIPO_ESTRUCTURA = CONST_TIPO_GRILLA THEN
                
                BEGIN
                
                    SELECT * INTO V_TYPE_GRILLA FROM IFRS_GRILLA WHERE ID_GRILLA = C_ESTRUCTURA_ROW.ID_ESTRUCTURA;
                
                EXCEPTION WHEN NO_DATA_FOUND THEN
                    ROLLBACK;
                    UPDATE IFRS_LOG_PROCESO SET LOG = '6 - ERROR - LA GRILLA NO EXISTE PARA LA ESTRUCTURA : ' || C_ESTRUCTURA_ROW.ID_ESTRUCTURA  WHERE ID_LOG = V_ID_LOG;
                    P_ERRNO := 8;
                    COMMIT;
                    RETURN;
                
                END;
                
                --COPIANDO GRILLAS
                FOR C_GRILLA_ROW IN (SELECT * FROM IFRS_GRILLA WHERE ID_GRILLA = C_ESTRUCTURA_ROW.ID_ESTRUCTURA) LOOP
                
                    INSERT INTO IFRS_GRILLA (
                                ID_GRILLA, 
                                TITULO, 
                                TIPO_FORMULA) 
                    VALUES     (
                                V_ID_ESTRUCTURA_NUEVA, 
                                C_GRILLA_ROW.TITULO, 
                                C_GRILLA_ROW.TIPO_FORMULA);
                                
                    --COPIANDO COLUMNAS
                    FOR C_COLUMNA_ROW IN (SELECT * FROM IFRS_COLUMNA WHERE ID_GRILLA = C_ESTRUCTURA_ROW.ID_ESTRUCTURA ) LOOP
                  
                        INSERT INTO IFRS_COLUMNA (
                                                ID_GRILLA,
                                                ID_COLUMNA, 
                                                TITULO_COLUMNA, 
                                                ORDEN, 
                                                ANCHO, 
                                                ROW_HEADER) 
                        VALUES (                
                                                V_ID_ESTRUCTURA_NUEVA, 
                                                C_COLUMNA_ROW.ID_COLUMNA, 
                                                C_COLUMNA_ROW.TITULO_COLUMNA, 
                                                C_COLUMNA_ROW.ORDEN, 
                                                C_COLUMNA_ROW.ANCHO, 
                                                C_COLUMNA_ROW.ROW_HEADER); 
                        
                        --COPIANDO CELDAS
                        FOR C_CELDA_ROW IN (SELECT * FROM IFRS_CELDA WHERE ID_GRILLA = C_ESTRUCTURA_ROW.ID_ESTRUCTURA AND ID_COLUMNA = C_COLUMNA_ROW.ID_COLUMNA) LOOP
                        
                              INSERT INTO IFRS_CELDA (
/*1*/                                                    ID_GRILLA, 
/*2*/                                                    ID_COLUMNA, 
/*3*/                                                    ID_FILA, 
/*4*/                                                    ID_TIPO_CELDA, 
/*5*/                                                    ID_TIPO_DATO, 
/*6*/                                                    VALOR, 
/*7*/                                                    FORMULA, 
/*8*/                                                    CHILD_HORIZONTAL, 
/*9*/                                                    PARENT_HORIZONTAL, 
/*10*/                                                    CHILD_VERTICAL, 
/*11*/                                                    PARENT_VERTICAL) 
                              VALUES (
/*1*/                                                    V_ID_ESTRUCTURA_NUEVA, 
/*2*/                                                    C_COLUMNA_ROW.ID_COLUMNA, 
/*3*/                                                    C_CELDA_ROW.ID_FILA, 
/*4*/                                                    C_CELDA_ROW.ID_TIPO_CELDA, 
/*5*/                                                    C_CELDA_ROW.ID_TIPO_DATO, 
/*6*/                                                    C_CELDA_ROW.VALOR, 
/*7*/                                                    C_CELDA_ROW.FORMULA, 
/*8*/                                                    C_CELDA_ROW.CHILD_HORIZONTAL, 
/*9*/                                                    C_CELDA_ROW.PARENT_HORIZONTAL, 
/*10*/                                                    C_CELDA_ROW.CHILD_VERTICAL, 
/*11*/                                                    C_CELDA_ROW.PARENT_VERTICAL); 
                              
                        END LOOP; --TERMINA LOOP CELDA
                        
                        
                  END LOOP; -- TERMINA LOOP DE COLUMNA
                  
                --COPIANDO AGRUPACIONES DE COLUMNAS
                FOR C_AGRUPACION_COLUMNA_ROW IN (SELECT * FROM IFRS_AGRUPACION_COLUMNA WHERE ID_GRILLA = C_ESTRUCTURA_ROW.ID_ESTRUCTURA) LOOP
    
                    INSERT INTO IFRS_AGRUPACION_COLUMNA (
                                                        ID_GRILLA,
                                                        ID_COLUMNA, 
                                                        ID_NIVEL, 
                                                        TITULO, 
                                                        ANCHO, 
                                                        GRUPO)
                    VALUES (                            
                                                        V_ID_ESTRUCTURA_NUEVA,
                                                        C_AGRUPACION_COLUMNA_ROW.ID_COLUMNA, 
                                                        C_AGRUPACION_COLUMNA_ROW.ID_NIVEL, 
                                                        C_AGRUPACION_COLUMNA_ROW.TITULO, 
                                                        C_AGRUPACION_COLUMNA_ROW.ANCHO, 
                                                        C_AGRUPACION_COLUMNA_ROW.GRUPO );
                  
                END LOOP; --TERMINA LOOP AGRUPACION COLUMNA
                  
                END LOOP; -- TERMINA LOOP DE GRILLA
            
            
            --TERMINA IF SI ES GRILLA
            
            ELSIF C_ESTRUCTURA_ROW.ID_TIPO_ESTRUCTURA = CONST_TIPO_HTML THEN
            
                BEGIN
                
                    SELECT * INTO V_TYPE_HTML FROM IFRS_HTML WHERE ID_HTML = C_ESTRUCTURA_ROW.ID_ESTRUCTURA;
                
                EXCEPTION WHEN NO_DATA_FOUND THEN
                    ROLLBACK;
                    UPDATE IFRS_LOG_PROCESO SET LOG = '7 - ERROR - HTML NO EXISTE PARA LA ESTRUCTURA : ' || C_ESTRUCTURA_ROW.ID_ESTRUCTURA  WHERE ID_LOG = V_ID_LOG;
                    P_ERRNO := 10;
                    COMMIT;
                    RETURN;
                
                END;
                
                INSERT INTO IFRS_HTML (ID_HTML, CONTENIDO, TITULO) VALUES (V_ID_ESTRUCTURA_NUEVA, V_TYPE_HTML.CONTENIDO, V_TYPE_HTML.TITULO);
            
            --TERMINA IF SI ES HTML
            
            ELSIF C_ESTRUCTURA_ROW.ID_TIPO_ESTRUCTURA = CONST_TIPO_TEXTO THEN
            
                BEGIN
                
                    SELECT * INTO V_TYPE_TEXTO FROM IFRS_TEXTO WHERE ID_TEXTO = C_ESTRUCTURA_ROW.ID_ESTRUCTURA;
                
                 EXCEPTION WHEN NO_DATA_FOUND THEN
                    ROLLBACK;
                    DBMS_OUTPUT.PUT_LINE('8 - ERROR - EL TEXTO NO EXISTE PARA LA ESTRUCTURA : ' || C_ESTRUCTURA_ROW.ID_ESTRUCTURA);
                    UPDATE IFRS_LOG_PROCESO SET LOG = '8 - ERROR - EL TEXTO NO EXISTE PARA LA ESTRUCTURA : ' || C_ESTRUCTURA_ROW.ID_ESTRUCTURA  WHERE ID_LOG = V_ID_LOG;
                    P_ERRNO := 9;
                    COMMIT;
                    RETURN;
                
                END;
                
                INSERT INTO IFRS_TEXTO VALUES (V_ID_ESTRUCTURA_NUEVA, V_TYPE_TEXTO.TEXTO , V_TYPE_TEXTO.NEGRITA);
            
            --TERMINA IF SI ES TEXTO
            
            END IF; 
            
        
        
        END LOOP; -- TERMINA LOOP ESTRUCTURA

        
        END LOOP; --TERMINA LOOP VERSION
        
        --SE ACTUALIZA LA VIGENCIA PARA TODAS LAS VERSIONES DEL PERIODO ANTIGUO
        
        UPDATE IFRS_VERSION SET VIGENCIA = CONST_NO_VIGENTE WHERE ID_PERIODO =  V_TYPE_PERIODO_ACTUAL.ID_PERIODO;
        
        
    
    EXCEPTION WHEN OTHERS THEN 
        ROLLBACK;
        V_ERRNO := SQLCODE;
        V_MSG := SQLERRM;
        P_ERRNO := 7;
        UPDATE IFRS_LOG_PROCESO SET LOG = '9 - ERROR : ' || V_ERRNO || ' - ' || V_MSG WHERE ID_LOG = V_ID_LOG;
        COMMIT;
        RETURN;
    
END;
    
  
END;

END PRC_NUEVO_PERIODO;


END PKG_IFRS_PERIODO;

/
