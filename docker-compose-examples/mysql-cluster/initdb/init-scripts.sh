#!/bin/bash
set -e

NODE_NAME=${1:-mysql}
ROOT_PASS="admin"

echo "[$NODE_NAME]: Starting initialization..."

# Check if MySQL data directory is initialized
if [ ! -d "/var/lib/mysql/mysql" ]; then
    echo "[$NODE_NAME]: First run - initializing database..."
    
    # Initialize MySQL data directory
    mysqld --initialize-insecure --user=mysql
    
    # Start MySQL temporarily to run init script
    mysqld --user=mysql --skip-networking --socket=/tmp/mysql_init.sock &
    INIT_PID=$!
    
    # Wait for temporary server
    echo "[$NODE_NAME]: Waiting for init server..."
    for i in {1..30}; do
        if mysql --socket=/tmp/mysql_init.sock -uroot -e "SELECT 1" &>/dev/null; then
            break
        fi
        sleep 1
    done
    
    # Run initialization
    echo "[$NODE_NAME]: Setting up root user..."
    mysql --socket=/tmp/mysql_init.sock -uroot <<EOF
ALTER USER 'root'@'localhost' IDENTIFIED BY '${ROOT_PASS}';
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY '${ROOT_PASS}';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;
EOF
    
    # Shutdown temporary server
    mysqladmin --socket=/tmp/mysql_init.sock -uroot -p"${ROOT_PASS}" shutdown
    wait $INIT_PID
    
    echo "[$NODE_NAME]: Initialization complete."
fi

echo "[$NODE_NAME]: Starting MySQL server..."
exec mysqld --user=mysql \
    --server-id=${SERVER_ID:-1} \
    --report-host=${NODE_NAME} \
    --gtid-mode=ON \
    --enforce-gtid-consistency=ON \
    --binlog-checksum=NONE \
    --log-bin=binlog \
    --log-slave-updates=ON \
    --binlog-format=ROW \
    --master-info-repository=TABLE \
    --relay-log-info-repository=TABLE \
    --transaction-write-set-extraction=XXHASH64 \
    --binlog-transaction-dependency-tracking=WRITESET \
    --plugin-load-add=group_replication.so \
    --group-replication-group-name=aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee \
    --group-replication-start-on-boot=OFF \
    --disabled-storage-engines="MyISAM,BLACKHOLE,FEDERATED,ARCHIVE,MEMORY" \
    --bind-address=0.0.0.0 \
    --default-authentication-plugin=mysql_native_password
