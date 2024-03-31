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
1. Run `docker-compose -f etc/docker-compose.yaml --profile dep up -d`
2. Run `pl.archala.FinanceManagerApplication.java`
### Docker
1. Run `docker-compose -f etc/docker-compose.yaml --profile dev up -d`