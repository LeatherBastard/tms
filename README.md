# tms
Task Management System

# Инструкция по запуску проекта

## Требования
- Установленный Docker и Docker Compose.

## Запуск проекта
1. Склонируйте репозиторий:
  ```bash
  git clone https://github.com/LeatherBastard/tms/tree/master
  ```

2.Перейдите в директорию проекта:
  ```bash
  cd tms-master
  ```

   
3.Соберите и запустите проект с помощью Docker Compose:
```bash
docker-compose up --build
```
  

Приложение будет доступно по адресу: http://localhost:8080.

## Остановка проекта
Чтобы остановить проект, выполните:
```bash
docker-compose down
```
