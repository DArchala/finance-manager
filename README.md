# Finance-manager
## Description
Application allows to create user account, create user balance, send money to another balance.
## TECH
- Java 21
- Spring Boot 3.2.4
- Postgresql
- Docker
- JUnit 5
## How to run?
### IntelliJ IDEA + Docker
1. Run `docker-compose -f etc/docker-compose.yaml up -d postgres`
2. Run `pl.archala.FinanceManagerApplication.java`
### Docker
1. Run `docker-compose -f etc/docker-compose.yaml up -d`
## How to use?
1. There is no login/logout page.
2. To register user use POST /api/users/register.
3. Remaining endpoints need to use basic auth, to authorize use header:
   Key: "Authorization", Value: "Basic {base64-encoded-username:password}"