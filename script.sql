-- insert roles
-- CREATE EXTENSION IF NOT EXISTS "pgcrypto";

INSERT INTO roles (id, name, created_at, assigned_at)
VALUES (gen_random_uuid(), 'ADMIN', NOW(), NOW());

INSERT INTO roles (id, name, created_at, assigned_at)
VALUES (gen_random_uuid(), 'USER', NOW(), NOW());

INSERT INTO roles (id, name, created_at, assigned_at)
VALUES (gen_random_uuid(), 'ORGANIZER', NOW(), NOW());

INSERT INTO roles (id, name, created_at, assigned_at)
VALUES (gen_random_uuid(), 'STAFF', NOW(), NOW());

-- Update user roles
select * from user_roles;

select rs.id as user_role_id,u.id as user_id,u.username,r.id as role_id,r.name from user_roles rs
inner join users u on rs.user_id = u.id
inner join roles r on rs.role_id = r.id;

update user_roles set
user_id = '425191c4-a0e8-4605-974f-9d82841ddf63',
role_id = '4525c625-4713-4a6c-abdf-7aa0a18ceee5'
where id = '0e762b18-e87a-400e-9a16-5a245e25adf7'

