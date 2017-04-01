package pl.poznan.lolx.rest.worker

import org.hibernate.validator.constraints.NotEmpty

class BaseWorkerDto {
    @NotEmpty
    String userId
    @NotEmpty
    String description
    @NotEmpty
    List<String> categoryIds
}
