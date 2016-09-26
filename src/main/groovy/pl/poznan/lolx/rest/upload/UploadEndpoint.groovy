package pl.poznan.lolx.rest.upload

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.FileSystemResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import pl.poznan.lolx.domain.upload.UploadHandler
import pl.poznan.lolx.domain.upload.UploadResult

@RestController
@Slf4j
class UploadEndpoint {

    def SUPPORTED_EXTENSIONS = ["jpg", "jpeg", "bmp", "png", "gif"]

    @Autowired
    UploadHandler uploadHandler

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    ResponseEntity upload(@RequestParam("file") MultipartFile file) {
        log.info("uploaded file {}", file.originalFilename)
        String ext = getExtension(file.originalFilename)
        if (!ext || !SUPPORTED_EXTENSIONS.contains(ext)) {
            return ResponseEntity.badRequest().build()
        }
        ResponseEntity.ok(new UploadResult(fileName: uploadHandler.save(ext, file.bytes)))
    }

    //TODO: will be removed
    @RequestMapping(value = "/upload/{fileName:.+}", method = RequestMethod.GET)
    @ResponseBody
    FileSystemResource get(@PathVariable("fileName") String fileName) {
        log.info("get file {}", fileName)
        return uploadHandler.getFile(fileName).map {
            new FileSystemResource(it)
        }.orElseThrow {
            throw new FileNotFoundException()
        }
    }

    @ExceptionHandler([FileNotFoundException.class])
    public ResponseEntity fileNotFound() {
        return ResponseEntity.notFound().build()
    }

    def getExtension(filename) {
        def parts = filename.split("\\.")
        parts[parts.size() - 1].toLowerCase()
    }

}
