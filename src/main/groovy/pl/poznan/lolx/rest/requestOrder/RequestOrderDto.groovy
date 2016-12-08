package pl.poznan.lolx.rest.requestOrder

import com.fasterxml.jackson.annotation.JsonFormat
import groovy.transform.ToString
import org.hibernate.validator.constraints.NotEmpty
import pl.poznan.lolx.domain.requestOrder.Status

@ToString
class RequestOrderDto {
    @NotEmpty
    String id
    @NotEmpty
    String anounceId
    @NotEmpty
    String authorId
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    Date creationDate
    Status status
}
