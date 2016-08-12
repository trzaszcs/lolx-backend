package pl.poznan.lolx.infrastructure.db

import com.github.fakemongo.Fongo
import com.mongodb.MongoClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories
class MongoConfig {

    @Bean
    Fongo fongo() {
        new Fongo("fongo");
    }

    @Bean
    MongoClient mongo() {
        fongo().mongo
    }

}
