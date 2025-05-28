#!/bin/bash
echo "Esperando a que MySQL esté disponible..."
until mysql -h mysql -u root -p1234 -e "SELECT 1" > /dev/null 2>&1; do
  sleep 2
done

echo "Restaurando base de datos desde backup.sql..."
mysql -h mysql -u root -p1234 mf_db < /backup.sql
