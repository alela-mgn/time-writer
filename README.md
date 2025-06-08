# Time Writer

**Time Writer** — это приложение, которое каждую секунду сохраняет текущее время в базу данных и предоставляет API для получения всех сохранённых значений.

## Пререквизиты

Перед запуском убедитесь, что установлены:

- Java 17+
  ```bash
  java -version
  ```
- Apache Maven 3.8+
  ```bash
  mvn -version
  ```
- Docker и Docker Compose
  ```bash
  docker --version
  docker compose --version
  ```

## Запуск проекта

### 1. Клонирование проекта

```bash
git clone https://github.com/alela-mgn/time-writer.git
cd time-writer
```

### 2. Запуск через Docker Compose

```bash
docker compose up --build
```

Будут подняты:
- PostgreSQL (`localhost:5432`)
- Приложение Time Writer (`http://localhost:8080`)

Остановка:

```bash
docker compose down
```

### 3. Альтернативный запуск вручную

1. Убедитесь, что PostgreSQL запущен и есть база `timewriter`.
2. Запустите приложение:
```bash
java -jar target/time-writer-1.0.0.jar
```

---

## API

### Получить все записи времени

```
GET /timestamps
```

Ответ:
```json
[
  {
    "id": 1,
    "timestamp": "2025-06-08T12:00:00"
  },
  ...
]
```

Swagger доступен по адресу:
```
http://localhost:8080/swagger-ui/index.html
```

---

## Тесты

- Unit и интеграционные тесты запускаются командой:

```bash
mvn test
```

- Чтобы пропустить интеграционные тесты:
```bash
mvn verify -Pskip-integration-tests
```

---

## Структура проекта

- `TimeWriterApplication` — точка входа
- `TimeService` — логика записи времени и буферизации
- `RetryingService` — проверка доступности БД
- `TimeController` — REST endpoint
- `TimeRepository` — Spring Data JPA интерфейс

---

## База данных

- Используется PostgreSQL
- Миграции управляются с помощью Liquibase
- Таблица `time_entries` с полем `timestamp`

---
