package com.denniseckerskorn.seeder;

import com.denniseckerskorn.entities.class_managment.Assistance;
import com.denniseckerskorn.entities.class_managment.Membership;
import com.denniseckerskorn.entities.class_managment.TrainingGroup;
import com.denniseckerskorn.entities.class_managment.TrainingSession;
import com.denniseckerskorn.entities.finance.*;
import com.denniseckerskorn.enums.MembershipTypeValues;
import com.denniseckerskorn.enums.PaymentMethodValues;
import com.denniseckerskorn.enums.PermissionValues;
import com.denniseckerskorn.enums.StatusValues;
import com.denniseckerskorn.entities.user_managment.Notification;
import com.denniseckerskorn.entities.user_managment.Permission;
import com.denniseckerskorn.entities.user_managment.Role;
import com.denniseckerskorn.entities.user_managment.StudentHistory;
import com.denniseckerskorn.entities.user_managment.users.Admin;
import com.denniseckerskorn.entities.user_managment.users.Student;
import com.denniseckerskorn.entities.user_managment.users.Teacher;
import com.denniseckerskorn.entities.user_managment.users.User;
import com.denniseckerskorn.services.class_managment_services.AssistanceService;
import com.denniseckerskorn.services.class_managment_services.MembershipService;
import com.denniseckerskorn.services.class_managment_services.TrainingGroupService;
import com.denniseckerskorn.services.class_managment_services.TrainingSessionService;
import com.denniseckerskorn.services.finance_services.*;
import com.denniseckerskorn.services.user_managment_services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * TestDataSeeder is a component that seeds the database with initial test data
 * when the application starts in the "dev" profile.
 * It creates roles, permissions, users, memberships, training groups, sessions,
 * and other entities to facilitate development and testing.
 */
