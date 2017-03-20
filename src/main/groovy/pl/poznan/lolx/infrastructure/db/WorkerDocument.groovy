package pl.poznan.lolx.infrastructure.db

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
class WorkerDocument {
    @Id
    String id
    @Indexed(unique = true)
    String userId
    String description
    String photoUrl
    LocationDocument location
    List<String> categoriesId

    Date creationDate
    Date updateDate

    static WorkerDocument create(String userId, String description, List<String> categoriesId, String photoUrl, LocationDocument locationDocument) {
        new WorkerDocument(
                userId: userId,
                description: description,
                categoriesId: categoriesId,
                photoUrl: photoUrl,
                location: locationDocument
        )
    }

    void update(String description, String photoUrl, LocationDocument locationDocument, List<String> categoriesId) {
        this.description = description
        this.photoUrl = photoUrl
        this.location = locationDocument
        this.categoriesId = categoriesId
        this.updateDate = new Date()
    }
}
