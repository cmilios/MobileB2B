select m.mtrl as code,
m.name AS descr,
f.QTY as qty,
f.QTY1 as qty1,
f.PRICE as prc,
f.DISC1VAL as disc,
f.LINEVAL as kathaxia,
f.VATAMNT as axiafpa,
0 as mtrunit,
(select name from mtrunit mm where mm.mtrunit = m.mtrunit1) as sUnit,
(select mtrunit from mtrunit where mtrunit = m.MTRUNIT1) as untCode,
f.num02 as num02
from mtrlines f
inner join mtrl m on m.mtrl = f.mtrl
inner join MTRUNIT mu on m.MTRUNIT1 = mu.MTRUNIT
where FINDOC = '{param1}' -- findoc number