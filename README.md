# java-sliding-statistics

Demonstration of sliding statistics. Uses window of 60 seconds.

The service accepts transactions and exposes a bunch of statistics based on the sliding window:

* Minimum amount
* Maximum amount
* Average amount
* Sum of amounts
* Number of transactions

## Running locally

```sh
$ mvn clean package
$ java  -jar target/java-sliding-statistics-1.0.jar
```

Then, from another terminal:

```sh
$ curl -X POST localhost:8080/transactions  -H "Content-Type: application/json" -d"{\"timestamp\": \"$(date +%s)000\", \"amount\": 100}"
$ curl -X POST localhost:8080/transactions  -H "Content-Type: application/json" -d"{\"timestamp\": \"$(date +%s)000\", \"amount\": 100}"
$ curl localhost:8080/statistics
{"min":100.0,"max":100.0,"sum":200.0,"avg":100.0,"count":2}
```

## API

#### Insert a transaction
```
   {
     "timestamp": 1499567300000,
     "amount": 42.1
   }
   POST /transactions
```
`timestamp` can be from the past, the application handles it correctly.

Returns 201 if the transaction is accepted, 204 if it too old (older than 60 seconds).

#### Get statistics

```
   GET /statistics
   {
     "min":100.0,
     "max":100.0,
     "sum":200.0,
     "avg":100.0,
     "count":2
   }
```

## License

Copyright 2017 Dmitrii Balakhonskii
