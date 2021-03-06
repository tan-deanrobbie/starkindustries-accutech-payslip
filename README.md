# Payslip Service

## Description
An internal RESTful API for AccuTech (Stark Industries) which handles all pay slip related processes including a monthly payslip generation.

## Endpoints
### `POST` _/payslip/month_ Generate Monthly Payslip
Receives a list of employee details: First name, Last name, Annual Salary, Super Rate, and Payment Start Date. Returns a list of the monthly payslip of the requested employee: Employee Details, Pay Period (start and end dates), Gross Income, Inome Tax, Net Income and Super Annuation.

#### Sample Request Payload:
```
[
	{
		"firstName":"David",
		"lastName":"Rudd",
		"annualSalary":60050, // must be a positive integer
        "paymentMonth":3, // must be between 0 to 11 (January to December, default: 0)
		"superRate":0.09 // must be within 0 to 0.5 (or 50%)
	}
]
```
#### Sample Response Payload:
```
[
	{
        "employee": {
            "firstName":"David",
            "lastName":"Rudd",
            "annualSalary":60050,
            "paymentMonth":3,
            "superRate":0.09
        },
        "fromDate": "03 April",
        "toDate:": "02 May",
        "grossIncome": 5004,
        "incomeTax": 922,
        "superannuation": 450,
        "netIncome": 4082
	}
]
```
#### Sample Error Response Payload:
```
{
    "timestamp": "07-04-2022 08:50:02",
    "errors": [
        {
            "message": "Field 'paymentMonth' must be between 0 and 11 (January to December).",
            "field": "getMonthlyPayslip.employees[1].paymentMonth",
            "value": "13"
        },
        {
            "message": "Field 'annualSalary' must be a positive value.",
            "field": "getMonthlyPayslip.employees[0].annualSalary",
            "value": "-60050"
        },
        {
            "message": "Field 'superRate' must not be below 0.0.",
            "field": "getMonthlyPayslip.employees[0].annualSalary",
            "value": "-0.5"
        },
        {
            "message": "Field 'superRate' must not exceed 0.5.",
            "field": "getMonthlyPayslip.employees[1].annualSalary",
            "value": "0.75"
        }
    ]
}
```
## Running/Deployment
### Locally
- Test: `./mvnw clean test`
- Run application: `./mvnw clean spring-boot:run`

### Production
- Create release branch then create PR to master.
    - This triggers _"Run Tests"_ workflow which runs the checkstyle plugin and the unit tests.
- If build/test succeeds, merge PR.
- Create tag using maven project version (e.g. 1.0.1).
- Create release in GitHub using tag.
    - This triggers _"Release and Deploy to ECS"_ workflow which builds the application and a Docker image then pushes to _docker.io/tan-deanrobbie_. If previous step is successful, image would then be deployed to an EC2 instance in Sydney (ap-southeast-2) through an ECS task/cluster.
_(See `.github/workflows` for more details.)_

## Notes
- This project uses `projectlombok` -- please install a plugin for it on your favorite IDE (e.g. Visual Studio Code - vscode-lombok, etc.)

## Assumptions
- _All numeric values (`"annualSalary"`, `"grossIncome"`, etc.) can never exceed 2 trillion._
    - These would only be represented as integers (except `"superRate"`).
- _API is used internally by the organization._
    - Basic authentication is in place (user is defined in-memory for demo purposes -- iamironman/sh4w4rm4_1sL0v3).
    - Cross site request forgery is disabled.

## Future Improvements
- For easier debugging and better incident support, we could integrate SumoLogic for easy access to log entries. Debug logs should also be added.