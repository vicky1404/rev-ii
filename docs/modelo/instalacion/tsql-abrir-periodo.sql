CREATE PROCEDURE [dbo].[PRC_NUEVO_PERIODO](

@P_USUARIO VARCHAR(256), 
@P_ERRNO INT OUTPUT
)

AS
BEGIN

SET NOCOUNT ON;

DECLARE
--SECUENCIAS
    @V_ID_LOG INT,
    @V_ID_VERSION_NUEVA INT,
    @V_ID_ESTRUCTURA_NUEVA INT,
    @V_ID_HISTORIAL_VERSION INT,

--REPRESENTACION TABLA EN VARIABLE
	--IFRS_PERIODO
	@V_PERIODO_ID INT,
	@V_PERIODO_ID_ESTADO_PERIODO INT,
	--IFRS_TEXTO
	@V_TEXTO_ID INT,
	@V_TEXTO_TEXTO VARCHAR(4000),
	@V_TEXTO_NEGRITA INT,
	--IFRS_GRILLA
	@V_GRILLA_ID INT,
	@V_GRILLA_TITULO VARCHAR(256),
	@V_TIPO_FORMULA INT,
	--IFRS_HTML
	@V_HTML_ID INT,
	@V_HTML_CONTENIDO VARBINARY(MAX),
	@V_HTML_TITULO VARCHAR(1024),
	
	
    /*V_TYPE_PERIODO_ACTUAL IFRS_PERIODO%ROWTYPE;
    V_TYPE_TEXTO IFRS_TEXTO%ROWTYPE;       --SE ASIGNA A TABLA
    V_TYPE_GRILLA IFRS_GRILLA%ROWTYPE;     --SE ASIGNA A TABLA
    V_TYPE_HTML IFRS_HTML%ROWTYPE;*/


--CONSTANTES
    @CONST_PERIODO_ABIERTO  INT,
    @CONST_CUADRO_INICIADO  INT,
    @CONST_PERIODO_CERRADO  INT,
    @CONST_MESES_PERIODO  INT,
    @CONST_VIGENTE  INT,
    @CONST_NO_VIGENTE  INT,
    @CONST_COMENTARIO_VERSION VARCHAR(64),
    @CONST_TIPO_GRILLA INT,
    @CONST_TIPO_HTML INT,
    @CONST_TIPO_TEXTO INT,


--VARIABLES VALIDACION
    @V_COUNT_PERIODO_ABIERTO INT,
    @V_COUNT_PERIODO_NUEVO INT,
    
--VARIABLES GENERAL
    @V_PERIODO_DATE DATE,
    @V_ID_PERIODO_NUEVO INT,
    @V_MES_PERIODO INT,
    @V_ANIO_PERIODO INT,
  
--VARIABLES DE ERROR
    @V_ERRNO INT,
    @V_MSG VARCHAR(2048)
    
