create or replace
PACKAGE BODY                "PKG_REV_PERIODO" AS


PROCEDURE PRC_ABRIR_PERIODO (P_USUARIO in varchar2, P_ERRNO OUT number) IS
BEGIN

DECLARE

--VARIABLES COMUNES
  V_FECHA_PERIODO date;
  V_FECHA_NUEVO_PERIODO number(6);
  V_EXISTE_PERIODO number := 0;
  V_EXISTE_ESTRUCTURA number := 0;
  V_EXISTE_COLUMNA number := 0;   
  V_EXISTE_CELDA  number := 0;   
  V_EXISTE_FORMULA_GRILLA number := 0;   
  V_MES number;
  V_ANNO number;
  V_COMENTARIO_HISTORIAL_VERSION varchar(256) := 'SE ABRE NUEVA VERSION';
  --CONSTANTES Cantidad de meses entre periodo
  V_INTERVALO_MESES_PERIODO number := 3;
  V_TIPO_GRILLA number := 0;
  V_TIPO_HTML number := 1;
  V_TIPO_TEXTO number := 2;
  --IDs 
  V_ID_PERIODO number := 0;           
  V_ID_PERIODO_ACTUAL number := 0;           
  V_ID_VERSION number := 0;
  V_ID_ESTRUCTURA number := 0;     
  V_ID_CELDA number := 0;
  V_ID_ESTADO_PERIODO number := 0;
  V_ID_LOG number;
  V_ERRNO NUMBER;
  V_MSG VARCHAR2(4000);
  --VARIABLES ERROR
  ERRORES NUMBER := 0;
  ERRNO NUMBER;
  ERRMSG varchar2(255);
  
  --Errores
  ERROR_PERIODO_ABIERTO number := -1000;
  --Variables TYPE
  TYPE_TEXTO REV_TEXTO%ROWTYPE;       --Se asigna a tabla
  TYPE_GRILLA REV_GRILLA%ROWTYPE;     --Se asigna a tabla
  TYPE_HTML REV_HTML%ROWTYPE;         --Se asigna a tabla
  TYPE_VERSION_PERIODO REV_VERSION_PERIODO%ROWTYPE;         --Se asigna a tabla    
  
