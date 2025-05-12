# Loan API – cURL Examples

This document provides example `cURL` commands to interact with the Loan API.

User Roles:
- ADMIN: Can manage loans for any customer.
- CUSTOMER: Can manage their own loans.

Users:
- admin (Role: ADMIN) - Username: admin, Password: adminpass
- Mert (Role: CUSTOMER) - Username: mert, Password: mertpass
- John (Role: CUSTOMER) - Username: john, Password: johnpass

**Admin user has access to all endpoints while CUSTOMER users only have access to endpoints other than 
"Create a Loan" and can only operate for their users.**

---

## Create a Loan

Creates a new loan for a customer.

```bash
curl -X POST http://localhost:8080/v1/loans \
     -H "Content-Type: application/json" \
     -H "Authorization: Basic YWRtaW46YWRtaW5wYXNz" \
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
     -H "Authorization: Basic bWVydDptZXJ0cGFzcw==" \
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
     -H "Accept: application/json" \
     -H "Authorization: Basic bWVydDptZXJ0cGFzcw=="
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
     -H "Accept: application/json" \
     -H "Authorization: Basic bWVydDptZXJ0cGFzcw=="
```
 
If the loan ID doesn't exist:

```json
{
  "code": 103,
  "message": "Loan not found."
}
```

---

## Pay Installments for a Loan

Pays upcoming loan installments (within the next 3 months) with the provided payment amount.

```bash
curl -X POST http://localhost:8080/v1/loans/1/payments \
     -H "Content-Type: application/json" \
     -H "Authorization: Basic bWVydDptZXJ0cGFzcw==" \
     -d '{
           "amount": 890
         }'
```
