## Пример 1:

> [[_оглавление_]](../README.md/#25-исключения)

> [[**2.5.2 Обработка исключений**]](/conspect/2.md/#252-обработка-исключений)

- контроллер:

```java
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import Animals.entity.PetAvatar;
import Animals.service.PetAvatarServiceImpl;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@Log4j
@RequestMapping(path = "/pet-avatar")
public class PetAvatarController {
    private final PetAvatarServiceImpl petAvatarService;

    public PetAvatarController(PetAvatarServiceImpl petAvatarService) {
        this.petAvatarService = petAvatarService;
    }

    @GetMapping(path = "/{id}")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        PetAvatar petAvatar = petAvatarService.findAvatar(id);
        Path avatarPath = Path.of(petAvatar.getFilePath());
        try (
                InputStream is = Files.newInputStream(avatarPath);
                OutputStream os = response.getOutputStream()
        ) {
            response.setContentType(petAvatar.getMediaType());
            response.setContentLength((int) petAvatar.getFileSize());
            is.transferTo(os);
        }
    }

    @GetMapping(path = "{id}/preview")
    public ResponseEntity<byte[]> downloadAvatarPreview(@PathVariable Long id) {
        PetAvatar petAvatar = petAvatarService.findAvatar(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(petAvatar.getMediaType()));
        headers.setContentLength(petAvatar.getData().length);
        return ResponseEntity.ok().headers(headers).body(petAvatar.getData());
    }

    @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadAvatar(@RequestParam @PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() >= 1024 * 500) {
            return ResponseEntity.status(400).body("File is too big!");
        }
        try {
            petAvatarService.uploadAvatar(id, avatar);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
```

- сервис:

```java
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import Animals.entity.Pet;
import Animals.entity.PetAvatar;
import Animals.repository.PetAvatarRepository;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Log4j
@Transactional
public class PetAvatarServiceImpl implements PetAvatarService {
    Logger log = LoggerFactory.getLogger(PetAvatarServiceImpl.class);
    @Value("${pet.avatar.dir.path}")
    private String petAvatarsDir;
    private final PetServiceImpl petService;
    private final PetAvatarRepository petAvatarRepository;

    public PetAvatarServiceImpl(PetServiceImpl petService, PetAvatarRepository petAvatarRepository) {
        this.petService = petService;
        this.petAvatarRepository = petAvatarRepository;
    }

    @Override
    @Cacheable("avatar")
    public PetAvatar findAvatar(Long id) {
        log.info("Searching avatar by id " + id);
        PetAvatar answer = petAvatarRepository.findById(id).get();
        log.debug("Getting answer " + answer);
        return answer;
    }

    @Override
    @CachePut(value = "avatar", key = "#avatar.id")
    public void uploadAvatar(Long id, MultipartFile file) throws IOException {
        log.info("Trying to upload avatar for student with id " + id);
        Pet pet = petService.getById(id);
        Path filePath = Path.of(petAvatarsDir, id + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = file.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        PetAvatar petAvatar = getPetAvatar(id);
        petAvatar.setPet(pet);
        petAvatar.setFilePath(filePath.toString());
        petAvatar.setFileSize(file.getSize());
        petAvatar.setMediaType(file.getContentType());
        petAvatar.setData(generateImagePreview(filePath));
        petAvatarRepository.save(petAvatar);
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    public PetAvatar getPetAvatar(Long id) {
        return petAvatarRepository.findByPetId(id).orElse(new PetAvatar());
    }

    public byte[] generateImagePreview(Path filePath) throws IOException {
        try (
                InputStream is = Files.newInputStream(filePath);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ) {
            BufferedImage image = ImageIO.read(bis);
            int height = 0;
            int width = 0;
            BufferedImage preview = null;
            if (image.getHeight() > image.getWidth()) {
                height = image.getHeight() / (image.getWidth() / 100);
                preview = new BufferedImage(100, height, image.getType());
            } else {
                width = image.getWidth() / (image.getHeight() / 100);
                preview = new BufferedImage(width, 100, image.getType());
            }
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();
            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }
}
```

- модель:

```java
import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "avatar")
public class PetAvatar {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String filePath;
    private long fileSize;
    private String mediaType;
    @Lob
    private byte[] data;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    public PetAvatar() {
    }

    public PetAvatar(Long id, String filePath, long fileSize, String mediaType, byte[] data, Pet pet) {
        this.id = id;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.data = data;
        this.pet = pet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetAvatar petAvatar = (PetAvatar) o;
        return fileSize == petAvatar.fileSize && Objects.equals(id, petAvatar.id) && Objects.equals(filePath, petAvatar.filePath) && Objects.equals(pet, petAvatar.pet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, filePath, fileSize, pet);
    }

    @Override
    public String toString() {
        return "PetAvatar{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", mediaType='" + mediaType + '\'' +
                ", data=" + Arrays.toString(data) +
                ", pet=" + pet +
                '}';
    }
}
```

- репозиторий:

```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Animals.entity.PetAvatar;

import java.util.Optional;

@Repository
public interface PetAvatarRepository extends JpaRepository<PetAvatar, Long> {
    Optional<PetAvatar> findByPetId(Long petId);
}
```

## Пример 2:

> [[_оглавление_]](../README.md/#25-исключения)

> [[**2.5.2 Обработка исключений**]](/conspect/2.md/#252-обработка-исключений)

- контроллер:

```java
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping(path = "/{id}/avatar")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.findAvatar(id);
        Path avatarPath = Path.of(avatar.getFilePath());
        try (
                InputStream is = Files.newInputStream(avatarPath);
                OutputStream os = response.getOutputStream()
        ) {
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
    }

    @GetMapping(path = "{id}/avatar/preview")
    public ResponseEntity<byte[]> downloadAvatarPreview(@PathVariable Long id) {
        Avatar avatar = avatarService.findAvatar(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        return ResponseEntity.ok().headers(headers).body(avatar.getData());
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() >= 1024 * 500) {
            return ResponseEntity.status(400).body("File is too big!");
        }
        avatarService.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/avatars")
    public ResponseEntity<List<Avatar>> getAvatars(@RequestParam int pageNum, @RequestParam int pageSize) {
        if (pageNum <= 0 || pageSize <= 0) {
            return ResponseEntity.status(400).build();
        }
        return ResponseEntity.ok().body(avatarService.getAvatars(pageNum, pageSize));
    }
}
```

- сервис:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarServiceImpl implements AvatarService {
    @Value("${student.avatar.dir.path}")
    private String avatarsDir;
    private final StudentService studentService;
    private final AvatarRepository avatarRepository;
    private final Logger logger = LoggerFactory.getLogger(AvatarServiceImpl.class);

    public AvatarServiceImpl(StudentService studentService, AvatarRepository avatarRepository) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
    }

    @Override
    public Avatar findAvatar(Long id) {
        logger.info("Searching avatar by id {}", id);
        Avatar answer = avatarRepository.findById(id).get();
        logger.debug("Getting answer {}", answer);
        return answer;
    }

    @Override
    public void uploadAvatar(Long id, MultipartFile file) throws IOException {
        logger.info("Trying to upload avatar for student with id {}", id);
        Student student = studentService.findStudent(id);
        Path filePath = Path.of(avatarsDir, id + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = file.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = getAvatar(id);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(generateImagePreview(filePath));
        avatarRepository.save(avatar);
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    private Avatar getAvatar(Long id) {
        return avatarRepository.findByStudentId(id).orElse(new Avatar());
    }

    private byte[] generateImagePreview(Path filePath) throws IOException {
        try (
                InputStream is = Files.newInputStream(filePath);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ) {
            BufferedImage image = ImageIO.read(bis);
            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();
            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }

    @Override
    public List<Avatar> getAvatars(int pageNum, int pageSize) {
        logger.info("Getting pageable list of avatars with page number {} and page size {}", pageNum, pageSize);
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
        List<Avatar> answer = avatarRepository.findAll(pageRequest).getContent();
        logger.debug("Getting answer {}", answer);
        return answer;
    }
}
```

- модель:

```java
import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "avatar")
public class Avatar {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String filePath;
    private long fileSize;
    private String mediaType;
    @Lob
    private byte[] data;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    public Avatar(Long id, String filePath, long fileSize, String mediaType, byte[] data) {
        this.id = id;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.data = data;
    }

    public Avatar() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avatar avatar = (Avatar) o;
        return Objects.equals(id, avatar.id);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", mediaType='" + mediaType + '\'' +
                ", data=" + Arrays.toString(data) +
                ", student=" + student +
                '}';
    }
}
```

- репозиторий:

```java
import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Avatar;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Optional<Avatar> findByStudentId(Long studentId);
}
```

## Пример 3:

> [[_оглавление_]](../README.md/#25-исключения)

> [[**2.5.2 Обработка исключений**]](/conspect/2.md/#252-обработка-исключений)

- контроллер:

```java

@CrossOrigin(value = "http://localhost:3000")
@RestController
public class ImageController {
    private final ImageServiceImpl imageService;
    private static final Logger log = Logger.getLogger(ImageController.class);

    public ImageController(ImageServiceImpl imageService) {
        this.imageService = imageService;
    }

    @Operation(
            tags = "Изображения",
            summary = "Получение изображения пользователя или объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    )
            }
    )
    @GetMapping(path = "/{imageName}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public ResponseEntity<byte[]> downloadImage(@Parameter(description = "Ссылка на изображение в файловой системе", example = "user_1.png") @PathVariable String imageName, HttpServletResponse response) {
        try {
            return imageService.getImage(imageName, response);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).build();
        }
    }
}
```

- сервис:

```java

@Service
public class ImageServiceImpl implements ImageService {
    @Value("${user.image.dir.path}")
    private String userImageDir;
    @Value("${ad.image.dir.path}")
    private String addsImageDir;
    private static final Logger log = Logger.getLogger(ImageServiceImpl.class);

    @Override
    public ResponseEntity<byte[]> getImage(String imageName, HttpServletResponse response) throws IOException {
        Path imagePath = Path.of(userImageDir, imageName);
        if (!Files.isExecutable(imagePath)) {
            imagePath = Path.of(addsImageDir, imageName);
        }
        if (!Files.isExecutable(imagePath)) {
            throw new IOException("File doesn't exist");
        }
        Tika tika = new Tika();
        String mimeType = tika.detect(imagePath);
        MediaType mediaType = MediaType.parseMediaType(mimeType);
        byte[] imageInBytes = new byte[(int) Files.size(imagePath)];
        try (
                InputStream is = Files.newInputStream(imagePath)
        ) {
            IOUtils.readFully(is, imageInBytes);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setContentLength(imageInBytes.length);
        headers.setContentDispositionFormData(imageName, imageName);
        return ResponseEntity.ok().headers(headers).body(imageInBytes);
    }
}
```

- модель:

```java

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    private String image;
    private String password;

    public User() {
    }

    public User(Integer id, String email, String firstName, String lastName, String phone, Role role, String image, String password) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
        this.image = image;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(phone, user.phone) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName, phone, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                ", image='" + image + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
```

```java

@Entity
@Table(name = "ad")
public class Ad {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer pk;
    @ManyToOne(fetch = FetchType.EAGER)
    private User author;
    private String image;
    private int price;
    private String title;
    private String description;

    public Ad() {
    }

    public Ad(Integer pk, User author, String image, int price, String title, String description) {
        this.pk = pk;
        this.author = author;
        this.image = image;
        this.price = price;
        this.title = title;
        this.description = description;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    @JsonBackReference
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ad ad = (Ad) o;
        return price == ad.price && Objects.equals(pk, ad.pk) && Objects.equals(author, ad.author) && Objects.equals(image, ad.image) && Objects.equals(title, ad.title) && Objects.equals(description, ad.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pk, author, image, price, title, description);
    }

    @Override
    public String toString() {
        return "Ad{" +
                "pk=" + pk +
                ", author=" + author +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
```

- DTO:

```java
public class UserDTO {
    @Schema(example = "1", description = "id пользователя")
    private int id;
    @Schema(example = "user@test.com", description = "логин автора объявления")
    private String email;
    @Schema(example = "Иван", description = "имя пользователя", minLength = 2, maxLength = 16)
    private String firstName;
    @Schema(example = "Иванов", description = "фамилия пользователя", minLength = 2, maxLength = 16)
    private String lastName;
    @Schema(example = "+79998887766", description = "телефон пользователя", pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;
    @Schema(description = "роль пользователя")
    private Role role;
    @Schema(example = "/user_1.png", description = "ссылка на картинку объявления")
    private String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
```

```java
public class AdDTO {
    @Schema(example = "1", description = "id автора объявления")
    private int author;
    @Schema(example = "/user_1_ad_1.png", description = "ссылка на картинку объявления")
    private String image;
    @Schema(example = "1", description = "id объявления")
    private int pk;
    @Schema(example = "1000", description = "цена объявления")
    private int price;
    @Schema(example = "Новое объявление", description = "заголовок объявления")
    private String title;

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
```

- репозиторий:

```java

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
```

```java

@Repository
public interface AdRepository extends JpaRepository<Ad, Integer> {
    List<Ad> findByAuthor(User user);

    Optional<Ad> findByPk(Integer pk);
}
```

- маппер:

```java

@Mapper(componentModel = "spring")
public interface UserMapper {
    default UserDTO userToUserDto(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        if (user.getId() != null) {
            userDTO.setId(user.getId());
        }
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhone(user.getPhone());
        userDTO.setRole(user.getRole());
        userDTO.setImage("/" + user.getImage());
        return userDTO;
    }

    User userDtoToUser(UserDTO user);

    User updateUserDtoToUser(UpdateUserDTO updateUser);

    UpdateUserDTO userToUpdateUserDto(User user);

    @Mapping(target = "email", source = "register.username")
    User registerDtoToUser(RegisterDTO register);

    @Mapping(target = "firstName", source = "extendedAd.authorFirstName")
    @Mapping(target = "lastName", source = "extendedAd.authorLastName")
    @Mapping(target = "image", ignore = true)
    User extendedAdToUser(ExtendedAdDTO extendedAd);

    @Mapping(target = "username", source = "user.email")
    LoginDTO userToLoginDto(User user);

    @Mapping(target = "email", source = "loginDTO.username")
    User loginDtoToUser(LoginDTO loginDTO);
}

```

```java

@Mapper(componentModel = "spring")
public interface AdMapper {
    default AdDTO adToAdDto(Ad ad) {
        AdDTO adDto = new AdDTO();
        adDto.setAuthor(ad.getAuthor().getId());
        adDto.setImage("/" + ad.getImage());
        adDto.setPk(ad.getPk());
        adDto.setPrice(ad.getPrice());
        adDto.setTitle(ad.getTitle());
        return adDto;
    }

    default AdsDTO adsListToAdsDto(List<Ad> adsList) {
        AdsDTO ads = new AdsDTO();
        ads.setCount(adsList.size());
        List<AdDTO> adsDtoList = new ArrayList<>();
        for (Ad ad : adsList) {
            AdDTO adDto = adToAdDto(ad);
            adsDtoList.add(adDto);
        }
        ads.setResults(adsDtoList);
        return ads;
    }

    Ad createOrUpdateAdDtoToAd(CreateOrUpdateAdDTO createOrUpdateAd);

    CreateOrUpdateAdDTO adToCreateOrUpdateDto(Ad ad);

    default Ad extendedAdDtoToAd(ExtendedAdDTO extendedAd, User author) {
        Ad ad = new Ad(extendedAd.getPk(), author, extendedAd.getImage(), extendedAd.getPrice(), extendedAd.getTitle(), extendedAd.getDescription());
        return ad;
    }

    default ExtendedAdDTO adToExtendedAd(Ad ad, User author) {
        ExtendedAdDTO extendedAd = new ExtendedAdDTO();
        extendedAd.setPk(ad.getPk());
        extendedAd.setAuthorFirstName(author.getFirstName());
        extendedAd.setAuthorLastName(author.getLastName());
        extendedAd.setDescription(ad.getDescription());
        extendedAd.setEmail(author.getEmail());
        extendedAd.setImage("/" + ad.getImage());
        extendedAd.setPhone(author.getPhone());
        extendedAd.setPrice(ad.getPrice());
        extendedAd.setTitle(ad.getTitle());
        return extendedAd;
    }
}
```

- тест:

```java

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class HomeworkApplicationTest {
    @Value("${user.image.dir.path}")
    private String userImageDir;
    @Value("${ad.image.dir.path}")
    private String adsImageDir;
    @Value("${source.image.dir.path}")
    private String sourceImageDir;
    private AdDTO AD_1_DTO = new AdDTO();
    private AdDTO AD_2_DTO = new AdDTO();
    private AdDTO AD_3_DTO = new AdDTO();
    private AdsDTO ADS_DTO = new AdsDTO();
    private AdsDTO ADS_USER_DTO = new AdsDTO();
    private AdsDTO ADS_ADMIN_DTO = new AdsDTO();
    private CommentDTO COMMENT_1_DTO = new CommentDTO();
    private CommentDTO COMMENT_2_DTO = new CommentDTO();
    private CommentDTO COMMENT_3_DTO = new CommentDTO();
    private CommentsDTO COMMENTS_DTO = new CommentsDTO();
    private CreateOrUpdateAdDTO CREATE_OR_UPDATE_AD_1_DTO = new CreateOrUpdateAdDTO();
    private CreateOrUpdateAdDTO CREATE_OR_UPDATE_AD_2_DTO = new CreateOrUpdateAdDTO();
    private CreateOrUpdateAdDTO CREATE_OR_UPDATE_AD_3_DTO = new CreateOrUpdateAdDTO();
    private CreateOrUpdateCommentDTO CREATE_OR_UPDATE_COMMENT_1_DTO = new CreateOrUpdateCommentDTO();
    private CreateOrUpdateCommentDTO CREATE_OR_UPDATE_COMMENT_2_DTO = new CreateOrUpdateCommentDTO();
    private CreateOrUpdateCommentDTO CREATE_OR_UPDATE_COMMENT_3_DTO = new CreateOrUpdateCommentDTO();
    private ExtendedAdDTO EXTENDED_AD_1_DTO = new ExtendedAdDTO();
    private ExtendedAdDTO EXTENDED_AD_2_DTO = new ExtendedAdDTO();
    private ExtendedAdDTO EXTENDED_AD_3_DTO = new ExtendedAdDTO();
    private LoginDTO LOGIN_USER_DTO = new LoginDTO();
    private LoginDTO LOGIN_ADMIN_DTO = new LoginDTO();
    private LoginDTO LOGIN_ANOTHER_USER_DTO = new LoginDTO();
    private NewPasswordDTO NEW_PASSWORD_USER_DTO = new NewPasswordDTO();
    private NewPasswordDTO NEW_PASSWORD_ANOTHER_USER_DTO = new NewPasswordDTO();
    private NewPasswordDTO NEW_PASSWORD_ADMIN_DTO = new NewPasswordDTO();
    private RegisterDTO REGISTER_USER_DTO = new RegisterDTO();
    private RegisterDTO REGISTER_ADMIN_DTO = new RegisterDTO();
    private RegisterDTO REGISTER_ANOTHER_USER_DTO = new RegisterDTO();
    private UpdateUserDTO UPDATE_USER_DTO = new UpdateUserDTO();
    private UpdateUserDTO UPDATE_ADMIN_DTO = new UpdateUserDTO();
    private UserDTO USER_DTO = new UserDTO();
    private UserDTO ADMIN_DTO = new UserDTO();
    @Autowired
    MockMvc mockMvc;
    @Autowired
    WebApplicationContext context;
    @MockBean
    PasswordEncoderConfig passwordEncoderConfig;
    @MockBean
    DaoAuthenticationProvider daoAuthenticationProvider;
    @MockBean
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    SecurityFilterChainConfig securityFilterChainConfig;
    @MockBean
    AdRepository adRepository;
    @MockBean
    CommentRepository commentRepository;
    @MockBean
    UserRepository userRepository;
    @Autowired
    AdMapper adMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    UserMapper userMapper;
    @SpyBean
    AdsServiceImpl adsService;
    @SpyBean
    AuthenticationServiceImpl authenticationService;
    @SpyBean
    ImageServiceImpl imageService;
    @SpyBean
    UsersServiceImpl usersService;
    @Autowired
    AdsController adsController;
    @Autowired
    AuthenticationController authenticationController;
    @Autowired
    ImageController imageController;
    @Autowired
    UsersController usersController;
    @MockBean
    Clock clock;
    @MockBean
    ClockConfig clockConfig;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        AD_1_DTO.setAuthor(AD_1.getAuthor().getId());
        AD_1_DTO.setImage("/" + AD_1.getImage());
        AD_1_DTO.setPk(AD_1.getPk());
        AD_1_DTO.setPrice(AD_1.getPrice());
        AD_1_DTO.setTitle(AD_1.getTitle());
        AD_2_DTO.setAuthor(AD_2.getAuthor().getId());
        AD_2_DTO.setImage("/" + AD_2.getImage());
        AD_2_DTO.setPk(AD_2.getPk());
        AD_2_DTO.setPrice(AD_2.getPrice());
        AD_2_DTO.setTitle(AD_2.getTitle());
        AD_3_DTO.setAuthor(AD_3.getAuthor().getId());
        AD_3_DTO.setImage("/" + AD_3.getImage());
        AD_3_DTO.setPk(AD_3.getPk());
        AD_3_DTO.setPrice(AD_3.getPrice());
        AD_3_DTO.setTitle(AD_3.getTitle());
        ADS_DTO.setCount(3);
        ADS_DTO.setResults(List.of(AD_1_DTO, AD_2_DTO, AD_3_DTO));
        ADS_USER_DTO.setCount(2);
        ADS_USER_DTO.setResults(List.of(AD_1_DTO, AD_2_DTO));
        ADS_ADMIN_DTO.setCount(1);
        ADS_ADMIN_DTO.setResults(List.of(AD_3_DTO));
        COMMENT_1_DTO.setAuthor(COMMENT_1.getAuthor().getId());
        COMMENT_1_DTO.setAuthorImage("/" + COMMENT_1.getAuthorImage());
        COMMENT_1_DTO.setAuthorFirstName(COMMENT_1.getAuthorFirstName());
        COMMENT_1_DTO.setCreatedAt(COMMENT_1.getCreatedAt());
        COMMENT_1_DTO.setPk(COMMENT_1.getPk());
        COMMENT_1_DTO.setText(COMMENT_1.getText());
        COMMENT_2_DTO.setAuthor(COMMENT_2.getAuthor().getId());
        COMMENT_2_DTO.setAuthorImage("/" + COMMENT_2.getAuthorImage());
        COMMENT_2_DTO.setAuthorFirstName(COMMENT_2.getAuthorFirstName());
        COMMENT_2_DTO.setCreatedAt(COMMENT_2.getCreatedAt());
        COMMENT_2_DTO.setPk(COMMENT_2.getPk());
        COMMENT_2_DTO.setText(COMMENT_2.getText());
        COMMENT_3_DTO.setAuthor(COMMENT_3.getAuthor().getId());
        COMMENT_3_DTO.setAuthorImage("/" + COMMENT_3.getAuthorImage());
        COMMENT_3_DTO.setAuthorFirstName(COMMENT_3.getAuthorFirstName());
        COMMENT_3_DTO.setCreatedAt(COMMENT_3.getCreatedAt());
        COMMENT_3_DTO.setPk(COMMENT_3.getPk());
        COMMENT_3_DTO.setText(COMMENT_3.getText());
        COMMENTS_DTO.setCount(3);
        COMMENTS_DTO.setResults(List.of(COMMENT_1_DTO, COMMENT_2_DTO, COMMENT_3_DTO));
        CREATE_OR_UPDATE_AD_1_DTO.setTitle(AD_1.getTitle());
        CREATE_OR_UPDATE_AD_1_DTO.setPrice(AD_1.getPrice());
        CREATE_OR_UPDATE_AD_1_DTO.setDescription(AD_1.getDescription());
        CREATE_OR_UPDATE_AD_2_DTO.setTitle(AD_2.getTitle());
        CREATE_OR_UPDATE_AD_2_DTO.setPrice(AD_2.getPrice());
        CREATE_OR_UPDATE_AD_2_DTO.setDescription(AD_2.getDescription());
        CREATE_OR_UPDATE_AD_3_DTO.setTitle(AD_3.getTitle());
        CREATE_OR_UPDATE_AD_3_DTO.setPrice(AD_3.getPrice());
        CREATE_OR_UPDATE_AD_3_DTO.setDescription(AD_3.getDescription());
        CREATE_OR_UPDATE_COMMENT_1_DTO.setText(COMMENT_1.getText());
        CREATE_OR_UPDATE_COMMENT_2_DTO.setText(COMMENT_2.getText());
        CREATE_OR_UPDATE_COMMENT_3_DTO.setText(COMMENT_3.getText());
        EXTENDED_AD_1_DTO.setPk(AD_1.getPk());
        EXTENDED_AD_1_DTO.setAuthorFirstName(AD_1.getAuthor().getFirstName());
        EXTENDED_AD_1_DTO.setAuthorLastName(AD_1.getAuthor().getLastName());
        EXTENDED_AD_1_DTO.setDescription(AD_1.getDescription());
        EXTENDED_AD_1_DTO.setEmail(AD_1.getAuthor().getEmail());
        EXTENDED_AD_1_DTO.setImage("/" + AD_1.getImage());
        EXTENDED_AD_1_DTO.setPhone(AD_1.getAuthor().getPhone());
        EXTENDED_AD_1_DTO.setPrice(AD_1.getPrice());
        EXTENDED_AD_1_DTO.setTitle(AD_1.getTitle());
        EXTENDED_AD_2_DTO.setPk(AD_2.getPk());
        EXTENDED_AD_2_DTO.setAuthorFirstName(AD_2.getAuthor().getFirstName());
        EXTENDED_AD_2_DTO.setAuthorLastName(AD_2.getAuthor().getLastName());
        EXTENDED_AD_2_DTO.setDescription(AD_2.getDescription());
        EXTENDED_AD_2_DTO.setEmail(AD_2.getAuthor().getEmail());
        EXTENDED_AD_2_DTO.setImage("/" + AD_2.getImage());
        EXTENDED_AD_2_DTO.setPhone(AD_2.getAuthor().getPhone());
        EXTENDED_AD_2_DTO.setPrice(AD_2.getPrice());
        EXTENDED_AD_2_DTO.setTitle(AD_2.getTitle());
        EXTENDED_AD_3_DTO.setPk(AD_3.getPk());
        EXTENDED_AD_3_DTO.setAuthorFirstName(AD_3.getAuthor().getFirstName());
        EXTENDED_AD_3_DTO.setAuthorLastName(AD_3.getAuthor().getLastName());
        EXTENDED_AD_3_DTO.setDescription(AD_3.getDescription());
        EXTENDED_AD_3_DTO.setEmail(AD_3.getAuthor().getEmail());
        EXTENDED_AD_3_DTO.setImage("/" + AD_3.getImage());
        EXTENDED_AD_3_DTO.setPhone(AD_3.getAuthor().getPhone());
        EXTENDED_AD_3_DTO.setPrice(AD_3.getPrice());
        EXTENDED_AD_3_DTO.setTitle(AD_3.getTitle());
        LOGIN_USER_DTO.setUsername(USER.getEmail());
        LOGIN_USER_DTO.setPassword(USER.getPassword());
        LOGIN_ADMIN_DTO.setUsername(ADMIN.getEmail());
        LOGIN_ADMIN_DTO.setPassword(ADMIN.getPassword());
        LOGIN_ANOTHER_USER_DTO.setUsername(ANOTHER_USER.getEmail());
        LOGIN_ANOTHER_USER_DTO.setPassword(ANOTHER_USER.getPassword());
        NEW_PASSWORD_USER_DTO.setCurrentPassword(USER.getPassword());
        NEW_PASSWORD_USER_DTO.setNewPassword(ADMIN.getPassword());
        NEW_PASSWORD_ADMIN_DTO.setCurrentPassword(ADMIN.getPassword());
        NEW_PASSWORD_ADMIN_DTO.setNewPassword(USER.getPassword());
        NEW_PASSWORD_ANOTHER_USER_DTO.setCurrentPassword(ANOTHER_USER.getPassword());
        NEW_PASSWORD_ANOTHER_USER_DTO.setNewPassword(ADMIN.getPassword());
        REGISTER_USER_DTO.setUsername(USER.getEmail());
        REGISTER_USER_DTO.setPassword(USER.getPassword());
        REGISTER_USER_DTO.setFirstName(USER.getFirstName());
        REGISTER_USER_DTO.setLastName(USER.getLastName());
        REGISTER_USER_DTO.setPhone(USER.getPhone());
        REGISTER_USER_DTO.setRole(USER.getRole());
        REGISTER_ADMIN_DTO.setUsername(ADMIN.getEmail());
        REGISTER_ADMIN_DTO.setPassword(ADMIN.getPassword());
        REGISTER_ADMIN_DTO.setFirstName(ADMIN.getFirstName());
        REGISTER_ADMIN_DTO.setLastName(ADMIN.getLastName());
        REGISTER_ADMIN_DTO.setPhone(ADMIN.getPhone());
        REGISTER_ADMIN_DTO.setRole(ADMIN.getRole());
        REGISTER_ANOTHER_USER_DTO.setUsername(ANOTHER_USER.getEmail());
        REGISTER_ANOTHER_USER_DTO.setPassword(ANOTHER_USER.getPassword());
        REGISTER_ANOTHER_USER_DTO.setFirstName(ANOTHER_USER.getFirstName());
        REGISTER_ANOTHER_USER_DTO.setLastName(ANOTHER_USER.getLastName());
        REGISTER_ANOTHER_USER_DTO.setPhone(ANOTHER_USER.getPhone());
        REGISTER_ANOTHER_USER_DTO.setRole(ANOTHER_USER.getRole());
        UPDATE_USER_DTO.setFirstName(USER.getFirstName());
        UPDATE_USER_DTO.setLastName(USER.getLastName());
        UPDATE_USER_DTO.setPhone(USER.getPhone());
        UPDATE_ADMIN_DTO.setFirstName(ADMIN.getFirstName());
        UPDATE_ADMIN_DTO.setLastName(ADMIN.getLastName());
        UPDATE_ADMIN_DTO.setPhone(ADMIN.getPhone());
        USER_DTO.setId(USER.getId());
        USER_DTO.setEmail(USER.getEmail());
        USER_DTO.setFirstName(USER.getFirstName());
        USER_DTO.setLastName(USER.getLastName());
        USER_DTO.setPhone(USER.getPhone());
        USER_DTO.setRole(USER.getRole());
        USER_DTO.setImage("/" + USER.getImage());
        ADMIN_DTO.setId(ADMIN.getId());
        ADMIN_DTO.setEmail(ADMIN.getEmail());
        ADMIN_DTO.setFirstName(ADMIN.getFirstName());
        ADMIN_DTO.setLastName(ADMIN.getLastName());
        ADMIN_DTO.setPhone(ADMIN.getPhone());
        ADMIN_DTO.setRole(ADMIN.getRole());
        ADMIN_DTO.setImage("/" + ADMIN.getImage());
        lenient().when(clockConfig.clock()).thenReturn(clock);
        lenient().when(clock.millis()).thenReturn(111111L);
        lenient().when(userRepository.findByEmail(USER.getEmail())).thenReturn(Optional.of(USER));
        lenient().when(userRepository.findByEmail(ADMIN.getEmail())).thenReturn(Optional.of(ADMIN));
        lenient().when(userRepository.save(USER)).thenReturn(USER);
        lenient().when(userRepository.save(ADMIN)).thenReturn(ADMIN);
        lenient().when(userRepository.save(ANOTHER_USER_REGISTER)).thenReturn(ANOTHER_USER);
        lenient().when(passwordEncoderConfig.passwordEncoder()).thenReturn(passwordEncoder);
        lenient().when(passwordEncoder.encode(USER.getPassword())).thenReturn(USER.getPassword());
        lenient().when(passwordEncoder.encode(ADMIN.getPassword())).thenReturn(ADMIN.getPassword());
        lenient().when(passwordEncoder.encode(ANOTHER_USER.getPassword())).thenReturn(ANOTHER_USER.getPassword());
        lenient().when(passwordEncoder.matches(USER.getPassword(), USER.getPassword())).thenReturn(true);
        lenient().when(passwordEncoder.matches(ADMIN.getPassword(), ADMIN.getPassword())).thenReturn(true);
        lenient().when(adRepository.findAll()).thenReturn(ADS);
        lenient().when(adRepository.findByPk(AD_1.getPk())).thenReturn(Optional.of(AD_1));
        lenient().when(adRepository.findByPk(AD_2.getPk())).thenReturn(Optional.of(AD_2));
        lenient().when(adRepository.findByPk(AD_3.getPk())).thenReturn(Optional.of(AD_3));
        lenient().doNothing().when(adRepository).delete(any(Ad.class));
        lenient().when(adRepository.save(AD_1)).thenReturn(AD_1);
        lenient().when(adRepository.save(AD_2)).thenReturn(AD_2);
        lenient().when(adRepository.save(AD_3)).thenReturn(AD_3);
        lenient().when(adRepository.save(AD_1_CREATE)).thenReturn(AD_1);
        lenient().when(adRepository.save(AD_2_CREATE)).thenReturn(AD_2);
        lenient().when(adRepository.save(AD_3_CREATE)).thenReturn(AD_3);
        lenient().when(adRepository.findByAuthor(USER)).thenReturn(ADS_USER);
        lenient().when(adRepository.findByAuthor(ADMIN)).thenReturn(ADS_ADMIN);
        lenient().when(commentRepository.findByAd(AD_1)).thenReturn(COMMENTS);
        lenient().when(commentRepository.save(COMMENT_1_SAVE)).thenReturn(COMMENT_1);
        lenient().when(commentRepository.save(COMMENT_2_SAVE)).thenReturn(COMMENT_2);
        lenient().when(commentRepository.save(COMMENT_3_SAVE)).thenReturn(COMMENT_3);
        lenient().when(commentRepository.save(COMMENT_1)).thenReturn(COMMENT_1);
        lenient().when(commentRepository.save(COMMENT_2)).thenReturn(COMMENT_2);
        lenient().when(commentRepository.save(COMMENT_3)).thenReturn(COMMENT_3);
        lenient().when(commentRepository.findByPk(COMMENT_1.getPk())).thenReturn(Optional.of(COMMENT_1));
        lenient().when(commentRepository.findByPk(COMMENT_2.getPk())).thenReturn(Optional.of(COMMENT_2));
        lenient().when(commentRepository.findByPk(COMMENT_3.getPk())).thenReturn(Optional.of(COMMENT_3));
        lenient().doNothing().when(commentRepository).delete(any(Comment.class));
    }

    @Test
    void setPassword() throws Exception {
        mockMvc.perform(post("/users/set_password")
                                .with(user(USER.getEmail()).password(USER.getPassword()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(NEW_PASSWORD_USER_DTO)))
               .andExpect(status().isOk());
        mockMvc.perform(post("/users/set_password")
                                .with(user(ADMIN.getEmail()).password(ADMIN.getPassword()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(NEW_PASSWORD_ADMIN_DTO)))
               .andExpect(status().isOk());
        mockMvc.perform(post("/users/set_password")
                                .with(anonymous())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(NEW_PASSWORD_ANOTHER_USER_DTO)))
               .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/users/set_password")
                                .with(user(ADMIN.getEmail()).password(ADMIN.getPassword()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(NEW_PASSWORD_ANOTHER_USER_DTO)))
               .andExpect(status().isForbidden());
    }

    @Test
    void me() throws Exception {
        mockMvc.perform(get("/users/me")
                                .with(user("user@test.com").password("123"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(USER_DTO.getId()))
               .andExpect(jsonPath("$.email").value(USER_DTO.getEmail()))
               .andExpect(jsonPath("$.firstName").value(USER_DTO.getFirstName()))
               .andExpect(jsonPath("$.lastName").value(USER_DTO.getLastName()))
               .andExpect(jsonPath("$.role").value(String.valueOf(USER_DTO.getRole())))
               .andExpect(jsonPath("$.image").value(USER_DTO.getImage()));
        mockMvc.perform(get("/users/me")
                                .with(user("admin@test.com").password("321"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(ADMIN_DTO.getId()))
               .andExpect(jsonPath("$.email").value(ADMIN_DTO.getEmail()))
               .andExpect(jsonPath("$.firstName").value(ADMIN_DTO.getFirstName()))
               .andExpect(jsonPath("$.lastName").value(ADMIN_DTO.getLastName()))
               .andExpect(jsonPath("$.role").value(String.valueOf(ADMIN_DTO.getRole())))
               .andExpect(jsonPath("$.image").value(ADMIN_DTO.getImage()));
        mockMvc.perform(get("/users/me")
                                .with(anonymous())
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void meUpdate() throws Exception {
        mockMvc.perform(patch("/users/me")
                                .with(user("user@test.com").password("123"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(UPDATE_USER_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.firstName").value(UPDATE_USER_DTO.getFirstName()))
               .andExpect(jsonPath("$.lastName").value(UPDATE_USER_DTO.getLastName()))
               .andExpect(jsonPath("$.phone").value(UPDATE_USER_DTO.getPhone()));
        mockMvc.perform(patch("/users/me")
                                .with(user("admin@test.com").password("321"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(UPDATE_ADMIN_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.firstName").value(UPDATE_ADMIN_DTO.getFirstName()))
               .andExpect(jsonPath("$.lastName").value(UPDATE_ADMIN_DTO.getLastName()))
               .andExpect(jsonPath("$.phone").value(UPDATE_ADMIN_DTO.getPhone()));
        mockMvc.perform(patch("/users/me")
                                .with(anonymous())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(UPDATE_ADMIN_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void meImage() throws Exception {
        byte[] inputImage = Files.readAllBytes(Path.of(sourceImageDir, "user.jpg"));
        MockMultipartFile multipartFile = new MockMultipartFile("image", "user.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/users/me/image").file(multipartFile)
                                                                      .with(user("user@test.com").password("123")))
               .andExpect(status().isOk());
        byte[] result = Files.readAllBytes(Path.of(userImageDir, USER.getImage()));
        try (
                InputStream inputStream1 = new ByteArrayInputStream(result);
                InputStream inputStream2 = new FileInputStream(Path.of(userImageDir, USER.getImage()).toFile())) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "admin.jpg"));
        multipartFile = new MockMultipartFile("image", "admin.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/users/me/image").file(multipartFile)
                                                                      .with(user("admin@test.com").password("321")))
               .andExpect(status().isOk());
        result = Files.readAllBytes(Path.of(userImageDir, ADMIN.getImage()));
        try (
                InputStream inputStream1 = new ByteArrayInputStream(result);
                InputStream inputStream2 = new FileInputStream(Path.of(userImageDir, ADMIN.getImage()).toFile())) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "user.jpg"));
        multipartFile = new MockMultipartFile("image", "user.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/users/me/image").file(multipartFile)
                                                                      .with(anonymous()))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void login() throws Exception {
        mockMvc.perform(post("/login")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(LOGIN_USER_DTO)))
               .andExpect(status().isOk());
        mockMvc.perform(post("/login")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(LOGIN_ADMIN_DTO)))
               .andExpect(status().isOk());
        mockMvc.perform(post("/login")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(LOGIN_ANOTHER_USER_DTO)))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void register() throws Exception {
        mockMvc.perform(post("/register")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(REGISTER_USER_DTO)))
               .andExpect(status().isBadRequest());
        mockMvc.perform(post("/register")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(REGISTER_ADMIN_DTO)))
               .andExpect(status().isBadRequest());
        mockMvc.perform(post("/register")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(REGISTER_ANOTHER_USER_DTO)))
               .andExpect(status().isCreated());
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(get("/ads")
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.count").value(ADS_DTO.getCount()))
               .andExpect(jsonPath("$.results[0].author").value(AD_1_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[0].image").value(AD_1_DTO.getImage()))
               .andExpect(jsonPath("$.results[0].pk").value(AD_1_DTO.getPk()))
               .andExpect(jsonPath("$.results[0].price").value(AD_1_DTO.getPrice()))
               .andExpect(jsonPath("$.results[0].title").value(AD_1_DTO.getTitle()))
               .andExpect(jsonPath("$.results[1].author").value(AD_2_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[1].image").value(AD_2_DTO.getImage()))
               .andExpect(jsonPath("$.results[1].pk").value(AD_2_DTO.getPk()))
               .andExpect(jsonPath("$.results[1].price").value(AD_2_DTO.getPrice()))
               .andExpect(jsonPath("$.results[1].title").value(AD_2_DTO.getTitle()))
               .andExpect(jsonPath("$.results[2].author").value(AD_3_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[2].image").value(AD_3_DTO.getImage()))
               .andExpect(jsonPath("$.results[2].pk").value(AD_3_DTO.getPk()))
               .andExpect(jsonPath("$.results[2].price").value(AD_3_DTO.getPrice()))
               .andExpect(jsonPath("$.results[2].title").value(AD_3_DTO.getTitle()));
    }

    @Test
    void postAd() throws Exception {
        byte[] inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_1.jpg"));
        MockMultipartFile multipartFile = new MockMultipartFile("image", "ad_1.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        MockMultipartFile propertiesFile = new MockMultipartFile("properties", "properties", MediaType.APPLICATION_JSON_VALUE, new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_AD_1_DTO).getBytes());
        mockMvc.perform(multipart(HttpMethod.POST, "/ads").file(multipartFile).file(propertiesFile)
                                                          .with(user("user@test.com").password("123"))
                                                          .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                                          .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.author").value(AD_1_DTO.getAuthor()))
               .andExpect(jsonPath("$.image").value(AD_1_DTO.getImage()))
               .andExpect(jsonPath("$.pk").value(AD_1_DTO.getPk()))
               .andExpect(jsonPath("$.price").value(AD_1_DTO.getPrice()))
               .andExpect(jsonPath("$.title").value(AD_1_DTO.getTitle()));
        byte[] outputImage = Files.readAllBytes(Path.of(adsImageDir, AD_1.getImage()));
        try (
                InputStream inputStream1 = new ByteArrayInputStream(inputImage);
                InputStream inputStream2 = new ByteArrayInputStream(outputImage)
        ) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_2.jpg"));
        multipartFile = new MockMultipartFile("image", "ad_2.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        propertiesFile = new MockMultipartFile("properties", "properties", MediaType.APPLICATION_JSON_VALUE, new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_AD_2_DTO).getBytes());
        mockMvc.perform(multipart(HttpMethod.POST, "/ads").file(multipartFile).file(propertiesFile)
                                                          .with(user("user@test.com").password("123"))
                                                          .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                                          .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.author").value(AD_2_DTO.getAuthor()))
               .andExpect(jsonPath("$.image").value(AD_2_DTO.getImage()))
               .andExpect(jsonPath("$.pk").value(AD_2_DTO.getPk()))
               .andExpect(jsonPath("$.price").value(AD_2_DTO.getPrice()))
               .andExpect(jsonPath("$.title").value(AD_2_DTO.getTitle()));
        outputImage = Files.readAllBytes(Path.of(adsImageDir, AD_2.getImage()));
        try (
                InputStream inputStream1 = new ByteArrayInputStream(inputImage);
                InputStream inputStream2 = new ByteArrayInputStream(outputImage)
        ) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_3.jpg"));
        multipartFile = new MockMultipartFile("image", "ad_3.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        propertiesFile = new MockMultipartFile("properties", "properties", MediaType.APPLICATION_JSON_VALUE, new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_AD_3_DTO).getBytes());
        mockMvc.perform(multipart(HttpMethod.POST, "/ads").file(multipartFile).file(propertiesFile)
                                                          .with(user("admin@test.com").password("321"))
                                                          .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                                          .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.author").value(AD_3_DTO.getAuthor()))
               .andExpect(jsonPath("$.image").value(AD_3_DTO.getImage()))
               .andExpect(jsonPath("$.pk").value(AD_3_DTO.getPk()))
               .andExpect(jsonPath("$.price").value(AD_3_DTO.getPrice()))
               .andExpect(jsonPath("$.title").value(AD_3_DTO.getTitle()));
        outputImage = Files.readAllBytes(Path.of(adsImageDir, AD_3.getImage()));
        try (
                InputStream inputStream1 = new ByteArrayInputStream(inputImage);
                InputStream inputStream2 = new ByteArrayInputStream(outputImage)
        ) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_1.jpg"));
        multipartFile = new MockMultipartFile("image", "ad_1.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        propertiesFile = new MockMultipartFile("properties", "properties", MediaType.APPLICATION_JSON_VALUE, new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_AD_1_DTO).getBytes());
        mockMvc.perform(multipart(HttpMethod.POST, "/ads").file(multipartFile).file(propertiesFile)
                                                          .with(anonymous())
                                                          .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                                          .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void getAd() throws Exception {
        mockMvc.perform(get("/ads/1")
                                .with(user("user@test.com").password("123"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.pk").value(EXTENDED_AD_1_DTO.getPk()))
               .andExpect(jsonPath("$.authorFirstName").value(EXTENDED_AD_1_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.authorLastName").value(EXTENDED_AD_1_DTO.getAuthorLastName()))
               .andExpect(jsonPath("$.description").value(EXTENDED_AD_1_DTO.getDescription()))
               .andExpect(jsonPath("$.email").value(EXTENDED_AD_1_DTO.getEmail()))
               .andExpect(jsonPath("$.image").value(EXTENDED_AD_1_DTO.getImage()))
               .andExpect(jsonPath("$.phone").value(EXTENDED_AD_1_DTO.getPhone()))
               .andExpect(jsonPath("$.price").value(EXTENDED_AD_1_DTO.getPrice()))
               .andExpect(jsonPath("$.title").value(EXTENDED_AD_1_DTO.getTitle()));
        mockMvc.perform(get("/ads/1")
                                .with(user("admin@test.com").password("321"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.pk").value(EXTENDED_AD_1_DTO.getPk()))
               .andExpect(jsonPath("$.authorFirstName").value(EXTENDED_AD_1_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.authorLastName").value(EXTENDED_AD_1_DTO.getAuthorLastName()))
               .andExpect(jsonPath("$.description").value(EXTENDED_AD_1_DTO.getDescription()))
               .andExpect(jsonPath("$.email").value(EXTENDED_AD_1_DTO.getEmail()))
               .andExpect(jsonPath("$.image").value(EXTENDED_AD_1_DTO.getImage()))
               .andExpect(jsonPath("$.phone").value(EXTENDED_AD_1_DTO.getPhone()))
               .andExpect(jsonPath("$.price").value(EXTENDED_AD_1_DTO.getPrice()))
               .andExpect(jsonPath("$.title").value(EXTENDED_AD_1_DTO.getTitle()));
        mockMvc.perform(get("/ads/1")
                                .with(anonymous())
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/ads/4")
                                .with(user("user@test.com").password("123"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNotFound());
    }

    @Test
    void deleteAd() throws Exception {
        mockMvc.perform(delete("/ads/1")
                                .with(user("user@test.com").password("123")))
               .andExpect(status().isNoContent());
        mockMvc.perform(delete("/ads/1")
                                .with(user("admin@test.com").password("321")))
               .andExpect(status().isNoContent());
        mockMvc.perform(delete("/ads/1")
                                .with(anonymous()))
               .andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/ads/3")
                                .with(user("user@test.com").password("123")))
               .andExpect(status().isForbidden());
        mockMvc.perform(delete("/ads/4")
                                .with(user("user@test.com").password("123")))
               .andExpect(status().isNotFound());
    }

    @Test
    void updateAd() throws Exception {
        mockMvc.perform(patch("/ads/2")
                                .with(user("user@test.com").password("123"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_AD_2_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.author").value(AD_2_DTO.getAuthor()))
               .andExpect(jsonPath("$.image").value(AD_2_DTO.getImage()))
               .andExpect(jsonPath("$.pk").value(AD_2_DTO.getPk()))
               .andExpect(jsonPath("$.price").value(AD_2_DTO.getPrice()))
               .andExpect(jsonPath("$.title").value(AD_2_DTO.getTitle()));
        mockMvc.perform(patch("/ads/2")
                                .with(user("admin@test.com").password("321"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_AD_2_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.author").value(AD_2_DTO.getAuthor()))
               .andExpect(jsonPath("$.image").value(AD_2_DTO.getImage()))
               .andExpect(jsonPath("$.pk").value(AD_2_DTO.getPk()))
               .andExpect(jsonPath("$.price").value(AD_2_DTO.getPrice()))
               .andExpect(jsonPath("$.title").value(AD_2_DTO.getTitle()));
        mockMvc.perform(patch("/ads/2")
                                .with(anonymous())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_AD_2_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
        mockMvc.perform(patch("/ads/3")
                                .with(user("user@test.com").password("123"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_AD_2_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isForbidden());
        mockMvc.perform(patch("/ads/4")
                                .with(user("user@test.com").password("123"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_AD_2_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNotFound());
    }

    @Test
    void getMe() throws Exception {
        mockMvc.perform(get("/ads/me")
                                .with(user("user@test.com").password("123"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.count").value(ADS_USER_DTO.getCount()))
               .andExpect(jsonPath("$.results[0].author").value(AD_1_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[0].image").value(AD_1_DTO.getImage()))
               .andExpect(jsonPath("$.results[0].pk").value(AD_1_DTO.getPk()))
               .andExpect(jsonPath("$.results[0].price").value(AD_1_DTO.getPrice()))
               .andExpect(jsonPath("$.results[0].title").value(AD_1_DTO.getTitle()))
               .andExpect(jsonPath("$.results[1].author").value(AD_2_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[1].image").value(AD_2_DTO.getImage()))
               .andExpect(jsonPath("$.results[1].pk").value(AD_2_DTO.getPk()))
               .andExpect(jsonPath("$.results[1].price").value(AD_2_DTO.getPrice()))
               .andExpect(jsonPath("$.results[1].title").value(AD_2_DTO.getTitle()));
        mockMvc.perform(get("/ads/me")
                                .with(user("admin@test.com").password("321"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.count").value(ADS_ADMIN_DTO.getCount()))
               .andExpect(jsonPath("$.results[0].author").value(AD_3_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[0].image").value(AD_3_DTO.getImage()))
               .andExpect(jsonPath("$.results[0].pk").value(AD_3_DTO.getPk()))
               .andExpect(jsonPath("$.results[0].price").value(AD_3_DTO.getPrice()))
               .andExpect(jsonPath("$.results[0].title").value(AD_3_DTO.getTitle()));
        mockMvc.perform(get("/ads/me")
                                .with(anonymous())
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void patchAdImage() throws Exception {
        byte[] inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_1.jpg"));
        MockMultipartFile multipartFile = new MockMultipartFile("image", "ad_1.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/1/image").file(multipartFile)
                                                                   .with(user("user@test.com").password("123"))
                                                                   .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(content().string(String.valueOf(Path.of(adsImageDir, AD_1.getImage()))));
        byte[] outputImage = Files.readAllBytes(Path.of(adsImageDir, AD_1.getImage()));
        try (
                InputStream inputStream1 = new ByteArrayInputStream(inputImage);
                InputStream inputStream2 = new ByteArrayInputStream(outputImage)
        ) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_2.jpg"));
        multipartFile = new MockMultipartFile("image", "ad_2.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/2/image").file(multipartFile)
                                                                   .with(user("user@test.com").password("123"))
                                                                   .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(content().string(String.valueOf(Path.of(adsImageDir, AD_2.getImage()))));
        outputImage = Files.readAllBytes(Path.of(adsImageDir, AD_2.getImage()));
        try (
                InputStream inputStream1 = new ByteArrayInputStream(inputImage);
                InputStream inputStream2 = new ByteArrayInputStream(outputImage)
        ) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_3.jpg"));
        multipartFile = new MockMultipartFile("image", "ad_3.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/3/image").file(multipartFile)
                                                                   .with(user("user@test.com").password("123"))
                                                                   .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isForbidden());
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_1.jpg"));
        multipartFile = new MockMultipartFile("image", "ad_1.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/1/image").file(multipartFile)
                                                                   .with(user("admin@test.com").password("321"))
                                                                   .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(content().string(String.valueOf(Path.of(adsImageDir, AD_1.getImage()))));
        outputImage = Files.readAllBytes(Path.of(adsImageDir, AD_1.getImage()));
        try (
                InputStream inputStream1 = new ByteArrayInputStream(inputImage);
                InputStream inputStream2 = new ByteArrayInputStream(outputImage)
        ) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_2.jpg"));
        multipartFile = new MockMultipartFile("image", "ad_2.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/2/image").file(multipartFile)
                                                                   .with(user("admin@test.com").password("321"))
                                                                   .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(content().string(String.valueOf(Path.of(adsImageDir, AD_2.getImage()))));
        outputImage = Files.readAllBytes(Path.of(adsImageDir, AD_2.getImage()));
        try (
                InputStream inputStream1 = new ByteArrayInputStream(inputImage);
                InputStream inputStream2 = new ByteArrayInputStream(outputImage)
        ) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_3.jpg"));
        multipartFile = new MockMultipartFile("image", "ad_3.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/3/image").file(multipartFile)
                                                                   .with(user("admin@test.com").password("321"))
                                                                   .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(content().string(String.valueOf(Path.of(adsImageDir, AD_3.getImage()))));
        outputImage = Files.readAllBytes(Path.of(adsImageDir, AD_3.getImage()));
        try (
                InputStream inputStream1 = new ByteArrayInputStream(inputImage);
                InputStream inputStream2 = new ByteArrayInputStream(outputImage)
        ) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_3.jpg"));
        multipartFile = new MockMultipartFile("image", "ad_3.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/3/image").file(multipartFile)
                                                                   .with(anonymous())
                                                                   .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_1.jpg"));
        multipartFile = new MockMultipartFile("image", "ad_1.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/4/image").file(multipartFile)
                                                                   .with(user("user@test.com").password("123"))
                                                                   .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNotFound());
    }

    @Test
    void getAdComments() throws Exception {
        mockMvc.perform(get("/ads/1/comments")
                                .with(user("user@test.com").password("123"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.count").value(COMMENTS_DTO.getCount()))
               .andExpect(jsonPath("$.results[0].author").value(COMMENT_1_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[0].authorImage").value(COMMENT_1_DTO.getAuthorImage()))
               .andExpect(jsonPath("$.results[0].authorFirstName").value(COMMENT_1_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.results[0].createdAt").value(COMMENT_1_DTO.getCreatedAt()))
               .andExpect(jsonPath("$.results[0].pk").value(COMMENT_1_DTO.getPk()))
               .andExpect(jsonPath("$.results[0].text").value(COMMENT_1_DTO.getText()))
               .andExpect(jsonPath("$.results[1].author").value(COMMENT_2_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[1].authorImage").value(COMMENT_2_DTO.getAuthorImage()))
               .andExpect(jsonPath("$.results[1].authorFirstName").value(COMMENT_2_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.results[1].createdAt").value(COMMENT_2_DTO.getCreatedAt()))
               .andExpect(jsonPath("$.results[1].pk").value(COMMENT_2_DTO.getPk()))
               .andExpect(jsonPath("$.results[1].text").value(COMMENT_2_DTO.getText()))
               .andExpect(jsonPath("$.results[2].author").value(COMMENT_3_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[2].authorImage").value(COMMENT_3_DTO.getAuthorImage()))
               .andExpect(jsonPath("$.results[2].authorFirstName").value(COMMENT_3_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.results[2].createdAt").value(COMMENT_3_DTO.getCreatedAt()))
               .andExpect(jsonPath("$.results[2].pk").value(COMMENT_3_DTO.getPk()))
               .andExpect(jsonPath("$.results[2].text").value(COMMENT_3_DTO.getText()));
        mockMvc.perform(get("/ads/1/comments")
                                .with(user("admin@test.com").password("321"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.count").value(COMMENTS_DTO.getCount()))
               .andExpect(jsonPath("$.results[0].author").value(COMMENT_1_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[0].authorImage").value(COMMENT_1_DTO.getAuthorImage()))
               .andExpect(jsonPath("$.results[0].authorFirstName").value(COMMENT_1_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.results[0].createdAt").value(COMMENT_1_DTO.getCreatedAt()))
               .andExpect(jsonPath("$.results[0].pk").value(COMMENT_1_DTO.getPk()))
               .andExpect(jsonPath("$.results[0].text").value(COMMENT_1_DTO.getText()))
               .andExpect(jsonPath("$.results[1].author").value(COMMENT_2_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[1].authorImage").value(COMMENT_2_DTO.getAuthorImage()))
               .andExpect(jsonPath("$.results[1].authorFirstName").value(COMMENT_2_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.results[1].createdAt").value(COMMENT_2_DTO.getCreatedAt()))
               .andExpect(jsonPath("$.results[1].pk").value(COMMENT_2_DTO.getPk()))
               .andExpect(jsonPath("$.results[1].text").value(COMMENT_2_DTO.getText()))
               .andExpect(jsonPath("$.results[2].author").value(COMMENT_3_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[2].authorImage").value(COMMENT_3_DTO.getAuthorImage()))
               .andExpect(jsonPath("$.results[2].authorFirstName").value(COMMENT_3_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.results[2].createdAt").value(COMMENT_3_DTO.getCreatedAt()))
               .andExpect(jsonPath("$.results[2].pk").value(COMMENT_3_DTO.getPk()))
               .andExpect(jsonPath("$.results[2].text").value(COMMENT_3_DTO.getText()));
        mockMvc.perform(get("/ads/1/comments")
                                .with(anonymous())
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/ads/2/comments")
                                .with(user("user@test.com").password("123"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNotFound());
    }

    @Test
    void postAdComment() throws Exception {
        mockMvc.perform(post("/ads/1/comments")
                                .with(user("user@test.com").password("123"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_COMMENT_1_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.author").value(COMMENT_1_DTO.getAuthor()))
               .andExpect(jsonPath("$.authorImage").value(COMMENT_1_DTO.getAuthorImage()))
               .andExpect(jsonPath("$.authorFirstName").value(COMMENT_1_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.createdAt").value(COMMENT_1_DTO.getCreatedAt()))
               .andExpect(jsonPath("$.pk").value(COMMENT_1_DTO.getPk()))
               .andExpect(jsonPath("$.text").value(COMMENT_1_DTO.getText()));
        mockMvc.perform(post("/ads/1/comments")
                                .with(user("admin@test.com").password("321"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_COMMENT_3_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.author").value(COMMENT_3_DTO.getAuthor()))
               .andExpect(jsonPath("$.authorImage").value(COMMENT_3_DTO.getAuthorImage()))
               .andExpect(jsonPath("$.authorFirstName").value(COMMENT_3_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.createdAt").value(COMMENT_3_DTO.getCreatedAt()))
               .andExpect(jsonPath("$.pk").value(COMMENT_3_DTO.getPk()))
               .andExpect(jsonPath("$.text").value(COMMENT_3_DTO.getText()));
        mockMvc.perform(post("/ads/3/comments")
                                .with(anonymous())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_COMMENT_1_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/ads/4/comments")
                                .with(user("user@test.com").password("123"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_COMMENT_1_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNotFound());
    }

    @Test
    void deleteAdComment() throws Exception {
        mockMvc.perform(delete("/ads/1/comments/1")
                                .with(user("user@test.com").password("123"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk());
        mockMvc.perform(delete("/ads/1/comments/1")
                                .with(user("admin@test.com").password("321"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk());
        mockMvc.perform(delete("/ads/1/comments/1")
                                .with(anonymous())
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/ads/1/comments/3")
                                .with(user("user@test.com").password("123"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isForbidden());
        mockMvc.perform(delete("/ads/2/comments/1")
                                .with(user("user@test.com").password("123"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNotFound());
        mockMvc.perform(delete("/ads/1/comments/4")
                                .with(user("user@test.com").password("123"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNotFound());
    }

    @Test
    void updateAdComment() throws Exception {
        mockMvc.perform(patch("/ads/1/comments/1")
                                .with(user("user@test.com").password("123"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_COMMENT_1_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.author").value(COMMENT_1_DTO.getAuthor()))
               .andExpect(jsonPath("$.authorImage").value(COMMENT_1_DTO.getAuthorImage()))
               .andExpect(jsonPath("$.authorFirstName").value(COMMENT_1_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.createdAt").value(COMMENT_1_DTO.getCreatedAt()))
               .andExpect(jsonPath("$.pk").value(COMMENT_1_DTO.getPk()))
               .andExpect(jsonPath("$.text").value(COMMENT_1_DTO.getText()));
        mockMvc.perform(patch("/ads/1/comments/1")
                                .with(user("admin@test.com").password("321"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_COMMENT_1_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.author").value(COMMENT_1_DTO.getAuthor()))
               .andExpect(jsonPath("$.authorImage").value(COMMENT_1_DTO.getAuthorImage()))
               .andExpect(jsonPath("$.authorFirstName").value(COMMENT_1_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.createdAt").value(COMMENT_1_DTO.getCreatedAt()))
               .andExpect(jsonPath("$.pk").value(COMMENT_1_DTO.getPk()))
               .andExpect(jsonPath("$.text").value(COMMENT_1_DTO.getText()));
        mockMvc.perform(patch("/ads/1/comments/1")
                                .with(anonymous())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_COMMENT_1_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
        mockMvc.perform(patch("/ads/1/comments/3")
                                .with(user("user@test.com").password("123"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_COMMENT_3_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isForbidden());
        mockMvc.perform(patch("/ads/2/comments/1")
                                .with(user("user@test.com").password("123"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_COMMENT_1_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNotFound());
        mockMvc.perform(patch("/ads/1/comments/4")
                                .with(user("user@test.com").password("123"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_COMMENT_1_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNotFound());
    }

    @Test
    void downloadImage() throws Exception {
        byte[] result = mockMvc.perform(get("/" + USER.getImage())
                                                .with(user("user@test.com").password("123")))
                               .andExpect(status().isOk())
                               .andReturn().getResponse().getContentAsByteArray();
        try (InputStream inputStream1 = new ByteArrayInputStream(result);
             InputStream inputStream2 = new FileInputStream(Path.of(userImageDir, USER.getImage()).toFile())) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        result = mockMvc.perform(get("/" + ADMIN.getImage())
                                         .with(user("user@test.com").password("123")))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsByteArray();
        try (InputStream inputStream1 = new ByteArrayInputStream(result);
             InputStream inputStream2 = new FileInputStream(Path.of(userImageDir, ADMIN.getImage()).toFile())) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        result = mockMvc.perform(get("/" + USER.getImage())
                                         .with(user("admin@test.com").password("321")))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsByteArray();
        try (InputStream inputStream1 = new ByteArrayInputStream(result);
             InputStream inputStream2 = new FileInputStream(Path.of(userImageDir, USER.getImage()).toFile())) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        result = mockMvc.perform(get("/" + ADMIN.getImage())
                                         .with(user("admin@test.com").password("321")))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsByteArray();
        try (InputStream inputStream1 = new ByteArrayInputStream(result);
             InputStream inputStream2 = new FileInputStream(Path.of(userImageDir, ADMIN.getImage()).toFile())) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        result = mockMvc.perform(get("/" + AD_1.getImage())
                                         .with(user("user@test.com").password("123")))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsByteArray();
        try (InputStream inputStream1 = new ByteArrayInputStream(result);
             InputStream inputStream2 = new FileInputStream(Path.of(adsImageDir, AD_1.getImage()).toFile())) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        result = mockMvc.perform(get("/" + AD_2.getImage())
                                         .with(user("admin@test.com").password("321")))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsByteArray();
        try (InputStream inputStream1 = new ByteArrayInputStream(result);
             InputStream inputStream2 = new FileInputStream(Path.of(adsImageDir, AD_2.getImage()).toFile())) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        result = mockMvc.perform(get("/" + AD_3.getImage())
                                         .with(anonymous()))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsByteArray();
        try (InputStream inputStream1 = new ByteArrayInputStream(result);
             InputStream inputStream2 = new FileInputStream(Path.of(adsImageDir, AD_3.getImage()).toFile())) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        mockMvc.perform(get("/wrong_image_name.jpg")
                                .with(user("user@test.com").password("123")))
               .andExpect(status().isBadRequest());
        mockMvc.perform(get("/wrong_image_name.jpg")
                                .with(user("admin@test.com").password("321")))
               .andExpect(status().isBadRequest());
        mockMvc.perform(get("/wrong_image_name.jpg")
                                .with(anonymous()))
               .andExpect(status().isBadRequest());
    }
}
```