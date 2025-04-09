create docker 

docker run --name postgres-db -e POSTGRES_PASSWORD=admin -e POSTGRES_USER=admin -e POSTGRES_DB=reservation -p 5432:5432 -d postgres

docker exec -it postgres-db psql -U admin -d reservation