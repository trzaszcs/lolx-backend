package pl.poznan.lolx.rest.worker

import pl.poznan.lolx.rest.add.LocationDto

class WorkerDto extends BaseWorkerDto {
    String id
    String name
    String photoUrl
    LocationDto location
}