--ERRORES
--0 ABRE PERIODO CORRECTO
--2 PERIODO NO CERRADO    
--3 PERIODO NO CERRADO POR EMPRESA
--4 INSERTAR LOG
--5 PERIODO NUEVO EXISTE
--6 ERROR INSERTAR PERIODO
--7 ERROR AL GENERAR VERSIONES
--8 GRILLA INCONSITENTE
--9 TEXTO INCONSISTENTE
--10 HTML INCONSISTENTE
--11 ERROR INESPERADO

    SET @CONST_PERIODO_ABIERTO = 0
    SET @CONST_CUADRO_INICIADO = 0
    SET @CONST_PERIODO_CERRADO = 1
    SET @CONST_MESES_PERIODO = 3
    SET @CONST_VIGENTE = 1
    SET @CONST_NO_VIGENTE = 1
    SET @CONST_COMENTARIO_VERSION = 'VERSION INICIAL NUEVO PERIODO'
    SET @CONST_TIPO_GRILLA = 0
    SET @CONST_TIPO_HTML = 1
    SET @CONST_TIPO_TEXTO = 2
    SET @P_ERRNO = 1
    
    PRINT 'INICIANDO PROCEDURE';
    
    
   

    --INSERTANDO LOG
    BEGIN TRANSACTION;
    BEGIN TRY
		
		INSERT INTO IFRS_LOG_PROCESO(USUARIO,FECHA,LOG) VALUES(@P_USUARIO,GETDATE(),'INICIANDO ABRIR PERIODO');
		SET @V_ID_LOG = @@IDENTITY;
		PRINT 'INSERTANDO LOG ' + CAST(@V_ID_LOG AS VARCHAR)
		COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
		ROLLBACK TRANSACTION;
		set @P_ERRNO = 4;
        RETURN;
    END CATCH
    
    --TRANSACCION DE INSERCION
    BEGIN TRANSACTION
	BEGIN TRY
	
	--SE OBTIENE PERIODO ACTUAL
    SELECT 
		@V_PERIODO_ID = ID_PERIODO,
		@V_PERIODO_ID_ESTADO_PERIODO = ID_ESTADO_PERIODO
    FROM 
        IFRS_PERIODO
    WHERE 
        ID_PERIODO = (SELECT MAX(ID_PERIODO) FROM IFRS_PERIODO);
        
    
    -- SE VALIDA EL ESTADO DEL PERIODO POR EMPRESA Y GENERAL
    SELECT 
        @V_COUNT_PERIODO_ABIERTO = COUNT(*) 
    FROM 
        IFRS_PERIODO_EMPRESA 
    WHERE 
        ID_PERIODO = @V_PERIODO_ID AND 
        ID_ESTADO_PERIODO <> @CONST_PERIODO_CERRADO;
        
    
    --SE VALIDA QUE EL PERIODO NO ESTE ABIERTO    
    IF @V_COUNT_PERIODO_ABIERTO > 0
		BEGIN
			SET @P_ERRNO = 3;
			goto ERR_HANDLER;
		END;
		
	IF @V_PERIODO_ID_ESTADO_PERIODO <> @CONST_PERIODO_CERRADO
		BEGIN
			SET @P_ERRNO = 2;
			goto ERR_HANDLER;
		END
	
	SET @V_PERIODO_DATE = CONVERT(DATETIME, (CAST(@V_PERIODO_ID AS VARCHAR)+'01'));
	SET @V_MES_PERIODO = MONTH(@V_PERIODO_DATE);
	SET @V_ANIO_PERIODO = YEAR(@V_PERIODO_DATE);
	
	PRINT 'PERIODO DATE' + CAST(@V_PERIODO_DATE AS VARCHAR) 
	PRINT 'PERIODO MONTH' + CAST(@V_MES_PERIODO AS VARCHAR) 
	PRINT 'PERIODO YEAR' + CAST(@V_ANIO_PERIODO AS VARCHAR) 
	
	IF @V_MES_PERIODO = 12
		BEGIN
			SET @V_MES_PERIODO = 3;
			SET @V_ANIO_PERIODO = @V_ANIO_PERIODO + 1;
		END;
	ELSE
		BEGIN
			SET @V_MES_PERIODO = @V_MES_PERIODO + 3
		END;
	
	-- SE CALCULA EL NUEVO ID_PERIODO
    SET @V_ID_PERIODO_NUEVO =  (@V_ANIO_PERIODO * 100) + @V_MES_PERIODO;
    
    -- SE VALIDA QUE NO EXISTE NUEVO PERIODO GENERADO
    SELECT 
        @V_COUNT_PERIODO_NUEVO = COUNT(ID_PERIODO) 
    FROM 
        IFRS_PERIODO 
    WHERE 
        ID_PERIODO = @V_ID_PERIODO_NUEVO;
    
    IF @V_COUNT_PERIODO_NUEVO > 0
        BEGIN
			SET @P_ERRNO = 5;
			goto ERR_HANDLER;
		END;
		
	
	
	--INSERTANDO PERIODO Y PERIODO EMPRESA
    BEGIN TRY
    
		PRINT  'PERIODO NUEVO -> ' + CAST(@V_ID_PERIODO_NUEVO AS VARCHAR)
		PRINT 'ESTADO PERIODO NUEVO -> ' + CAST(@CONST_PERIODO_ABIERTO AS VARCHAR)
		
        INSERT INTO IFRS_PERIODO (ID_PERIODO, ID_ESTADO_PERIODO)  VALUES ( @V_ID_PERIODO_NUEVO, @CONST_PERIODO_ABIERTO);
        
        DECLARE
		@V_P_ID_RUT INT;
		
        
        DECLARE CUR_PERIODO_EMPRESA CURSOR FOR
			SELECT ID_RUT
			FROM IFRS_PERIODO_EMPRESA 
			WHERE ID_PERIODO = @V_PERIODO_ID ORDER BY ID_RUT;
			
		OPEN CUR_PERIODO_EMPRESA;
		
		FETCH CUR_PERIODO_EMPRESA INTO @V_P_ID_RUT;
		
		WHILE (@@FETCH_STATUS = 0)
			BEGIN
				
				PRINT 'INSERTANDO PERIODO EMPRESA : '
				PRINT @V_ID_PERIODO_NUEVO
				PRINT @V_P_ID_RUT
				PRINT @CONST_PERIODO_ABIERTO
				
				INSERT INTO IFRS_PERIODO_EMPRESA (ID_PERIODO, ID_RUT, ID_ESTADO_PERIODO) VALUES (@V_ID_PERIODO_NUEVO, @V_P_ID_RUT, @CONST_PERIODO_ABIERTO);
				
				FETCH CUR_PERIODO_EMPRESA INTO @V_P_ID_RUT;
				
			END;
		CLOSE CUR_PERIODO_EMPRESA;
		DEALLOCATE CUR_PERIODO_EMPRESA;
       
    END TRY
    BEGIN CATCH
        CLOSE CUR_PERIODO_EMPRESA;
		DEALLOCATE CUR_PERIODO_EMPRESA;
			SET @P_ERRNO = 6;
			goto ERR_HANDLER;
    END CATCH
    
	--COPIANDO INFORMACION    
    BEGIN TRY
		
		--DECLARANDO VARIABLES CURSOR CUR_VERSION
		DECLARE
		@V_CUR_ID_VERSION INT,
		@V_CUR_ID_CATALOGO INT, 
		@V_CUR_ID_PERIODO INT, 
		@V_CUR_ID_RUT INT, 
		@V_CUR_VERSION INT, 
		@V_CUR_VIGENCIA INT, 
		@V_CUR_COMENTARIO VARCHAR(256);
		
		--ITERANDO VERSIONES PARA COPIAR
		DECLARE CUR_VERSION CURSOR FOR
			SELECT 
				ID_VERSION, 
				ID_CATALOGO, 
				ID_PERIODO, 
				ID_RUT, 
				VERSION, 
				VIGENCIA,
				COMENTARIO 
			FROM 
				IFRS_VERSION 
			WHERE 
				VIGENCIA = 1 AND 
				ID_PERIODO = @V_PERIODO_ID;
				
		OPEN CUR_VERSION;
		--SE LEE PRIMER REGISTRO
		FETCH CUR_VERSION INTO  @V_CUR_ID_VERSION,
								@V_CUR_ID_CATALOGO,
								@V_CUR_ID_PERIODO,
								@V_CUR_ID_RUT,
								@V_CUR_VERSION,
								@V_CUR_VIGENCIA,
								@V_CUR_COMENTARIO;
								
		--ITERANDO VERSION
		WHILE (@@FETCH_STATUS = 0)
			BEGIN
				
				PRINT 'INSERTANDO VERSION : '
				PRINT @V_CUR_ID_VERSION
				PRINT @V_CUR_ID_CATALOGO
				PRINT @V_CUR_ID_PERIODO
				PRINT @V_CUR_ID_RUT
				
				SELECT @V_ID_VERSION_NUEVA = (MAX(ID_VERSION)+1) FROM IFRS_VERSION;
		
				INSERT INTO IFRS_VERSION 
		/*1*/            (   ID_VERSION, 
		/*2*/                ID_CATALOGO, 
		/*3*/                ID_PERIODO, 
		/*4*/                ID_RUT, 
		/*5*/                ID_ESTADO_CUADRO, 
		/*6*/                VERSION, 
		/*7*/                VIGENCIA, 
		/*8*/                FECHA_CREACION, 
		/*9*/                COMENTARIO)
				VALUES 
		/*1*/            (   @V_ID_VERSION_NUEVA, 
		/*2*/                @V_CUR_ID_CATALOGO, 
		/*3*/                @V_ID_PERIODO_NUEVO, 
		/*4*/                @V_CUR_ID_RUT,
		/*5*/                @CONST_CUADRO_INICIADO, 
		/*6*/                (@V_CUR_VERSION + 1), 
		/*7*/                @CONST_VIGENTE, 
		/*8*/                GETDATE(), 
		/*9*/                @CONST_COMENTARIO_VERSION);		

				--SELECT @V_ID_HISTORIAL_VERSION = (MAX(ID_HISTORIAL)+1) FROM IFRS_HISTORIAL_VERSION;
				--SET @V_ID_HISTORIAL_VERSION = @@IDENTITY;

				INSERT INTO IFRS_HISTORIAL_VERSION 
		/*1*/                (   --ID_HISTORIAL,
		/*2*/                    ID_ESTADO_CUADRO, 
		/*3*/                    ID_VERSION, 
		/*4*/                    NOMBRE_USUARIO, 
		/*5*/                    FECHA_PROCESO, 
		/*6*/                    COMENTARIO    ) 
				VALUES 
		/*1*/            (       --@V_ID_HISTORIAL_VERSION, 
		/*2*/                    @CONST_VIGENTE, 
		/*3*/                    @V_ID_VERSION_NUEVA, 
		/*4*/                    @P_USUARIO, 
		/*5*/                    GETDATE(), 
		/*6*/                    @CONST_COMENTARIO_VERSION);
			
			
			
			
			DECLARE @V_CUR_ID_ESTRUCTURA INT, 
					@V_CUR_ID_TIPO_ESTRUCTURA INT,
					@V_CUR_ORDEN INT;
                        
			
					
				--ITERANDO ESTRUCTURAS PARA COPIAR
				DECLARE CUR_ESTRUCTURA CURSOR FOR
					SELECT ID_ESTRUCTURA,ID_TIPO_ESTRUCTURA,ORDEN 
					FROM IFRS_ESTRUCTURA 
					WHERE ID_VERSION = @V_CUR_ID_VERSION;
				

				OPEN CUR_ESTRUCTURA;
				--SE LEE PRIMER REGISTRO
				FETCH CUR_ESTRUCTURA INTO   @V_CUR_ID_ESTRUCTURA,
											@V_CUR_ID_TIPO_ESTRUCTURA,
											@V_CUR_ORDEN;
	                        
				--ITERANDO ESTRUCTURA 
				WHILE (@@FETCH_STATUS = 0)
					BEGIN
					
					SELECT @V_ID_ESTRUCTURA_NUEVA = (MAX(ID_ESTRUCTURA)+1) FROM IFRS_ESTRUCTURA;
					
					EXEC PRC_COPIA_TIPO_ESTRUCTURA @V_ID_VERSION_NUEVA, @V_CUR_ID_ESTRUCTURA, @V_CUR_ID_TIPO_ESTRUCTURA
					
					INSERT INTO IFRS_ESTRUCTURA 
                       (ID_ESTRUCTURA, 
                        ID_VERSION, 
                        ID_TIPO_ESTRUCTURA, 
                        ORDEN) 
					VALUES 
                        (@V_ID_ESTRUCTURA_NUEVA, 
                         @V_ID_VERSION_NUEVA, 
						 @V_CUR_ID_TIPO_ESTRUCTURA, 
                         @V_CUR_ORDEN);
					END;
					
					--ITERANDO SIGUIENTE REGISTRO DE ESTRUCTURA
					FETCH CUR_ESTRUCTURA INTO   @V_CUR_ID_ESTRUCTURA,
												@V_CUR_ID_TIPO_ESTRUCTURA,
												@V_CUR_ORDEN;
			
			--ITERANDO SIGUIENTE REGISTRO DE VERSION
			FETCH CUR_VERSION INTO  @V_CUR_ID_VERSION,
								@V_CUR_ID_CATALOGO,
								@V_CUR_ID_PERIODO,
								@V_CUR_ID_RUT,
								@V_CUR_VERSION,
								@V_CUR_VIGENCIA,
								@V_CUR_COMENTARIO;
				
			END;
		CLOSE CUR_VERSION;
		DEALLOCATE CUR_VERSION;
		
    END TRY    
    BEGIN CATCH
		
		CLOSE CUR_VERSION;
		DEALLOCATE CUR_VERSION;
		SET @P_ERRNO = 7
		
		goto ERR_HANDLER;
        
    END CATCH
    
    
    END TRY
    BEGIN CATCH
		SET @P_ERRNO = 11
		goto ERR_HANDLER;
    END CATCH
    
    ERR_HANDLER:
		PRINT 'ERROR TSQL : ' +  CAST(@@ERROR AS VARCHAR)
		
		IF(@@ERROR<>0)
			BEGIN
				SET @V_ERRNO = ERROR_NUMBER()
				SET	@V_MSG = ERROR_MESSAGE()
			END;
		ELSE
			BEGIN
				SET @V_ERRNO = 0
				SET	@V_MSG = 'SIN ERROR DE SQL - ERROR DE VALIDACION'
			END;
			
		--ROLLBACK DE LA TRANSACCION
		ROLLBACK TRANSACTION
		
		--COMMIT AL LOG
		BEGIN TRANSACTION
		BEGIN TRY
			
			PRINT 'TRY LOG'
			PRINT 'ACTUALIZANDO LOG ' + CAST(@V_ID_LOG AS VARCHAR)
			--CONTROL DE EXCEPTION
			
			IF (@P_ERRNO IS NULL)
				UPDATE IFRS_LOG_PROCESO SET LOG = 'P_ERRNO = NULL - ERROR : ' + CAST(@V_ERRNO AS VARCHAR) + ' - ' + @V_MSG WHERE ID_LOG = @V_ID_LOG;
			ELSE IF @P_ERRNO = 3
				UPDATE IFRS_LOG_PROCESO SET LOG = 'P_ERRNO = 3 - ERROR : NO TODAS LAS EMPRESAS HAN CERRADO PERIODO' WHERE ID_LOG = @V_ID_LOG;
			ELSE IF @P_ERRNO = 2
				UPDATE IFRS_LOG_PROCESO SET LOG = 'P_ERRNO = 2 - ERROR : EL PERIODO NO ESTA EN ESTADO CERRADO' WHERE ID_LOG = @V_ID_LOG;
			ELSE IF @P_ERRNO = 5
				UPDATE IFRS_LOG_PROCESO SET LOG = 'P_ERRNO = 5 - EL PERIODO NUEVO' + @V_ID_PERIODO_NUEVO + 'ESTA REGISTRADO EN BASE DE DATOS' WHERE ID_LOG = @V_ID_LOG;
			ELSE IF @P_ERRNO IN(6,7)
				UPDATE IFRS_LOG_PROCESO SET LOG = 'P_ERRNO = '+CAST(@P_ERRNO AS VARCHAR)+' - ERROR : ' + CAST(@V_ERRNO AS VARCHAR) + ' - ' + @V_MSG  WHERE ID_LOG = @V_ID_LOG;
			ELSE
				UPDATE IFRS_LOG_PROCESO SET LOG = 'P_ERRNO = '+CAST(@P_ERRNO AS VARCHAR)+' - ERROR : ' + CAST(@V_ERRNO AS VARCHAR) + ' - ' + @V_MSG WHERE ID_LOG = @V_ID_LOG;
			
		COMMIT TRANSACTION;
		RETURN;
				
		END TRY
		BEGIN CATCH
			ROLLBACK TRANSACTION
			RETURN;
		END CATCH 
	

