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
- Docker
- GitHub Actions
- Railway Platform

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

## Деплой на Railway

### Передумови
1. Створіть акаунт на [Railway](https://railway.app/)
2. Встановіть [Railway CLI](https://docs.railway.app/develop/cli)
3. Створіть новий проект на Railway
4. Створіть PostgreSQL базу даних в проекті

### Налаштування змінних середовища
В налаштуваннях проекту на Railway додайте наступні змінні:
```
BOT_NAME=your_bot_name
BOT_TOKEN=your_bot_token
DATABASE_URL=postgresql://user:password@host:5432/database
PORT=8080
SPRING_PROFILES_ACTIVE=prod
```

### Локальний запуск з Docker
```bash
# Збірка образу
docker build -t predicted-again-bot .

# Запуск контейнера
docker run -p 8080:8080 \
  --env-file .env \
  predicted-again-bot
```

### Деплой через Railway CLI
```bash
# Логін в Railway
railway login

# Ініціалізація проекту
railway init

# Прив'язка до існуючого проекту
railway link

# Деплой
railway up
```

### Автоматичний деплой
1. Додайте в GitHub Secrets наступні змінні:
   - `RAILWAY_TOKEN` - токен доступу Railway
   - `RAILWAY_SERVICE_NAME` - назва сервісу в Railway

2. Push в main гілку автоматично запустить деплой

### Моніторинг
- Статус бота: `https://your-app-url/actuator/health`
- Метрики: `https://your-app-url/actuator/metrics`
- Логи доступні в Railway Dashboard

## Особливості реалізації
- Використання патерну Command для обробки команд
- Асинхронна обробка сповіщень
- Кешування передбачень через Caffeine
- Оптимізована робота з базою даних
- Обробка помилок та логування
- Повне покриття тестами
- Docker контейнеризація
- CI/CD через GitHub Actions

## Безпека
- Валідація вхідних даних
- Захист від SQL-ін'єкцій (через JPA)
- Безпечне зберігання конфігураційних даних
- Обмеження доступу до API
- Безпечна обробка змінних середовища

## Моніторинг
- Логування через SLF4J
- Spring Boot Actuator для моніторингу
- Метрики виконання
- Health check endpoints

## Розробка
- Дотримання принципів SOLID
- Чистий код та документація
- Покриття тестами
- Контроль якості коду

## Ліцензія
MIT License

## Автор
Roman Yushchenko 