BEGIN --PRINCIPAL

  SELECT SEQ_LOG_PROCESO.NEXTVAL INTO V_ID_LOG FROM DUAL;              
  INSERT INTO REV_LOG_PROCESO(ID_LOG,USUARIO,FECHA,LOG) VALUES(V_ID_LOG,P_USUARIO,SYSDATE,'INICIANDO ABRIR PERIODO');
  ----COMMIT;
  --DBMS_OUTPUT.PUT_LINE('INICIANDO EL PL/SQL...');

  SELECT SEQ_LOG_PROCESO.NEXTVAL INTO V_ID_LOG FROM DUAL;
              
  INSERT INTO REV_LOG_PROCESO(ID_LOG,USUARIO,FECHA,LOG) VALUES(V_ID_LOG,P_USUARIO,SYSDATE,'INICIANDO ABRIR PERIODO');
  --COMMIT;
  
  IF P_USUARIO = '' THEN
    UPDATE REV_LOG_PROCESO SET LOG = 'PASO 1 - EL USUARIO NO PUEDE SER NULL' WHERE ID_LOG = V_ID_LOG;
    --COMMIT;
    RETURN;
  END IF;
  
  P_ERRNO := 0;
  
  select TO_DATE( CONCAT( NVL( max(PERIODO), 190001 ) , '01') , 'YYYYMMDD' ) INTO V_FECHA_PERIODO from REV_PERIODO ;--where ID_ESTADO_PERIODO = 1;
  
  --DBMS_OUTPUT.PUT_LINE('PERIODO ACTUAL ->' || to_char(V_FECHA_PERIODO, 'dd-MM-yyyy'));
  
  if TO_CHAR(V_FECHA_PERIODO, 'dd-MM-yyyy') = '01-01-1900'  then --Si el periodo no existe entonces la V_FECHA_PERIODO = '01-01-1900'
    --DBMS_OUTPUT.PUT_LINE('LA TABLA PERIODO ESTA VACIA');
    P_ERRNO := ERROR_PERIODO_ABIERTO;
    UPDATE REV_LOG_PROCESO SET LOG = 'PASO 2 - EL PERIODO SE ENCUENTRA ABIERTO' WHERE ID_LOG = V_ID_LOG;
    --COMMIT;
    RETURN;
  END IF;
  
  --DBMS_OUTPUT.PUT_LINE('PASANDO 10');
  
  SELECT ID_ESTADO_PERIODO INTO V_ID_ESTADO_PERIODO FROM REV_PERIODO WHERE PERIODO = TO_CHAR(V_FECHA_PERIODO, 'yyyyMM');
  
  if V_ID_ESTADO_PERIODO = 0 then
    --DBMS_OUTPUT.PUT_LINE('ERROR_PERIODO_ABIERTO');
    P_ERRNO := ERROR_PERIODO_ABIERTO;
    UPDATE REV_LOG_PROCESO SET LOG = 'PASO 3 - EL PERIODO SE ENCUENTRA ABIERTO' WHERE ID_LOG = V_ID_LOG;
    --COMMIT;
    RETURN;
  END IF;
  
  --DBMS_OUTPUT.PUT_LINE('PASANDO 11');
  
  select ID_PERIODO INTO V_ID_PERIODO_ACTUAL from REV_PERIODO where PERIODO = TO_NUMBER (TO_CHAR( V_FECHA_PERIODO , 'YYYYMM')); 

  V_MES := extract( month from ADD_MONTHS( V_FECHA_PERIODO, V_INTERVALO_MESES_PERIODO ) ); 
  V_ANNO := extract( year from ADD_MONTHS( V_FECHA_PERIODO, V_INTERVALO_MESES_PERIODO ) );
  
  --DBMS_OUTPUT.PUT_LINE('PASANDO 12');
  
  --inserto el numero del periodo en la tabla periodo con estado iniciado              
  V_FECHA_NUEVO_PERIODO :=  (V_ANNO * 100) + V_MES ;
  
  --DBMS_OUTPUT.PUT_LINE('V_FECHA_NUEVO_PERIODO ->' || V_FECHA_NUEVO_PERIODO);
  
  BEGIN --UNO
  select distinct 1 into V_EXISTE_PERIODO from REV_PERIODO where PERIODO = V_FECHA_NUEVO_PERIODO;
   EXCEPTION
    when NO_DATA_FOUND then
    V_EXISTE_PERIODO := 0;
    --DBMS_OUTPUT.PUT_LINE('NO EXISTE PERIODO');
    when OTHERS then
    ROLLBACK;
    --DBMS_OUTPUT.PUT_LINE('ERROR 01');
    V_ERRNO := SQLCODE;
    P_ERRNO := V_ERRNO;
    V_MSG := SQLERRM;
    UPDATE REV_LOG_PROCESO SET LOG = 'PASO 4 - ERROR : ' || V_ERRNO || ' - ' || V_MSG WHERE ID_LOG = V_ID_LOG;
    --COMMIT;
    RETURN;
   END; --UNO
   
   --DBMS_OUTPUT.PUT_LINE('PASANDO 13');
  
  BEGIN --DOS
  
  if V_EXISTE_PERIODO <> 1 then
    V_ID_PERIODO    := SEQ_PERIODO.NEXTVAL;
    --DBMS_OUTPUT.PUT_LINE('INSERTANDO PERIODO');
    insert into REV_PERIODO (ID_PERIODO, ID_ESTADO_PERIODO, PERIODO) 
    VALUES ( V_ID_PERIODO, 0, V_FECHA_NUEVO_PERIODO );
    --DBMS_OUTPUT.PUT_LINE('SALE INSERTANDO PERIODO');
  END IF;
  
  if  V_EXISTE_PERIODO = 1 then
    --DBMS_OUTPUT.PUT_LINE('RETORNO... EXISTE PERIODO');
    RETURN;
  END IF;
  
  EXCEPTION when OTHERS then
    ROLLBACK;
    V_ERRNO := SQLCODE;
    P_ERRNO := V_ERRNO;
    V_MSG := SQLERRM;
    UPDATE REV_LOG_PROCESO SET LOG = 'PASO 5 - ERROR : ' || V_ERRNO || ' - ' || V_MSG WHERE ID_LOG = V_ID_LOG;
    --COMMIT;
    RETURN;
  END; --DOS
  
  BEGIN
  FOR C_VERSION_ROW in (select ID_VERSION, ID_CATALOGO, version, VIGENCIA, FECHA_CREACION, COMENTARIO from REV_VERSION where VIGENCIA = 1) LOOP
  --DBMS_OUTPUT.PUT_LINE('FOR DE LA VERSION...');
  V_ID_VERSION    := SEQ_VERSION.NEXTVAL;
  --DBMS_OUTPUT.PUT_LINE('INSERTANDO VERSION...' || V_ID_VERSION );
  
  insert into REV_VERSION (ID_VERSION, ID_CATALOGO, version, VIGENCIA, FECHA_CREACION, COMENTARIO) 
  values (V_ID_VERSION, C_VERSION_ROW.ID_CATALOGO, (C_VERSION_ROW.version + 1), 1, sysdate, C_VERSION_ROW.COMENTARIO);
  
  UPDATE REV_VERSION SET VIGENCIA = 0 WHERE ID_VERSION = C_VERSION_ROW.ID_VERSION;
  
  
  --DBMS_OUTPUT.PUT_LINE('PERIODO ACTUAL V_ID_PERIODO_ACTUAL ...' || V_ID_PERIODO_ACTUAL );
  --Insertar la version-periodo
  select * into TYPE_VERSION_PERIODO from REV_VERSION_PERIODO where ID_VERSION = C_VERSION_ROW.ID_VERSION and ID_PERIODO = V_ID_PERIODO_ACTUAL;
  
  --DBMS_OUTPUT.PUT_LINE('REV_VERSION_PERIODO...' || V_ID_VERSION );
  insert into REV_VERSION_PERIODO (ID_VERSION, ID_PERIODO, ID_ESTADO_CUADRO, FECHA_CREACION, USUARIO)
  values (V_ID_VERSION, V_ID_PERIODO, 0, sysdate, P_USUARIO);
  
  --DBMS_OUTPUT.PUT_LINE('INSERTANDO REV_HISTORIAL_VERSION');
  insert into REV_HISTORIAL_VERSION (ID_HISTORIAL, ID_VERSION, ID_PERIODO, ID_ESTADO_CUADRO, FECHA_PROCESO, COMENTARIO, USUARIO ) 
  values (SEQ_HISTORIAL_VERSION.NEXTVAL, V_ID_VERSION, V_ID_PERIODO, 0, sysdate, V_COMENTARIO_HISTORIAL_VERSION, P_USUARIO);
  
  FOR C_ESTRUCTURA_ROW IN (SELECT * FROM REV_ESTRUCTURA WHERE ID_VERSION = C_VERSION_ROW.ID_VERSION) LOOP
                               
  V_ID_ESTRUCTURA := SEQ_ESTRUCTURA.NEXTVAL;
  
  --DBMS_OUTPUT.PUT_LINE('PASANDO 16.1');
  
  --DBMS_OUTPUT.PUT_LINE('REV_ESTRUCTURA...' || V_ID_ESTRUCTURA || ' VERSION ' || V_ID_VERSION);
  insert into REV_ESTRUCTURA (ID_ESTRUCTURA, ID_VERSION, ID_TIPO_ESTRUCTURA, ORDEN) 
  VALUES (V_ID_ESTRUCTURA, V_ID_VERSION, C_ESTRUCTURA_ROW.ID_TIPO_ESTRUCTURA, C_ESTRUCTURA_ROW.ORDEN);
  
  --DBMS_OUTPUT.PUT_LINE('PASANDO 16.2');
  
  IF C_ESTRUCTURA_ROW.ID_TIPO_ESTRUCTURA = V_TIPO_GRILLA THEN
      --DBMS_OUTPUT.PUT_LINE('PASANDO 16.3');
      --Inserto en la tabla GRILLA
      BEGIN
          
          SELECT * INTO TYPE_GRILLA FROM REV_GRILLA WHERE ID_GRILLA = C_ESTRUCTURA_ROW.ID_ESTRUCTURA;

          --DBMS_OUTPUT.PUT_LINE('PASANDO 16.4');
          
          EXCEPTION WHEN NO_DATA_FOUND THEN 
            ROLLBACK;
            UPDATE REV_LOG_PROCESO SET LOG = 'ERROR : NO EXISTE GRILLA PARA ESTRUCTURA ->' || C_ESTRUCTURA_ROW.ID_ESTRUCTURA WHERE ID_LOG = V_ID_LOG;
            --COMMIT;
            RETURN;
          when OTHERS then
            ROLLBACK;
            V_ERRNO := SQLCODE;
            P_ERRNO := V_ERRNO;
            V_MSG := SQLERRM;
            UPDATE REV_LOG_PROCESO SET LOG = 'PASO 8 - ERROR : ' || V_ERRNO || ' - ' || V_MSG WHERE ID_LOG = V_ID_LOG;
            --COMMIT;
          RETURN;
      END;
      
      begin
        for C_GRILLA_ROW in (select * from REV_GRILLA where ID_GRILLA = C_ESTRUCTURA_ROW.ID_ESTRUCTURA) LOOP
        
                --DBMS_OUTPUT.PUT_LINE('REV_GRILLA... V_ID_ESTRUCTURA ' || V_ID_ESTRUCTURA);
                
                insert into REV_GRILLA (ID_GRILLA, TITULO, TIPO_FORMULA) 
                values (V_ID_ESTRUCTURA, C_GRILLA_ROW.TITULO, C_GRILLA_ROW.TIPO_FORMULA);
                
                  for C_COLUMNA_ROW in (select * from REV_COLUMNA where ID_GRILLA = C_ESTRUCTURA_ROW.ID_ESTRUCTURA ) LOOP
                  
                        --DBMS_OUTPUT.PUT_LINE('REV_COLUMNA...' || C_COLUMNA_ROW.ID_COLUMNA || ' ID_GRILLA ' || V_ID_ESTRUCTURA);
                        
                        insert into REV_COLUMNA (ID_COLUMNA, ID_GRILLA, TITULO_COLUMNA, ORDEN, ANCHO, ROW_HEADER) 
                        VALUES (C_COLUMNA_ROW.ID_COLUMNA, V_ID_ESTRUCTURA, C_COLUMNA_ROW.TITULO_COLUMNA, C_COLUMNA_ROW.ORDEN, C_COLUMNA_ROW.ANCHO, C_COLUMNA_ROW.ROW_HEADER); 
                        
                        --DBMS_OUTPUT.PUT_LINE('PASANDO 17');
                        
                        for C_CELDA_ROW in (select * from REV_CELDA where ID_GRILLA = C_ESTRUCTURA_ROW.ID_ESTRUCTURA and ID_COLUMNA = C_COLUMNA_ROW.ID_COLUMNA) LOOP
                        
                              insert into REV_CELDA (ID_GRILLA, ID_COLUMNA, ID_FILA, ID_TIPO_CELDA, ID_TIPO_DATO, VALOR, FORMULA, CHILD_HORIZONTAL, PARENT_HORIZONTAL, CHILD_VERTICAL, PARENT_VERTICAL) 
                              values (V_ID_ESTRUCTURA, C_COLUMNA_ROW.ID_COLUMNA, C_CELDA_ROW.ID_FILA, C_CELDA_ROW.ID_TIPO_CELDA, C_CELDA_ROW.ID_TIPO_DATO, 
									  C_CELDA_ROW.VALOR, C_CELDA_ROW.FORMULA, C_CELDA_ROW.CHILD_HORIZONTAL, C_CELDA_ROW.PARENT_HORIZONTAL, C_CELDA_ROW.CHILD_VERTICAL, C_CELDA_ROW.PARENT_VERTICAL); 
                              
                        end LOOP;
                        
                  end LOOP;
            
            FOR C_AGRUPACION_COLUMNA_ROW in (select * from REV_AGRUPACION_COLUMNA where ID_GRILLA = C_ESTRUCTURA_ROW.ID_ESTRUCTURA) LOOP --AKI
            
            INSERT INTO REV_AGRUPACION_COLUMNA 
            (ID_NIVEL, ID_COLUMNA, ID_GRILLA, TITULO, ANCHO, GRUPO)
            VALUES 
            (C_AGRUPACION_COLUMNA_ROW.ID_NIVEL, C_AGRUPACION_COLUMNA_ROW.ID_COLUMNA, V_ID_ESTRUCTURA,C_AGRUPACION_COLUMNA_ROW.TITULO, C_AGRUPACION_COLUMNA_ROW.ANCHO, C_AGRUPACION_COLUMNA_ROW.GRUPO );
          
            END LOOP;
                  
        END LOOP;
        
        --COMMIT;
        
        EXCEPTION when OTHERS then
          ROLLBACK;
          V_ERRNO := SQLCODE;
          P_ERRNO := V_ERRNO;
          V_MSG := SQLERRM;
          UPDATE REV_LOG_PROCESO SET LOG = 'PASO 9 - ERROR : ' || V_ERRNO || ' - ' || V_MSG WHERE ID_LOG = V_ID_LOG;
          --COMMIT;
          RETURN;
        
      END;
      
  ELSIF C_ESTRUCTURA_ROW.ID_TIPO_ESTRUCTURA = V_TIPO_HTML THEN                         
      --Inserto en la tabla HTML  
      begin
        
        select * into TYPE_HTML from REV_HTML where ID_HTML = C_ESTRUCTURA_ROW.ID_ESTRUCTURA;
        
        EXCEPTION when NO_DATA_FOUND then
          ROLLBACK;
          UPDATE REV_LOG_PROCESO SET LOG = 'ERROR : NO EXISTE HTML PARA ESTRUCTURA ->' || C_ESTRUCTURA_ROW.ID_ESTRUCTURA WHERE ID_LOG = V_ID_LOG;
          --COMMIT;
          RETURN;
        when OTHERS then
          ROLLBACK;
          V_ERRNO := SQLCODE;
          P_ERRNO := V_ERRNO;
          V_MSG := SQLERRM;
          UPDATE REV_LOG_PROCESO SET LOG = 'PASO 10 - ERROR : ' || V_ERRNO || ' - ' || V_MSG WHERE ID_LOG = V_ID_LOG;
          --COMMIT;
          RETURN;
      END;  
      
      insert into REV_HTML values (V_ID_ESTRUCTURA, TYPE_HTML.CONTENIDO, TYPE_HTML.TITULO);
  
  ELSIF C_ESTRUCTURA_ROW.ID_TIPO_ESTRUCTURA = V_TIPO_TEXTO THEN
      --Inserto en la tabla TEXTO 
      begin
        
        select * into TYPE_TEXTO from REV_TEXTO where ID_TEXTO = C_ESTRUCTURA_ROW.ID_ESTRUCTURA;
        
        EXCEPTION when NO_DATA_FOUND then
          
          ROLLBACK;
          UPDATE REV_LOG_PROCESO SET LOG = 'ERROR : NO EXISTE TEXTO PARA ESTRUCTURA ->' || C_ESTRUCTURA_ROW.ID_ESTRUCTURA WHERE ID_LOG = V_ID_LOG;
          --COMMIT;
          RETURN;
          
        when OTHERS then
          
          ROLLBACK;
          V_ERRNO := SQLCODE;
          P_ERRNO := V_ERRNO;
          V_MSG := SQLERRM;
          UPDATE REV_LOG_PROCESO SET LOG = 'PASO 11 - ERROR : ' || V_ERRNO || ' - ' || V_MSG WHERE ID_LOG = V_ID_LOG;
          --COMMIT;
          RETURN;
          
      end;  
      
      insert into REV_TEXTO values (V_ID_ESTRUCTURA, TYPE_TEXTO.TEXTO , TYPE_TEXTO.NEGRITA);
      
  end if;
