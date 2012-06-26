select * from rev_estructura where id_estructura not in(select id_grilla from rev_grilla) and id_tipo_estructura = 0;

select * from rev_estructura where id_estructura not in(select id_html from rev_html) and id_tipo_estructura = 1;

select * from rev_estructura where id_estructura not in(select id_texto from rev_texto) and id_tipo_estructura = 2;


select count(*) contador from rev_version group by id_catalogo, version order by contador desc;


update rev_version set vigencia = 1 where id_version in(
select 
id_version
from
rev_version ve,
(select max(version) version, id_catalogo from rev_version group by id_catalogo) ul
where
ul.id_catalogo = ve.id_catalogo and
ul.version = ve.version);



select * 
from 
rev_estructura es,
rev_version ver
where 
ver.id_version = es.id_version and
es.tipo_estructura = 2 and
ver.vigencia = 1
es.id_estructura not in(select );