--SI NO HAY ERRORE COMMIT
COMMIT TRANSACTION;			

END;










-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[PRC_COPIA_TIPO_ESTRUCTURA](
	@P_ID_ESTRUCTURA_NUEVA INT,
	@P_ID_ESTRUCTURA_ANTERIOR INT,
	@P_ID_TIPO_ESTRUCTURA INT
)
AS
BEGIN

	DECLARE
	@CONST_TIPO_GRILLA INT,
	@CONST_TIPO_HTML INT,
	@CONST_TIPO_TEXTO INT;
	
	
	SET @CONST_TIPO_GRILLA = 0
    SET @CONST_TIPO_HTML = 1
    SET @CONST_TIPO_TEXTO = 2
	
	IF @P_ID_TIPO_ESTRUCTURA = @CONST_TIPO_GRILLA
	BEGIN
	
		DECLARE 
		@V_CUR_TITULO_GRILLA VARCHAR(256),
		@V_CUR_TIPO_FORMULA INT;
		
		SELECT @V_CUR_TITULO_GRILLA = TITULO, @V_CUR_TIPO_FORMULA = TIPO_FORMULA
		FROM IFRS_GRILLA
		WHERE ID_GRILLA = @P_ID_ESTRUCTURA_ANTERIOR;
		
		-- 'INSERTANDO GRILLA : '
		INSERT INTO IFRS_GRILLA
                        (ID_GRILLA, 
                         TITULO, 
                         TIPO_FORMULA) 
        VALUES			(
                        @P_ID_ESTRUCTURA_NUEVA, 
                        @V_CUR_TITULO_GRILLA, 
                        @V_CUR_TIPO_FORMULA);
		