end LOOP;
                
  END LOOP;
  
  EXCEPTION when OTHERS then
    ROLLBACK;
    V_ERRNO := SQLCODE;
    P_ERRNO := V_ERRNO;
    V_MSG := SQLERRM;
    UPDATE REV_LOG_PROCESO SET LOG = 'PASO 5 - ERROR : ' || V_ERRNO || ' - ' || V_MSG WHERE ID_LOG = V_ID_LOG;
    --COMMIT;
    RETURN;
  END; --TRES LOOP

UPDATE REV_LOG_PROCESO SET LOG = 'PROCESO EJECUTADO CORRECTAMENTE' WHERE ID_LOG = V_ID_LOG;

--COMMIT;

EXCEPTION when OTHERS then --PRINCIPAL
ROLLBACK;
V_ERRNO := SQLCODE;
P_ERRNO := V_ERRNO;
V_MSG := SQLERRM;
UPDATE REV_LOG_PROCESO SET LOG = 'ERROR : ' || V_ERRNO || ' - ' || V_MSG WHERE ID_LOG = V_ID_LOG;
--COMMIT;
END;

END PRC_ABRIR_PERIODO;

PROCEDURE PRC_CERRAR_PERIODO (P_USUARIO in varchar2, P_ID_PERIODO in NUMBER, P_ERRNO OUT NUMBER) is 
    /**********************************************************************
        Procedimiento que cierra un periodo.
    
    Autor:          Manuel Gutierrez C.
    Subgerencia :   IFRS
    Fecha creaci�n: 03/02/2012
    
    ***********************************************************************/
    BEGIN

      declare 
        --VARIABLES ERROR
        ERRORES NUMBER := 0;
        ERRNO NUMBER;
        ERRMSG varchar2(2048);   
        V_ID_LOG NUMBER;

        begin 
           SELECT SEQ_LOG_PROCESO.NEXTVAL INTO V_ID_LOG FROM DUAL;              
           INSERT INTO REV_LOG_PROCESO(ID_LOG,USUARIO,FECHA,LOG) VALUES(V_ID_LOG,P_USUARIO,SYSDATE,'INICIANDO CERRAR PERIODO');
           COMMIT;
           
           update REV_PERIODO set ID_ESTADO_PERIODO = 1 where PERIODO = P_ID_PERIODO;
           P_ERRNO := 0;           
           COMMIT;
           
           EXCEPTION when OTHERS then
           ROLLBACK;
           ERRNO := SQLCODE;
           P_ERRNO := ERRNO;
           ERRMSG := SQLERRM;
           UPDATE REV_LOG_PROCESO SET LOG = 'ERROR : ' || ERRNO || ' - ' || ERRMSG WHERE ID_LOG = V_ID_LOG;
           COMMIT;
       end;
    
    

END PRC_CERRAR_PERIODO;


END PKG_REV_PERIODO;