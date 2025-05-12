-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mf_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mf_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS mf_db DEFAULT CHARACTER SET utf8 ;
USE mf_db ;

-- -----------------------------------------------------
-- Table mf_db.ROLES
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS mf_db.ROLES (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(45) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX name_UNIQUE (name ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table mf_db.USERS
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS mf_db.USERS (
  id INT NOT NULL AUTO_INCREMENT,
  fk_role INT NOT NULL,
  name VARCHAR(50) NOT NULL,
  surname VARCHAR(50) NOT NULL,
  phone_number VARCHAR(30) NOT NULL,
  email VARCHAR(100) NOT NULL,
  password VARCHAR(255) NOT NULL,
  address VARCHAR(100) NULL,
  register_date DATETIME NULL,
  status VARCHAR(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX email_UNIQUE (email ASC) ,
  INDEX fk_USERS_ROLES1_idx (fk_role ASC) ,
  CONSTRAINT fk_USERS_ROLES1
    FOREIGN KEY (fk_role)
    REFERENCES mf_db.ROLES (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table mf_db.MEMBERSHIPS
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS mf_db.MEMBERSHIPS (
  id INT NOT NULL AUTO_INCREMENT,
  type VARCHAR(50) NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  status VARCHAR(20) NOT NULL,
  PRIMARY KEY (id))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table mf_db.STUDENTS
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS mf_db.STUDENTS (
  id INT NOT NULL,
  fk_user INT NOT NULL,
  fk_membership INT NULL,
  dni VARCHAR(10) NOT NULL,
  birthdate DATE NOT NULL,
  belt VARCHAR(20) NULL,
  progress TEXT NULL,
  medical_report VARCHAR(500) NULL,
  parent_name VARCHAR(50) NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX dni_UNIQUE (dni ASC) ,
  INDEX fk_STUDENTS_MEMBERSHIPS1_idx (fk_membership ASC) ,
  INDEX fk_STUDENTS_USERS1_idx (fk_user ASC) ,
  UNIQUE INDEX fk_user_UNIQUE (fk_user ASC) ,
  CONSTRAINT fk_STUDENTS_MEMBERSHIPS1
    FOREIGN KEY (fk_membership)
    REFERENCES mf_db.MEMBERSHIPS (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_STUDENTS_USERS1
    FOREIGN KEY (fk_user)
    REFERENCES mf_db.USERS (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table mf_db.TEACHERS
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS mf_db.TEACHERS (
  id INT NOT NULL,
  fk_user INT NOT NULL,
  discipline VARCHAR(50) NULL,
  PRIMARY KEY (id),
  INDEX fk_TEACHERS_USERS1_idx (fk_user ASC) ,
  UNIQUE INDEX fk_user_UNIQUE (fk_user ASC) ,
  CONSTRAINT fk_TEACHERS_USERS1
    FOREIGN KEY (fk_user)
    REFERENCES mf_db.USERS (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table mf_db.TRAINING_GROUPS
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS mf_db.TRAINING_GROUPS (
  id INT NOT NULL AUTO_INCREMENT,
  fk_teacher INT NOT NULL,
  name VARCHAR(45) NOT NULL,
  level VARCHAR(45) NULL,
  schedule DATETIME NULL,
  PRIMARY KEY (id),
  INDEX fk_GROUPS_TEACHERS1_idx (fk_teacher ASC) ,
  CONSTRAINT fk_GROUPS_TEACHERS1
    FOREIGN KEY (fk_teacher)
    REFERENCES mf_db.TEACHERS (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table mf_db.TRAINING_SESSIONS
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS mf_db.TRAINING_SESSIONS (
  id INT NOT NULL AUTO_INCREMENT,
  fk_group INT NOT NULL,
  date_time DATETIME NULL,
  status VARCHAR(50) NULL,
  PRIMARY KEY (id),
  INDEX fk_CLASSES_GROUPS1_idx (fk_group ASC) ,
  CONSTRAINT fk_CLASSES_GROUPS1
    FOREIGN KEY (fk_group)
    REFERENCES mf_db.TRAINING_GROUPS (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table mf_db.INVOICES
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS mf_db.INVOICES (
  id INT NOT NULL AUTO_INCREMENT,
  fk_user INT NOT NULL,
  date DATETIME NOT NULL,
  total DECIMAL(10,2) NOT NULL,
  status VARCHAR(20) NOT NULL,
  PRIMARY KEY (id),
  INDEX fk_INVOICES_USERS1_idx (fk_user ASC) ,
  CONSTRAINT fk_INVOICES_USERS1
    FOREIGN KEY (fk_user)
    REFERENCES mf_db.USERS (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table mf_db.IVA_TYPE
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS mf_db.IVA_TYPE (
  id INT NOT NULL AUTO_INCREMENT,
  percentage DECIMAL(10,2) NOT NULL,
  description VARCHAR(50) NULL,
  PRIMARY KEY (id))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table mf_db.PRODUCTS_SERVICES
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS mf_db.PRODUCTS_SERVICES (
  id INT NOT NULL AUTO_INCREMENT,
  fk_iva_type INT NOT NULL,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(250) NULL,
  price DECIMAL(10,2) NOT NULL,
  type VARCHAR(45) NOT NULL,
  status VARCHAR(20) NULL,
  PRIMARY KEY (id),
  INDEX fk_PRODUCTS_SERVICES_IVA_TYPE1_idx (fk_iva_type ASC) ,
  CONSTRAINT fk_PRODUCTS_SERVICES_IVA_TYPE1
    FOREIGN KEY (fk_iva_type)
    REFERENCES mf_db.IVA_TYPE (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table mf_db.INVOICE_LINES
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS mf_db.INVOICE_LINES (
  id INT NOT NULL AUTO_INCREMENT,
  fk_invoice INT NOT NULL,
  fk_product_service INT NOT NULL,
  description TEXT NULL,
  quantity INT NOT NULL,
  unit_price DECIMAL(10,2) NOT NULL,
  subtotal DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id),
  INDEX fk_INVOICE_LINES_INVOICES_idx (fk_invoice ASC) ,
  INDEX fk_INVOICE_LINES_PRODUCTS_SERVICES1_idx (fk_product_service ASC) ,
  CONSTRAINT fk_INVOICE_LINES_INVOICES
    FOREIGN KEY (fk_invoice)
    REFERENCES mf_db.INVOICES (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_INVOICE_LINES_PRODUCTS_SERVICES1
    FOREIGN KEY (fk_product_service)
    REFERENCES mf_db.PRODUCTS_SERVICES (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table mf_db.NOTIFICATIONS
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS mf_db.NOTIFICATIONS (
  id INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(200) NOT NULL,
  message TEXT NULL,
  shipping_date DATETIME NOT NULL,
  type VARCHAR(100) NULL,
  status VARCHAR(50) NULL,
  PRIMARY KEY (id))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table mf_db.STUDENT_HISTORY
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS mf_db.STUDENT_HISTORY (
  id INT NOT NULL AUTO_INCREMENT,
  fk_student INT NOT NULL,
  event_date DATE NULL,
  event_type VARCHAR(200) NULL,
  description TEXT NULL,
  PRIMARY KEY (id),
  INDEX fk_STUDENT_HISTORY_STUDENTS1_idx (fk_student ASC) ,
  CONSTRAINT fk_STUDENT_HISTORY_STUDENTS1
    FOREIGN KEY (fk_student)
    REFERENCES mf_db.STUDENTS (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table mf_db.STUDENTS_GROUPS
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS mf_db.STUDENTS_GROUPS (
  fk_student INT NOT NULL,
  fk_group INT NOT NULL,
  PRIMARY KEY (fk_student, fk_group),
  INDEX fk_STUDENTS_has_GROUPS_GROUPS1_idx (fk_group ASC) ,
  INDEX fk_STUDENTS_has_GROUPS_STUDENTS1_idx (fk_student ASC) ,
  CONSTRAINT fk_STUDENTS_has_GROUPS_STUDENTS1
    FOREIGN KEY (fk_student)
    REFERENCES mf_db.STUDENTS (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_STUDENTS_has_GROUPS_GROUPS1
    FOREIGN KEY (fk_group)
    REFERENCES mf_db.TRAINING_GROUPS (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table mf_db.ASSISTANCE
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS mf_db.ASSISTANCE (
  id INT NOT NULL AUTO_INCREMENT,
  fk_training_session INT NOT NULL,
  fk_student INT NOT NULL,
  date_time DATETIME NOT NULL,
  PRIMARY KEY (id),
  INDEX fk_ASISTENCIAS_CLASSES1_idx (fk_training_session ASC) ,
  INDEX fk_ASISTENCIAS_STUDENTS1_idx (fk_student ASC) ,
  CONSTRAINT fk_ASISTENCIAS_CLASSES1
    FOREIGN KEY (fk_training_session)
    REFERENCES mf_db.TRAINING_SESSIONS (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_ASISTENCIAS_STUDENTS1
    FOREIGN KEY (fk_student)
    REFERENCES mf_db.STUDENTS (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table mf_db.PAYMENTS
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS mf_db.PAYMENTS (
  id INT NOT NULL AUTO_INCREMENT,
  fk_invoice INT NOT NULL,
  payment_date DATETIME NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  payment_method VARCHAR(50) NOT NULL,
  status VARCHAR(20) NOT NULL,
  PRIMARY KEY (id),
  INDEX fk_PAYMENTS_INVOICES1_idx (fk_invoice ASC) ,
  CONSTRAINT fk_PAYMENTS_INVOICES1
    FOREIGN KEY (fk_invoice)
    REFERENCES mf_db.INVOICES (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table mf_db.PERMISSIONS
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS mf_db.PERMISSIONS (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(45) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX name_UNIQUE (name ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table mf_db.ROLES_PERMISSIONS
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS mf_db.ROLES_PERMISSIONS (
  fk_role INT NOT NULL,
  fk_permission INT NOT NULL,
  PRIMARY KEY (fk_role, fk_permission),
  INDEX fk_ROLES_has_PERMISSIONS_PERMISSIONS1_idx (fk_permission ASC) ,
  INDEX fk_ROLES_has_PERMISSIONS_ROLES1_idx (fk_role ASC) ,
  CONSTRAINT fk_ROLES_has_PERMISSIONS_ROLES1
    FOREIGN KEY (fk_role)
    REFERENCES mf_db.ROLES (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_ROLES_has_PERMISSIONS_PERMISSIONS1
    FOREIGN KEY (fk_permission)
    REFERENCES mf_db.PERMISSIONS (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table mf_db.USERS_NOTIFICATIONS
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS mf_db.USERS_NOTIFICATIONS (
  fk_user INT NOT NULL,
  fk_notification INT NOT NULL,
  PRIMARY KEY (fk_user, fk_notification),
  INDEX fk_USERS_has_NOTIFICATIONS_NOTIFICATIONS1_idx (fk_notification ASC) ,
  INDEX fk_USERS_has_NOTIFICATIONS_USERS1_idx (fk_user ASC) ,
  CONSTRAINT fk_USERS_has_NOTIFICATIONS_USERS1
    FOREIGN KEY (fk_user)
    REFERENCES mf_db.USERS (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_USERS_has_NOTIFICATIONS_NOTIFICATIONS1
    FOREIGN KEY (fk_notification)
    REFERENCES mf_db.NOTIFICATIONS (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table mf_db.ADMINS
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS mf_db.ADMINS (
  id INT NOT NULL AUTO_INCREMENT,
  fk_user INT NOT NULL,
  PRIMARY KEY (id),
  INDEX fk_ADMINS_USERS1_idx (fk_user ASC) ,
  UNIQUE INDEX fk_user_UNIQUE (fk_user ASC) ,
  CONSTRAINT fk_ADMINS_USERS1
    FOREIGN KEY (fk_user)
    REFERENCES mf_db.USERS (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
