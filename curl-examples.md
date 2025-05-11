# Loan API – cURL Examples

This document provides example `cURL` commands to interact with the Loan API.

---

## Create a Loan

Creates a new loan for a customer.

```bash
curl -X POST http://localhost:8080/v1/loans \
     -H "Content-Type: application/json" \
     -d '{
           "customerId": 1,
           "interestRate": 0.1,
           "loanAmount": 6000,
           "numberOfInstallments": 6
         }'
```

If you attempt to create a loan with an unsupported installment count:

```bash
curl -X POST http://localhost:8080/v1/loans \
     -H "Content-Type: application/json" \
     -d '{
           "customerId": 1,
           "interestRate": 0.1,
           "loanAmount": 6000,
           "numberOfInstallments": 7
         }'
```

Response:

```json
{
  "code": 140,
  "message": "Number of installments must be one of the following: 6, 9, 12, 24"
}
```

---

## List Loans by Customer

Returns all loans for the given customer ID.

```bash
curl -X GET "http://localhost:8080/v1/loans?customerId=1" \
     -H "Accept: application/json"
```

If no loans exist, the response will be:

```json
{
  "code": 102,
  "message": "No loan found for customer."
}
```

---

## List Installments for a Loan

Returns the list of installments for a specific loan ID.

```bash
curl -X GET "http://localhost:8080/v1/loans/42/installments" \
     -H "Accept: application/json"
```
 
If the loan ID doesn't exist:

```json
{
  "code": 103,
  "message": "Loan not found."
}
```



