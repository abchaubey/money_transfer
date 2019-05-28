
# moneytransfer 
Money Transfer rest service

Default starts on  http://localhost:8080

Run this jar to start application
```sh
java -jar revoult_money_transfer-1.0-SNAPSHOT-jar-with-dependencies.jar
```
Maven :

```sh
mvn exec:java
```
## Account API - `/accounts`

**GET** - retrieves all accounts

Response:
**Status: 200 OK**
```javascript
[
    {
        "accountNumber": 1,
        "userName": "abhay",
        "balance": 122
    },
    {
        "accountNumber": 2,
        "userName": "sonu",
        "balance": 112
    },
    {
        "accountNumber": 3,
        "userName": "amar",
        "balance": 192
    },
    {
        "accountNumber": 4,
        "userName": "amlan",
        "balance": 100
    }
]
```
---
**POST** - persists new account 
**Request Body** -

Sample request:
```javascript
{
	"accountNumber" : "1",
	"userName" : "abhay",
	"balance" : "122.0"
}

```

Sample response:
**Status: 200 OK**
```javascript
{
	"accountNumber" : "1",
	"userName" : "abhay",
	"balance" : "122.0"
}

```
Account exist response:
**Status: 400 Bad Request**
```javascript
Account with account Number:4 already exists.
```
---
**/{accountNumber}** - account Number
**GET** - retrieves particular account number

Response:
**Status: 200 OK**
```javascript
{
    "accountNumber": 1,
    "userName": "abhay",
    "balance": 122
}
```
Account with account Number:5 doesn't exists.
**Status: 404 Not Found**

**/{accountNumber}/balance** - account Number
**GET** - retrieves particular account number

Response:
**Status: 200 OK**
```javascript
{
    "accountNumber": 1,
    "userName": "abhay",
    "balance": 122
}
```
Account with account Number:5 doesn't exists.
**Status: 404 Not Found**

**/{accountNumber}/deposit/{amount}** - account Number
**PUT** - deposit money into accountNumber

Response:
**Status: 200 OK**
```javascript
{
    "accountNumber": 1,
    "userName": "abhay",
    "balance": 222
}
```
Account with account Number:5 doesn't exists.
**Status: 404 Not Found**

**/{accountNumber}/withdraw/{amount}** - account Number
**PUT** - withdraw money from accountNumber

Response:
**Status: 200 OK**
```javascript
{
    "accountNumber": 1,
    "userName": "abhay",
    "balance": 122
}
```
Account with account Number:5 doesn't exists.
**Status: 404 Not Found**

## Transaction API - `/transactions`

**POST** - submit money transfer between accounts

**Request Body** -

Sample request:
```javascript
{
		"fromAccountNumber" : "1",
		"toAccountNumber" : "2",
		"amount" : "100"
}
```

Sample response:
**Status: 200 OK**
```javascript
[
    {
        "accountNumber": 1,
        "userName": "abhay",
        "balance": 22
    },
    {
        "accountNumber": 2,
        "userName": "sonu",
        "balance": 212
    }
]
```

Insufficient balance on source account:
**Status: 409 Conflict**
```javascript
Insufficient account balance to perform fund transfer.
```

Amount negative
**Status: 400 Bad Request**
```javascript
Amount is invalid. Please provide a valid amount
```
