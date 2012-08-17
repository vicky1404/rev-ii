begin
for C_DATA_UPDATE
in(
select 
gri.id_grilla,
es.orden
from
rev_catalogo ca,
rev_grilla gri,
rev_estructura es,
rev_version ve1,
(select 
max(ve.id_version) id_version,
ve.id_catalogo
from 
rev_version ve,
rev_version_periodo vp,
rev_periodo pe
where 
ve.id_version = vp.id_version and
vp.id_periodo = pe.id_periodo and
pe.periodo = 201112
group by
ve.id_catalogo) uni1
where
ve1.id_version = uni1.id_version and
es.id_version = ve1.id_version and
es.id_estructura = gri.id_grilla and
ca.id_catalogo = ve1.id_catalogo
order by ca.id_catalogo) loop

MERGE INTO rev_celda cel 
USING
(select 
cel.*
from
rev_celda cel,
rev_grilla gri,
rev_estructura es,
rev_version ve1,
(select 
max(ve.id_version) id_version
from 
rev_version ve,
rev_version_periodo vp,
rev_periodo pe
where 
ve.id_version = vp.id_version and
vp.id_periodo = pe.id_periodo and
pe.periodo = 201112
group by
ve.id_catalogo
order by
id_version) uni1
where
ve1.id_version = uni1.id_version and
es.id_version = ve1.id_version and
es.id_estructura = gri.id_grilla and
gri.id_grilla = cel.id_grilla and
es.id_estructura = C_DATA_UPDATE.id_grilla
order by cel.id_columna, cel.id_fila) comparador
ON 
(
cel.id_columna = comparador.id_columna and 
cel.id_fila = comparador.id_fila and 
cel.id_grilla = 
(select
estruc.id_estructura
from
rev_estructura estruc,
(select max(ver.id_version) id_version
from 
rev_version ver,
rev_version_periodo vp,
rev_periodo pe,
(select 
ve.id_catalogo
from 
rev_estructura es,
rev_version ve
where
es.id_estructura = C_DATA_UPDATE.id_grilla and
ve.id_version = es.id_version) catalogo
where
ver.id_catalogo = catalogo.id_catalogo and
vp.id_version = ver.id_version and
pe.id_periodo = vp.id_periodo
and pe.periodo = 201109) version
where
version.id_version = estruc.id_version and
estruc.orden = C_DATA_UPDATE.orden)
)
WHEN MATCHED THEN
UPDATE SET cel.valor  = comparador.valor;
end loop;
end;