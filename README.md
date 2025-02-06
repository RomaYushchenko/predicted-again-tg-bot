# Predicted Again Telegram Bot 🔮

## Огляд
Predicted Again - це Telegram бот для щоденних та швидких передбачень, розроблений на Spring Boot. Бот надає користувачам можливість отримувати випадкові передбачення та налаштовувати щоденні сповіщення.

## Функціональність
- 🎯 Швидкі передбачення на вимогу
- 📅 Щоденні передбачення за розкладом
- ⏰ Налаштування часу сповіщень
- 🔔 Увімкнення/вимкнення сповіщень
- 🔄 Можливість отримати нове передбачення

## Технічний стек
- Java 17
- Spring Boot 3.x
- Spring Framework (Core, MVC, Data JPA)
- PostgreSQL
- Maven
- JUnit 5
- Mockito
- Liquibase (міграції БД)
- Telegram Bot API
- Docker (опціонально)

## Структура проекту
```
src/
├── main/
│   ├── java/
│   │   └── com/ua/yushchenko/
│   │       ├── bot/           # Конфігурація та основна логіка бота
│   │       ├── command/       # Обробники команд
│   │       ├── config/        # Конфігураційні класи
│   │       ├── model/         # Сутності
│   │       └── service/       # Бізнес-логіка
│   └── resources/
│       ├── db/               # Міграції БД
│       └── predictions.json  # Дані передбачень
└── test/                    # Тести
```

## Команди бота
- `/start` - Початок роботи з ботом
- `/quick` - Отримати швидке передбачення
- `/daily` - Отримати щоденне передбачення
- `/settings` - Налаштування сповіщень
- `/toggle` - Увімкнути/вимкнути сповіщення

## Налаштування проекту

### Передумови
- JDK 17+
- Maven 3.8+
- PostgreSQL 14+

### Конфігурація
1. Створіть базу даних PostgreSQL
2. Налаштуйте `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
spring.datasource.username=your_username
spring.datasource.password=your_password
bot.token=your_telegram_bot_token
bot.username=your_bot_username
```

### Запуск
```bash
./mvnw spring-boot:run
```

### Тестування
```bash
./mvnw test
```

## Особливості реалізації
- Використання патерну Command для обробки команд
- Асинхронна обробка сповіщень
- Кешування передбачень
- Оптимізована робота з базою даних
- Обробка помилок та логування
- Повне покриття тестами

## Безпека
- Валідація вхідних даних
- Захист від SQL-ін'єкцій (через JPA)
- Безпечне зберігання конфігураційних даних
- Обмеження доступу до API

## Моніторинг
- Логування через SLF4J
- Spring Boot Actuator для моніторингу
- Метрики виконання

## Розробка
- Дотримання принципів SOLID
- Чистий код та документація
- Покриття тестами
- Контроль якості коду

## Ліцензія
MIT License

## Автор
Roman Yushchenko 