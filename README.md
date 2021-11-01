# Online_learning_system SWP391

Project SWP391 in Spring 2021 Semester.

This project using: 

* Front end: Thymeleaf, Bootstrap.

* Backend: 3-tier architecture, Spring boot framework, OAuth2 combine with Spring security.

* Others: MS Sql Server, IntelliJ, DataGrip

## Installation
Run the script file to create the Database (use MS).

Run in Intelliji and open localhost:8081 to see the project.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
Not have lincense yet.

## Guideline for beginner
This project using Spring boot framework so before running project
you should open the maven tab, and click on reload all maven to download all dependencies 
of this project

After that you must go to application.properties to change to your own computer
the data source url is the sever of your jdbc
user name and password of your sql
At the end, you will see 'spring.jpa.hibernate.ddl-auto = update'
you can change to create in your first time, leave update when you want to change
leave none and push data into the database

