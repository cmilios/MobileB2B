select
aa.MTRL as mtrl, 
sodata as img,
AA.CODE as mtrcode,
AA.NAME as mtrname,
man.NAME as manufacturer,
aa.NAME1 AS unit1, 
AA.NAME2 as unit2, 
AA.NAME4 as unit4, 
aa.code1 as code1,
aa.code2 as code2,
aa.code4 as code4, 
AA.MU21 as c21, 
AA.MU41 as c41,
aa.mu12mode as c21mode,
aa.mu14mode as c41mode
from (
select
mm.MTRL,
MM.CODE,
MM.NAME,
mm.MTRMANFCTR,
mm.PRICER,
mm.PRICEW,
MM.MU21,
MM.MU41,
mm.mu12mode,
mm.mu14mode, 
(SELECT NAME FROM MTRUNIT WHERE MTRUNIT=MM.MTRUNIT1) AS NAME1,
(SELECT NAME FROM MTRUNIT WHERE MTRUNIT=MM.MTRUNIT2) AS NAME2,
(SELECT NAME FROM MTRUNIT WHERE MTRUNIT=MM.MTRUNIT4) AS NAME4,
(select MTRUNIT from mtrunit where MTRUNIT=mm.MTRUNIT1) as code1,
(select MTRUNIT from mtrunit where MTRUNIT=mm.MTRUNIT2) as code2,
(select MTRUNIT from mtrunit where MTRUNIT=mm.MTRUNIT4) as code4
from mtrl mm
where mm.ISACTIVE=1
and mm.COMPANY = 1000              
AND mm.SODTYPE  = 51 
) aa
left join XTRDOCDATA xd on xd.REFOBJID = MTRL  
left join MTRMANFCTR man on man.MTRMANFCTR = aa.MTRMANFCTR
where aa.NAME like(concat('%','{param1}','%'))
order by aa.mtrl OFFSET {param2} rows Fetch next 50 rows only

