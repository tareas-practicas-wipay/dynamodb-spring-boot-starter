package com.wipay.dynamodb.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.time.Instant;

@DynamoDbBean
@Getter
@Setter
@NoArgsConstructor
public abstract class MainTable {
    public static final String GINDEX2 = "gIndex2"; // los creo como constantes porque los voy a usar en varios sitios.
    public static final String GINDEX3 = "gIndex3";

    /*
     * las anotaciones van en el getter, pero Lombok genera los getters
     * -SOLUCIÓN-> se usa onMethod_: Significa "pon estas anotaciones encima del getPk() que vas a generar"
     *
     * el campo pk se guardaría así en minúsculas, DynamoDB rechazaría el item, pq la clave es PK
     * -SOLUCIÓN-> se usa @DynamoDbAttribute("PK") para que se guarde con el nombre correcto
     */
    @Getter(onMethod_ = {@DynamoDbPartitionKey, @DynamoDbAttribute("PK")})
    private String pk;

    @Getter(onMethod_ = {@DynamoDbSortKey, @DynamoDbSecondarySortKey(indexNames = {GINDEX2, GINDEX3}), @DynamoDbAttribute("SK")})
    private String sk;

    /*
     * Con gIndex2Pk pasa igual, pero aún peor (y difícil de detectar): DynamoDB no rechazaría el item al no ser una clave
     * de la tabla principal, pero no se podría consultar por gIndex2, pq no se guardaría con el nombre correcto.
     */
    @Getter(onMethod_ = {@DynamoDbSecondaryPartitionKey(indexNames = GINDEX2), @DynamoDbAttribute("gIndex2Pk")})
    private String gIndex2Pk;

    @Getter(onMethod_ = {@DynamoDbSecondaryPartitionKey(indexNames = GINDEX3), @DynamoDbAttribute("gIndex3Pk")})
    private String gIndex3Pk;

    private String id;
    private String status;
    private Instant createdDate;
}
