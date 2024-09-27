## Пример 1:

> [[_оглавление_]](../README.md/#53-mockito)

> [[**5.3 Mockito**]](/conspect/5.md/#53-mockito)

- файл _application-test.properties_:

```properties
ad.image.dir.path=tests_images
user.image.dir.path=tests_images
source.image.dir.path=tests_source
```

- интеграционный тест:

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

- _unit_-тесты:

```java

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class AuthenticationServiceImplTest {
    private LoginDTO LOGIN_USER_DTO = new LoginDTO();
    private LoginDTO LOGIN_ANOTHER_USER_DTO = new LoginDTO();
    private LoginDTO LOGIN_ADMIN_DTO = new LoginDTO();
    private RegisterDTO REGISTER_USER_DTO = new RegisterDTO();
    private RegisterDTO REGISTER_ANOTHER_USER_DTO = new RegisterDTO();
    @Mock
    UserRepository userRepository;
    @Mock
    UserMapper userMapper;
    @Mock
    PasswordEncoderConfig encoderConfiguration;
    @Mock
    BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        LOGIN_USER_DTO.setUsername(USER.getEmail());
        LOGIN_USER_DTO.setPassword(USER.getPassword());
        LOGIN_ADMIN_DTO.setUsername(ADMIN.getEmail());
        LOGIN_ADMIN_DTO.setPassword(ADMIN.getPassword());
        LOGIN_ANOTHER_USER_DTO.setUsername(ANOTHER_USER.getEmail());
        LOGIN_ANOTHER_USER_DTO.setPassword(ANOTHER_USER.getPassword());
        REGISTER_USER_DTO.setUsername(USER.getEmail());
        REGISTER_USER_DTO.setPassword(USER.getPassword());
        REGISTER_USER_DTO.setFirstName(USER.getFirstName());
        REGISTER_USER_DTO.setLastName(USER.getLastName());
        REGISTER_USER_DTO.setPhone(USER.getPhone());
        REGISTER_USER_DTO.setRole(USER.getRole());
        REGISTER_ANOTHER_USER_DTO.setUsername(ANOTHER_USER.getEmail());
        REGISTER_ANOTHER_USER_DTO.setPassword(ANOTHER_USER.getPassword());
        REGISTER_ANOTHER_USER_DTO.setFirstName(ANOTHER_USER.getFirstName());
        REGISTER_ANOTHER_USER_DTO.setLastName(ANOTHER_USER.getLastName());
        REGISTER_ANOTHER_USER_DTO.setPhone(ANOTHER_USER.getPhone());
        REGISTER_ANOTHER_USER_DTO.setRole(ANOTHER_USER.getRole());
        lenient().when(userMapper.registerDtoToUser(REGISTER_USER_DTO)).thenReturn(USER);
        lenient().when(userMapper.registerDtoToUser(REGISTER_ANOTHER_USER_DTO)).thenReturn(ANOTHER_USER);
        lenient().when(encoderConfiguration.passwordEncoder()).thenReturn(passwordEncoder);
        lenient().when(passwordEncoder.encode(USER.getPassword())).thenReturn(USER.getPassword());
        lenient().when(passwordEncoder.encode(ADMIN.getPassword())).thenReturn(ADMIN.getPassword());
        lenient().when(passwordEncoder.encode(ANOTHER_USER.getPassword())).thenReturn(ANOTHER_USER.getPassword());
        lenient().when(userRepository.save(USER)).thenReturn(USER);
        lenient().when(userRepository.save(ADMIN)).thenReturn(ADMIN);
        lenient().when(userRepository.save(ANOTHER_USER)).thenReturn(ANOTHER_USER);
        lenient().when(userMapper.loginDtoToUser(LOGIN_USER_DTO)).thenReturn(USER);
        lenient().when(userMapper.loginDtoToUser(LOGIN_ADMIN_DTO)).thenReturn(ADMIN);
        lenient().when(userMapper.loginDtoToUser(LOGIN_ANOTHER_USER_DTO)).thenReturn(ANOTHER_USER);
        lenient().when(passwordEncoder.matches(LOGIN_USER_DTO.getPassword(), USER.getPassword())).thenReturn(true);
        lenient().when(passwordEncoder.matches(LOGIN_ADMIN_DTO.getPassword(), ADMIN.getPassword())).thenReturn(true);
        lenient().when(userRepository.findByEmail(USER.getEmail())).thenReturn(Optional.of(USER));
        lenient().when(userRepository.findByEmail(ADMIN.getEmail())).thenReturn(Optional.of(ADMIN));
    }

    @Test
    void loadUserByUsername() {
        assertNotNull(authenticationService.loadUserByUsername(USER.getEmail()));
        assertNotNull(authenticationService.loadUserByUsername(ADMIN.getEmail()));
        assertNull(authenticationService.loadUserByUsername(ANOTHER_USER.getEmail()));
    }

    @Test
    void login() {
        assertEquals(USER, authenticationService.login(LOGIN_USER_DTO));
        assertEquals(ADMIN, authenticationService.login(LOGIN_ADMIN_DTO));
        assertNull(authenticationService.login(LOGIN_ANOTHER_USER_DTO));
    }

    @Test
    void register() {
        assertNull(authenticationService.register(REGISTER_USER_DTO));
        assertEquals(ANOTHER_USER, authenticationService.register(REGISTER_ANOTHER_USER_DTO));
    }
}
```

```java

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class UsersServiceImplTest {
    private NewPasswordDTO NEW_PASSWORD_USER_DTO = new NewPasswordDTO();
    private NewPasswordDTO NEW_PASSWORD_ANOTHER_USER_DTO = new NewPasswordDTO();
    private UpdateUserDTO UPDATE_USER_DTO = new UpdateUserDTO();
    private UpdateUserDTO UPDATE_ANOTHER_USER_DTO = new UpdateUserDTO();
    private UserDTO USER_DTO = new UserDTO();
    @Mock
    PasswordEncoderConfig passwordEncoderConfig;
    @Mock
    BCryptPasswordEncoder passwordEncoder;
    @Mock
    UserRepository userRepository;
    @Mock
    UserMapper userMapper;
    @InjectMocks
    UsersServiceImpl usersService;

    @BeforeEach
    void setUp() {
        NEW_PASSWORD_USER_DTO.setCurrentPassword(USER.getPassword());
        NEW_PASSWORD_USER_DTO.setNewPassword(USER.getPassword());
        NEW_PASSWORD_ANOTHER_USER_DTO.setCurrentPassword(USER.getPassword());
        NEW_PASSWORD_ANOTHER_USER_DTO.setNewPassword(USER.getPassword());
        UPDATE_USER_DTO.setFirstName(USER.getFirstName());
        UPDATE_USER_DTO.setLastName(USER.getLastName());
        UPDATE_USER_DTO.setPhone(USER.getPhone());
        UPDATE_ANOTHER_USER_DTO.setFirstName(USER.getFirstName());
        UPDATE_ANOTHER_USER_DTO.setLastName(USER.getLastName());
        UPDATE_ANOTHER_USER_DTO.setPhone(USER.getPhone());
        USER_DTO.setId(USER.getId());
        USER_DTO.setEmail(USER.getEmail());
        USER_DTO.setFirstName(USER.getFirstName());
        USER_DTO.setLastName(USER.getLastName());
        USER_DTO.setPhone(USER.getPhone());
        USER_DTO.setRole(USER.getRole());
        USER_DTO.setImage(USER.getImage());
        lenient().when(userRepository.findByEmail(USER.getEmail())).thenReturn(Optional.of(USER));
        lenient().when(userRepository.findByEmail(ADMIN.getEmail())).thenReturn(Optional.of(ADMIN));
        lenient().when(passwordEncoderConfig.passwordEncoder()).thenReturn(passwordEncoder);
        lenient().when(passwordEncoder.encode(USER.getPassword())).thenReturn(USER.getPassword());
        lenient().when(passwordEncoder.matches(USER.getPassword(), NEW_PASSWORD_USER_DTO.getCurrentPassword())).thenReturn(true);
        lenient().when(userMapper.userToUserDto(USER)).thenReturn(USER_DTO);
        lenient().when(userMapper.updateUserDtoToUser(UPDATE_USER_DTO)).thenReturn(USER);
        lenient().when(userMapper.userToUpdateUserDto(USER)).thenReturn(UPDATE_USER_DTO);
    }

    @Test
    @WithMockUser(value = "user@test.com")
    void setPassword() {
        usersService.setPassword(NEW_PASSWORD_USER_DTO);
        verify(userRepository, times(1)).save(USER);
        usersService.setPassword(NEW_PASSWORD_ANOTHER_USER_DTO);
        verify(userRepository, times(0)).save(ANOTHER_USER);
    }

    @Test
    @WithMockUser(value = "user@test.com")
    void getUser() {
        assertEquals(USER_DTO, usersService.getUser());
    }

    @Test
    @WithAnonymousUser
    void getAnotherUser() {
        assertNull(usersService.getUser());
    }

    @Test
    @WithMockUser(value = "user@test.com")
    void updateUser() {
        assertEquals(UPDATE_USER_DTO, usersService.updateUser(UPDATE_USER_DTO));
    }

    @Test
    @WithAnonymousUser
    void updateAnotherUser() {
        assertNull(usersService.updateUser(UPDATE_ANOTHER_USER_DTO));
    }
}
```

```java

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class AdsServiceImplTest {
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
    @Mock
    AdRepository adRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    AdMapper adMapper;
    @Mock
    CommentMapper commentMapper;
    @Mock
    ClockConfig clockConfig;
    @Mock
    Clock clock;
    @InjectMocks
    AdsServiceImpl adsService;

    @BeforeEach
    void setUp() {
        AD_1_DTO.setAuthor(AD_1.getAuthor().getId());
        AD_1_DTO.setImage(AD_1.getImage());
        AD_1_DTO.setPk(AD_1.getPk());
        AD_1_DTO.setPrice(AD_1.getPrice());
        AD_1_DTO.setTitle(AD_1.getTitle());
        AD_2_DTO.setAuthor(AD_2.getAuthor().getId());
        AD_2_DTO.setImage(AD_2.getImage());
        AD_2_DTO.setPk(AD_2.getPk());
        AD_2_DTO.setPrice(AD_2.getPrice());
        AD_2_DTO.setTitle(AD_2.getTitle());
        AD_3_DTO.setAuthor(AD_3.getAuthor().getId());
        AD_3_DTO.setImage(AD_3.getImage());
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
        COMMENT_1_DTO.setAuthorImage(COMMENT_1.getAuthorImage());
        COMMENT_1_DTO.setAuthorFirstName(COMMENT_1.getAuthorFirstName());
        COMMENT_1_DTO.setCreatedAt(COMMENT_1.getCreatedAt());
        COMMENT_1_DTO.setPk(COMMENT_1.getPk());
        COMMENT_1_DTO.setText(COMMENT_1.getText());
        COMMENT_2_DTO.setAuthor(COMMENT_2.getAuthor().getId());
        COMMENT_2_DTO.setAuthorImage(COMMENT_2.getAuthorImage());
        COMMENT_2_DTO.setAuthorFirstName(COMMENT_2.getAuthorFirstName());
        COMMENT_2_DTO.setCreatedAt(COMMENT_2.getCreatedAt());
        COMMENT_2_DTO.setPk(COMMENT_2.getPk());
        COMMENT_2_DTO.setText(COMMENT_2.getText());
        COMMENT_3_DTO.setAuthor(COMMENT_3.getAuthor().getId());
        COMMENT_3_DTO.setAuthorImage(COMMENT_3.getAuthorImage());
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
        EXTENDED_AD_1_DTO.setImage(AD_1.getImage());
        EXTENDED_AD_1_DTO.setPhone(AD_1.getAuthor().getPhone());
        EXTENDED_AD_1_DTO.setPrice(AD_1.getPrice());
        EXTENDED_AD_1_DTO.setTitle(AD_1.getTitle());
        EXTENDED_AD_2_DTO.setPk(AD_2.getPk());
        EXTENDED_AD_2_DTO.setAuthorFirstName(AD_2.getAuthor().getFirstName());
        EXTENDED_AD_2_DTO.setAuthorLastName(AD_2.getAuthor().getLastName());
        EXTENDED_AD_2_DTO.setDescription(AD_2.getDescription());
        EXTENDED_AD_2_DTO.setEmail(AD_2.getAuthor().getEmail());
        EXTENDED_AD_2_DTO.setImage(AD_2.getImage());
        EXTENDED_AD_2_DTO.setPhone(AD_2.getAuthor().getPhone());
        EXTENDED_AD_2_DTO.setPrice(AD_2.getPrice());
        EXTENDED_AD_2_DTO.setTitle(AD_2.getTitle());
        EXTENDED_AD_3_DTO.setPk(AD_3.getPk());
        EXTENDED_AD_3_DTO.setAuthorFirstName(AD_3.getAuthor().getFirstName());
        EXTENDED_AD_3_DTO.setAuthorLastName(AD_3.getAuthor().getLastName());
        EXTENDED_AD_3_DTO.setDescription(AD_3.getDescription());
        EXTENDED_AD_3_DTO.setEmail(AD_3.getAuthor().getEmail());
        EXTENDED_AD_3_DTO.setImage(AD_3.getImage());
        EXTENDED_AD_3_DTO.setPhone(AD_3.getAuthor().getPhone());
        EXTENDED_AD_3_DTO.setPrice(AD_3.getPrice());
        EXTENDED_AD_3_DTO.setTitle(AD_3.getTitle());
        lenient().when(clockConfig.clock()).thenReturn(clock);
        lenient().when(clock.millis()).thenReturn(111111L);
        lenient().when(userRepository.findByEmail(USER.getEmail())).thenReturn(Optional.of(USER));
        lenient().when(userRepository.findByEmail(ADMIN.getEmail())).thenReturn(Optional.of(ADMIN));
        lenient().when(adRepository.findAll()).thenReturn(ADS);
        lenient().when(adMapper.adsListToAdsDto(ADS)).thenReturn(ADS_DTO);
        lenient().when(adRepository.findByPk(1)).thenReturn(Optional.of(AD_1));
        lenient().when(adRepository.findByPk(2)).thenReturn(Optional.of(AD_2));
        lenient().when(adRepository.findByPk(3)).thenReturn(Optional.of(AD_3));
        lenient().when(adMapper.adToExtendedAd(AD_1, AD_1.getAuthor())).thenReturn(EXTENDED_AD_1_DTO);
        lenient().when(adMapper.adToExtendedAd(AD_2, AD_2.getAuthor())).thenReturn(EXTENDED_AD_2_DTO);
        lenient().when(adMapper.adToExtendedAd(AD_3, AD_3.getAuthor())).thenReturn(EXTENDED_AD_3_DTO);
        lenient().doNothing().when(adRepository).delete(any(Ad.class));
        lenient().when(adMapper.createOrUpdateAdDtoToAd(CREATE_OR_UPDATE_AD_1_DTO)).thenReturn(AD_1);
        lenient().when(adMapper.createOrUpdateAdDtoToAd(CREATE_OR_UPDATE_AD_2_DTO)).thenReturn(AD_2);
        lenient().when(adMapper.createOrUpdateAdDtoToAd(CREATE_OR_UPDATE_AD_3_DTO)).thenReturn(AD_3);
        lenient().when(adRepository.save(AD_1)).thenReturn(AD_1);
        lenient().when(adRepository.save(AD_2)).thenReturn(AD_2);
        lenient().when(adRepository.save(AD_3)).thenReturn(AD_3);
        lenient().when(adMapper.adToAdDto(AD_1)).thenReturn(AD_1_DTO);
        lenient().when(adMapper.adToAdDto(AD_2)).thenReturn(AD_2_DTO);
        lenient().when(adMapper.adToAdDto(AD_3)).thenReturn(AD_3_DTO);
        lenient().when(adRepository.findByAuthor(USER)).thenReturn(ADS_USER);
        lenient().when(adRepository.findByAuthor(ADMIN)).thenReturn(ADS_ADMIN);
        lenient().when(adMapper.adsListToAdsDto(ADS_USER)).thenReturn(ADS_USER_DTO);
        lenient().when(adMapper.adsListToAdsDto(ADS_ADMIN)).thenReturn(ADS_ADMIN_DTO);
        lenient().when(commentRepository.findByAd(AD_1)).thenReturn(COMMENTS);
        lenient().when(commentMapper.commentListToCommentsDto(COMMENTS)).thenReturn(COMMENTS_DTO);
        lenient().when(commentRepository.save(COMMENT_1_SAVE)).thenReturn(COMMENT_1);
        lenient().when(commentRepository.save(COMMENT_2_SAVE)).thenReturn(COMMENT_2);
        lenient().when(commentRepository.save(COMMENT_3_SAVE)).thenReturn(COMMENT_3);
        lenient().when(commentRepository.save(COMMENT_1)).thenReturn(COMMENT_1);
        lenient().when(commentRepository.save(COMMENT_2)).thenReturn(COMMENT_2);
        lenient().when(commentRepository.save(COMMENT_3)).thenReturn(COMMENT_3);
        lenient().when(commentMapper.commentToCommentDto(COMMENT_1)).thenReturn(COMMENT_1_DTO);
        lenient().when(commentMapper.commentToCommentDto(COMMENT_2)).thenReturn(COMMENT_2_DTO);
        lenient().when(commentMapper.commentToCommentDto(COMMENT_3)).thenReturn(COMMENT_3_DTO);
        lenient().when(commentRepository.findByPk(1)).thenReturn(Optional.of(COMMENT_1));
        lenient().when(commentRepository.findByPk(2)).thenReturn(Optional.of(COMMENT_2));
        lenient().when(commentRepository.findByPk(3)).thenReturn(Optional.of(COMMENT_3));
        lenient().doNothing().when(commentRepository).delete(any(Comment.class));
    }

    @Test
    @WithMockUser(value = "user@test.com")
    void getAll() {
        assertEquals(ADS_DTO, adsService.getAll());
    }

    @Test
    @WithMockUser(value = "user@test.com")
    void addAd() {
    }

    @Test
    @WithMockUser(value = "user@test.com")
    void getAd() {
        assertEquals(EXTENDED_AD_1_DTO, adsService.getAd(1));
        assertEquals(EXTENDED_AD_2_DTO, adsService.getAd(2));
        assertEquals(EXTENDED_AD_3_DTO, adsService.getAd(3));
        assertNull(adsService.getAd(4));
    }

    @Test
    @WithMockUser(value = "user@test.com")
    void deleteAdByUser() {
        assertTrue(adsService.deleteAd(1));
        assertTrue(adsService.deleteAd(2));
        assertThrows(UsernameNotFoundException.class, () -> adsService.deleteAd(3));
        assertFalse(adsService.deleteAd(4));
    }

    @Test
    @WithMockUser(value = "admin@test.com")
    void deleteAdByAdmin() {
        assertTrue(adsService.deleteAd(1));
        assertTrue(adsService.deleteAd(2));
        assertTrue(adsService.deleteAd(3));
        assertFalse(adsService.deleteAd(4));
    }

    @Test
    @WithMockUser(value = "anotheruser@test.com")
    void deleteAdByAnotherUser() {
        assertFalse(adsService.deleteAd(1));
        assertFalse(adsService.deleteAd(2));
        assertFalse(adsService.deleteAd(3));
    }

    @Test
    @WithMockUser(value = "user@test.com")
    void updateAdWithUser() {
        assertEquals(AD_1_DTO, adsService.updateAd(1, CREATE_OR_UPDATE_AD_1_DTO));
        assertEquals(AD_2_DTO, adsService.updateAd(2, CREATE_OR_UPDATE_AD_2_DTO));
        assertThrows(UsernameNotFoundException.class, () -> adsService.updateAd(3, CREATE_OR_UPDATE_AD_3_DTO));
    }

    @Test
    @WithMockUser(value = "admin@test.com")
    void updateAdWithAdmin() {
        assertEquals(AD_1_DTO, adsService.updateAd(1, CREATE_OR_UPDATE_AD_1_DTO));
        assertEquals(AD_2_DTO, adsService.updateAd(2, CREATE_OR_UPDATE_AD_2_DTO));
        assertEquals(AD_3_DTO, adsService.updateAd(3, CREATE_OR_UPDATE_AD_3_DTO));
    }

    @Test
    @WithMockUser(value = "anotheruser@test.com")
    void updateAdWithAnotherUser() {
        assertNull(adsService.updateAd(1, CREATE_OR_UPDATE_AD_1_DTO));
        assertNull(adsService.updateAd(2, CREATE_OR_UPDATE_AD_2_DTO));
        assertNull(adsService.updateAd(3, CREATE_OR_UPDATE_AD_3_DTO));
    }

    @Test
    @WithMockUser(value = "user@test.com")
    void getAllMineWithUser() {
        assertEquals(ADS_USER_DTO, adsService.getAllMine());
    }

    @Test
    @WithMockUser(value = "admin@test.com")
    void getAllMineWithAdmin() {
        assertEquals(ADS_ADMIN_DTO, adsService.getAllMine());
    }

    @Test
    @WithMockUser(value = "anotheruser@test.com")
    void getAllMineWithAnotherUser() {
        assertNull(adsService.getAllMine());
    }

    @Test
    @WithMockUser(value = "user@test.com")
    void getAdComments() {
        assertEquals(COMMENTS_DTO, adsService.getAdComments(1));
    }

    @Test
    @WithMockUser(value = "user@test.com")
    void addComment() {
        assertEquals(COMMENT_1_DTO, adsService.addComment(1, CREATE_OR_UPDATE_COMMENT_1_DTO));
        assertEquals(COMMENT_2_DTO, adsService.addComment(1, CREATE_OR_UPDATE_COMMENT_2_DTO));
        assertNull(adsService.addComment(1, CREATE_OR_UPDATE_COMMENT_3_DTO));
    }

    @Test
    @WithMockUser(value = "user@test.com")
    void deleteCommentWithUser() {
        assertTrue(adsService.deleteComment(1, 1));
        assertTrue(adsService.deleteComment(1, 2));
        assertThrows(UsernameNotFoundException.class, () -> adsService.deleteComment(1, 3));
        assertFalse(adsService.deleteComment(1, 4));
        assertFalse(adsService.deleteComment(2, 1));
    }

    @Test
    @WithMockUser(value = "admin@test.com")
    void deleteCommentWithAdmin() {
        assertTrue(adsService.deleteComment(1, 1));
        assertTrue(adsService.deleteComment(1, 2));
        assertTrue(adsService.deleteComment(1, 3));
        assertFalse(adsService.deleteComment(1, 4));
        assertFalse(adsService.deleteComment(2, 1));
    }

    @Test
    @WithMockUser(value = "anotheruser@test.com")
    void deleteCommentWithAnotherUser() {
        assertFalse(adsService.deleteComment(1, 1));
        assertFalse(adsService.deleteComment(1, 2));
        assertFalse(adsService.deleteComment(1, 3));
        assertFalse(adsService.deleteComment(1, 4));
        assertFalse(adsService.deleteComment(2, 1));
    }

    @Test
    @WithMockUser(value = "user@test.com")
    void updateCommentWithUser() {
        assertEquals(COMMENT_1_DTO, adsService.updateComment(1, 1, CREATE_OR_UPDATE_COMMENT_1_DTO));
        assertEquals(COMMENT_2_DTO, adsService.updateComment(1, 2, CREATE_OR_UPDATE_COMMENT_2_DTO));
        assertThrows(UsernameNotFoundException.class, () -> adsService.updateComment(1, 3, CREATE_OR_UPDATE_COMMENT_3_DTO));
        assertNull(adsService.updateComment(1, 4, CREATE_OR_UPDATE_COMMENT_1_DTO));
        assertNull(adsService.updateComment(2, 1, CREATE_OR_UPDATE_COMMENT_1_DTO));
    }

    @Test
    @WithMockUser(value = "admin@test.com")
    void updateCommentWithAdmin() {
        assertEquals(COMMENT_1_DTO, adsService.updateComment(1, 1, CREATE_OR_UPDATE_COMMENT_1_DTO));
        assertEquals(COMMENT_2_DTO, adsService.updateComment(1, 2, CREATE_OR_UPDATE_COMMENT_2_DTO));
        assertEquals(COMMENT_3_DTO, adsService.updateComment(1, 3, CREATE_OR_UPDATE_COMMENT_3_DTO));
        assertNull(adsService.updateComment(1, 4, CREATE_OR_UPDATE_COMMENT_1_DTO));
        assertNull(adsService.updateComment(2, 1, CREATE_OR_UPDATE_COMMENT_1_DTO));
    }

    @Test
    @WithMockUser(value = "anotheruser@test.com")
    void updateCommentWithAnotherUser() {
        assertNull(adsService.updateComment(1, 1, CREATE_OR_UPDATE_COMMENT_1_DTO));
        assertNull(adsService.updateComment(1, 2, CREATE_OR_UPDATE_COMMENT_2_DTO));
        assertNull(adsService.updateComment(1, 3, CREATE_OR_UPDATE_COMMENT_3_DTO));
        assertNull(adsService.updateComment(1, 4, CREATE_OR_UPDATE_COMMENT_1_DTO));
        assertNull(adsService.updateComment(2, 1, CREATE_OR_UPDATE_COMMENT_1_DTO));
    }
}
```