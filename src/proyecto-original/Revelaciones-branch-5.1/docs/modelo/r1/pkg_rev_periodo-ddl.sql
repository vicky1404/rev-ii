--------------------------------------------------------
--  DDL for package PKG_REV_PERIODO
--------------------------------------------------------	  
create or replace
package PKG_REV_PERIODO as 

  procedure PRC_ABRIR_PERIODO (P_USUARIO in varchar2, P_ERRNO OUT number);
  PROCEDURE PRC_CERRAR_PERIODO (P_USUARIO in varchar2, P_ID_PERIODO in NUMBER, P_ERRNO OUT NUMBER);

END PKG_REV_PERIODO;

create or replace
PACKAGE BODY PKG_REV_PERIODO AS

procedure PRC_ABRIR_PERIODO (P_USUARIO in varchar2, P_ERRNO OUT NUMBER) is 
    /**********************************************************************
        Procedimiento que abre un nuevo periodo a partir del último periodo
      registrado y vigente.
      cargados.   
    
    Autor:          Manuel Gutierrez C.
    Subgerencia :   IFRS
    Fecha creación: 30/01/2012
    
    ***********************************************************************/
    BEGIN

      declare 
        --VARIABLES COMUNES
        V_FECHA_PERIODO date;
        V_FECHA_NUEVO_PERIODO number(6);
        V_EXISTE_PERIODO number := 0;
        V_EXISTE_ESTRUCTURA number := 0;
        V_EXISTE_GRILLA number := 0;
        V_EXISTE_HTML number := 0;
        V_EXISTE_TEXTO number := 0;
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
        V_ID_CAMPO_FORMULA number := 0;
        V_ID_FORMULA_GRILLA number := 0;
        V_ID_ESTADO_PERIODO number := 0;
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
        

        begin 
              P_ERRNO := 0;
              --DBMS_OUTPUT.PUT_LINE('INICIANDO EL PL/SQL...'||SYSDATE);
        
              --Obtengo el ultimo periodo y lo asigno como fecha tipo Date
              select TO_DATE( CONCAT( NVL( max(PERIODO), 190001 ) , '01') , 'YYYYMMDD' ) INTO V_FECHA_PERIODO from REV_PERIODO ;--where ID_ESTADO_PERIODO = 1;
              
              DBMS_OUTPUT.PUT_LINE('PERIODO ACTUAL ->' || to_char(V_FECHA_PERIODO, 'dd-MM-yyyy'));
              
              if TO_CHAR(V_FECHA_PERIODO, 'dd-MM-yyyy') = '01-01-1900'  then --Si el periodo no existe entonces la V_FECHA_PERIODO = '01-01-1900'
                DBMS_OUTPUT.PUT_LINE('LA TABLA PERIODO ESTA VACIA');
                P_ERRNO := ERROR_PERIODO_ABIERTO;
                RETURN;
              END IF;
              
              SELECT ID_ESTADO_PERIODO INTO V_ID_ESTADO_PERIODO FROM REV_PERIODO WHERE PERIODO = TO_CHAR(V_FECHA_PERIODO, 'yyyyMM');
              
              if V_ID_ESTADO_PERIODO = 0 then
                DBMS_OUTPUT.PUT_LINE('ERROR_PERIODO_ABIERTO');
                P_ERRNO := ERROR_PERIODO_ABIERTO;
                 DBMS_OUTPUT.PUT_LINE('P_ERRNO ->' || P_ERRNO);
                return;
              END IF;
              
              select ID_PERIODO INTO V_ID_PERIODO_ACTUAL from REV_PERIODO where PERIODO = TO_NUMBER (TO_CHAR( V_FECHA_PERIODO , 'YYYYMM')); 
              
              --Extraigo el mes y año del proximo periodo              
              V_MES := extract( month from ADD_MONTHS( V_FECHA_PERIODO, V_INTERVALO_MESES_PERIODO ) ); 
              V_ANNO := extract( year from ADD_MONTHS( V_FECHA_PERIODO, V_INTERVALO_MESES_PERIODO ) );
              
              --DBMS_OUTPUT.PUT_LINE('V_MES ->' || V_MES);
              --DBMS_OUTPUT.PUT_LINE('V_ANNO ->' || V_ANNO);
              
              
              --inserto el numero del periodo en la tabla periodo con estado iniciado              
              V_FECHA_NUEVO_PERIODO :=  (V_ANNO * 100) + V_MES ;
              
              DBMS_OUTPUT.PUT_LINE('V_FECHA_NUEVO_PERIODO ->' || V_FECHA_NUEVO_PERIODO);
              
              BEGIN
              select distinct 1 into V_EXISTE_PERIODO from REV_PERIODO where PERIODO = V_FECHA_NUEVO_PERIODO;
               EXCEPTION
                when NO_DATA_FOUND then
                --DBMS_OUTPUT.PUT_LINE('EXCEPTION -> NO_DATA_FOUND');
                V_EXISTE_PERIODO := 0;                
                when OTHERS then
                --DBMS_OUTPUT.PUT_LINE('EXCEPTION ->' || P_ERRNO);
                P_ERRNO := SQLCODE;
               END; 
              
             
              if V_EXISTE_PERIODO <> 1 then
                V_ID_PERIODO    := SEQ_PERIODO.NEXTVAL;
                insert into REV_PERIODO (ID_PERIODO, ID_ESTADO_PERIODO, PERIODO) 
                VALUES ( V_ID_PERIODO, 0, V_FECHA_NUEVO_PERIODO );
                
              END IF;   
              
              if  V_EXISTE_PERIODO = 1 then
                DBMS_OUTPUT.PUT_LINE('RETORNO...');
                RETURN;
              END IF;
              
             --Inserto la version            
             for C_VERSION_ROW in (select ID_VERSION, ID_CATALOGO, version, VIGENCIA, FECHA_CREACION, COMENTARIO from REV_VERSION where VIGENCIA = 1) LOOP
             DBMS_OUTPUT.PUT_LINE('FOR DE LA VERSION...');
                --Asigno la secuencia
                V_ID_VERSION    := SEQ_VERSION.NEXTVAL;   
                --Insertar la version
                insert into REV_VERSION (ID_VERSION, ID_CATALOGO, version, VIGENCIA, FECHA_CREACION, COMENTARIO) 
                values (V_ID_VERSION, C_VERSION_ROW.ID_CATALOGO, C_VERSION_ROW.version, 1, sysdate, C_VERSION_ROW.COMENTARIO);
                
                UPDATE REV_VERSION SET VIGENCIA = 0 WHERE ID_VERSION = C_VERSION_ROW.ID_VERSION;
                
                --Insertar la version-periodo
                select * into TYPE_VERSION_PERIODO from REV_VERSION_PERIODO where ID_VERSION = C_VERSION_ROW.ID_VERSION and ID_PERIODO = V_ID_PERIODO_ACTUAL;
                insert into REV_VERSION_PERIODO (ID_VERSION, ID_PERIODO, ID_ESTADO_CUADRO, FECHA_CREACION, USUARIO) 
                --values (V_ID_VERSION, V_ID_PERIODO, TYPE_VERSION_PERIODO.ID_ESTADO_CUADRO, sysdate, P_USUARIO);
                values (V_ID_VERSION, V_ID_PERIODO, 0, sysdate, P_USUARIO);
                
                --Insertar historial                 
                insert into REV_HISTORIAL_VERSION (ID_HISTORIAL, ID_VERSION, ID_PERIODO, ID_ESTADO_CUADRO, FECHA_PROCESO, COMENTARIO, USUARIO ) 
                values (SEQ_HISTORIAL_VERSION.NEXTVAL, V_ID_VERSION, V_ID_PERIODO, 0, sysdate, V_COMENTARIO_HISTORIAL_VERSION, P_USUARIO);
               
                begin
                  select distinct 1 into V_EXISTE_ESTRUCTURA from REV_ESTRUCTURA where ID_VERSION = V_ID_VERSION;
                  EXCEPTION WHEN NO_DATA_FOUND then
                  V_EXISTE_ESTRUCTURA := 0;
                  when OTHERS then
                  P_ERRNO := SQLCODE;
                END;
                
                  if V_EXISTE_ESTRUCTURA <> 1 then
                  begin
                    
                     --Inserto la estructura y las tablas grilla, texto y html.
                               FOR C_ESTRUCTURA_ROW IN (SELECT * FROM REV_ESTRUCTURA WHERE ID_VERSION = C_VERSION_ROW.ID_VERSION) LOOP
                               
                                          V_ID_ESTRUCTURA := SEQ_ESTRUCTURA.NEXTVAL;
                               
                                          insert into REV_ESTRUCTURA (ID_ESTRUCTURA, ID_VERSION, ID_TIPO_ESTRUCTURA, ORDEN) 
                                          VALUES (V_ID_ESTRUCTURA, V_ID_VERSION, C_ESTRUCTURA_ROW.ID_TIPO_ESTRUCTURA, C_ESTRUCTURA_ROW.ORDEN);
                                          
                                          IF C_ESTRUCTURA_ROW.ID_TIPO_ESTRUCTURA = V_TIPO_GRILLA THEN
                                              --Inserto en la tabla GRILLA
                                              BEGIN
                                                V_EXISTE_GRILLA := 1;
                                                  SELECT * INTO TYPE_GRILLA FROM REV_GRILLA WHERE ID_GRILLA = C_ESTRUCTURA_ROW.ID_ESTRUCTURA;
                                                  EXCEPTION WHEN NO_DATA_FOUND THEN 
                                                V_EXISTE_GRILLA := -1;
                                                  when OTHERS then
                                                  P_ERRNO := SQLCODE;
                                              end;
                                              
                                              if V_EXISTE_GRILLA <> -1 then 
                                              
                                                begin
                                                  for C_GRILLA_ROW in (select * from REV_GRILLA where ID_GRILLA = C_ESTRUCTURA_ROW.ID_ESTRUCTURA) LOOP
                                                  
                                                          insert into REV_GRILLA (ID_GRILLA, TITULO, TIPO_FORMULA) 
                                                          values (V_ID_ESTRUCTURA, C_GRILLA_ROW.TITULO, C_GRILLA_ROW.TIPO_FORMULA);
                                                          
                                                            for C_COLUMNA_ROW in (select * from REV_COLUMNA where ID_GRILLA = C_ESTRUCTURA_ROW.ID_ESTRUCTURA ) LOOP
                                                            
                                                                  insert into REV_COLUMNA (ID_COLUMNA, ID_GRILLA, TITULO_COLUMNA, ORDEN, ANCHO, ROW_HEADER) 
                                                                  VALUES (C_COLUMNA_ROW.ID_COLUMNA, V_ID_ESTRUCTURA, C_COLUMNA_ROW.TITULO_COLUMNA, C_COLUMNA_ROW.ORDEN, C_COLUMNA_ROW.ANCHO, C_COLUMNA_ROW.ROW_HEADER); 
                                                                  
                                                                  for C_CELDA_ROW in (select * from REV_CELDA where ID_GRILLA = C_ESTRUCTURA_ROW.ID_ESTRUCTURA and ID_COLUMNA = C_COLUMNA_ROW.ID_COLUMNA) LOOP
                                                                  
                                                                        insert into REV_CELDA (ID_COLUMNA, ID_FILA, ID_TIPO_CELDA, ID_TIPO_DATO, VALOR, GRUPO, ID_GRILLA) 
                                                                        values (C_COLUMNA_ROW.ID_COLUMNA, C_CELDA_ROW.ID_FILA, C_CELDA_ROW.ID_TIPO_CELDA, C_CELDA_ROW.ID_TIPO_DATO, C_CELDA_ROW.VALOR, C_CELDA_ROW.GRUPO, V_ID_ESTRUCTURA); 
                                                                        
                                                                        
                                                                                FOR C_CAMPO_FORMULA_ROW in (select * from REV_CAMPO_FORMULA where ID_GRILLA = C_ESTRUCTURA_ROW.ID_ESTRUCTURA AND ID_COLUMNA = C_CELDA_ROW.ID_COLUMNA AND ID_FILA = C_CELDA_ROW.ID_FILA) LOOP
                                                                                    
                                                                                      V_ID_CAMPO_FORMULA := SEQ_CAMPO_FORMULA.NEXTVAL;
                                                                                      
                                                                                      insert into REV_CAMPO_FORMULA (ID_CAMPO_FORMULA, ID_COLUMNA, ID_FILA, ID_GRILLA) 
                                                                                      values (V_ID_CAMPO_FORMULA, C_CAMPO_FORMULA_ROW.ID_COLUMNA, C_CAMPO_FORMULA_ROW.ID_FILA, V_ID_ESTRUCTURA);
                                                                                      
                                                                                end LOOP;
                                                                                
                                                                                FOR C_FORMULA_GRILLA_ROW IN (SELECT * FROM REV_FORMULA_GRILLA WHERE ID_GRILLA = C_ESTRUCTURA_ROW.ID_ESTRUCTURA AND ID_COLUMNA = C_CELDA_ROW.ID_COLUMNA AND ID_FILA = C_CELDA_ROW.ID_FILA) LOOP
                                                                                
                                                                                    insert into REV_FORMULA_GRILLA (ID_FORMULA, ID_TIPO_OPERACION, ID_CAMPO_FORMULA, ID_COLUMNA, ID_FILA, ID_GRILLA)
                                                                                    values (SEQ_FORMULA_GRILLA.NEXTVAL, C_FORMULA_GRILLA_ROW.ID_TIPO_OPERACION, C_FORMULA_GRILLA_ROW.ID_CAMPO_FORMULA, C_FORMULA_GRILLA_ROW.ID_COLUMNA, C_FORMULA_GRILLA_ROW.ID_FILA, V_ID_ESTRUCTURA);
                                                                                    
                                                                                END LOOP;
                                                                        
                                                                  end LOOP;
                                                                  
                                                            end LOOP;
                                                  end LOOP;  
                                                  
                                                  EXCEPTION when OTHERS then
                                                  P_ERRNO := SQLCODE;
                                                  
                                                END;
                                                
                                              END IF;
                                              
                                          ELSIF C_ESTRUCTURA_ROW.ID_TIPO_ESTRUCTURA = V_TIPO_HTML THEN                         
                                              --Inserto en la tabla HTML  
                                              begin
                                                V_EXISTE_HTML := 1;
                                                select * into TYPE_HTML from REV_HTML where ID_HTML = C_ESTRUCTURA_ROW.ID_ESTRUCTURA;
                                                EXCEPTION when 
                                                NO_DATA_FOUND then
                                                V_EXISTE_HTML := -1;
                                                when OTHERS then
                                                P_ERRNO := SQLCODE;
                                              END;  
                                              
                                              IF V_EXISTE_HTML <> -1 THEN
                                                insert into REV_HTML values (V_ID_ESTRUCTURA, TYPE_HTML.CONTENIDO, TYPE_HTML.TITULO);
                                              END IF;  
                                          
                                          ELSIF C_ESTRUCTURA_ROW.ID_TIPO_ESTRUCTURA = V_TIPO_TEXTO THEN
                                              --Inserto en la tabla TEXTO 
                                              begin
                                                V_EXISTE_TEXTO := 1;
                                                select * into TYPE_TEXTO from REV_TEXTO where ID_TEXTO = C_ESTRUCTURA_ROW.ID_ESTRUCTURA;
                                                EXCEPTION when NO_DATA_FOUND then
                                                V_EXISTE_TEXTO := -1;
                                                when OTHERS then
                                                P_ERRNO := SQLCODE;
                                              end;  
                                              
                                              IF V_EXISTE_TEXTO <> -1 THEN
                                                insert into REV_TEXTO values (V_ID_ESTRUCTURA, TYPE_TEXTO.TEXTO , TYPE_TEXTO.NEGRITA);
                                              end if;
                                              
                                          end if;
                                  end LOOP;
                  END;   
                  end if;  
                  
              end LOOP;
              
               
               
    end;
    
    --ROLLBACK;

END PRC_ABRIR_PERIODO;



procedure PRC_CERRAR_PERIODO (P_USUARIO in varchar2, P_ID_PERIODO in NUMBER, P_ERRNO OUT NUMBER) is 
    /**********************************************************************
        Procedimiento que cierra un periodo.
    
    Autor:          Manuel Gutierrez C.
    Subgerencia :   IFRS
    Fecha creación: 03/02/2012
    
    ***********************************************************************/
    BEGIN

      declare 
        --VARIABLES ERROR
        ERRORES NUMBER := 0;
        ERRNO NUMBER;
        ERRMSG varchar2(255);   
        

        begin 
           
           update REV_PERIODO set ID_ESTADO_PERIODO = 1 where PERIODO = P_ID_PERIODO;
           P_ERRNO := 0;
           
           EXCEPTION when OTHERS then
           P_ERRNO := SQLCODE;
           
       end;
    
    

END PRC_CERRAR_PERIODO;


END PKG_REV_PERIODO;
