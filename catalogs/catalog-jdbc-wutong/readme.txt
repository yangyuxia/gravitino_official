gravitino REST API调用
-----------------------------------------------------------------------------------
1、创建metalake
curl -X POST -H "Accept: application/vnd.gravitino.v1+json" \
-H "Content-Type: application/json" \
-d '{"name":"metalake","comment":"Test metalake"}' http://localhost:8090/api/metalakes

2、创建catalog
curl -X POST -H "Accept: application/vnd.gravitino.v1+json" \
-H "Content-Type: application/json" -d '{
  "name": "catalog",
  "type": "RELATIONAL",
  "comment": "comment",
  "provider": "jdbc-wutong",
  "properties": {
    "jdbc-url": "jdbc:postgresql://localhost:5432/testdb",
    "jdbc-driver": "org.postgresql.Driver",
    "jdbc-database": "testdb",
    "jdbc-user": "postgres",
    "jdbc-password": "chenzhizhou98"
  }
}' http://localhost:8090/api/metalakes/metalake/catalogs

3、展示所有catalog
curl -X GET -H "Accept: application/vnd.gravitino.v1+json" \
-H "Content-Type: application/json" \
http://localhost:8090/api/metalakes/metalake/catalogs

4、加载catalog
curl -X GET -H "Accept: application/vnd.gravitino.v1+json" \
-H "Content-Type: application/json" http://localhost:8090/api/metalakes/metalake/catalogs/catalog


5、创建schema---postgresql不支持properties
curl -X POST -H "Accept: application/vnd.gravitino.v1+json" \
-H "Content-Type: application/json" -d '{
  "name": "schema",
  "comment": "comment"
}' http://localhost:8090/api/metalakes/metalake/catalogs/catalog/schemas

6、创建表table
curl -X POST -H "Accept: application/vnd.gravitino.v1+json" \
-H "Content-Type: application/json" -d '{
  "name": "example_table",
  "comment": "This is an example table",
  "columns": [
    {
      "name": "id",
      "type": "integer",
      "comment": "id column comment",
      "nullable": false,
      "autoIncrement": true
    },
    {
      "name": "name",
      "type": "varchar(500)",
      "comment": "name column comment",
      "nullable": true,
      "autoIncrement": false,
      "defaultValue": {
        "type": "literal",
        "dataType": "null",
        "value": "null"
      }
    },
    {
      "name": "age",
      "type": "integer",
      "comment": "age column comment",
      "nullable": false,
      "autoIncrement": false,
      "defaultValue": {
        "type": "literal",
        "dataType": "integer",
        "value": "-1"
      }
    },
    {
      "name": "dt",
      "type": "date",
      "comment": "dt column comment",
      "nullable": true
    }
  ],
  "indexes": [
    {
      "indexType": "primary_key",
      "name": "PRIMARY",
      "fieldNames": [["id"]]
    }
  ]
}' http://localhost:8090/api/metalakes/metalake/catalogs/catalog/schemas/schema/tables

7、显示所有table
curl -X GET -H "Accept: application/vnd.gravitino.v1+json" \
-H "Content-Type: application/json" \
http://localhost:8090/api/metalakes/metalake/catalogs/catalog/schemas/schema/tables

