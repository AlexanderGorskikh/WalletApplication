RESTful приложение Spring Boot, для взаимодействия с кошельками (Wallet):

Приложение принимает запрос вида:

POST {{base_url}}/api/v1/wallet - создаёт кошелёк с уникальным UUID возвращает в теле ответа UUID и баланс (по умолчанию 0)

GET {{base_url}}/api/v1/wallets/transaction - запрос для получения баланса кошелька, в теле запроса нужно передать конкретный UUID пример: JSON: { "id": "cbaac9a5-966f-45cc-9c0d-99745d0468e4" }

POST {{base_url}}/api/v1/wallets/transaction/withdraw - запрос на снятие, в теле запроса нужно указать UUID кошелька и сумму JSON: { "id": "cbaac9a5-966f-45cc-9c0d-99745d0468e4", "amount": 600 }

POST {{base_url}}/api/v1/wallets/transaction/deposit - запрос на внесение, в теле запроса передаётся UUID кошелька и сумма JSON: { "id": "cbaac9a5-966f-45cc-9c0d-99745d0468e4", "amount": 200 }

стек: java 17 Spring 3.4.0 Postgres 15