# Token verifier

This library provides a simple to use interface to validate Facebook and Google client tokens on your backend. It 
only provides validation, it will not request long term access tokens and will not provide methods to interact
with the social services in any other way.

The code validates the token's integrity, check if it's intended for your app id and the claimed user, 
and checks it with the provider's service (it requires network access).

It's suitable for authentication login on your backend when you use the JavaScript libraries
provided by Facebook/Google on your client side.

It uses the slf4j api for logging.


## Installation

Clone the repository:

```bash
git clone https://github.com/miklosn/tokenverifier.git
```

Install it in your local maven repo:


```bash
cd tokenverifier
mvn install
```

Use it in your Maven project:

```xml
        <dependency>
            <groupId>hu.cray</groupId>
            <artifactId>tokenverifier</artifactId>
            <version>0.1-SNAPSHOT</version>
        </dependency>
```

## Usage

```java
import hu.cray.tokenverifier.*;

...

// set Facebook app id and secret
FacebookVerifier.setAppId("your-app-id", "your-app-secret");

// Set Google app id
GoogleVerifier.setAppId("your-app-id");

// verify token and uid provided by your client code
result1 = FacebookVerifier.verify(token, uid);
result2 = GoogleVerifier.verify(token, uid);

switch (result1) {
    case TokenVerifier.TOKEN_VALID: ;
    case TokenVerifier.TOKEN_INVALID: ;
}
```
