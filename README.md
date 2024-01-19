### Задание:
Необходимо реализовать REST API, которое взаимодействует 
с файловым хранилищем AWS S3 и предоставляет 
возможность получать доступ к файлам и истории 
загрузок. Логика безопасности должна быть 
реализована средствами JWT токена. 
Приложение должно быть докеризировано и готового к 
развертыванию в виде Docker контейнера.<br/>
### Сущности:
- **User** -> List<Event> events,  Status status, …
- **Event** -> User user, File file, Status status
- **File** -> id, location, Status status ...
<br/>
Взаимодействие с S3 должно быть реализовано с помощью AWS SDK.
---
### Уровни доступа:
-  **ADMIN** - полный доступ к приложению
-  **MODERATOR** - права USER + чтение всех User + чтение/изменение/удаление всех Events + чтение/изменение/удаление всех Files
-  **USER** - только чтение всех своих данных + загрузка файлов для себя

**Технологии**: Java, MySQL, Spring (Boot, Reactive Data, WebFlux, Security), AWS SDK, MySQL, Docker, JUnit, Mockito, Gradle, Flyway.
___
### Инструкция по запуску проекта:
1. Установить Java, Docker и Intellij Idea
2. Склонировать проект себе на компьютер `git clone https://github.com/MilkoEvgen/aws-file-storage.git`
3. Открыть проект в Intellij Idea и выполнить команду `build`
4. Запустить файл docker-compose.yaml
