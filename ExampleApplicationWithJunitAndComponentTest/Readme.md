1. To run only Integration Test ( It will fetch all test classes ending with "IT" and run.)
   For this, maven-failsafe-plugin is added
===================================================================================================
>> mvn failsafe:integration-test failsafe:verify

2. To run only Unit Test ( surefire plugin is added )
===================================================================================================
>> mvn test
