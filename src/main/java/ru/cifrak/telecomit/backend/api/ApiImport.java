package ru.cifrak.telecomit.backend.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.cifrak.telecomit.backend.repository.RepositoryOperator;
import ru.cifrak.telecomit.backend.service.storage.StorageService;

@RestController
@RequestMapping("/api/import")
public class ApiImport {
    private final StorageService storageService;
    // Service serviceOfImoport;


    private RepositoryOperator repository;

    @Autowired
    public ApiImport(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/location")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
//    public String handleFileUpload(@RequestParam("file") MultipartFile file,
//                                   RedirectAttributes redirectAttributes) {

//        storageService.store(file);
//        redirectAttributes.addFlashAttribute("message",
//                "You successfully uploaded " + file.getOriginalFilename() + "!");

        // Locations
        // todo: fillup list of Location entitis
        // magic with Apache POI
        // List<Entity> entities =  servicePOI.getEntitiesFromDTO(List<DTO> items);
        // for ech new Location(); loc.setName(importedLoc.getName)
        // todo: save this list
        // serviceOfImport.pushImportedLocations(List<Location>locs)
        //
        // Tech.Caps
        // todo: fillup list of Texh.Caps entitis
        // magic with Apache POI
        // for ech new Location(); loc.setName(importedLoc.getName)
        // todo: save this list
        // serviceOfImport.pushTechCaps(List<Location>locs)

        return "Ku: " + file.getName();
    }


//    @PostMapping("/location")
//    public String item() {
//        return "'name': 'ok from import'";
//        ApachePOI = new


//
//        if (true){
//            return ResponseEntity.ok(item.get());
//        } else
//            return ResponseEntity.notFound().build();
//    }
//        return null;
//    }
}
