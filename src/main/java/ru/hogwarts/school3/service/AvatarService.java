package ru.hogwarts.school3.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school3.model.Avatar;
import ru.hogwarts.school3.repository.AvatarRepository;
import ru.hogwarts.school3.repository.StudentRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;
    private final String pathToAvatars;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository, String pathToAvatars) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
        this.pathToAvatars = pathToAvatars;
    }

    public void save (Long studentId, MultipartFile multipartFile) throws IOException {
        //save to db
        Avatar avatar = avatarRepository.findByStudentId(studentId).orElse(new Avatar());
        avatar.setStudent(studentRepository.getReferenceById(studentId));
        avatar.setData(multipartFile.getBytes());
        avatar.setFileSize(multipartFile.getSize());
        avatar.setMediaType(multipartFile.getContentType());
        //save to disk
        Files.createDirectories(Path.of("avatars"));
        InputStream is = multipartFile.getInputStream();
        int lastIndexOf = multipartFile.getOriginalFilename().lastIndexOf('.');
        String extension = multipartFile.getOriginalFilename().substring(lastIndexOf);
        String filePath = pathToAvatars + "/" + studentId + "." + extension;
        FileOutputStream fos = new FileOutputStream(filePath);
        is.transferTo(fos);
        fos.close();
        avatar.setFilePath(filePath);
        avatarRepository.save(avatar);
    }
    public Avatar getAvatar (Long studentId) {
        return avatarRepository.findByStudentId(studentId).orElseThrow();
    }
}
