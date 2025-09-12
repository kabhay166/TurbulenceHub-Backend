package com.example.spring_example.controller;
import com.example.spring_example.entity.data.EulerData;
import com.example.spring_example.entity.data.HydroData;
import com.example.spring_example.entity.data.MhdData;
import com.example.spring_example.service.EulerDataService;
import com.example.spring_example.service.HydroDataService;
import com.example.spring_example.service.MhdDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/euler")
    public ResponseEntity<List<EulerData>> getEulerData() {
        List<EulerData> eulerDataList =  eulerDataService.getAll();
        return ResponseEntity.ok().body(eulerDataList);
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

    @GetMapping("/download/{model}/{id}")
    public ResponseEntity<Resource> downloadData(@PathVariable String model, @PathVariable Long id, @RequestHeader(value=HttpHeaders.RANGE, required = false) String rangeHeader) throws IOException {

        if(model.equals("Euler")) {
            Optional<EulerData> eulerData = eulerDataService.getById(id);
        }


        System.out.println("Inside download data function");
        File file = new File("E:\\Clair-Obscur-Exp-33-SteamRIP.com.rar");

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
