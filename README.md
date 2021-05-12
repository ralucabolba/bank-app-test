# Bank-App

Bank-App is a small banking like application that allows to manage current accounts and transactions. The application is implemented using Spring Boot and Maven, and it uses an in-memory H2 database for simplicity. The database is recreated at every application startup so the data loaded during one application running is volatile. 

## Current accounts API:
- add a current account by providing an initial balance and currency of the account :

```
POST http://localhost:8080/bank-app/currentAccounts
Request Body:
{
    "balance": "2000",
    "currency": "EUR"
}
```

- update an existing account, in order to change its balance, currency or status. For example, in order to close an account, an update with status CLOSED should be performed: 

```
PUT http://localhost:8080/bank-app/currentAccounts/2
Request Body:
{
    "id": 2,
    "accountNo": "ROAWMUMFR77JLKQBBZSXHTZR",
    "status": "CLOSED",
    "currency": "EUR",
    "balance": 2000.0
}
```

- view all current accounts and filter them by various criteria in a paginated manner; the API allows a generic filtering mechanism, by every field of the entity :

```
GET http://localhost:8080/bank-app/currentAccounts?page=0&size=10
GET http://localhost:8080/bank-app/currentAccounts?search=id:1 //search current accounts with id 1
GET http://localhost:8080/bank-app/currentAccounts?search=accountNo:RO09 //search current accounts whose account number contains string 'RO09'
GET http://localhost:8080/bank-app/currentAccounts?search=balance>100 //search current accounts having balance greater than 100
GET http://localhost:8080/bank-app/currentAccounts?search=dateCreated>"01-01-2021 00:00";dateCreated<"01-02-2021 00:00" //search for current accounts created between January 1st 2021 and February 1st 2021
```
- view currency account :
GET http://localhost:8080/bank-app/currentAccounts/2

## Transactions API:

- add a transaction for a current account. The transaction can be either a withdrawal/transfer from account or a deposit/transfer to account. Each transaction created for a current account updates its balance accordingly. Examples :
```
POST http://localhost:8080/bank-app/transactions
{
    "currentAccountId": 1,
    "debit" : -140,
    "description": "Transfer -200 lei"
}

POST http://localhost:8080/bank-app/transactions
{
    "currentAccountId": 1,
    "credit" : 120,
    "description": "Deposit 120 lei"
}
```

- view all transactions and filter them by various criteria in a paginated manner:

```
GET http://localhost:8080/bank-app/transactions?page=0&size=10
GET http://localhost:8080/bank-app/transactions?search=currentAccount.accountNo:RO09BCYP0000001234567890 // search transactions for current account with account no RO09BCYP0000001234567890; similarly search by current account id can be made to filter transactions by a current account
GET http://localhost:8080/bank-app/transactions?search=dateCreated>"12-05-2021 10:57";dateCreated<"12-05-2021 11:03" // search transaction created in a date interval; alternatively, for date filtering the following can be used:
GET http://localhost:8080/bank-app/transactions?lastUnits=15&timeUnit=MINUTES // search transactions created in last 15 minutes
GET http://localhost:8080/bank-app/transactions?lastUnits=1&timeUnit=HOURS // search transactions created in last 1 hour
```

- view transaction :
```
GET http://localhost:8080/bank-app/transaction/3
```

Swagger was also used for documenting the application Rest API. The documentation can be accessed at: http://localhost:8080/bank-app/swagger-ui.