CREATE DATABASE sms;
CREATE USER admin2@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON sms.* TO admin2@'%';
FLUSH PRIVILEGES;