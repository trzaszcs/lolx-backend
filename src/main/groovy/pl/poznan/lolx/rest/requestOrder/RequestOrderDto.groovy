package pl.poznan.lolx.rest.requestOrder

import com.fasterxml.jackson.annotation.JsonFormat
import groovy.transform.ToString
import org.hibernate.validator.constraints.NotEmpty
import pl.poznan.lolx.domain.AnounceType
import pl.poznan.lolx.domain.requestOrder.Status

import javax.validation.constraints.NotNull

@ToString
class RequestOrderDto {
    @NotEmpty
    String id
    @NotEmpty
    String anounceId
    @NotEmpty
    String authorId
    @NotEmpty
    String anounceAuthorId
    @NotNull
    AnounceType anounceType
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    Date creationDate
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    Date statusUpdateDate
    Status status
    boolean seen
}
