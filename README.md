# Create Selenium Tests for Coupon Code and Discount Validation

A small Java + Selenium + TestNG + Maven project for testing coupon and discount rules on a local demo shopping cart.

## Test cases
1. `SAVE10` applies a 10% discount (₹100) and sets the total to ₹900.
2. `SAVE20` applies a 20% discount (₹200) and sets the total to ₹800.
3. An invalid code displays an error and leaves the total unchanged.
4. An empty code displays a validation message.
5. A second coupon cannot be applied after a successful coupon.

## Requirements
- JDK 21 (or compatible JDK; Maven compiler target is Java 17)
- Maven 3.9+
- Google Chrome

## Run locally
From the project root:

```powershell
mvn clean test
```

If Maven is not in PATH, use your Maven installation directly, for example:

```powershell
& "D:\Tools\apache-maven-3.10.0\bin\mvn.cmd" clean test
```

Test reports are written to `target/surefire-reports/`.

## Jenkins
The Jenkinsfile is intended for a Windows Jenkins agent and uses the tool names `JDK21` and `Maven3`, which must match the names configured under **Manage Jenkins → Tools**. It runs `mvn -B clean test` and publishes Surefire XML test results.

The demo is local and educational; it is not connected to a real shopping site or payment service.