--FIN COPIADO GRILLA		
		
		--COPIANDO COLUMNA
		  DECLARE
		  @V_CUR_ID_COLUMNA INT,
		  @V_CUR_TITULO_COLUMNA VARCHAR(128),
		  @V_CUR_ORDEN INT,
		  @V_CUR_ANCHO INT,
		  @V_CUR_ROW_HEADER INT;
	      
		  DECLARE CUR_COLUMNA CURSOR FOR
				SELECT ID_COLUMNA, TITULO_COLUMNA, ORDEN, ANCHO, ROW_HEADER
				FROM IFRS_COLUMNA 
				WHERE ID_GRILLA = @P_ID_ESTRUCTURA_ANTERIOR
				ORDER BY ID_COLUMNA;
				
		  OPEN CUR_COLUMNA;
		
		  --PRIMER REGISTRO COLUMNA
		  FETCH CUR_COLUMNA INTO @V_CUR_ID_COLUMNA, @V_CUR_TITULO_COLUMNA,@V_CUR_ORDEN, @V_CUR_ANCHO, @V_CUR_ROW_HEADER;
		  
		  WHILE (@@FETCH_STATUS = 0)
			BEGIN
				
				INSERT INTO IFRS_COLUMNA (
                                                ID_GRILLA,
                                                ID_COLUMNA, 
                                                TITULO_COLUMNA, 
                                                ORDEN, 
                                                ANCHO, 
                                                ROW_HEADER) 
                        VALUES (                
                                                @P_ID_ESTRUCTURA_NUEVA, 
                                                @V_CUR_ID_COLUMNA, 
                                                @V_CUR_TITULO_COLUMNA, 
                                                @V_CUR_ORDEN, 
                                                @V_CUR_ANCHO, 
                                                @V_CUR_ROW_HEADER); 
			
				--ITERANDO REGISTRO COLUMNA
				FETCH CUR_COLUMNA INTO @V_CUR_ID_COLUMNA, @V_CUR_TITULO_COLUMNA,@V_CUR_ORDEN, @V_CUR_ANCHO, @V_CUR_ROW_HEADER;	
			END;
			
			--LIBERANDO MEMORIA
			CLOSE CUR_COLUMNA;
			DEALLOCATE CUR_COLUMNA;

