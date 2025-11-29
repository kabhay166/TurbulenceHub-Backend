package com.example.spring_example.controller;
import com.example.spring_example.dto.mapper.DataUploadDtoMapper;
import com.example.spring_example.dto.request.DataUploadDto;
import com.example.spring_example.entity.AppUser;
import com.example.spring_example.entity.data.*;
import com.example.spring_example.service.UserService;
import com.example.spring_example.service.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("data")
@CrossOrigin(origins = "*")
public class DataController {

    @Autowired
    private EulerDataService eulerDataService;

    @Autowired
    private HydroDataService hydroDataService;

    @Autowired
    private MhdDataService mhdDataService;

    @Autowired
    private RbcDataService rbcDataService;

    @Autowired
    private MiscellaneousDataService miscellaneousDataService;

    @Autowired
    UserService userService;

    @GetMapping("/euler")
    public ResponseEntity<?> getEulerData() {
        System.out.println("Finding euler data.");
        try {
            List<EulerData> eulerDataList = eulerDataService.getAll();
            return ResponseEntity.ok().body(eulerDataList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new String("Error occurred: " + e.getMessage()));
        }
    }

    @GetMapping("/hydro")
    public ResponseEntity<List<HydroData>> getHydroData() {
        List<HydroData> hydroDataList =  hydroDataService.getAll();
        return ResponseEntity.ok().body(hydroDataList);
    }

    @GetMapping("/mhd")
    public ResponseEntity<List<MhdData>> getMhdData() {
        List<MhdData> mhdDataList =  mhdDataService.getAll();
        return ResponseEntity.ok().body(mhdDataList);
    }

    @GetMapping("/rbc")
    public ResponseEntity<?> getRbcData() {
        try {
            List<RbcData> rbcDataList =  rbcDataService.getAll();
            return ResponseEntity.ok().body(rbcDataList);
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred on the server.");
        }

    }

    @GetMapping("/miscellaneous")
    public ResponseEntity<?> getMiscellaneousData() {
        try {
            List<MiscellaneousData> miscellaneousDataList =  miscellaneousDataService.getAll();
            return ResponseEntity.ok().body(miscellaneousDataList);
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred on the server.");
        }

    }


    @PostMapping("/add-data")
    public ResponseEntity<String> uploadData(@RequestBody DataUploadDto dataUploadDto) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<AppUser> user = userService.findByUsername(username);

        if(user.isEmpty()) {
            return ResponseEntity.badRequest().body("User with " + username + " does not exist.");
        }

        String role = user.get().getRoles().get(0);
        if(!role.equalsIgnoreCase("admin")) {
            return ResponseEntity.badRequest().body("You do not have the permission to upload data.");
        }

        String kind = dataUploadDto.getKind();
        boolean success = false;
        if(kind.equalsIgnoreCase("euler")) {

            success = eulerDataService.addData(dataUploadDto);
        } else if(kind.equalsIgnoreCase("hydro")) {
            success =  hydroDataService.addData(dataUploadDto);
        } else if(kind.equalsIgnoreCase("mhd")) {
            success =  mhdDataService.addData(dataUploadDto);
        } else if(kind.equalsIgnoreCase("rbc")) {
            success =  rbcDataService.addData(dataUploadDto);
        } else if(kind.equalsIgnoreCase("miscellaneous")) {
            success = miscellaneousDataService.addData(dataUploadDto);
        }

        if(success) {
            return ResponseEntity.ok().body("Data added successfully.");
        } else {
            return ResponseEntity.badRequest().body("Error occurred while adding data");
        }

    }

    @GetMapping("/download/{model}/{id}")
    public ResponseEntity<Resource> downloadData(@PathVariable String model, @PathVariable Long id, @RequestHeader(value=HttpHeaders.RANGE, required = false) String rangeHeader) throws IOException {
        String downloadPath = "";
        if(model.equalsIgnoreCase ("Euler")) {
            Optional<EulerData> eulerData = eulerDataService.getById(id);
            if(eulerData.isPresent()) {
                downloadPath = eulerData.get().getDownloadPath();
            }
        } else if(model.equalsIgnoreCase("Hydro")) {
            Optional<HydroData> hydroData = hydroDataService.getById(id);
            if(hydroData.isPresent()) {
                downloadPath = hydroData.get().getDownloadPath();
            }
        } else if(model.equalsIgnoreCase("MHD")) {
            Optional<MhdData> mhdData = mhdDataService.getById(id);
            if(mhdData.isPresent()) {
                downloadPath = mhdData.get().getDownloadPath();
            }
        } else if(model.equalsIgnoreCase("RBC")) {
            Optional<RbcData> rbcData = rbcDataService.getById(id);
            if(rbcData.isPresent()) {
                downloadPath = rbcData.get().getDownloadPath();
            }
        }

        downloadPath += "/TurbulenceData.zip";

        File file = new File(downloadPath);

        if(!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        long fileLength = file.length();
        FileSystemResource resource = new FileSystemResource(file);


        if(rangeHeader == null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(file.length())
                    .body(resource);
        }

        String[] ranges = rangeHeader.replace("bytes=","").split("-");
        long start = Long.parseLong(ranges[0]);
        long end = (ranges.length > 1 && !ranges[1].isEmpty()) ? Long.parseLong(ranges[1]) : fileLength - 1;
        if(end >= fileLength) {
            end = fileLength - 1;
        }

        long contentLength = end - start + 1;
        InputStream inputStream = new FileInputStream(file);
        inputStream.skip(start);

        InputStreamResource partialResource = new InputStreamResource(new LimitedInputStream(inputStream,contentLength));

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + file.getName() + "\"")
                .header(HttpHeaders.ACCEPT_RANGES,"bytes")
                .header(HttpHeaders.CONTENT_RANGE,"bytes " + start + "-" + end + "/" + fileLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(contentLength)
                .body(partialResource);

    }

}

class LimitedInputStream extends FilterInputStream {
    private long remaining;

    protected LimitedInputStream(InputStream in, long limit) {
        super(in);
        this.remaining = limit;
    }

    @Override
    public int read() throws IOException {
        if (remaining <= 0) return -1;
        int result = super.read();
        if (result != -1) remaining--;
        return result;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (remaining <= 0) return -1;
        len = (int) Math.min(len, remaining);
        int result = super.read(b, off, len);
        if (result != -1) remaining -= result;
        return result;
    }
}
