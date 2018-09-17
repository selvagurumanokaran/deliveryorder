docker pull mysql

docker run --name deliveryordermysqlimage -e MYSQL_ROOT_PASSWORD=ThePassword -e MYSQL_DATABASE=deliveryorderdb -e MYSQL_USER=deliveryorderuser -e MYSQL_PASSWORD=ThePassword -d mysql

docker run -d --name deliveryorderwebimage --link deliveryordermysqlimage:deliveryordermysqlimage -p 8080:8080 -e DATABASE_HOST=deliveryordermysqlimage -e DATABASE_PORT=3306 -e DATABASE_NAME=deliveryorderdb -e DATABASE_USER=deliveryorderuser -e DATABASE_PASSWORD=ThePassword selvagurumanokaran/deliveryorder-web