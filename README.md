# dynamodb-spring-boot-starter

Starter de Spring Boot con la configuración de DynamoDB y la entidad base `MainTable` que comparten `client-microservice` y `merchant-microservice`.

Se creó para no tener el mismo código duplicado en los dos microservicios.

## Qué incluye

- Los beans `DynamoDbClient` y `DynamoDbEnhancedClient`, configurados a partir de las propiedades `aws.dynamodb.*`.
- Una comprobación al arrancar: si DynamoDB no responde o la tabla no existe, la aplicación no arranca.
- La clase abstracta `MainTable` (`com.wipay.dynamodb.entity`), de la que extienden todas las entidades.

## Instalación

El starter no está publicado en ningún repositorio remoto. Hay que instalarlo en el repositorio Maven local antes de compilar los microservicios:

```
./mvnw clean install
```

Después se añade como dependencia en el `pom.xml` del microservicio:

```xml
<dependency>
    <groupId>com.wipay</groupId>
    <artifactId>wipay-dynamodb-spring-boot-starter</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

Cada vez que se modifique el starter hay que volver a ejecutar `install`; si no, los microservicios seguirán usando la versión anterior.

## Propiedades

| Propiedad | Obligatoria | Descripción |
|---|---|---|
| `aws.dynamodb.endpoint` | Sí | URL de DynamoDB, por ejemplo `http://localhost:8000` |
| `aws.dynamodb.region` | Sí | Región de AWS, por ejemplo `eu-west-1` |
| `aws.dynamodb.table-name` | Sí | Nombre de la tabla (`MainTable`) |
| `aws.dynamodb.access-key` | Sí | Access key (en local vale cualquier valor) |
| `aws.dynamodb.secret-key` | Sí | Secret key (en local vale cualquier valor) |
| `aws.dynamodb.connection-check-enabled` | No | Activa la comprobación al arrancar. Por defecto `true` |

Ejemplo para usarlo con la [infraestructura local](https://github.com/tareas-practicas-wipay/infraestructure):

```properties
aws.dynamodb.endpoint=http://localhost:8000
aws.dynamodb.region=eu-west-1
aws.dynamodb.table-name=MainTable
aws.dynamodb.access-key=local
aws.dynamodb.secret-key=local
```

## Sustituir la configuración

Todos los beans usan `@ConditionalOnMissingBean`. Si un microservicio necesita otro `DynamoDbClient` (por ejemplo, con otras credenciales), basta con declarar su propio `@Bean` y el del starter deja de crearse.

## Requisitos

- Java 21
- Spring Boot 4.1.1 (la misma versión que los microservicios)