@Component
@Profile("dev")
public class TestDataSeeder implements CommandLineRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final AdminService adminService;
    private final NotificationService notificationService;
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final StudentHistoryService studentHistoryService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final UserService userService;
    private final MembershipService membershipService;
    private final AssistanceService assistanceService;
    private final TrainingGroupService trainingGroupService;
    private final TrainingSessionService trainingSessionService;
    private final InvoiceService invoiceService;
    private final InvoiceLineService invoiceLineService;
    private final IVATypeService ivaTypeService;
    private final ProductServiceService productServiceService;
    private final PaymentService paymentService;

    /**
     * Constructor for TestDataSeeder.
     *
     * @param adminService           Service for managing admin entities.
     * @param notificationService    Service for managing notifications.
     * @param permissionService      Service for managing permissions.
     * @param roleService            Service for managing roles.
     * @param studentHistoryService  Service for managing student history.
     * @param studentService         Service for managing student entities.
     * @param teacherService         Service for managing teacher entities.
     * @param userService            Service for managing user entities.
     * @param membershipService      Service for managing memberships.
     * @param assistanceService      Service for managing assistance records.
     * @param trainingGroupService   Service for managing training groups.
     * @param trainingSessionService Service for managing training sessions.
     * @param invoiceService         Service for managing invoices.
     * @param invoiceLineService     Service for managing invoice lines.
     * @param ivaTypeService         Service for managing IVA types.
     * @param productServiceService  Service for managing product/services.
     * @param paymentService         Service for managing payments.
     */
    @Autowired
    public TestDataSeeder(AdminService adminService,
                          NotificationService notificationService,
                          PermissionService permissionService,
                          RoleService roleService,
                          StudentHistoryService studentHistoryService,
                          StudentService studentService,
                          TeacherService teacherService,
                          UserService userService,
                          MembershipService membershipService,
                          AssistanceService assistanceService,
                          TrainingGroupService trainingGroupService,
                          TrainingSessionService trainingSessionService,
                          InvoiceService invoiceService,
                          InvoiceLineService invoiceLineService,
                          IVATypeService ivaTypeService,
                          ProductServiceService productServiceService,
                          PaymentService paymentService) {
        this.adminService = adminService;
        this.notificationService = notificationService;
        this.permissionService = permissionService;
        this.roleService = roleService;
        this.studentHistoryService = studentHistoryService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.userService = userService;
        this.membershipService = membershipService;
        this.assistanceService = assistanceService;
        this.trainingGroupService = trainingGroupService;
        this.trainingSessionService = trainingSessionService;
        this.invoiceService = invoiceService;
        this.invoiceLineService = invoiceLineService;
        this.ivaTypeService = ivaTypeService;
        this.productServiceService = productServiceService;
        this.paymentService = paymentService;
    }

    /**
     * This method is executed when the application starts.
     * It seeds the database with initial data for roles, permissions, users,
     * memberships, training groups, sessions, and other entities.
     *
     * @param args command line arguments
     * @throws Exception if an error occurs during seeding
     */
    @Override
    public void run(String... args) throws Exception {
        // Roles
        Role studentRole = new Role();
        studentRole.setName("ROLE_STUDENT");
        this.roleService.save(studentRole);

        Role teacherRole = new Role();
        teacherRole.setName("ROLE_TEACHER");
        this.roleService.save(teacherRole);

        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        this.roleService.save(adminRole);

        // Permissions
        for (PermissionValues value : PermissionValues.values()) {
            Permission p = new Permission();
            p.setPermissionName(value);
            this.permissionService.save(p);
        }

        // Obtain permissions
        Permission fullAccess = this.permissionService.findPermissionByName(PermissionValues.FULL_ACCESS);
        Permission manageUsers = this.permissionService.findPermissionByName(PermissionValues.MANAGE_STUDENTS);
        Permission viewOwnData = this.permissionService.findPermissionByName(PermissionValues.VIEW_OWN_DATA);

        // Assign permissions to roles
        studentRole.addPermission(viewOwnData);
        teacherRole.addPermission(manageUsers);
        teacherRole.addPermission(viewOwnData);
        adminRole.addPermission(fullAccess);

        // Update roles with permissions
        this.roleService.update(studentRole);
        this.roleService.update(teacherRole);
        this.roleService.update(adminRole);

        // Memberships
        Membership basicMembership = new Membership();
        basicMembership.setType(MembershipTypeValues.BASIC);
        basicMembership.setStartDate(LocalDate.now());
        basicMembership.setEndDate(LocalDate.now().plusMonths(1));
        basicMembership.setStatus(StatusValues.ACTIVE);
        this.membershipService.save(basicMembership);

        Membership advancedMembership = new Membership();
        advancedMembership.setType(MembershipTypeValues.ADVANCED);
        advancedMembership.setStartDate(LocalDate.now());
        advancedMembership.setEndDate(LocalDate.now().plusMonths(3));
        advancedMembership.setStatus(StatusValues.ACTIVE);
        this.membershipService.save(advancedMembership);

        Membership premiumMembership = new Membership();
        premiumMembership.setType(MembershipTypeValues.PREMIUM);
        premiumMembership.setStartDate(LocalDate.now());
        premiumMembership.setEndDate(LocalDate.now().plusMonths(6));
        premiumMembership.setStatus(StatusValues.ACTIVE);
        this.membershipService.save(premiumMembership);

        Membership noLimitMembership = new Membership();
        noLimitMembership.setType(MembershipTypeValues.NO_LIMIT);
        noLimitMembership.setStartDate(LocalDate.now());
        noLimitMembership.setEndDate(LocalDate.now().plusYears(1));
        noLimitMembership.setStatus(StatusValues.ACTIVE);
        this.membershipService.save(noLimitMembership);

        // Users and their roles
        User studentUser = new User();
        studentUser.setName("Student");
        studentUser.setSurname("One");
        studentUser.setPhoneNumber("600123456");
        studentUser.setEmail("student@example.com");
        studentUser.setPassword(passwordEncoder.encode("12345678"));
        studentUser.setStatus(StatusValues.ACTIVE);
        studentUser.setRegisterDate(LocalDateTime.now());
        studentUser.setAddress("Calle Estudiante 123");
        studentUser.setRole(studentRole);
        this.userService.save(studentUser);

        Student student = new Student();
        student.setUser(studentUser);
        student.setBirthdate(LocalDate.of(2005, 5, 20));
        student.setDni("12345678A");
        student.setBelt("Blanco");
        student.setParentName("Padre Estudiante");
        student.setMedicalReport("Apto");
        student.setProgress("Buena evolución");
        this.studentService.save(student);

        StudentHistory history = new StudentHistory();
        history.setStudent(student);
        history.setEventDate(LocalDate.now());
        history.setDescription("Primera clase");
        history.setEventType("Clase de prueba");
        this.studentHistoryService.save(history);

        this.studentService.addStudentToMembership(student, basicMembership);

        User teacherUser = new User();
        teacherUser.setName("Teacher");
        teacherUser.setSurname("Uno");
        teacherUser.setPhoneNumber("611123456");
        teacherUser.setEmail("teacher@example.com");
        teacherUser.setPassword(passwordEncoder.encode("123456789"));
        teacherUser.setStatus(StatusValues.ACTIVE);
        teacherUser.setRegisterDate(LocalDateTime.now());
        teacherUser.setAddress("Calle Maestro 1");
        teacherUser.setRole(teacherRole);
        this.userService.save(teacherUser);

        Teacher teacher = new Teacher();
        teacher.setUser(teacherUser);
        teacher.setDiscipline("Jiu-Jitsu");
        this.teacherService.save(teacher);

        User adminUser = new User();
        adminUser.setName("Admin");
        adminUser.setSurname("Root");
        adminUser.setPhoneNumber("622123456");
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword(passwordEncoder.encode("admin123"));
        adminUser.setStatus(StatusValues.ACTIVE);
        adminUser.setRegisterDate(LocalDateTime.now());
        adminUser.setAddress("Central");
        adminUser.setRole(adminRole);
        this.userService.save(adminUser);

        Admin admin = new Admin();
        admin.setUser(adminUser);
        this.adminService.save(admin);

        Notification notification = new Notification();
        notification.setShippingDate(LocalDateTime.now());
        notification.setType("Bienvenida");
        notification.setTitle("Bienvenido a MemberFlow");
        notification.setMessage("Gracias por registrarte");
        notification.setStatus(StatusValues.ACTIVE);
        this.notificationService.save(notification);

        notification.addUser(studentUser);
        notification.addUser(teacherUser);
        notification.addUser(adminUser);
        this.notificationService.update(notification);

        // Create a training group
        TrainingGroup trainingGroup = new TrainingGroup();
        trainingGroup.setName("Grupo JiuJitsu Avanzado");
        trainingGroup.setSchedule(LocalDateTime.now().plusDays(1));
        trainingGroup.setTeacher(teacher);
        this.trainingGroupService.save(trainingGroup);

        // Assign the training group to the student
        this.studentService.addGroupToStudent(student.getId(), trainingGroup);

        // Create a training session
        TrainingSession trainingSession = new TrainingSession();
        trainingSession.setTrainingGroup(trainingGroup);
        trainingSession.setDate(LocalDateTime.now().plusDays(2));
        trainingSession.setStatus(StatusValues.ACTIVE);
        this.trainingSessionService.save(trainingSession);

        // Add the training session to the group
        Assistance validAssistance = new Assistance();
        validAssistance.setDate(LocalDateTime.now());
        validAssistance.setStudent(student);
        validAssistance.setTrainingSession(trainingSession);
        this.studentService.addAssistanceToStudent(student, validAssistance);

        // Create an invalid assistance
        Invoice invoice = new Invoice();
        invoice.setUser(studentUser);
        invoice.setDate(LocalDateTime.now().minusDays(1));
        invoice.setStatus(StatusValues.NOT_PAID);
        invoice.setTotal(new BigDecimal("59.99"));
        this.invoiceService.save(invoice);
        this.userService.addInvoiceToUser(studentUser, invoice);

        // Create an IVA type
        IVAType ivaGeneral = new IVAType();
        ivaGeneral.setPercentage(new BigDecimal("21.00"));
        ivaGeneral.setDescription("IVA General");
        this.ivaTypeService.save(ivaGeneral);

        // Create a product/service
        ProductService jiuJitsuClass = new ProductService();
        jiuJitsuClass.setName("Clase de JiuJitsu");
        jiuJitsuClass.setType("Servicio");
        jiuJitsuClass.setPrice(new BigDecimal("50.00"));
        jiuJitsuClass.setIvaType(ivaGeneral);
        jiuJitsuClass.setStatus(StatusValues.ACTIVE);
        this.productServiceService.save(jiuJitsuClass);

        // Add the product/service to the invoice
        InvoiceLine line = new InvoiceLine();
        line.setInvoice(invoice);
        line.setProductService(jiuJitsuClass);
        line.setQuantity(1);
        line.setUnitPrice(jiuJitsuClass.getPrice());
        line.setSubtotal(jiuJitsuClass.getPrice().multiply(BigDecimal.valueOf(line.getQuantity())));
        this.invoiceService.addLineToInvoice(invoice, line);

        // Calculate the total with IVA
        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setAmount(invoice.getTotal());
        payment.setPaymentMethod(PaymentMethodValues.CREDIT_CARD);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(StatusValues.PAID);
        this.paymentService.save(payment);

        // Show all data in the console
        System.out.println("--- ROLES ---");
        this.roleService.findAll().forEach(System.out::println);

        System.out.println("--- PERMISOS ---");
        this.permissionService.findAll().forEach(System.out::println);

        System.out.println("--- USUARIOS ---");
        this.userService.findAll().forEach(System.out::println);

        System.out.println("--- ESTUDIANTES ---");
        this.studentService.findAll().forEach(System.out::println);

        System.out.println("--- HISTORIAL ESTUDIANTES ---");
        this.studentHistoryService.findAll().forEach(System.out::println);

        System.out.println("--- PROFESORES ---");
        this.teacherService.findAll().forEach(System.out::println);

        System.out.println("--- ADMINISTRADORES ---");
        this.adminService.findAll().forEach(System.out::println);

        System.out.println("--- NOTIFICACIONES ---");
        this.notificationService.findAll().forEach(System.out::println);

        System.out.println("--- MEMBRESÍAS ---");
        this.membershipService.findAll().forEach(System.out::println);

        System.out.println("--- GRUPOS DE ENTRENAMIENTO ---");
        this.trainingGroupService.findAll().forEach(System.out::println);

        System.out.println("--- SESIONES DE ENTRENAMIENTO ---");
        this.trainingSessionService.findAll().forEach(System.out::println);

        System.out.println("--- ASISTENCIAS ---");
        this.assistanceService.findAll().forEach(System.out::println);

        System.out.println("--- FACTURAS ---");
        this.invoiceService.findAll().forEach(System.out::println);

        System.out.println("--- LÍNEAS DE FACTURA ---");
        this.invoiceLineService.findAll().forEach(System.out::println);

        System.out.println("--- TIPOS DE IVA ---");
        this.ivaTypeService.findAll().forEach(System.out::println);

        System.out.println("--- PRODUCTOS/SERVICIOS ---");
        this.productServiceService.findAll().forEach(System.out::println);

        System.out.println("--- PAGOS ---");
        this.paymentService.findAll().forEach(System.out::println);

    }
}
