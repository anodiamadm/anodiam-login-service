insert into anodiam.roles select * from (
select 1, 'ADMIN' union
select 2, 'TEACHER' union
select 3, 'STUDENT'
) x where not exists(select * from anodiam.roles);