--FIN COPIADO COLUMNA	


		  --COPIANDO CELDA
		  DECLARE
		  @V_CUR_CEL_ID_COLUMNA INT,
		  @V_CUR_CEL_ID_FILA INT,
		  @V_CUR_CEL_ID_TIPO_CELDA INT,
		  @V_CUR_CEL_ID_TIPO_DATO INT,
		  @V_CUR_CEL_VALOR VARCHAR(2048),
		  @V_CUR_CEL_CHILD_HORIZONTAL INT,
		  @V_CUR_CEL_PARENT_HORIZONTAL INT,
		  @V_CUR_CEL_CHILD_VERTICAL INT,
		  @V_CUR_CEL_PARENT_VERTICAL INT,
		  @V_CUR_CEL_FORMULA VARCHAR(256);
	      
		  DECLARE CUR_CELDA CURSOR FOR
				SELECT ID_COLUMNA, ID_FILA,ID_TIPO_CELDA,ID_TIPO_DATO,VALOR,CHILD_HORIZONTAL,PARENT_HORIZONTAL,CHILD_VERTICAL,PARENT_VERTICAL,FORMULA 
				FROM IFRS_CELDA 
				WHERE ID_GRILLA = @P_ID_ESTRUCTURA_ANTERIOR
				ORDER BY ID_COLUMNA, ID_FILA;
				
		  OPEN CUR_CELDA;
		
		  --PRIMER REGISTRO COLUMNA
		  FETCH CUR_CELDA INTO @V_CUR_CEL_ID_COLUMNA,@V_CUR_CEL_ID_FILA,@V_CUR_CEL_ID_TIPO_CELDA,@V_CUR_CEL_ID_TIPO_DATO,@V_CUR_CEL_VALOR ,@V_CUR_CEL_CHILD_HORIZONTAL ,@V_CUR_CEL_PARENT_HORIZONTAL ,@V_CUR_CEL_CHILD_VERTICAL ,@V_CUR_CEL_PARENT_VERTICAL ,@V_CUR_CEL_FORMULA;
		  
		  WHILE (@@FETCH_STATUS = 0)
			BEGIN
				
							INSERT INTO					IFRS_CELDA (
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
/*1*/                                                    @P_ID_ESTRUCTURA_NUEVA, 
/*2*/                                                    @V_CUR_CEL_ID_COLUMNA, 
/*3*/                                                    @V_CUR_CEL_ID_FILA, 
/*4*/                                                    @V_CUR_CEL_ID_TIPO_CELDA, 
/*5*/                                                    @V_CUR_CEL_ID_TIPO_DATO, 
/*6*/                                                    @V_CUR_CEL_VALOR, 
/*7*/                                                    @V_CUR_CEL_FORMULA, 
/*8*/                                                    @V_CUR_CEL_CHILD_HORIZONTAL, 
/*9*/                                                    @V_CUR_CEL_PARENT_HORIZONTAL, 
/*10*/                                                   @V_CUR_CEL_CHILD_VERTICAL, 
/*11*/                                                   @V_CUR_CEL_PARENT_VERTICAL);  
			
				--ITERANDO REGISTRO CELDA
				FETCH CUR_CELDA INTO @V_CUR_CEL_ID_COLUMNA,@V_CUR_CEL_ID_FILA,@V_CUR_CEL_ID_TIPO_CELDA,@V_CUR_CEL_ID_TIPO_DATO,@V_CUR_CEL_VALOR ,@V_CUR_CEL_CHILD_HORIZONTAL ,@V_CUR_CEL_PARENT_HORIZONTAL ,@V_CUR_CEL_CHILD_VERTICAL ,@V_CUR_CEL_PARENT_VERTICAL ,@V_CUR_CEL_FORMULA;
			END;
			
			--LIBERANDO MEMORIA
			CLOSE CUR_CELDA;
			DEALLOCATE CUR_CELDA;
			
