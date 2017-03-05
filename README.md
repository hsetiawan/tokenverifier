# Token verifier

This library provides a simple to use interface to validate Facebook and Google client tokens on your backend. It 
only provides validation, it will not request long term access tokens and will not provide methods to interact
with the social services in any other way.

The code validates the token's integrity, check if it's intended for your app id, and checks it with the provider's
service (therefore it requires network access).

## Installation

Clone the repository:

```
git clone https://github.com/miklosn/tokenverifier.git
```

Install it in your local maven repo:


```
cd tokenverifier
mvn install
```

Use it in your Maven project:

```
        <dependency>
            <groupId>hu.cray</groupId>
            <artifactId>tokenverifier</artifactId>
            <version>0.1-SNAPSHOT</version>
        </dependency>
```

## Usage

```
import hu.cray.tokenverifiyer.*;

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
