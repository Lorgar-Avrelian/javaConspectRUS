#!/bin/bash
set -e

ROOT_PASS="admin"
MAX_RETRIES=60

echo "[Orchestrator]: Starting..."

# Wait for all nodes to be ready with authentication
for node in mysql pmdb pmdb-mgr; do
    echo "[Orchestrator]: Waiting for $node..."
    RETRY=0
    until mysql -h"$node" -uroot -p"${ROOT_PASS}" -e "SELECT 1" 2>/dev/null; do
        RETRY=$((RETRY + 1))
        if [ $RETRY -gt $MAX_RETRIES ]; then
            echo "[Orchestrator]: ERROR - $node failed"
            exit 1
        fi
        sleep 3
    done
    echo "[Orchestrator]: $node is ready"
done

echo "[Orchestrator]: All nodes ready. Checking cluster status..."
sleep 5

# Check if cluster already exists
CLUSTER_EXISTS=$(mysqlsh --uri root:${ROOT_PASS}@127.0.0.1:3306 --js -e "try { var c = dba.getCluster(); print('EXISTS'); } catch(e) { print('NONE'); }" 2>/dev/null | grep -o "EXISTS\|NONE" || echo "NONE")

if [ "$CLUSTER_EXISTS" = "EXISTS" ]; then
    echo "[Orchestrator]: Cluster already exists. Checking status..."
    
    mysqlsh --uri root:${ROOT_PASS}@127.0.0.1:3306 --js <<'EOF'
    try {
        var cluster = dba.getCluster();
        var status = cluster.status();
        print('\n=== Current Cluster Status ===\n');
        print(JSON.stringify(status, null, 2));
        
        // Check if cluster is OK or needs reboot
        if (status.defaultReplicaSet.status === 'OK') {
            print('\nCluster is healthy!\n');
        } else {
            print('\nAttempting to reboot cluster from complete outage...\n');
            cluster = dba.rebootClusterFromCompleteOutage();
            print('\nCluster rebooted successfully!\n');
        }
    } catch (error) {
        print('ERROR: ' + error.message + '\n');
    }
EOF
    
else
    echo "[Orchestrator]: Creating new cluster..."
    
    mysqlsh --uri root:${ROOT_PASS}@127.0.0.1:3306 --js <<'EOF'
    try {
        print('Creating cluster...\n');
        
        var cluster = dba.createCluster('my_cluster', {
            interactive: false,
            force: true,
            gtidSetIsComplete: true
        });

        print('Adding pmdb...\n');
        cluster.addInstance('root@pmdb:3306', {
            password: 'admin',
            interactive: false,
            recoveryMethod: 'incremental'
        });

        print('Adding pmdb-mgr...\n');
        cluster.addInstance('root@pmdb-mgr:3306', {
            password: 'admin',
            interactive: false,
            recoveryMethod: 'incremental'
        });

        print('Creating router user...\n');
        cluster.setupRouterAccount('mysql_router', {
            password: 'router_pass',
            interactive: false
        });

        print('\n=== Cluster Status ===\n');
        print(JSON.stringify(cluster.status(), null, 2));
        print('\n=== Setup Complete! ===\n');
        
    } catch (error) {
        print('ERROR: ' + error.message + '\n');
        throw error;
    }
EOF
fi

# Grant additional privileges to mysql_router user for bootstrap
echo "[Orchestrator]: Granting privileges to mysql_router..."
mysql -h127.0.0.1 -uroot -p"${ROOT_PASS}" <<EOF
GRANT CREATE USER ON *.* TO 'mysql_router'@'%';
GRANT RELOAD ON *.* TO 'mysql_router'@'%';
GRANT SELECT, RELOAD, PROCESS ON *.* TO 'mysql_router'@'%' WITH GRANT OPTION;
GRANT SELECT, EXECUTE ON mysql_innodb_cluster_metadata.* TO 'mysql_router'@'%' WITH GRANT OPTION;
GRANT INSERT, UPDATE, DELETE ON mysql_innodb_cluster_metadata.routers TO 'mysql_router'@'%';
GRANT INSERT, UPDATE, DELETE ON mysql_innodb_cluster_metadata.v2_routers TO 'mysql_router'@'%';
GRANT SELECT ON performance_schema.* TO 'mysql_router'@'%';
GRANT SELECT ON sys.* TO 'mysql_router'@'%';
GRANT SELECT ON performance_schema.* TO 'mysql_router'@'%';
GRANT SELECT ON sys.* TO 'mysql_router'@'%';
FLUSH PRIVILEGES;
EOF

echo "[Orchestrator]: Complete!"
