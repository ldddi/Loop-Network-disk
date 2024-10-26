package com.looppan.looppan.service.impl.sharefile;

import com.looppan.looppan.service.sharefile.GetAvatarByteService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class GetAvatarByteServiceImpl implements GetAvatarByteService {
    @Override
    public ResponseEntity<FileSystemResource> getAvatar(String filePath) throws IOException {

        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            return ResponseEntity.ok().body(null);
        }

        String contentType = Files.probeContentType(path);
        FileSystemResource resource = new FileSystemResource(path.toFile());


        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
    }
}
