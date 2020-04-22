select f.TRDR, f.FINDOC, f.fincode, f.TRNDATE, t.NAME, f.SUMAMNT, f.finstates as state, f.comments as comms from findoc f
inner join mtrlines m on m.findoc = f.findoc
inner join mtrl mt on mt.mtrl = m.mtrl
inner join trdr t on f.TRDR = t.TRDR
where f.SOSOURCE = 1351 
and trndate >= ISNULL('{param1}','19000101') -- Formatted date passed as parameter from @fromDate date picker @Logistic-i mobile app
and trndate <= ISNULL('{param2}','31000101') -- Formatted date passed as parameter from @toDate date picker @Logistic-i mobile app
and f.tfprms = 201
and t.trdr = '{param3}' -- Refrence id based on what the user that has logged in is(e.g. customer, user, member etc.)
and f.finstates <> 4
group by f.findoc,f.TRDR, f.fincode, f.TRNDATE, t.name, f.SUMAMNT, f.finstates, f.comments
order by f.trndate desc,f.fincode desc