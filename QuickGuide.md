# Quick Guide

***

## 1. Git Flow Guide

### С чего начать

- Клонировать проект на локальную машину
- Запустить проект и убедиться что он работает
- Запустить все тесты и убедиться что они выполнились успешно
- Начать выполнять задачи

### Как выполнять задачи

- Открыть ***Giltab*** и перейти в репозиторий проекта
- Открыть доску ***Issues*** -> ***Boards***
- Открыть любую не занятую задачу (поле ***Assignee*** должно быть пустым, кликнуть на название задачи)
- Нажать на кнопку ***Create merge request***
- Нажать на ***Assign to me***
- Выбрать ***Reviewer - Zhassulan Kaitanov***
- Нажать на кнопку ***Create merge request***
- Открыть ***IDEA***
- Обновить удаленный репозиторий, для этого вверху нажмите на ***Git*** -> ***Fetch***
- Открыть список веток ***Git*** -> ***Branches***
- При создании ***Merge request Gitlab*** автоматически создал ветку в удаленном репозитории, формат названия следующий
  **%{id}-%{title}**. Например: **1-add-project-structure**
- Найти свою ветку в разделе ***Remote Branches*** и нажать на кнопку ***Checkout***
- Выполнить задачу согласно **ТЗ**
- Создать коммит. **ОБЯЗАТЕЛЬНО** прочитать формат сообщения
  тут <https://www.conventionalcommits.org/en/v1.0.0/#summary>
- Отправить свои изменения в удаленный репозиторий ***Git*** -> ***Push*** -> нажать на кнопку ***Push***
- Передвинуть задачу на доске в ***Code Review***
- Если после ***Code Review*** будут замечания, то их можно посмотреть внутри ***Merge Request***, а задача передвинется
  в ***Reworking***

***

## 2. Test Guide

### Как запустить тесты в IDEA

1. Открыть окно ***Run/Debug Configurations*** -> ***Add New Configuration (Alt + Insert)***
2. Выбрать ***JUnit***
3. Указать путь до тестового класса
4. Далее необходимо передать параметр в ***VM***: ***Modify options*** -> ***Add VM options*** -> Передать параметр
   ***-Dspring.profiles.active=test***
5. Запустить тест

### Как писать тесты

- В проекте используются интеграционные тесты
- Интеграционные тесты проверяют всю логику от контроллера до репозитория
- На каждый контроллер пишется свой тестовый класс
- Название тестового класса должно быть формата ***НазваниеКонтроллераTest***
- Каждый тестовый класс должен наследоваться от класса ***IntegrationTestContext***
- Каждый метод внутри тестового класса помечается аннотацией ***@Test***
- Название тестового метода должно быть формата ***чтоДелаетМетодTest***. Пример: ***createQuestionSuccessTest***
- Каждый тестовый метод должен добавлять необходимые ему тестовые данные в базу и удалять после себя, для этого
  используется аннотация ***@Sql***
- Тестовые данные должны лежать в папке ***sql*** -> ***НазваниеТестовогоКласса*** -> ***названиеТестовогоМетода***
- **ВАЖНО** два и более тестовых метода не должны использовать один и тот же ***sql*** файл
- Для отправки запроса в контроллер используется ***MockMvc***
- Для сериализации и десериализации объекта в json и обратно используется ***ObjectMapper***
- Для получения ***JWT токена*** используется ***TestUtil***

***

## 3. Start Application Guide

### Как запустить приложение в IDEA

1. Установить на локальную машину ***Postgres***, либо установить ***docker & docker-compose***
2. Запустить ***docker-compose файл*** (лежит в корне проекта), который запустит ***Postgres*** в контейнере
3. Для передачи своих параметров в ***application-local.yml*** необходимо перейти в ***Run/Debug Configurations*** ->
   ***Modify options*** -> ***Environment variables***
4. Перед запуском приложения необходимо передать параметр в ***VM***: ***-Dspring.profiles.active=local***
5. Запустить приложение
6. Для просмотра документации к ***REST API*** перейти ***/docs/swagger-ui/index.html***
7. Для отправки запроса на защищенный url нужно прикрепить к запросу header ***Authorization***
8. Значение хедера должно начинаться с ***Bearer jwt***

### Архитектура приложения

- Для обмена данными с клиентом используются ***DTO (Data Transfer Object)***
- ***ResponseDto*** являются обычными классами, ***RequestDto рекордами*** <https://metanit.com/java/tutorial/3.18.php>
- Ответы оборачиваются в классы ***Data*** - для успешного ответа, ***Error*** - для сообщения об ошибке
- Для перевода сущности из ***RequestDto*** в ***Entity*** используются ***кастомные мапперы***
- Для ***Entity*** создаются свои ***сервисы и репозитории***, для ***Dto*** свои. Сущности и дто должны быть четко
  разграничены
- Все ***сервисы сущностей*** должны имплеменитровать ***CrudService*** и наследоваться от ***CrudServiceImpl***
- Если ***дто сервис*** должен возвращать данные с пагинацией, то он должен имплеменитровать ***PaginationService***. А
  ***дто репозиторий*** имплеменитровать ***PaginationRepository***
- ***Репозиторный слой*** должен возвращать объект всегда в обертке ***Optional*** для этого нужно использовать
  ***JpaResultUtil***
- ***Сервисный слой*** должен возвращать объект, либо выбросить исключение ***EntityNotFoundException***
- Если нужно вернуть ***несколько объектов***, то список можеть быть ***пустым***, либо с данными, но ***не null***
- Все исключения выброшенные из контроллеров обрабатываются в ***AdviceRestController***
- ***Репозитории сущностей*** расширяют ***JpaRepository***. ***Репозитории дто*** используют интерфейс
  ***EntityManager***

***

### Полезные ссылки

- Java конвенции (именование классов, полей, методов и
  тд) <https://www.oracle.com/java/technologies/javase/codeconventions-introduction.html>
- Именование коммитов <https://www.conventionalcommits.org/en/v1.0.0/#summary>
- Что такое Record <https://metanit.com/java/tutorial/3.18.php>
- Что такое Optional <https://metanit.com/java/tutorial/10.12.php>
- Много полезных статей о Hibernate <https://thorben-janssen.com/hibernate-best-practices/>
- Как правильно организовывать архитектуру классов. Принципы SOLID <https://medium.com/webbdev/solid-4ffc018077da>
- Библиотека дизайн паттернов <https://refactoring.guru/ru/design-patterns>