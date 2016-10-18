package pl.poznan.lolx.rest.upload

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import groovyx.net.http.RESTClient
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.ByteArrayBody
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import pl.poznan.lolx.AppConfig
import pl.poznan.lolx.rest.IntTest

class UploadImageIntTest extends IntTest{


    @Test
    void "should upload file"() {
        // given
        def http = new HTTPBuilder("http://localhost:${serverPort}/upload")
        def entity =
                MultipartEntityBuilder
                        .create()
                        .addPart("file", new ByteArrayBody(fileToUpload(), "img.jpg"))
                        .setContentType(org.apache.http.entity.ContentType.MULTIPART_FORM_DATA)
                        .build()
        // when
        def response = http.request(Method.POST) { req ->
            req.setEntity(entity)
        }
        // then
        assert response.fileName
        try {
            response = new HTTPBuilder("http://localhost:${serverPort}/upload/${response.fileName}").request(Method.GET, org.apache.http.entity.ContentType.APPLICATION_OCTET_STREAM) { req ->
            }
            assert response
        }catch(Exception ex){
            println ex
        }
    }

    def fileToUpload() {
        getClass().getClassLoader().getResourceAsStream("img.png").bytes
    }
}
