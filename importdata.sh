#!/bin/bash

#postgres01.pr.getaccess.no

set -e


if [[ "$OSTYPE" == "darwin"* ]]; then
        PSQL='/Applications/pgAdmin3.app/Contents/SharedSupport/psql'
        PG_DUMP='/Applications/pgAdmin3.app/Contents/SharedSupport/pg_dump'
        PG_RESTORE='/Applications/pgAdmin3.app/Contents/SharedSupport/pg_restore'
else
        PSQL='psql'
        PG_DUMP='pg_dump'
        PG_RESTORE='pg_restore'
fi
DEST_HOST='192.168.31.11'

if [ "$1" == "qa" ]; then
        DEST_HOST="postgres01.qa.getaccess.no"
else
       echo "Hint: use './import.sh qa' to import data from production to qa"
fi

echo "Database at $DEST_HOST will be updated"

echo "Dropping mobeventprocessor_old if exists..."

PGPASSWORD="maiNee2k" $PSQL -h "$DEST_HOST" --username "get-core" "get-core" -c \
"drop schema IF EXISTS mobeventprocessor_old CASCADE;"

echo "renaming mobeventprocessor to mobeventprocessor_old..."

PGPASSWORD="maiNee2k" $PSQL -h "$DEST_HOST" --username "get-core" -d "get-core" -c \
"ALTER SCHEMA mobeventprocessor RENAME TO mobeventprocessor_old;" || echo "If schema mobeventprocessor not found, not a problem :)"

echo "Copying data from production..."

PGPASSWORD="maiNee2k" $PG_DUMP -h postgres01.pr.getaccess.no --username "get-core" --format custom --schema "mobeventprocessor" --dbname "get-core" | \
$PG_RESTORE  -h "$DEST_HOST" --dbname "get-core" --username "get-core"

echo "Finished successful!"