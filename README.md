## Project Tech Stack
- Java 17 ![Java](https://img.shields.io/badge/Java-17-blue)
- Gradle ![Gradle](https://img.shields.io/badge/Gradle-blue)
- Spring ![Spring](https://img.shields.io/badge/Spring-green)
- TestNG ![TestNG](https://img.shields.io/badge/TestNG-orange)

## Project Key Features
- RTM ![RTM](https://img.shields.io/badge/RTM-Traceability-blue)
Requirement Traceability Matrix, Automation is a tool, so all AUTOMATION TESTS should be linked to TEST CASES, and they should be linked to USER STORIES
- Parallelization ![Parallelization](https://img.shields.io/badge/Parallelization-Game_Changer-brightgreen)
It dramatically reduces Test Run time
- Data Provider ![Data Provider](https://img.shields.io/badge/Data_Provider-Game_Changer-brightgreen)
Add the possibility to run the same code with different data sets (together with Parallelization IT IS A GAME CHANGER)
- Docker ![Docker](https://img.shields.io/badge/Docker-Support-blue)
No more "it works locally" issue -> simplifies CI/CD and remote run
- Allure Report ![Allure Report](https://img.shields.io/badge/Allure_Report-Statistics-blue)
Just take a look at all statistics that it provides
- Page Object Structure ![Client Structure](https://img.shields.io/badge/Client_Structure-OOP-yellow)
It will make easier to develop UI tests

## Project Structure
automation-framework-example
├─ src
│  ├─ main/java
│  │   └─ automationframeworkexample
│  │       ├─ clients   ← API + UI page objects
│  │       └─ utils     ← custom loggers, retry, wrappers
│  └─ test/java
│      └─ tests
│          ├─ ui       ← TestNG UI suites
│          └─ api      ← (future) API suites
├─ Dockerfile
├─ README.md
└─ build.gradle.kts

## Project Report
![Main Report Screen](report-exmple/FavbetReport1.jpg)
![Statistics](report-exmple/FavbetReport2.jpg)
![Timing](report-exmple/FavbetReport3.jpg)
![Screen](report-exmple/FavbetReport4.jpg)
-------------------------------------------------------------------------------------
- Local run (Chrome needed)
git clone 
cd automation-framework-example
./gradlew clean test                # head-less Chromium is auto-downloaded
./gradlew allureReport              # generates HTML report at build/allure-report
- Container Run
docker build -t favbet-tests .
docker run --rm favbet-tests               # default browser = chromium
docker run --rm -e browser=firefox favbet-tests   # if Firefox is enabled


