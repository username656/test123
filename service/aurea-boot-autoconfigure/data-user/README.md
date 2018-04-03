# Aurea Boot Module with Autoconfiguration for User Data

This module autoconfigures default Aurea User data and access. 
It includes in module's resources `META-INF/spring.factories` property file with mapping 
to main `DataAutoConfiguration` class which autoconfigures `User` entity 
and `UserRepository` repository interface

`META-INF/spring.factories` content:
```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  com.aurea.boot.autoconfigure.data.config.DataAutoConfiguration
```