--COPIANDO AGRUPACION_COLUMNA
	--COPIANDO COLUMNA
		  DECLARE
		  @V_CUR_AGR_ID_NIVEL INT,
		  @V_CUR_AGR_ID_COLUMNA VARCHAR(128),
		  @V_CUR_AGR_TITULO INT,
		  @V_CUR_AGR_ANCHO INT,
		  @V_CUR_AGR_GRUPO INT;
	      
		  DECLARE CUR_AGRUPACION_COLUMNA CURSOR FOR
				SELECT ID_NIVEL ,ID_COLUMNA, TITULO, ANCHO, GRUPO
				FROM IFRS_AGRUPACION_COLUMNA 
				WHERE ID_GRILLA = @P_ID_ESTRUCTURA_ANTERIOR;
				
		  OPEN CUR_AGRUPACION_COLUMNA;
		
		  --PRIMER REGISTRO COLUMNA
		  FETCH CUR_AGRUPACION_COLUMNA INTO @V_CUR_AGR_ID_NIVEL,@V_CUR_AGR_ID_COLUMNA,@V_CUR_AGR_TITULO,@V_CUR_AGR_ANCHO,@V_CUR_AGR_GRUPO;
		  
		  WHILE (@@FETCH_STATUS = 0)
			BEGIN
				
				INSERT INTO IFRS_AGRUPACION_COLUMNA (
                                                        ID_GRILLA,
                                                        ID_COLUMNA, 
                                                        ID_NIVEL, 
                                                        TITULO, 
                                                        ANCHO, 
                                                        GRUPO)
                    VALUES (                            
                                                        @P_ID_ESTRUCTURA_NUEVA,
                                                        @V_CUR_AGR_ID_COLUMNA, 
                                                        @V_CUR_AGR_ID_NIVEL, 
                                                        @V_CUR_AGR_TITULO, 
                                                        @V_CUR_AGR_ANCHO, 
                                                        @V_CUR_AGR_GRUPO );
			
				--ITERANDO REGISTRO COLUMNA
				FETCH CUR_AGRUPACION_COLUMNA INTO @V_CUR_AGR_ID_NIVEL,@V_CUR_AGR_ID_COLUMNA,@V_CUR_AGR_TITULO,@V_CUR_AGR_ANCHO,@V_CUR_AGR_GRUPO;
			END;
			
			--LIBERANDO MEMORIA
			CLOSE CUR_AGRUPACION_COLUMNA;
			DEALLOCATE CUR_AGRUPACION_COLUMNA;			
		
		
	END;--END IF
	
	
END--END PROCEDURE
GO














	