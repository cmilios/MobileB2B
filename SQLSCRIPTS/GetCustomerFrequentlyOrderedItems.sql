SELECT
aa.MTRL as mtrl, 
xd.sodata as img,
AA.CODE as mtrcode,
AA.NAME as mtrname,
POLISEIS as sales,
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
FROM (
SELECT  
A.MTRL,
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
(select MTRUNIT from mtrunit where MTRUNIT=mm.MTRUNIT4) as code4,
COUNT(A.MTRL) AS POLISEIS
FROM 
(MTRTRN A JOIN TPRMS B ON A.COMPANY=B.COMPANY AND      
A.SODTYPE=B.SODTYPE AND A.TPRMS=B.TPRMS) JOIN TRDR C     
ON A.TRDR = C.TRDR  
INNER JOIN MTRL MM ON MM.MTRL=A.MTRL AND MM.ISACTIVE=1      
WHERE A.COMPANY = 1000              
AND A.SODTYPE  = 51               
AND A.TRNDATE>=DATEADD(month, DATEDIFF(month, 0, DATEADD(MONTH, -12, GETDATE())), 0)                                  
AND A.SOSOURCE = 1351             
AND C.TRDR='{param1}'
GROUP BY
A.MTRL,
mm.CODE,
mm.NAME,
mm.MTRMANFCTR,
mm.PRICER,
mm.PRICEW,
mm.MTRUNIT1,
mm.MTRUNIT2,
mm.MTRUNIT4,
MM.MU21,
MM.MU41,
mm.MU12MODE,
mm.mu14mode
) AA
left join XTRDOCDATA xd on xd.REFOBJID = MTRL  
left join MTRMANFCTR man on man.MTRMANFCTR = aa.MTRMANFCTR 
ORDER BY POLISEIS DESC OFFSET {param2} rows Fetch next 50 rows only
